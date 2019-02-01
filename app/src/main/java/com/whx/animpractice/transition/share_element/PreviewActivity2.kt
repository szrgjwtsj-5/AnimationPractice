package com.whx.animpractice.transition.share_element

import android.app.Activity
import android.app.SharedElementCallback
import android.content.Intent
import android.os.Bundle
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.ViewTreeObserver
import com.whx.animpractice.R


class PreviewActivity2 : AppCompatActivity(), MyPageAdapter.OnImageLoadComplete {

    private var mStartPosition: Int = 0
    private var mCurrentPosition: Int = 0
    private var mIsReturning: Boolean = false
    private var adapter: MyPageAdapter? = null

    private val mCallback = object : SharedElementCallback() {

        override fun onMapSharedElements(names: MutableList<String>, sharedElements: MutableMap<String, View>) {
            if (mIsReturning) {
                val sharedElement = adapter!!.getItemView()
                if (sharedElement == null) {
                    names.clear()
                    sharedElements.clear()
                } else if (mStartPosition != mCurrentPosition) {
                    names.clear()
                    names.add(sharedElement.transitionName)
                    sharedElements.clear()
                    sharedElements[sharedElement.transitionName] = sharedElement
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_pager)
        initShareElement()
        mStartPosition = intent.getIntExtra(EXTRA_START_POSITION, 0)
        initView()
        initEvent()
        initData()
    }

    override fun finishAfterTransition() {
        mIsReturning = true
        val data = Intent()
        data.putExtra(RecyclerActivity2.EXTRA_START_POSITION, mStartPosition)
        data.putExtra(RecyclerActivity2.EXTRA_CURRENT_POSITION, mCurrentPosition)
        setResult(Activity.RESULT_OK, data)
        super.finishAfterTransition()
    }

    override fun startEnterTransition() {
        if (mCurrentPosition == mStartPosition) {
            adapter!!.getItemView()!!.viewTreeObserver.addOnPreDrawListener(object : ViewTreeObserver.OnPreDrawListener {
                override fun onPreDraw(): Boolean {
                    adapter!!.getItemView()!!.viewTreeObserver.removeOnPreDrawListener(this)
                    startPostponedEnterTransition()
                    return true
                }
            })
        }
    }

    private fun initView() {
        mCurrentPosition = mStartPosition
        val pager = findViewById<ViewPager>(R.id.viewPager)
        adapter = MyPageAdapter(this)
        adapter!!.setData(intent.getStringArrayListExtra("imageUrls"))

        pager.adapter = adapter
        pager.currentItem = mCurrentPosition
        pager.addOnPageChangeListener(object : ViewPager.SimpleOnPageChangeListener() {
            override fun onPageSelected(position: Int) {
                mCurrentPosition = position
            }
        })
    }

    private fun initEvent() {

    }

    private fun initData() {

    }

    private fun initShareElement() {
        postponeEnterTransition()
        setEnterSharedElementCallback(mCallback)
    }

    companion object {

        const val EXTRA_START_POSITION = "start_position"
    }
}
