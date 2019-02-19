package com.whx.animpractice.transition.share_element

import android.os.Bundle
import android.transition.ChangeBounds
import android.transition.Transition
import android.view.View
import android.view.ViewAnimationUtils
import com.whx.animpractice.BaseActivity
import com.whx.animpractice.R

class SimpleAnotherActivity : BaseActivity() {

    private val topHalfBack by lazy { findViewById<View>(R.id.top_half_back) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_simple_another)

        initShareTransition()
    }

    private fun initShareTransition() {
        //直接在style里面设置了共享元素的Transition
        window.sharedElementEnterTransition = ChangeBounds()
        //		getWindow().setSharedElementExitTransition(new ChangeBounds());
        //		getWindow().setSharedElementReenterTransition(new ChangeBounds());
        //		getWindow().setSharedElementReturnTransition(new ChangeBounds());

        window.sharedElementEnterTransition.addListener(object : Transition.TransitionListener {
            override fun onTransitionStart(transition: Transition?) {
//                println("transition start---------")
                topHalfBack.visibility = View.GONE
            }

            override fun onTransitionResume(transition: Transition?) {
                println("transition resume---------")
            }

            override fun onTransitionPause(transition: Transition?) {
                println("transition pause---------")
            }

            override fun onTransitionEnd(transition: Transition?) {
//                println("transition end---------")
                topHalfBack.visibility = View.VISIBLE

                val width = topHalfBack.width
                val height = topHalfBack.height

                val animator = ViewAnimationUtils.createCircularReveal(topHalfBack, width, 0, 0f, Math.sqrt(width.toDouble() * width + height * height).toFloat())

                animator.duration = 1000
                animator.start()

                window.sharedElementEnterTransition.removeListener(this)
            }

            override fun onTransitionCancel(transition: Transition?) {
                println("transition cancel---------")
            }
        })
    }
}