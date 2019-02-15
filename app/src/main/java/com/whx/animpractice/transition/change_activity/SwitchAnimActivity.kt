package com.whx.animpractice.transition.change_activity

import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.FrameLayout
import com.whx.animpractice.BaseActivity
import com.whx.animpractice.R

class SwitchAnimActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.empty_layout)

        val container = findViewById<FrameLayout>(R.id.container)

        val button = Button(this).apply {
            width = 250
            height = 100
            text = "jump"
            setOnClickListener { slide() }
        }

        container.addView(button)
    }

    private fun jumpToOther() {
        scaleUp()
    }

    private fun scaleUp() {
        val view = window.decorView

        val options = ActivityOptions.makeScaleUpAnimation(view, 0, 0, view.width, view.height)

        val intent = Intent(this, SwitchAnimTargetActivity::class.java)

        startActivity(intent, options.toBundle())
    }

    private fun slide() {
        startActivity(Intent(this, SwitchAnimTargetActivity::class.java), ActivityOptions.makeSceneTransitionAnimation(this).toBundle())
    }
}