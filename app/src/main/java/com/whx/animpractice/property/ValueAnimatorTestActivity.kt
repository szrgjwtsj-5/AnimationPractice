package com.whx.animpractice.property

import android.animation.*
import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.AccelerateInterpolator
import android.view.animation.BounceInterpolator
import android.widget.Button
import android.widget.TextView
import com.whx.animpractice.R

class ValueAnimatorTestActivity : AppCompatActivity() {

    private lateinit var animText: TextView
    private lateinit var startBtn: Button

    @SuppressLint("NewApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_value_animator)

        animText = findViewById(R.id.anim_text)
        startBtn = findViewById(R.id.start_anim)

        val intAnim = ValueAnimator.ofInt(0, 1000).apply {
            duration = 5000
            startDelay = 1000
            interpolator = BounceInterpolator()
            addUpdateListener {
                animText.x = (it.animatedValue as Int).toFloat()
                animText.rotationY = (it.animatedValue as Int).toFloat()
            }
            addListener(object : Animator.AnimatorListener {
                override fun onAnimationStart(animation: Animator?) {
                    println("-------------start")
                }

                override fun onAnimationEnd(animation: Animator?) {
                    println("-------------end")
                }

                override fun onAnimationCancel(animation: Animator?) {
                    println("-------------cancel")
                }

                override fun onAnimationRepeat(animation: Animator?) {
                    println("-------------repeat")
                }
            })
        }

        val colorAnim = ValueAnimator.ofArgb(Color.parseColor("#10adc9"), Color.parseColor("#F54F83"), Color.parseColor("#10adc9")).apply {
            duration = 5000
            addUpdateListener {
                animText.setTextColor(it.animatedValue as Int)
            }
        }

        val objAnim = ValueAnimator.ofObject(TypeEvaluator<Data> { fraction, startValue, endValue ->
            Data((startValue.name.toFloat() + fraction * (endValue.name.toInt() - startValue.name.toInt())).toString(), ArgbEvaluator().evaluate(fraction, startValue.color, endValue.color) as Int) },
                Data("0", Color.parseColor("#10adc9")),
                Data("1000", Color.parseColor("#F54F83")),
                Data("0", Color.parseColor("#10adc9"))).apply {

            duration = 5000
            addUpdateListener {
                animText.text = (it.animatedValue as Data).name
                animText.setTextColor((it.animatedValue as Data).color)
            }
        }


        val xmlAnim = AnimatorInflater.loadAnimator(this, R.animator.value_anim)
        (xmlAnim as ValueAnimator).addUpdateListener {
            animText.x = (it.animatedValue as Int).toFloat()
            animText.y = (it.animatedValue as Int).toFloat()

            animText.rotationY = (it.animatedValue as Int).toFloat() / 2
        }

        startBtn.setOnClickListener {
            xmlAnim.start()
        }
    }

    class Data(val name: String, val color: Int)
}