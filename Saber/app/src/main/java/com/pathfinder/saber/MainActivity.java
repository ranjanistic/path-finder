package com.pathfinder.saber;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Layout;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.ToggleButton;

public class MainActivity extends AppCompatActivity {

    private CameraManager mCameraManager;
    private String mCameraId;
    private ToggleButton toggleButton;
    Window window;
    ConstraintLayout layout;
    TextView subhead;
    Animation fadeoff, fadeon;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().getDecorView().setBackgroundColor(getColor(R.color.teal));
        window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(this.getResources().getColor(R.color.teal));
        window.setNavigationBarColor(this.getResources().getColor(R.color.teal));


        fadeoff = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fadelitoff);
        fadeon = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fadeliton);
        subhead = findViewById(R.id.subheading);
        ImageButton info = findViewById(R.id.infoButt);
        info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, About.class);
                startActivity(intent);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        });

        boolean isFlashAvailable = getApplicationContext().getPackageManager()
                .hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);

        if (!isFlashAvailable) {
            showNoFlashError();
        }

        toggleButton = findViewById(R.id.toggleButton);
        mCameraManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
        try {
            mCameraId = mCameraManager.getCameraIdList()[0];
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }

        try {
            mCameraManager.setTorchMode(mCameraId, false);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }

        toggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                //toggleButton.startAnimation(fadeoff);
                switchFlashLight(isChecked);
                toggleImag(isChecked);
            }
        });
    }

    public void showNoFlashError() {
        AlertDialog alert = new AlertDialog.Builder(this)
                .create();
        alert.setTitle("Error");
        alert.setMessage("Flash hardware not found.");
        alert.setButton(DialogInterface.BUTTON_POSITIVE, "Okay", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        alert.show();
    }

    public void switchFlashLight(boolean status) {
        try {

            mCameraManager.setTorchMode(mCameraId, status);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    public void toggleImag(boolean status){
        if(status){
            window.setStatusBarColor(this.getResources().getColor(R.color.colorPrimaryDark));
            window.setNavigationBarColor(this.getResources().getColor(R.color.colorPrimaryDark));
            getWindow().getDecorView().setBackgroundColor(getColor(R.color.colorPrimaryDark));
            subhead.startAnimation(fadeon);
            subhead.setText(R.string.textlit);
            toggleButton.startAnimation(fadeon);
            toggleButton.setButtonDrawable(R.drawable.roomlitpartsc);

        } else{
            window.setStatusBarColor(this.getResources().getColor(R.color.teal));
            window.setNavigationBarColor(this.getResources().getColor(R.color.teal));
            getWindow().getDecorView().setBackgroundColor(getColor(R.color.teal));
            subhead.startAnimation(fadeon);
            subhead.setText(R.string.textunlit);
            toggleButton.startAnimation(fadeon);
            toggleButton.setButtonDrawable(R.drawable.ic_roomunlit);
        }
    }

}