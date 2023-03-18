package com.example.recipeapp.Model;

public class Recipe {
    String description,image,type,id;

    public Recipe(String description, String image, String type, String id) {
        this.description = description;
        this.image = image;
        this.type = type;
        this.id = id;
    }
    public Recipe(String description, String image) {
        this.description = description;
        this.image = image;
    }

    public String getDescription() {
        return description;
    }

    public String getImage() {
        return image;
    }

    public String getType() {
        return type;
    }

    public String getId() {
        return id;
    }
}
