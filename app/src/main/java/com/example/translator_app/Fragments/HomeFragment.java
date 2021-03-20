package com.example.translator_app.Fragments;

import android.Manifest;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.translator_app.FloatingWindow;
import com.example.translator_app.R;

import static android.content.Context.MODE_PRIVATE;


public class HomeFragment extends Fragment implements AdapterView.OnItemSelectedListener {

    Spinner spinTo;
    Spinner spinFrom;
    Button btnStart;
    String arr[]=new String[2];
    boolean started=false;
    RadioGroup radioGroup;
    int rad=0;

    AlertDialog alert;
    boolean start=false;
    private static final int CODE_DRAW_OVER_OTHER_APP_PERMISSION = 2084;

    SharedPreferences sharedPreferences;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_home, container, false);



        sharedPreferences=getContext().getSharedPreferences("Translator",MODE_PRIVATE);




        //Audio Record permission dialog box
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.RECORD_AUDIO},1);
        }

        btnStart=view.findViewById(R.id.btnStart);
        spinFrom=view.findViewById(R.id.spinFrom);
        spinTo=view.findViewById(R.id.spinTo);
        //radioGroup=findViewById(R.id.radiogrp);

        //Spinner1
        ArrayAdapter<CharSequence> adp=ArrayAdapter.createFromResource(getContext(),R.array.languages,android.R.layout.simple_spinner_item);
        adp.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinTo.setAdapter(adp);
        spinTo.setOnItemSelectedListener((AdapterView.OnItemSelectedListener) this);

        //Spinner2
        ArrayAdapter<CharSequence> adpt=ArrayAdapter.createFromResource(getContext(),R.array.languages,android.R.layout.simple_spinner_item);
        adpt.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinFrom.setAdapter(adpt);
        spinFrom.setOnItemSelectedListener((AdapterView.OnItemSelectedListener) this);


        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnStart.setBackgroundResource(R.drawable.ic_stop);
/**                if(rad==1)
 {
 //Female
 sharedPreferences.edit().putInt("Gender",rad).apply();
 }
 else if(rad==2)
 {
 //Male
 sharedPreferences.edit().putInt("Gender",rad).apply();
 }
 else if(rad==0)
 {
 sharedPreferences.edit().putInt("Gender",rad).apply();
 RadioButton radFemale=findViewById(R.id.radFemale);
 radFemale.setChecked(true);
 Toast.makeText(MainActivity.this,"No Voice Selected,Female Voice selected as default",Toast.LENGTH_SHORT).show();
 }
 **/
                try{

                    String full=arr[0]+arr[1];
                    Toast.makeText(getContext(),full,Toast.LENGTH_LONG).show();

                }
                catch(NullPointerException e)
                {
                    Toast.makeText(getContext(),"Please Select Something",Toast.LENGTH_LONG).show();

                }
                start_stop();


            }
        });
        if (isMyServiceRunning(FloatingWindow.class)){
            started = true;
            btnStart.setBackgroundResource(R.drawable.ic_stop);
        }



        return view;
    }


    public void start_stop() {
        if (checkPermission()) {
            if (started) {
                getContext().stopService(new Intent(getContext(), FloatingWindow.class));
                btnStart.setBackgroundResource(R.drawable.ic_go);
                started = false;
            } else {
                getContext().startService(new Intent(getContext(), FloatingWindow.class));
                btnStart.setBackgroundResource(R.drawable.ic_stop);
                started = true;

            }
        }else {
            reqPermission();
        }

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Activity.RESULT_OK) {
            if (checkPermission()) {
                start_stop();
            } else {
                reqPermission();
            }
        }
    }


    private boolean checkPermission() {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
            if (!Settings.canDrawOverlays(getContext())) {
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
        final AlertDialog.Builder alertBuilder = new AlertDialog.Builder(getContext());
        alertBuilder.setCancelable(true);
        alertBuilder.setTitle("Screen overlay detected");
        alertBuilder.setMessage("Enable 'Draw over other apps' in your system setting.");
        alertBuilder.setPositiveButton("OPEN SETTINGS", new DialogInterface.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                        Uri.parse("package:" + getContext().getPackageName()));
                startActivityForResult(intent,Activity.RESULT_OK);
            }
        });

        alert = alertBuilder.create();
        alert.show();


    }
    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getContext().getSystemService(Context.ACTIVITY_SERVICE);
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
            return "hi";
        }

        return "";
    }
}