package com.example.translator_app;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.ActivityManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    Spinner spinTo;
    Spinner spinFrom;
    Button btnStart;
    String arr[]=new String[2];
    boolean started=false;

    AlertDialog alert;
    boolean start=false;
    private static final int CODE_DRAW_OVER_OTHER_APP_PERMISSION = 2084;

    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sharedPreferences=getSharedPreferences("Translator",MODE_PRIVATE);

        getSupportActionBar().hide();


        //Audio Record permission dialog box
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.RECORD_AUDIO},1);
        }

        btnStart=findViewById(R.id.btnStart);
        spinFrom=findViewById(R.id.spinFrom);
        spinTo=findViewById(R.id.spinTo);


        //Spinner1
        ArrayAdapter<CharSequence> adp=ArrayAdapter.createFromResource(this,R.array.languages,android.R.layout.simple_spinner_item);
        adp.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinTo.setAdapter(adp);
        spinTo.setOnItemSelectedListener(this);

        //Spinner2
        ArrayAdapter<CharSequence> adpt=ArrayAdapter.createFromResource(this,R.array.languages,android.R.layout.simple_spinner_item);
        adpt.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinFrom.setAdapter(adpt);
        spinFrom.setOnItemSelectedListener(this);


        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnStart.setText("Stop");

               try{

                   String full=arr[0]+arr[1];
                   Toast.makeText(MainActivity.this,full,Toast.LENGTH_LONG).show();

               }
               catch(NullPointerException e)
               {
                   Toast.makeText(MainActivity.this,"Please Select Something",Toast.LENGTH_LONG).show();

               }
                start_stop();


            }
        });
        if (isMyServiceRunning(FloatingWindow.class)){
            started = true;
            btnStart.setText("Stop");
        }



    }

    public void start_stop() {
        if (checkPermission()) {
            if (started) {
                stopService(new Intent(MainActivity.this, FloatingWindow.class));
                btnStart.setText("Start");
                started = false;
            } else {
                startService(new Intent(MainActivity.this, FloatingWindow.class));
                btnStart.setText("Stop");
                started = true;

            }
        }else {
            reqPermission();
        }

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_OK) {
            if (checkPermission()) {
                start_stop();
            } else {
                reqPermission();
            }
        }
    }


    private boolean checkPermission() {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
            if (!Settings.canDrawOverlays(this)) {
                reqPermission();
                return false;
            }
            else {
                return true;
            }
        }else{
            return true;
        }

    }

    private void reqPermission(){
        final AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
        alertBuilder.setCancelable(true);
        alertBuilder.setTitle("Screen overlay detected");
        alertBuilder.setMessage("Enable 'Draw over other apps' in your system setting.");
        alertBuilder.setPositiveButton("OPEN SETTINGS", new DialogInterface.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                        Uri.parse("package:" + getPackageName()));
                startActivityForResult(intent,RESULT_OK);
            }
        });

        alert = alertBuilder.create();
        alert.show();


    }
    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }







    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        int txt =adapterView.getSelectedItemPosition();
        if(adapterView.getId()==R.id.spinFrom)
        {
            String from=Code(txt);
            arr[0]=from;
            sharedPreferences.edit().putString("from",from).apply();

        }
        else
        {
            String to=Code(txt);
            arr[1]=to;
            sharedPreferences.edit().putString("to",to).apply();
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }




public String Code(int code)                       //This method return the specific code associated to that language
{
    if(code==0)
    {
        return "en";
    }
    if(code==1)
    {
        return "fr";
    }
    if(code==2)
    {
        return "de";
    }
    if(code==3)
    {
        return "hi-IN";
    }

    return "";
}
}