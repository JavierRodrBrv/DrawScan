package com.example.drawscan

import android.graphics.drawable.AnimationDrawable
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout

class MainActivity : AppCompatActivity() {

    private var miLayout: ConstraintLayout? = null
    private var animationDrawable: AnimationDrawable? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        miLayout = findViewById<View>(R.id.miFondo) as ConstraintLayout
        animationDrawable = miLayout!!.getBackground() as AnimationDrawable?
        animationDrawable?.setEnterFadeDuration(4000)
        animationDrawable?.setExitFadeDuration(4000)
        animationDrawable?.start()
    }


    override fun onResume() {
        super.onResume()
        if (this.animationDrawable!=null && !animationDrawable!!.isRunning) {
            animationDrawable!!.start();
        }
    }

    override fun onPause() {
        super.onPause()
        if (this.animationDrawable!=null && !animationDrawable!!.isRunning) {
            animationDrawable!!.stop();
        }
    }
}
