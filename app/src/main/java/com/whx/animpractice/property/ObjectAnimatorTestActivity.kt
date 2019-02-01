package com.whx.animpractice.property

import android.animation.AnimatorInflater
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.Path
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.AccelerateInterpolator
import android.widget.Button
import android.widget.TextView
import com.whx.animpractice.R

class ObjectAnimatorTestActivity : AppCompatActivity() {

    private lateinit var textView: TextView
    private lateinit var startBtn: Button

    @SuppressLint("NewApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_object_animator)

        textView = findViewById(R.id.anim_text)
        startBtn = findViewById(R.id.start)

        val path = Path().apply {
            moveTo(10f, 10f)
            quadTo(200f, 100f, 400f, 600f)
            quadTo(600f, 100f,800f, 0f)
        }
        val pathAnim = ObjectAnimator.ofFloat(textView, View.X, View.Y, path).apply {
            duration = 3000
            interpolator = AccelerateDecelerateInterpolator()
        }

        val colorAnim = ObjectAnimator.ofArgb(textView, "textColor", Color.parseColor("#dc5f26"), Color.parseColor("#2684DC")).apply {
            duration = 5000
        }

        val intAnim = ObjectAnimator.ofFloat(textView, "x", 0f).apply {
            duration = 3000
        }

        val xmlAnim = AnimatorInflater.loadAnimator(this, R.animator.obj_anim)
        xmlAnim.setTarget(textView)


        val animSet = AnimatorSet()
        animSet.play(pathAnim)
        animSet.play(colorAnim).with(intAnim).after(pathAnim).after(2000)

        startBtn.setOnClickListener {
            xmlAnim.start()
        }
    }
}