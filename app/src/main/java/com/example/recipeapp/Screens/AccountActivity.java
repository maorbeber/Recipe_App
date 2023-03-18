package com.example.recipeapp.Screens;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.recipeapp.Fragments.LoginFragment;
import com.example.recipeapp.Fragments.RegisterFragment;
import com.example.recipeapp.R;

public class AccountActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
      //  showSignUpScreen();
    }


//    // function to load login fragment in activity
//    public void showLoginScreen(){
//     //   getSupportFragmentManager().beginTransaction().replace(R.id.frag,new LoginFragment()).commit();
//    }
//    // function to call signup fragment in activity
//    public void showSignUpScreen(){
//      //  getSupportFragmentManager().beginTransaction().replace(R.id.frag,new RegisterFragment()).commit();
//    }
}