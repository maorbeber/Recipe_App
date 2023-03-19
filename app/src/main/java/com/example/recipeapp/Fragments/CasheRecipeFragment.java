package com.example.recipeapp.Fragments;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.recipeapp.DatabaseService.DbHelper;
import com.example.recipeapp.Model.MyRecipe;
import com.example.recipeapp.Model.Recipe;
import com.example.recipeapp.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class CasheRecipeFragment extends Fragment {

    RecyclerView recylerView;
    private Dialog loadingDialog;
    ArrayList<Recipe> recipeArrayList =new ArrayList<Recipe>();
    RecipeAdapter recipeAdapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_cashe_recipe, container, false);

        recylerView=view.findViewById(R.id.recylerView);

        recylerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));

        loadingDialog=new Dialog(getContext());
        loadingDialog.setContentView(R.layout.loading_progress_dialog);
        loadingDialog.setCancelable(false);
        loadingDialog.getWindow().setBackgroundDrawable(getResources().getDrawable(R.drawable.slider_background));
        loadingDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);

        return view;
    }

    @Override
    public void onStart() {

        recipeArrayList=new DbHelper(getContext()).getRecipeData();

          recipeAdapter =new RecipeAdapter();
          recylerView.setAdapter(recipeAdapter);
        super.onStart();
    }

    public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.ImageViewHolder> {

        public RecipeAdapter(){

        }
        @NonNull
        @Override
        public RecipeAdapter.ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v= LayoutInflater.from(getContext()).inflate(R.layout.list_random_recipe,parent,false);
            return  new RecipeAdapter.ImageViewHolder(v);
        }

        @Override
        public void onBindViewHolder(@NonNull final RecipeAdapter.ImageViewHolder holder, @SuppressLint("RecyclerView") int position) {



            holder.textView_title.setText(recipeArrayList.get(position).title);
            holder.textView_title.setSelected(true);
            holder.textView_likes.setText(recipeArrayList.get(position).aggregateLikes+" Likes");
            holder.textView_servings.setText(recipeArrayList.get(position).servings+ " Servings");
            holder.textView_time.setText(recipeArrayList.get(position).readyInMinutes + " minutes");
            Picasso.with(getContext())
                    .load(recipeArrayList.get(position).image)
                    .placeholder(R.drawable.progress_animation)
                    .fit()
                    .centerCrop()
                    .into(holder.imageView_food);




        }

        @Override
        public int getItemCount() {
            return recipeArrayList.size();
        }

        public class ImageViewHolder extends RecyclerView.ViewHolder {
            CardView random_list_container;
            TextView textView_title,textView_servings,textView_likes,textView_time;
            ImageView imageView_food;
            public ImageViewHolder(@NonNull View itemView) {
                super(itemView);
                random_list_container = itemView.findViewById(R.id.random_list_container);
                textView_title = itemView.findViewById(R.id.textView_title);
                textView_servings = itemView.findViewById(R.id.textView_servings);
                textView_likes = itemView.findViewById(R.id.textView_likes);
                textView_time = itemView.findViewById(R.id.textView_time);
                imageView_food = itemView.findViewById(R.id.imageView_food);
            }
        }
    }
}