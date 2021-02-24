package com.example.translator_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity {

    EditText Username,Email,Password;
    Button SignUp,SignIn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Objects.requireNonNull(getSupportActionBar()).hide();

        Username=findViewById(R.id.etusername);
        Email=findViewById(R.id.etEmail);
        Password=findViewById(R.id.etPassword);
        SignUp=findViewById(R.id.btnSignUp);
        SignIn=findViewById(R.id.btnSignIn);
        SignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(LoginActivity.this,SignInActivity.class);
                startActivity(i);
                finish();
            }
        });
        SignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(LoginActivity.this,MainActivity.class);
                startActivity(i);
                finish();
                }
        });


    }
}
