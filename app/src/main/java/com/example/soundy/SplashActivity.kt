package com.example.soundy

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.UiThread

class SplashActivity : AppCompatActivity() {

    lateinit var thing1 : ImageView
    lateinit var thing2 : ImageView
    lateinit var thing3 : ImageView
    lateinit var logo : ImageView
    lateinit var soundy : TextView
    lateinit var my : TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        thing1 = findViewById(R.id.thing1)
        thing2 = findViewById(R.id.thing2)
        thing3 = findViewById(R.id.thing3)
        logo = findViewById(R.id.ivProfile)
        soundy = findViewById(R.id.tvTitle)
        my = findViewById(R.id.textView)

        val intent = Intent(this, MainActivity::class.java)

        splashAnimation(intent)

    }
    companion object {
        private const val DURATION : Long = 3000
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }

    @UiThread
    private fun splashAnimation(intent: Intent){
        val imgAnim: Animation = AnimationUtils.loadAnimation(this, R.anim.alpha)
        thing1.startAnimation(imgAnim)
        val imgAnim2: Animation = AnimationUtils.loadAnimation(this, R.anim.alpha2)
        thing2.startAnimation(imgAnim2)
        val imgAnim3: Animation = AnimationUtils.loadAnimation(this, R.anim.alpha4)
        thing3.startAnimation(imgAnim3)
        val logoAnim: Animation = AnimationUtils.loadAnimation(this, R.anim.alpha3)
        logo.startAnimation(logoAnim)
        soundy.startAnimation(logoAnim)
        my.startAnimation(logoAnim)

        logoAnim.setAnimationListener(object : Animation.AnimationListener{
            override fun onAnimationEnd(animation: Animation) {
                overridePendingTransition(0, 0)
                startActivity(intent)
                overridePendingTransition(0, 0)
                finish()
            }
            override fun onAnimationStart(p0: Animation?) { }
            override fun onAnimationRepeat(p0: Animation?) { }
        })
    }

}