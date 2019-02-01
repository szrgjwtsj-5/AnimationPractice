package com.whx.animpractice.transition.change_activity

import android.animation.AnimatorSet
import android.os.Build
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.support.v7.app.AppCompatActivity
import android.transition.Explode
import android.transition.Slide
import android.transition.Transition
import android.transition.TransitionSet
import android.view.Gravity
import android.view.View
import android.view.ViewAnimationUtils
import android.widget.ImageView
import com.whx.animpractice.R

class MakeSceneTransitionActivity : AppCompatActivity() {

    private val topBack by lazy { findViewById<ImageView>(R.id.image_up) }
    private val bottomBack by lazy { findViewById<View>(R.id.back_bottom) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            initTransition()
        }
        setContentView(R.layout.activity_make_scene_b)
    }


    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private fun initTransition() {
        val transitionSet = TransitionSet()

        val slide = Slide(Gravity.BOTTOM)
        slide.addTarget(R.id.image_1)
        transitionSet.addTransition(slide)

        val explode = Explode()
        explode.excludeTarget(android.R.id.statusBarBackground, true)
        explode.excludeTarget(android.R.id.navigationBarBackground, true)
        explode.excludeTarget(R.id.image_1, true)

        transitionSet.addTransition(explode)
        transitionSet.duration = 1000
        transitionSet.ordering = TransitionSet.ORDERING_SEQUENTIAL

        window.enterTransition = transitionSet

        transitionSet.addListener(object : Transition.TransitionListener {
            override fun onTransitionCancel(transition: Transition?) {

            }

            override fun onTransitionEnd(transition: Transition?) {
                topBack.visibility = View.VISIBLE
                bottomBack.visibility = View.VISIBLE

                val animationTop = ViewAnimationUtils.createCircularReveal(topBack, topBack.width/2, topBack.height/2, 0f, Math.max(topBack.width/2, topBack.height/2).toFloat())

                val animationBottom = ViewAnimationUtils.createCircularReveal(bottomBack, bottomBack.width, bottomBack.height, 0f, Math.hypot(topBack.width.toDouble(), topBack.height.toDouble()).toFloat())

                val animatorSet = AnimatorSet()
                animatorSet.duration = 1000
                animatorSet.playTogether(animationBottom, animationTop)
                animatorSet.start()

                transitionSet.removeListener(this)
            }

            override fun onTransitionPause(transition: Transition?) {

            }

            override fun onTransitionResume(transition: Transition?) {

            }

            override fun onTransitionStart(transition: Transition?) {
                topBack.visibility = View.GONE
                bottomBack.visibility = View.GONE
            }
        })
    }
}