package com.whx.animpractice.transition.scene

import android.graphics.Rect
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.transition.ChangeClipBounds
import android.transition.Scene
import android.transition.TransitionManager
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import com.whx.animpractice.R

class ChangeClipBoundsActivity : AppCompatActivity() {

    private var startScene: Scene? = null
    private var endScene: Scene? = null

    private var sceneStart = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auto_transition)

        val sceneRoot = findViewById<ViewGroup>(R.id.scene_root)


        val startView = LayoutInflater.from(this).inflate(R.layout.layout_clip_scene, sceneRoot, false)
        startView.findViewById<ImageView>(R.id.image_change_clip_bounds).clipBounds = Rect(0, 0, 300, 300)

        val endView = LayoutInflater.from(this).inflate(R.layout.layout_clip_scene, sceneRoot, false)
        endView.findViewById<ImageView>(R.id.image_change_clip_bounds).clipBounds = Rect(300, 300, 600, 600)

        startScene = Scene(sceneRoot, startView)
        endScene = Scene(sceneRoot, endView)

        TransitionManager.go(startScene)

        findViewById<Button>(R.id.change_clip_bounds).setOnClickListener {
            toggleScene()
        }
    }

    private fun toggleScene() {
        TransitionManager.go(if (sceneStart) endScene else startScene, ChangeClipBounds())
        sceneStart = !sceneStart
    }
}