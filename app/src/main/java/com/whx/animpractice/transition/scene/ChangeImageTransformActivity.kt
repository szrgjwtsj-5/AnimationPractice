package com.whx.animpractice.transition.scene

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.transition.ChangeImageTransform
import android.transition.Scene
import android.transition.TransitionManager
import android.view.ViewGroup
import android.widget.Button
import com.whx.animpractice.R

class ChangeImageTransformActivity : AppCompatActivity() {

    private var startScene: Scene? = null
    private var endScene: Scene? = null

    private var sceneStart = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auto_transition)

        val sceneRoot = findViewById<ViewGroup>(R.id.scene_root)

        startScene = Scene.getSceneForLayout(sceneRoot, R.layout.layout_image_transform_start, this)
        endScene = Scene.getSceneForLayout(sceneRoot, R.layout.layout_image_transform_end, this)

        TransitionManager.go(startScene)

        findViewById<Button>(R.id.change_image_transform).setOnClickListener {
            changeImageTransform()
        }
    }

    private fun changeImageTransform() {
        TransitionManager.go(if (sceneStart) endScene else startScene, ChangeImageTransform())
        sceneStart = !sceneStart
    }
}