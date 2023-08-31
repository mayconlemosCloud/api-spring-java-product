package com.api.api.model;

import com.mongodb.lang.Nullable;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Document(collection = "produtos")
public class Produto {


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public double getPreco() {
        return preco;
    }

    public void setPreco(double preco) {
        this.preco = preco;
    }
    public List<ImagemProduto> getImagens() {
        return Imagens;
    }

    public void setImagens(List<ImagemProduto> imagens) {
        Imagens = imagens;
    }


    public List<MultipartFile> getImagem() {
        return imagem;
    }

    public void setImagem(List<MultipartFile> imagem) {
        this.imagem = imagem;
    }

    @Id
    private String id;
    private String nome;
    private double preco;



    private List<ImagemProduto> Imagens;

    @Transient
    private List<MultipartFile> imagem;

}
