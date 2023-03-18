package com.example.recipeapp.Screens;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.example.recipeapp.MainActivity;
import com.example.recipeapp.R;
import com.example.recipeapp.Utils.Constant;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        Thread thread=new Thread(){
            @Override
            public void run() {
                try {
                    sleep(2000);
                    if(!Constant.getUserLoginStatus(SplashActivity.this)) {
                        startActivity(new Intent(SplashActivity.this, AccountActivity.class));
                        finish();
                    }
                    else {
                        startActivity(new Intent(SplashActivity.this, MainActivity.class));
                        finish();
                    }

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        thread.start();
    }
}