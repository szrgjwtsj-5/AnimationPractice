package com.whx.animpractice.transition.scene

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.TextView
import com.whx.animpractice.R

class SceneMainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_scene_main)

        findViewById<TextView>(R.id.auto_transition).setOnClickListener { startActivity(Intent(this, AutoTransitionActivity::class.java)) }

        findViewById<TextView>(R.id.change_transform).setOnClickListener { startActivity(Intent(this, ChangeTransformActivity::class.java)) }

        findViewById<TextView>(R.id.change_clip_bounds).setOnClickListener { startActivity(Intent(this, ChangeClipBoundsActivity::class.java)) }

        findViewById<TextView>(R.id.change_image_transform).setOnClickListener { startActivity(Intent(this, ChangeImageTransformActivity::class.java)) }

        findViewById<TextView>(R.id.visible_transition).setOnClickListener { startActivity(Intent(this, VisibleTransitionActivity::class.java)) }

        findViewById<TextView>(R.id.transition_set).setOnClickListener { startActivity(Intent(this, TransitionSetActivity::class.java)) }
    }
}