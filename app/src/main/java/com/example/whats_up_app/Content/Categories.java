package com.example.whats_up_app.Content;

public class Categories {
    private int url ;
    private String name;

    public Categories(int url, String name) {
        this.url = url;
        this.name = name;
    }

    public Categories() {
    }

    public int getUrl() {
        return url;
    }

    public void setUrl(int url) {
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
