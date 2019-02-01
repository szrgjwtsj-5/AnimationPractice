package com.whx.animpractice.transition.share_element

import android.app.Activity
import android.app.ActivityOptions
import android.app.SharedElementCallback
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewTreeObserver
import com.whx.animpractice.R
import java.util.*


class RecyclerActivity2 : AppCompatActivity(), MyRecyclerAdapter.ItemClickListener {

    private var mActivity: Activity? = null
    private var mContext: Context? = null
    private var mRecyclerView: RecyclerView? = null
    private var adapter: MyRecyclerAdapter? = null
    private var mReenterState: Bundle? = null
    private var urls: List<String>? = null

    private val mCallback = object : SharedElementCallback() {

        override fun onMapSharedElements(names: MutableList<String>, sharedElements: MutableMap<String, View>) {
            if (mReenterState != null) {
                //从别的界面返回当前界面
                val startingPosition = mReenterState!!.getInt(EXTRA_START_POSITION)
                val currentPosition = mReenterState!!.getInt(EXTRA_CURRENT_POSITION)
                if (startingPosition != currentPosition) {
                    val newTransitionName = urls!![currentPosition]
                    val newSharedElement = mRecyclerView!!.findViewWithTag<View>(newTransitionName)
                    if (newSharedElement != null) {
                        names.clear()
                        names.add(newTransitionName)
                        sharedElements.clear()
                        sharedElements[newTransitionName] = newSharedElement
                    }
                }
                mReenterState = null
            } else {
                //从当前界面进入到别的界面
                val navigationBar = findViewById<View>(android.R.id.navigationBarBackground)
                val statusBar = findViewById<View>(android.R.id.statusBarBackground)
                if (navigationBar != null) {
                    names.add(navigationBar.transitionName)
                    sharedElements[navigationBar.transitionName] = navigationBar
                }
                if (statusBar != null) {
                    names.add(statusBar.transitionName)
                    sharedElements[statusBar.transitionName] = statusBar
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_recycler)
        initShareElement()
        mActivity = this
        mContext = this
        initView()
        initEvent()
        initData()
    }

    override fun onActivityReenter(requestCode: Int, data: Intent) {
        super.onActivityReenter(requestCode, data)
        mReenterState = Bundle(data.extras)
        val startingPosition = mReenterState!!.getInt(EXTRA_START_POSITION)
        val currentPosition = mReenterState!!.getInt(EXTRA_CURRENT_POSITION)
        if (startingPosition != currentPosition) {
            mRecyclerView!!.scrollToPosition(currentPosition)
        }
        postponeEnterTransition()
        mRecyclerView!!.viewTreeObserver.addOnPreDrawListener(object : ViewTreeObserver.OnPreDrawListener {
            override fun onPreDraw(): Boolean {
                mRecyclerView!!.viewTreeObserver.removeOnPreDrawListener(this)
                mRecyclerView!!.requestLayout()
                startPostponedEnterTransition()
                return true
            }
        })
    }

    override fun onItemClick(view: View, url: String, position: Int) {
        val intent = Intent(mContext, ActivityBrowse::class.java)
        intent.putExtra(ActivityBrowse.EXTRA_START_POSITION, position)
        intent.putStringArrayListExtra("imageUrls", ArrayList(urls!!))

        val compat = ActivityOptions.makeSceneTransitionAnimation(mActivity, view, view.transitionName)
        startActivity(intent, compat.toBundle())
    }

    private fun initView() {
        mRecyclerView = findViewById(R.id.recyclerView)
        //        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        adapter = MyRecyclerAdapter()
        adapter!!.setOnItemClickListener(this)
        mRecyclerView!!.adapter = adapter
    }

    private fun initEvent() {

    }

    private fun initData() {
        urls = Arrays.asList("https://ws1.sinaimg.cn/large/0065oQSqly1fw0vdlg6xcj30j60mzdk7.jpg",
                "https://ws1.sinaimg.cn/large/0065oQSqly1fvexaq313uj30qo0wldr4.jpg",
                "https://ww1.sinaimg.cn/large/0065oQSqly1ftu6gl83ewj30k80tites.jpg",
                "http://ww1.sinaimg.cn/large/0065oQSqgy1ftt7g8ntdyj30j60op7dq.jpg",
                "https://ww1.sinaimg.cn/large/0065oQSqgy1ftrrvwjqikj30go0rtn2i.jpg",
                "https://ww1.sinaimg.cn/large/0065oQSqly1ftf1snjrjuj30se10r1kx.jpg",
                "https://ww1.sinaimg.cn/large/0065oQSqly1ftdtot8zd3j30ju0pt137.jpg",
                "http://ww1.sinaimg.cn/large/0073sXn7ly1ft82s05kpaj30j50pjq9v.jpg",
                "https://ww1.sinaimg.cn/large/0065oQSqly1ft5q7ys128j30sg10gnk5.jpg",
                "https://ww1.sinaimg.cn/large/0065oQSqgy1ft4kqrmb9bj30sg10fdzq.jpg")

        adapter!!.setData(urls)
    }

    private fun initShareElement() {
        setExitSharedElementCallback(mCallback)
    }

    companion object {
        const val EXTRA_START_POSITION = "start_position"
        const val EXTRA_CURRENT_POSITION = "current_position"
    }
}
