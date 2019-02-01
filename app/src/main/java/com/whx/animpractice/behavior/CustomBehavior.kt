package com.whx.animpractice.behavior

import android.content.Context
import android.support.design.widget.CoordinatorLayout
import android.support.v4.view.ViewCompat
import android.util.AttributeSet
import android.util.Log
import android.view.View
import com.whx.animpractice.MyApplication
import com.whx.animpractice.R

class CustomBehavior(context: Context?, attrs: AttributeSet?) : CoordinatorLayout.Behavior<View>(context, attrs) {
    companion object {
        const val TAG = "CustomBehavior"
    }

    override fun onLayoutChild(parent: CoordinatorLayout, child: View?, layoutDirection: Int): Boolean {
        Log.e(TAG, "onLayoutChild...")
        val params = child?.layoutParams as? CoordinatorLayout.LayoutParams

        if (params != null && params.height == CoordinatorLayout.LayoutParams.MATCH_PARENT) {
            child.layout(0, 0, parent.width, parent.height)
            child.translationY = getHeaderHeight()
            return true
        }
        return super.onLayoutChild(parent, child, layoutDirection)
    }

    override fun onStartNestedScroll(coordinatorLayout: CoordinatorLayout, child: View, directTargetChild: View, target: View, axes: Int, type: Int): Boolean {
        return (axes and ViewCompat.SCROLL_AXIS_VERTICAL) != 0
    }

    override fun onNestedPreScroll(coordinatorLayout: CoordinatorLayout, child: View, target: View, dx: Int, dy: Int, consumed: IntArray, type: Int) {
        super.onNestedPreScroll(coordinatorLayout, child, target, dx, dy, consumed, type)

        // 在这个方法里面只处理向上滑动
        if (dy < 0) {
            return
        }
        val transY = child.translationY - dy
        Log.e(TAG, "transY: $transY ---------- child.getTranslationY(): ${child.translationY} --------- dy: $dy")
        if (transY > 0) {
            child.translationY = transY
            consumed[1] = dy
        }
    }

    override fun onNestedScroll(coordinatorLayout: CoordinatorLayout, child: View, target: View, dxConsumed: Int, dyConsumed: Int, dxUnconsumed: Int, dyUnconsumed: Int, type: Int) {
        super.onNestedScroll(coordinatorLayout, child, target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed, type)

        if (dyConsumed > 0) {
            return
        }
        val transY = child.translationY - dyUnconsumed
        Log.e(TAG, "transY: $transY ************* child.getTranslationY(): ${child.translationY} --------- dyUnconsumed: $dyUnconsumed")
        if (transY > 0 && transY < getHeaderHeight()) {
            child.translationY = transY
        }
    }

    private fun getHeaderHeight(): Float {
        return MyApplication.getAppContext().resources.getDimension(R.dimen.header_height)
    }
}