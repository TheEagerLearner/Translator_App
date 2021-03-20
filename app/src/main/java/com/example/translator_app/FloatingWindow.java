package com.example.translator_app;


import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.os.IBinder;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.speech.tts.Voice;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.chaquo.python.PyObject;
import com.chaquo.python.Python;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

public class FloatingWindow extends Service {

    WindowManager wm;
    LinearLayout ll;
    int i=1;
    SpeechRecognizer speechRecognizer;
    Intent speechIntent;
    String text="";
    String translated_text="";

    Python py = Python.getInstance();
    final PyObject pythonObj = py.getModule("translate");

    TextToSpeech textToSpeech;

    SharedPreferences sharedPreferences;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        sharedPreferences=getSharedPreferences("Translator",MODE_PRIVATE);


        wm = (WindowManager) getSystemService(WINDOW_SERVICE);

        ll = new LinearLayout(this);
        ll.setBackgroundColor(Color.TRANSPARENT);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        ll.setLayoutParams(layoutParams);

        final WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);

        params.gravity = Gravity.CENTER;
        params.x = 0;
        params.y = 0;

        final ImageView open = new ImageView(this);
        open.setImageResource(R.drawable.ic_translateon);
        ViewGroup.LayoutParams btnparam = new ViewGroup.LayoutParams(
                150,150);
        open.setLayoutParams(btnparam);

        ll.addView(open);
        wm.addView(ll,params);

        //Setting the language here
        String from=sharedPreferences.getString("from","en");
        String to=sharedPreferences.getString("to","en");

        final  String langOut=to;                                             //here for now i have set the language as the String variable to
        final String langIn=from;
        final Locale locOut=new Locale(langOut);     //Output Langauge do not change
        final Locale locIn=new Locale(langOut);     //Change langOut to LangIn her form input language



        //SpeechToText is done over here
        speechRecognizer=SpeechRecognizer.createSpeechRecognizer(this);
        speechIntent=new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        speechIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        speechIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, locOut.getLanguage());

        speechRecognizer.setRecognitionListener(new RecognitionListener() {
            @Override
            public void onReadyForSpeech(Bundle bundle) {

            }

            @Override
            public void onBeginningOfSpeech() {

            }

            @Override
            public void onRmsChanged(float v) {

            }

            @Override
            public void onBufferReceived(byte[] bytes) {

            }

            @Override
            public void onEndOfSpeech() {
                open.setImageResource(R.drawable.ic_translateon);
                i=i+1;

            }

            @Override
            public void onError(int i) {
                Toast.makeText(FloatingWindow.this,"Error in recognizing.\nPlease Try again",Toast.LENGTH_SHORT).show();
                open.setImageResource(R.drawable.ic_translateon);
                i=i+1;
            }

            @Override
            public void onResults(Bundle bundle) {

                ArrayList<String> data=bundle.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
                if(data!=null)
                {
                    text=data.get(0);
                    PyObject obj = pythonObj.callAttr("translate",text , langIn, langOut);
                    translated_text = obj.toString();
                    speak(translated_text);
                    Toast.makeText(FloatingWindow.this,translated_text,Toast.LENGTH_LONG).show();
                    //txtSpoken.setText(text);
                }

            }

            @Override
            public void onPartialResults(Bundle bundle) {

            }

            @Override
            public void onEvent(int i, Bundle bundle) {

            }

        }
        );

        //TextToSpeech is Done Over here

        textToSpeech=new TextToSpeech(FloatingWindow.this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int i) {
                if(i==TextToSpeech.SUCCESS)
                {
                    int result=textToSpeech.setLanguage(Locale.forLanguageTag(locIn.getLanguage()));

                    if(result==TextToSpeech.LANG_MISSING_DATA || result==TextToSpeech.LANG_NOT_SUPPORTED)
                    {
                        Toast.makeText(FloatingWindow.this,"Language Not Supported",Toast.LENGTH_LONG).show();
                    }

                }
                else
                {
                    Toast.makeText(FloatingWindow.this,"Initialization Failed",Toast.LENGTH_LONG).show();
                }
            }
        });




        open.setOnTouchListener(new View.OnTouchListener() {
            WindowManager.LayoutParams updatepar = params;
            double x;
            double y;
            double px;
            double py;

            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                switch (motionEvent.getAction()){
                    case MotionEvent.ACTION_DOWN:

                        x = updatepar.x;
                        y = updatepar.y;

                        px = motionEvent.getRawX();
                        py = motionEvent.getRawY();

                        break;

                    case MotionEvent.ACTION_MOVE:

                        updatepar.x = (int) (x+(motionEvent.getRawX()-px));
                        updatepar.y = (int) (y+(motionEvent.getRawY()-py));

                        wm.updateViewLayout(ll,updatepar);

                    default:
                        break;

                }

                return false;

            }
        });

        open.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(i%2!=0)
                {
                    open.setImageResource(R.drawable.ic_translateoff);
                    speechRecognizer.startListening(speechIntent);

                }
                else
                {
                    open.setImageResource(R.drawable.ic_translateon);
                    speechRecognizer.stopListening();
                    text="";
                }
                i=i+1;

            }
        });



    }
    private void speak(String t)
    {
        //take the input t which is input text from here
        String val=t;  //change t to the spi outputed text/python code
        textToSpeech.speak(val,TextToSpeech.QUEUE_FLUSH,null);

/**        int gender=sharedPreferences.getInt("Gender",1);
        if(gender==1)
        {
          //Female Voice
            Set<String> a=new HashSet<>();
            a.add("female");
            Voice v=new Voice("en-us-x-sfg#female_2-local",new Locale(langOut),400,200,true,a);
            textToSpeech.setVoice(v);
            textToSpeech.setSpeechRate(0.8f);

        }
        else
        {   //Male voice
            Set<String> a=new HashSet<>();
            a.add("male");                          //here you can give male if you want to select male voice.
            Voice v=new Voice("en-us-x-sfg#male_2-local",new Locale(langOut),400,200,true,a);
            textToSpeech.setVoice(v);
            textToSpeech.setSpeechRate(0.8f);
            textToSpeech.speak(val,TextToSpeech.QUEUE_FLUSH,null);
        }

     **/

    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        stopSelf();
        wm.removeView(ll);
        textToSpeech.stop();
        textToSpeech.shutdown();
    }
}
