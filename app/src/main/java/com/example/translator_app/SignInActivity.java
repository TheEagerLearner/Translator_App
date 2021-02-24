package com.example.translator_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class SignInActivity extends AppCompatActivity {

    EditText Username,Password;
    Button SignUp,SignIn,Forgot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        getSupportActionBar().hide();

        Username=findViewById(R.id.username1);
        Password=findViewById(R.id.Password1);
        SignIn=findViewById(R.id.btnSignIn1);
        SignUp=findViewById(R.id.btnSignUp1);
        Forgot=findViewById(R.id.btnForget);

        Forgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i=new Intent(SignInActivity.this,ForgotActivity.class);
                startActivity(i);
                finish();

            }
        });

        SignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(SignInActivity.this,LoginActivity.class);
                startActivity(i);
                finish();
            }
        });
        SignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(SignInActivity.this,MainActivity.class);
                startActivity(i);
                finish();
            }
        });

    }
}
