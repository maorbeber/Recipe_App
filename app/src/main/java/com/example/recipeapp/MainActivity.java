package com.example.recipeapp;

import static com.example.recipeapp.Utils.Constant.setUserLoginStatus;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.Navigation;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.recipeapp.Fragments.AddRecipeFragment;
import com.example.recipeapp.Fragments.HomeFragment;
import com.example.recipeapp.Fragments.MyRecipeFragment;
import com.example.recipeapp.Screens.AccountActivity;
import com.google.android.material.navigation.NavigationView;

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
                }
                drawer.closeDrawer(GravityCompat.START);
                return true;
            }
        });
    }
        public void showHomeScreen(){
            getSupportFragmentManager().beginTransaction().replace(R.id.frag,new HomeFragment()).commit();

        }
}