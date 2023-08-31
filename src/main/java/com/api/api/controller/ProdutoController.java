package com.api.api.controller;

import com.api.api.model.ImagemProduto;
import com.api.api.model.Produto;
import com.api.api.service.ProdutoService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;



import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;


@RestController
@RequestMapping("/produtos")
public class ProdutoController {

    private final ProdutoService produtoService;

    @Autowired
    public ProdutoController(ProdutoService produtoService) {
        this.produtoService = produtoService;
    }

 

    @PostMapping
    public ResponseEntity<String> criarProduto(
           @ModelAttribute Produto produto
            ) {
        try {
            produtoService.criarProduto(produto, produto.getImagem());
            return new ResponseEntity<>("Recurso criado com sucesso", HttpStatus.CREATED);
        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping
    public ResponseEntity<List<Produto>> listaProduto() {
        List<Produto> produtos = produtoService.listaProduto();
        return new ResponseEntity<>(produtos, HttpStatus.OK);
    }

    @GetMapping("/{id}/{fileName:.+}")
    public ResponseEntity<byte[]> downloadImagem(@PathVariable String id, @PathVariable String fileName) {
        try {
            // Use o método ProdutoById para buscar o produto com base no id
            List<ImagemProduto> imagensDoProduto = produtoService.ProdutoById(id);

            // Verifique se o nome do arquivo corresponde ao arquivo associado ao produto
            boolean arquivoEncontrado = imagensDoProduto.stream()
                    .map(ImagemProduto::getUrl)
                    .anyMatch(url -> url.endsWith(fileName));

            if (arquivoEncontrado) {
                // Construa o caminho completo do arquivo com base no fileName
                String caminhoDoArquivo = "src/main/java/com/api/api/commons/imgs/" + fileName;

                // Carregue o conteúdo do arquivo em um array de bytes
                byte[] arquivo = CarregarConteudoDoArquivo(caminhoDoArquivo);

                // Configure os cabeçalhos da resposta para indicar o download do arquivo
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
                headers.setContentDispositionFormData("attachment", fileName);

                // Retorne a resposta com o arquivo
                return new ResponseEntity<>(arquivo, headers, HttpStatus.OK);
            } else {
                // Nome do arquivo na URL não corresponde a nenhum arquivo associado ao produto
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            }
        } catch (RuntimeException e) {
            // Produto com o id fornecido não encontrado
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    private byte[] CarregarConteudoDoArquivo(String caminhoDoArquivo) throws IOException {
        // Crie um objeto Path usando o caminho do arquivo
        Path path = Paths.get(caminhoDoArquivo);

        // Lê o conteúdo do arquivo em um array de bytes
        return Files.readAllBytes(path);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> ExcluirProduto(@PathVariable String id){
        try {
            produtoService.deleteProduto(id);
            return new ResponseEntity<>("Produto excluido " + id, HttpStatus.OK);
        }catch (IOException e){
            return new ResponseEntity<>("Não foi possivel excluir " + id, HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

}
