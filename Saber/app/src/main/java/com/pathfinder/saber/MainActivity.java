package com.pathfinder.saber;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Camera;
import android.os.Build;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import java.security.Policy;

public class MainActivity extends Activity {
    private boolean flash, flashOn;
    private ImageButton btn;
    private Camera mcamera;
    private android.hardware.Camera.Parameters fparams;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.CAMERA}, 9999);
        }
        btn = findViewById(R.id.onOff);

        flash = getApplicationContext().getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);

        if (!flash) {
            AlertDialog alert = new AlertDialog.Builder(MainActivity.this).create();
            alert.setTitle("Hardware Missing");
            alert.setMessage("No suitable hardware detected on your device. Can't start the application.");
            alert.setButton(1, "Close", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }
            });
            alert.show();
        }

        getCamWare();

        toggleImage();

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (flashOn) {
                    turnOff();
                } else {
                    turnOn();
                }
            }
        });
    }

    private void getCamWare(){
        if(mcamera == null){
            try{
                mcamera = new Camera();
                //fparams = mcamera.get;
            } catch (RuntimeException e){
                Log.e("Camera Error.", e.getMessage());
            }
        }
    }

    private void turnOn(){
        if(!flashOn){
            if(mcamera == null || fparams == null){
                return;
            }

            //fparams = mcamera.getParameters();
            fparams.setFlashMode(android.hardware.Camera.Parameters.FLASH_MODE_TORCH);
            //mcamera.setParameters(fparams);
            mcamera.notify();
            flashOn = true;

            toggleImage();
        }
    }

    public void turnOff(){
        if(flashOn){
            if(mcamera == null || fparams == null){
                return;
            }

            //fparams = mcamera.getParameters();
            fparams.setFlashMode(android.hardware.Camera.Parameters.FLASH_MODE_OFF);
            //mcamera.setParams(fparams);
           // mcamera.stop();
            flashOn = false;

            toggleImage();
        }
    }

    private void toggleImage(){
        if(flashOn){
            btn.setImageResource(R.drawable.ic_launcher_background);
        } else{
            btn.setImageResource(R.drawable.ic_launcher_foreground);
        }
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
    }

    @Override
    protected void onPause(){
        super.onPause();
        turnOff();
    }

    @Override
    protected void onRestart(){
        super.onRestart();
    }

    @Override
    protected void onResume(){
        super.onResume();
        if(flash)
            turnOn();
    }

    @Override
    protected void onStart(){
        super.onStart();
        getCamWare();
    }

    @Override
    protected void onStop(){
        super.onStop();

        if(mcamera!=null){
           // mcamera.release();
            mcamera = null;
        }
    }
}
