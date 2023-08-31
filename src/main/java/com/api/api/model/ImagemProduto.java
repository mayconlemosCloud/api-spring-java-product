package com.api.api.model;

public class ImagemProduto {



    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    private Integer id;
    private String url;

    public ImagemProduto(Integer id, String url) {
        this.id = id;
        this.url = url;
    }

}
