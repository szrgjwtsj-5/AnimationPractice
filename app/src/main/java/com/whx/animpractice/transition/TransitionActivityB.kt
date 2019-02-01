package com.whx.animpractice.transition

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.transition.Fade
import android.view.Window
import com.whx.animpractice.R

class TransitionActivityB : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.requestFeature(Window.FEATURE_CONTENT_TRANSITIONS)

        setContentView(R.layout.transition_b)

        setupWindowAnimations()
    }
    private fun setupWindowAnimations() {
        val fade = Fade()
        fade.duration = 1000
        window.exitTransition = fade
    }
}