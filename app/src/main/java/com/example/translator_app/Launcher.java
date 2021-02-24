package com.example.translator_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

public class Launcher extends AppCompatActivity {

    ImageView imgTranslate,imgMic;
    Animation top,bottom;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);

        getSupportActionBar().hide();

        top= AnimationUtils.loadAnimation(Launcher.this,R.anim.top);
        bottom=AnimationUtils.loadAnimation(Launcher.this,R.anim.bottom);
        imgTranslate=findViewById(R.id.imgTranslate);
        imgMic=findViewById(R.id.imgMic);
        imgMic.setAnimation(top);
        imgTranslate.setAnimation(bottom);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i=new Intent(Launcher.this,LoginActivity.class);
                startActivity(i);
                finish();
            }
        },3000);

    }
}
