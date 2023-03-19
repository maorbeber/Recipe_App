package com.example.recipeapp;

import static com.example.recipeapp.Utils.Constant.setUserLoginStatus;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.example.recipeapp.DatabaseService.DbHelper;
import com.example.recipeapp.Fragments.AddRecipeFragment;
import com.example.recipeapp.Fragments.CacheRecipeFragment;
import com.example.recipeapp.Fragments.HomeFragment;
import com.example.recipeapp.Fragments.MyRecipeFragment;
import com.example.recipeapp.Model.RandomRecipeAPIResponse;
import com.example.recipeapp.Model.Recipe;
import com.example.recipeapp.Screens.AccountActivity;
import com.example.recipeapp.Service.RequestManager;
import com.example.recipeapp.Utils.Constant;
import com.example.recipeapp.lisner.RandomRecipeResponseListener;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private DrawerLayout drawer;
    NavigationView navigationView;
    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        navigationView =findViewById(R.id.nav_view);
        drawer = findViewById(R.id.drawer_layout);
        toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBarDrawerToggle toogle=new ActionBarDrawerToggle(this,drawer, (androidx.appcompat.widget.Toolbar) findViewById(R.id.toolbar),R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawer.addDrawerListener(toogle);
        toogle.syncState();

                // call the api if current date is different from previous date its mean we call the api after 24 hours
        if(!Constant.getCurrentDate().equals(Constant.getPreviousDate(this))){
            APICall();
        }

        getSupportFragmentManager().beginTransaction().replace(R.id.frag,new HomeFragment()).commit();

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id._logout:
                        setUserLoginStatus(MainActivity.this,false);
                        startActivity(new Intent(MainActivity.this, AccountActivity.class));
                        finish();
                        break;
                    case R.id._home:
                        getSupportFragmentManager().beginTransaction().replace(R.id.frag,new HomeFragment()).commit();

                        break;
                    case R.id.my_recipe:
                        getSupportFragmentManager().beginTransaction().replace(R.id.frag,new MyRecipeFragment()).commit();
                        break;

                    case R.id.add_recipe:
                        getSupportFragmentManager().beginTransaction().replace(R.id.frag,new AddRecipeFragment()).commit();

                        break;
                        case R.id.cashe_recipe:
                        getSupportFragmentManager().beginTransaction().replace(R.id.frag,new CacheRecipeFragment()).commit();

                        break;
                }
                drawer.closeDrawer(GravityCompat.START);
                return true;
            }
        });
    }
        public void showHomeScreen(){
            getSupportFragmentManager().beginTransaction().replace(R.id.frag,new HomeFragment()).commit();

        }


        public void APICall(){
            List<String> tags = new ArrayList<>();
            tags.clear();
            tags.add("main course");
            RequestManager manager=new RequestManager(this);
            manager.getRandomRecipes(randomRecipeResponseListener, tags);




        }

    private final RandomRecipeResponseListener randomRecipeResponseListener = new RandomRecipeResponseListener() {
        @Override
        public void didFetch(RandomRecipeAPIResponse response, String message) {

                 ArrayList<Recipe> recipeArrayList=response.recipes;


                 Log.d("result",recipeArrayList.toString());

                 for(int i=0;i<recipeArrayList.size();i++){
                         Recipe recipe =new Recipe(recipeArrayList.get(i).title,recipeArrayList.get(i).aggregateLikes
                         ,recipeArrayList.get(i).servings,recipeArrayList.get(i).readyInMinutes,recipeArrayList.get(i).image);
                    new  DbHelper(MainActivity.this)
                            .addRecipe(recipe);

            }
                 Constant.setPreviousDate(MainActivity.this,Constant.getCurrentDate());


        }

        @Override
        public void didError(String message) {

        }

    };
    }