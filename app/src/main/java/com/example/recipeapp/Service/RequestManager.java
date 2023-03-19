package com.example.recipeapp.Service;

import android.content.Context;


import com.example.recipeapp.Model.RandomRecipeAPIResponse;
import com.example.recipeapp.lisner.RandomRecipeResponseListener;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public class RequestManager {

    Context context;

    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("https://api.spoonacular.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    public RequestManager(Context context) {
        this.context = context;
    }

    public void getRandomRecipes(RandomRecipeResponseListener listener, List<String> tags){

        CallRandomRecipes callRandomRecipes = retrofit.create(CallRandomRecipes.class);
        Call<RandomRecipeAPIResponse> call = callRandomRecipes.callRandomRecipe("bfcfdc02d58d4a839926e5c7df0794a4","10", tags);
        call.enqueue(new Callback<RandomRecipeAPIResponse>() {
            @Override
            public void onResponse(Call<RandomRecipeAPIResponse> call, Response<RandomRecipeAPIResponse> response) {
                if(!response.isSuccessful()){
                    listener.didError(response.message());

                    return;
                }
                listener.didFetch(response.body(), response.message());
            }

            @Override
            public void onFailure(Call<RandomRecipeAPIResponse> call, Throwable t) {
                listener.didError(t.getMessage());


            }
        });

    }




    private interface CallRandomRecipes{
        @GET("recipes/random")
    Call<RandomRecipeAPIResponse> callRandomRecipe(

            @Query("apiKey") String apiKey,
            @Query("number") String number,
            @Query("tags") List<String> tags


    );
    }
}

