package com.example.recipeapp.Model;

import java.util.ArrayList;


public class Recipe {


    public int id;


    public int aggregateLikes;

    public String title;
    public int readyInMinutes;
    public int servings;
    public String image;


    public Recipe(String title, int aggregateLikes, int servings, int readyInMinutes , String image) {
        this.title = title;
        this.image = image;
        this.servings=servings;
        this.aggregateLikes=aggregateLikes;
        this.readyInMinutes=readyInMinutes;

    }
    public Recipe(int id, String title, int aggregateLikes, int servings, int readyInMinutes , String image) {
        this.title = title;
        this.image = image;
        this.servings=servings;
        this.aggregateLikes=aggregateLikes;
        this.readyInMinutes=readyInMinutes;
        this.id=id;

    }

}

