package com.whx.animpractice.transition.scene

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.transition.Scene
import android.transition.TransitionInflater
import android.transition.TransitionManager
import android.view.ViewGroup
import android.widget.Button
import com.whx.animpractice.R

class TransitionSetActivity : AppCompatActivity() {

    private var startScene: Scene? = null
    private var endScene: Scene? = null

    private var sceneStart = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auto_transition)

        val sceneRoot = findViewById<ViewGroup>(R.id.scene_root)

        startScene = Scene.getSceneForLayout(sceneRoot, R.layout.layout_scene_start, this)
        endScene = Scene.getSceneForLayout(sceneRoot, R.layout.transition_set_end, this)

        TransitionManager.go(startScene)

        findViewById<Button>(R.id.transition_set).setOnClickListener {
            toggleScene()
        }
    }

    private fun toggleScene() {
        TransitionManager.go(if (sceneStart) endScene else startScene,
                TransitionInflater.from(this).inflateTransition(R.transition.change_bounds_and_change_transform))
        sceneStart = !sceneStart
    }
}