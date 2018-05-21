package com.example.diku.food.Module;

/**
 * Created by Diku on 16-05-2018.
 */

public class Category {

    private String name;
    private String img;

    public Category(){


    }

    public Category(String name, String img) {
        this.name = name;
        this.img = img;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }
}
