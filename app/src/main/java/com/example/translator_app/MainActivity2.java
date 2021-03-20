package com.example.translator_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.StatusBarManager;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.example.translator_app.Fragments.HomeFragment;
import com.example.translator_app.Fragments.SettingFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity2 extends AppCompatActivity {

    FrameLayout Frame;
    BottomNavigationView btmNav;

    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        Frame=findViewById(R.id.Frame);
        btmNav=findViewById(R.id.BtmNav);

        getSupportActionBar().hide();



        getSupportFragmentManager().beginTransaction().replace(R.id.Frame, new HomeFragment()).commit();

        btmNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                menuItem.setChecked(true);

                int id=menuItem.getItemId();
                if(id==R.id.Home)
                {

                    getSupportFragmentManager().beginTransaction().replace(R.id.Frame,new HomeFragment()).commit();
                }
                if(id==R.id.Setting)
                {
                    getSupportFragmentManager().beginTransaction().replace(R.id.Frame,new SettingFragment()).commit();

                }

                return false;
            }
        });

    }
}