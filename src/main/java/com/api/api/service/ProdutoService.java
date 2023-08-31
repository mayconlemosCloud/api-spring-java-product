package com.api.api.service;

import com.api.api.Repository.ProdutoRepository;
import com.api.api.model.ImagemProduto;
import com.api.api.model.Produto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ProdutoService {

    private final ProdutoRepository produtoRepository;

    @Value("${image.upload.directory}") // Lê o valor da propriedade image.upload.directory
    private String uploadDir; // O diretório onde as imagens serão salvas

    @Autowired
    public ProdutoService(ProdutoRepository produtoRepository) {
        this.produtoRepository = produtoRepository;
    }

    public List<Produto> listaProduto(){
      List<Produto> produto =  produtoRepository.findAll();
      return produto;
    }

    public List<ImagemProduto> ProdutoById(String Id){
        Optional<Produto> produtoOptional =  produtoRepository.findById(Id);

        if (produtoOptional.isPresent()) {
            Produto produto = produtoOptional.get();
            return  produto.getImagens();
        } else {
            throw new RuntimeException("Produto não encontrado com o ID: " + Id);
        }
    }

    public Produto criarProduto(Produto produto,  List<MultipartFile> imagens) throws IOException {
        Produto produtoSalvo = new Produto();
        List<ImagemProduto> imagensDoProduto = new ArrayList<>();

        for (MultipartFile imagem : imagens) {
            Integer index = imagens.indexOf(imagem);
            if (imagem != null && !imagem.isEmpty()) {
                String fileName = StringUtils.cleanPath(imagem.getOriginalFilename());
                Path uploadPath = Paths.get(uploadDir);
                Path filePath = uploadPath.resolve(fileName);

                if (Files.exists(filePath)) {
                    throw new IOException("Já existe um arquivo com o mesmo nome: " + fileName);
                }

                Files.copy(imagem.getInputStream(), filePath);

                ImagemProduto imagemProduto = new ImagemProduto(index,  fileName);
                imagensDoProduto.add(imagemProduto);
            }
        }

        produto.setImagens(imagensDoProduto);
        produtoSalvo = produtoRepository.save(produto);

        return produtoSalvo;
    }

    public void deleteProduto(String id) throws IOException {
        Optional<Produto> produtoOptional = produtoRepository.findById(id);
        Produto produto = produtoOptional.get();
        if (produtoOptional.isPresent()) {
            for(ImagemProduto imagem : produto.getImagens()){
                String caminhoDoArquivo = "src/main/java/com/api/api/commons/imgs/" + imagem.getUrl();
                Path path = Paths.get(caminhoDoArquivo);
                Files.delete(path);
            }
            produtoRepository.deleteById(id);
        } else {
            throw new RuntimeException("Produto não encontrado com o ID: " + id);
        }

    }


}
