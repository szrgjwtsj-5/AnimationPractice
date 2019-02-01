package com.whx.animpractice.behavior

import android.os.Bundle
import android.support.design.widget.CoordinatorLayout
import android.support.design.widget.SwipeDismissBehavior
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import com.whx.animpractice.R

class SwipeBehaviorActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_swipe_behavior)

        val swipeLayout = findViewById<LinearLayout>(R.id.swipe_layout)

        val swipeBehavior = SwipeDismissBehavior<View>()

        swipeBehavior.setSwipeDirection(SwipeDismissBehavior.SWIPE_DIRECTION_START_TO_END)
        swipeBehavior.setStartAlphaSwipeDistance(10f)
        swipeBehavior.setSensitivity(0.2f)

        swipeBehavior.setListener(object : SwipeDismissBehavior.OnDismissListener {
            override fun onDismiss(view: View?) {
                Log.e("---------", "onDismiss")
            }

            override fun onDragStateChanged(state: Int) {
                Log.e("---------", "onDragStateChanged  state = $state")
            }
        })

        val layoutParams = swipeLayout.layoutParams as? CoordinatorLayout.LayoutParams
        if (layoutParams != null) {
            layoutParams.behavior = swipeBehavior
        }


    }
}