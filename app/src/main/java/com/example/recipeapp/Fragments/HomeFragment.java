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

import com.example.recipeapp.Model.Recipe;
import com.example.recipeapp.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class HomeFragment extends Fragment {
          RecyclerView recylerView;
    private Dialog loadingDialog;
 ArrayList<Recipe> recipeArrayList =new ArrayList<Recipe>();
    RecipeAdapter recipeAdapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_home, container, false);
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
        getData();
        super.onStart();
    }

    public void getData(){
        recipeArrayList.clear();
        loadingDialog.show();
        DatabaseReference databaseReference=  FirebaseDatabase.getInstance().getReference().child("Recipes");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                    for(DataSnapshot dataSnapshot2:dataSnapshot1.getChildren()){
                        recipeArrayList.add(new Recipe(
                                dataSnapshot2.child("Description").getValue(String.class),
                                dataSnapshot2.child("Image").getValue(String.class),
                                dataSnapshot2.child("Type").getValue(String.class),
                                dataSnapshot2.child("Id").getValue(String.class)
                        ));
                    }
                }

                recipeAdapter=new RecipeAdapter();
                recylerView.setAdapter(recipeAdapter);
                loadingDialog.dismiss();





            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.ImageViewHolder> {

        public RecipeAdapter(){

        }
        @NonNull
        @Override
        public RecipeAdapter.ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v= LayoutInflater.from(getContext()).inflate(R.layout.item_recipe,parent,false);
            return  new RecipeAdapter.ImageViewHolder(v);
        }

        @Override
        public void onBindViewHolder(@NonNull final RecipeAdapter.ImageViewHolder holder, @SuppressLint("RecyclerView") int position) {





            holder.description.setText("Description:\n "+recipeArrayList.get(position).getDescription());
            holder.type.setText("Type:\n "+recipeArrayList.get(position).getType());
            Picasso.with(getContext())
                    .load(recipeArrayList.get(position).getImage())
                    .placeholder(R.drawable.progress_animation)
                    .fit()
                    .centerCrop()
                    .into(holder.imageView);






        }

        @Override
        public int getItemCount() {
            return recipeArrayList.size();
        }

        public class ImageViewHolder extends RecyclerView.ViewHolder {
            TextView description, type;
            ImageView imageView;
            CardView cardView;
            public ImageViewHolder(@NonNull View item) {
                super(item);
                description=item.findViewById(R.id.description);
                type=item.findViewById(R.id.type);
                imageView=item.findViewById(R.id.imageView);
            }
        }
    }
}