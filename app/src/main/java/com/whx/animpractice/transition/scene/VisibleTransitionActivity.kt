package com.whx.animpractice.transition.scene

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.transition.*
import android.view.ViewGroup
import android.widget.Button
import com.whx.animpractice.R

class VisibleTransitionActivity : AppCompatActivity() {

    private var startScene: Scene? = null
    private var endScene: Scene? = null

    private var sceneStart = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auto_transition)

        val sceneRoot = findViewById<ViewGroup>(R.id.scene_root)

        startScene = Scene.getSceneForLayout(sceneRoot, R.layout.layout_scene_start, this)
        endScene = Scene.getSceneForLayout(sceneRoot, R.layout.layout_scene_invisible, this)

        TransitionManager.go(startScene)

        findViewById<Button>(R.id.start_fade).setOnClickListener {
            startFade()
        }

        findViewById<Button>(R.id.start_slide).setOnClickListener {
            startSlide()
        }

        findViewById<Button>(R.id.start_explode).setOnClickListener {
            startExplode()
        }

    }

    private fun startFade() {
        TransitionManager.go(if (sceneStart) endScene else startScene, Fade())
        sceneStart = !sceneStart
    }
    private fun startSlide() {
        TransitionManager.go(if (sceneStart) endScene else startScene, Slide())
        sceneStart = !sceneStart
    }
    private fun startExplode() {
        TransitionManager.go(if (sceneStart) endScene else startScene, Explode())
        sceneStart = !sceneStart
    }
}