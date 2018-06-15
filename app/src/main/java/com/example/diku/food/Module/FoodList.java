package com.example.diku.food.Module;

/**
 * Created by Diku on 16-05-2018.
 */

public class FoodList {

   String description,discount,image,menuid,name,price,unit;

    public FoodList() {
    }

    public FoodList(String description, String discount, String image, String menuid, String name, String price, String unit) {
        this.description = description;
        this.discount = discount;
        this.image = image;
        this.menuid = menuid;
        this.name = name;
        this.price = price;
        this.unit = unit;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getMenuid() {
        return menuid;
    }

    public void setMenuid(String menuid) {
        this.menuid = menuid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }
}
