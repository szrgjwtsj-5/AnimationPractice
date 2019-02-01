package com.whx.animpractice.transition.scene

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.transition.ChangeBounds
import android.transition.Scene
import android.transition.TransitionManager
import android.view.ViewGroup
import android.widget.Button
import com.whx.animpractice.R

class AutoTransitionActivity : AppCompatActivity() {

    private var startScene: Scene? = null
    private var endScene: Scene? = null

    private var sceneStart = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auto_transition)

        val sceneRoot = findViewById<ViewGroup>(R.id.scene_root)

        startScene = Scene.getSceneForLayout(sceneRoot, R.layout.layout_scene_start, this)
        endScene = Scene.getSceneForLayout(sceneRoot, R.layout.layout_scene_end, this)

        TransitionManager.go(startScene)

        findViewById<Button>(R.id.auto_transition).setOnClickListener {
            autoTransition()
        }

        findViewById<Button>(R.id.change_bounds).setOnClickListener {
            changeBounds()
        }

    }

    private fun autoTransition() {
        TransitionManager.go(if (sceneStart) endScene else startScene)
        sceneStart = !sceneStart
    }
    private fun changeBounds() {
        TransitionManager.go(if (sceneStart) endScene else startScene, ChangeBounds())
        sceneStart = !sceneStart
    }

}