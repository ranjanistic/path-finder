package com.pathfinder.saber

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.WindowManager
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat

class About : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        }
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.statusBarColor = ContextCompat.getColor(this,R.color.colorPrimaryDark)
            window.navigationBarColor = ContextCompat.getColor(this,R.color.colorPrimaryDark)
        }
        findViewById<ImageButton>(R.id.closeButt).setOnClickListener {
            finish()
            overridePendingTransition(R.anim.fadeliton, R.anim.fadelitoff)
        }
        findViewById<TextView>(R.id.version).text = BuildConfig.VERSION_NAME
        findViewById<Button>(R.id.premiumButton).setOnClickListener{
            startActivity(
                    Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/"))
            )
            //TODO: replace with saber+ link
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.fadeliton, R.anim.fadelitoff)
    }
}