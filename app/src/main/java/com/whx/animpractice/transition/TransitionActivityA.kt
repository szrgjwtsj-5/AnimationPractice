package com.whx.animpractice.transition

import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.transition.Slide
import android.view.Window
import android.widget.Button
import com.whx.animpractice.R
import com.whx.animpractice.transition.change_activity.MakeSceneTransitionActivity
import com.whx.animpractice.transition.scene.SceneMainActivity
import com.whx.animpractice.transition.share_element.RecyclerActivity

class TransitionActivityA : AppCompatActivity() {

    private val explodeCode by lazy { findViewById<Button>(R.id.explode_code) }
    private val sceneTest by lazy { findViewById<Button>(R.id.explode_xml) }
    private val slideCode by lazy { findViewById<Button>(R.id.slide_code) }
    private val slideXml by lazy { findViewById<Button>(R.id.slide_code) }
    private val exit by lazy { findViewById<Button>(R.id.exit_a) }
    private val toRecycler by lazy { findViewById<Button>(R.id.to_recycler) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.requestFeature(Window.FEATURE_CONTENT_TRANSITIONS)

        setContentView(R.layout.transition_a)

        setupWindowAnimations()

        explodeCode.setOnClickListener {
            startActivity(Intent(this, TransitionActivityB::class.java))
        }

        slideCode.setOnClickListener {
            val option = ActivityOptions.makeSceneTransitionAnimation(this)
            startActivity(Intent(this, MakeSceneTransitionActivity::class.java), option.toBundle())
        }
        toRecycler.setOnClickListener { startActivity(Intent(this, RecyclerActivity::class.java)) }

        sceneTest.setOnClickListener { startActivity(Intent(this, SceneMainActivity::class.java)) }
    }

    private fun setupWindowAnimations() {
        val slide = Slide()
        slide.duration = 1000
        window.exitTransition = slide
    }
}