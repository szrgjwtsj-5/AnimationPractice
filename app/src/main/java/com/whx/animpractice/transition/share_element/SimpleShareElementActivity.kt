package com.whx.animpractice.transition.share_element

import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.whx.animpractice.BaseActivity
import com.whx.animpractice.R

class SimpleShareElementActivity : BaseActivity() {

    private val sampleImage1 by lazy { findViewById<ImageView>(R.id.sample_image1) }
    private val sampleImage2 by lazy { findViewById<ImageView>(R.id.sample_image2) }
    private val jumpBtn by lazy { findViewById<Button>(R.id.jump_to_other) }
    private val sampleText by lazy { findViewById<TextView>(R.id.sample_text) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_simple_share)

        // transitionName 已在xml 中定义
        val imagePair1 = android.util.Pair<View, String>(sampleImage1, sampleImage1.transitionName)
        val imagePair2 = android.util.Pair<View, String>(sampleImage2, sampleImage2.transitionName)
        val textPair = android.util.Pair<View, String>(sampleText, sampleText.transitionName)

        jumpBtn.setOnClickListener {
            val intent = Intent(this, SimpleAnotherActivity::class.java)
            val options = ActivityOptions.makeSceneTransitionAnimation(this, imagePair1, imagePair2, textPair)

            startActivity(intent, options.toBundle())
        }
    }
}