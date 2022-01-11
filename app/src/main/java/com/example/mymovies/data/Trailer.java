package com.example.mymovies.data;

public class Trailer {
    private String key;
    private String name;

    //Конструктор
    public Trailer(String key, String name) {
        this.key = key;
        this.name = name;
    }

    //Геттеры

    public String getKey() {
        return key;
    }

    public String getName() {
        return name;
    }

    //Сеттеры

    public void setKey(String key) {
        this.key = key;
    }

    public void setName(String name) {
        this.name = name;
    }
}
