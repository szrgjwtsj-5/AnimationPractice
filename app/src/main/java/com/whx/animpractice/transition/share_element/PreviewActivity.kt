package com.whx.animpractice.transition.share_element

import android.app.Activity
import android.app.SharedElementCallback
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.ImageView
import com.whx.animpractice.R

class PreviewActivity : AppCompatActivity(), MyPageAdapter.OnImageLoadComplete {

    private val viewPager by lazy { findViewById<ViewPager>(R.id.viewPager) }
    private var mAdapter: MyPageAdapter? = null

    private var curPos = 0
    private var startPos = 0

    private var imageUrls: List<String>? = null

    private var mIsReturning = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_pager)

        initSharedElement()

        curPos = intent.getIntExtra("position", 0)
        startPos = intent.getIntExtra("position", 0)

        imageUrls = intent.getStringArrayListExtra("imageUrls")

        mAdapter = MyPageAdapter(this)
        mAdapter!!.setData(imageUrls)
        viewPager.adapter = mAdapter

        viewPager.currentItem = startPos
        viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {}

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
                curPos = position
            }

            override fun onPageSelected(position: Int) {
                curPos = position
            }
        })
    }

    override fun startEnterTransition() {
        startPostponedEnterTransition()
//        if (startPos == curPos) {
//            val mAlbumImage = mAdapter?.getItemView()
//
//            mAlbumImage?.viewTreeObserver?.addOnPreDrawListener(object : ViewTreeObserver.OnPreDrawListener {
//                override fun onPreDraw(): Boolean {
//                    mAlbumImage.viewTreeObserver.removeOnPreDrawListener(this)
//                    startPostponedEnterTransition()
//                    return true
//                }
//            })
//        }
    }

    override fun finishAfterTransition() {
        mIsReturning = true
        val data = Intent().apply {
            putExtra("startPos", startPos)
            putExtra("curPos", curPos)
        }
        setResult(Activity.RESULT_OK, data)
        super.finishAfterTransition()
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private fun initSharedElement() {
        postponeEnterTransition()
        setEnterSharedElementCallback(object : SharedElementCallback() {
            override fun onMapSharedElements(names: MutableList<String>?, sharedElements: MutableMap<String, View>?) {
                if (mIsReturning) {
                    val sharedElement = viewPager.getChildAt(curPos) as? ImageView

                    if (sharedElement == null) {
                        names?.clear()
                        sharedElements?.clear()
                    } else if (startPos == curPos) {
                        names?.clear()
                        names?.add(sharedElement.transitionName)
                        sharedElements?.clear()
                        sharedElements?.put(sharedElement.transitionName, sharedElement)
                    }
                }
            }
        })
    }
}