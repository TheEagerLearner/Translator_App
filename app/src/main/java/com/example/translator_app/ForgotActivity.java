package com.example.translator_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class ForgotActivity extends AppCompatActivity {

    EditText FEmail,etOtp,etnewPass,etRnewPass;
    Button btnOtp,btnSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot);


        FEmail=findViewById(R.id.FEmail);
        etOtp=findViewById(R.id.etOtp);
        etnewPass=findViewById(R.id.etnewPass);
        etRnewPass=findViewById(R.id.etRnewPass);
        btnOtp=findViewById(R.id.btnOtp);
        btnSubmit=findViewById(R.id.btnSubmit);

        btnOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Toast.makeText(ForgotActivity.this,"OTP has been sent to entered email",Toast.LENGTH_SHORT).show();
            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(ForgotActivity.this,MainActivity.class);
                startActivity(i);
                finish();
            }
        });

    }
}
