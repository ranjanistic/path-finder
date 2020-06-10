package com.pathfinder.saber

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.hardware.camera2.CameraAccessException
import android.hardware.camera2.CameraManager
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.InterstitialAd
import com.google.android.gms.ads.MobileAds
import com.google.android.material.snackbar.Snackbar


class MainActivity : AppCompatActivity() {
    private var mCameraManager: CameraManager? = null
    private var mCameraId: String? = null
    private lateinit var flash: ImageView
    private var subhead: TextView? = null
    private var fadeoff: Animation? = null
    private var fadeon: Animation? = null
    private var state = true
    lateinit var mInterstitialAd: InterstitialAd
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)
        setContentView(R.layout.activity_main)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        }
        window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            window.attributes.layoutInDisplayCutoutMode =
                    WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES
        }
        MobileAds.initialize(this, "ca-app-pub-9252793240012402~1634633493")
        mInterstitialAd = InterstitialAd(this)

        //TODO: replace with actual adUnitId ca-app-pub-9252793240012402/9129980131
        mInterstitialAd.adUnitId = "ca-app-pub-3940256099942544/1033173712"
        mInterstitialAd.loadAd(AdRequest.Builder().build())
        fadeoff = AnimationUtils.loadAnimation(applicationContext, R.anim.fadelitoff)
        fadeon = AnimationUtils.loadAnimation(applicationContext, R.anim.fadeliton)
        subhead = findViewById(R.id.subheading)
        flash = findViewById(R.id.flashToggle)

        findViewById<ImageButton>(R.id.infoButt).setOnClickListener {
            startActivity(Intent(this@MainActivity, About::class.java))
            overridePendingTransition(R.anim.fadeliton, R.anim.fadelitoff)
        }
        var isFlashAvailable = applicationContext.packageManager
                .hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mCameraManager = getSystemService(Context.CAMERA_SERVICE) as CameraManager
            try {
                mCameraId = mCameraManager?.cameraIdList?.get(0)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    mCameraManager?.setTorchMode(mCameraId!!, false)
                } else isFlashAvailable = false
            } catch (e: CameraAccessException) {
                isFlashAvailable = false
            }
        } else isFlashAvailable = false
        flash.setOnClickListener {
            if (!isFlashAvailable) {
                showNoFlashError()
            } else {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    switchFlashLight(state)
                } else showNoFlashError()
            }
        }
    }

    private fun adListener() {
        mInterstitialAd.adListener = object : AdListener() {
            override fun onAdClosed() {
                Snackbar.make(findViewById(R.id.mainActivity), getString(R.string.remove_ads), 8000)
                        .setAction("Show me") {
                            Toast.makeText(applicationContext, getString(R.string.unlock_premium), Toast.LENGTH_LONG).show()
                            startActivity(Intent(this@MainActivity, About::class.java))
                            overridePendingTransition(R.anim.fadeliton, R.anim.fadelitoff)
                        }
            }
        }
    }
    override fun onRestart() {
        super.onRestart()
        if (mInterstitialAd.isLoaded) {
            mInterstitialAd.show()
            adListener()
        }
    }
    private fun showNoFlashError() {
        val alert = AlertDialog.Builder(this).create()
        alert.setTitle(getString(R.string.flash_error_title))
        alert.setMessage(getString(R.string.flash_error_message))
        alert.setIcon(ContextCompat.getDrawable(this, R.drawable.ic_launcher_foreground))
        alert.setButton(DialogInterface.BUTTON_POSITIVE, getString(R.string.sad)) { _, _ -> finish() }
        alert.show()
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun switchFlashLight(status: Boolean) {
        try {
            mCameraManager?.setTorchMode(mCameraId!!, status)
            if (status) {
                window.statusBarColor = ContextCompat.getColor(this, R.color.colorPrimaryDark)
                window.navigationBarColor = ContextCompat.getColor(this, R.color.colorPrimaryDark)
                subhead!!.startAnimation(fadeon)
                subhead!!.setText(R.string.textlit)
                flash.startAnimation(fadeon)
                flash.setBackgroundResource(R.drawable.square_btn_teal_dark)
                flash.setImageDrawable(getDrawable(R.drawable.roomlit))
            } else {
                window.statusBarColor = ContextCompat.getColor(this, R.color.teal)
                window.navigationBarColor = ContextCompat.getColor(this, R.color.teal)
                subhead!!.startAnimation(fadeon)
                subhead!!.setText(R.string.textunlit)
                flash.startAnimation(fadeon)
                flash.setBackgroundResource(R.drawable.square_btn_teal)
                flash.setImageDrawable(getDrawable(R.drawable.ic_roomunlit))
            }
            state = !status
        } catch (e: CameraAccessException) {
            showNoFlashError()
        }
    }
}