package com.example.recipeapp.lisner;


import com.example.recipeapp.Model.RandomRecipeAPIResponse;

public interface RandomRecipeResponseListener {
    void didFetch(RandomRecipeAPIResponse response, String message);
    void didError(String message);
}
