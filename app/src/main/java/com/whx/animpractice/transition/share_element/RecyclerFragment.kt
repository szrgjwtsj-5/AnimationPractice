package com.whx.animpractice.transition.share_element

import android.app.ActivityOptions
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.support.v4.app.Fragment
import android.support.v4.app.SharedElementCallback
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.ImageView
import com.whx.animpractice.R

class RecyclerFragment : Fragment(), MyRecyclerAdapter.ItemClickListener {

    companion object {
        @JvmStatic
        fun getInstance(): RecyclerFragment {
            val fragment = RecyclerFragment()
            return fragment
        }
    }

    private val recyclerView by lazy { view?.findViewById<RecyclerView>(R.id.recyclerView) }

    private var urls: ArrayList<String>? = null
    private var mReenterState: Bundle? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initSharedElement()
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater?.inflate(R.layout.layout_recycler, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        makeData()
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onItemClick(view: View, url: String, position: Int) {
        val intent = Intent(context, PreviewActivity::class.java).apply {
            putExtra("position", position)
            putStringArrayListExtra("imageUrls", urls)
        }

        val option = ActivityOptions.makeSceneTransitionAnimation(activity, view, view.transitionName)

        startActivity(intent, option.toBundle())
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private fun initSharedElement() {
//        exitTransition = TransitionInflater.from(context)
//                .inflateTransition(R.transition.grid_exit_transition)

        activity.setExitSharedElementCallback(object : SharedElementCallback() {

            override fun onMapSharedElements(names: MutableList<String>?, sharedElements: MutableMap<String, View>?) {
//                super.onMapSharedElements(names, sharedElements)
                if (mReenterState != null) {
                    //从别的界面返回当前界面
                    val startingPosition = mReenterState!!.getInt("startPos")
                    val currentPosition = mReenterState!!.getInt("curPos")
                    if (startingPosition != currentPosition) {
                        val newTransitionName = urls?.get(currentPosition)
                        val newSharedElement = recyclerView?.findViewWithTag<ImageView>(newTransitionName)

                        if (newSharedElement != null) {
                            names?.clear()
                            names?.add(newTransitionName!!)
                            sharedElements?.clear()
                            sharedElements?.put(newTransitionName!!, newSharedElement)
                        }
                    }

                    mReenterState = null
                } else {
                    //从当前界面进入到别的界面
                    val navigationBar = activity.findViewById<View>(android.R.id.navigationBarBackground)
                    val statusBar = activity.findViewById<View>(android.R.id.statusBarBackground)
                    if (navigationBar != null) {
                        names?.add(navigationBar.transitionName)
                        sharedElements?.put(navigationBar.transitionName, navigationBar)
                    }
                    if (statusBar != null) {
                        names?.add(statusBar.transitionName)
                        sharedElements?.put(statusBar.transitionName, statusBar)
                    }
                }
            }
        })
    }

    private fun makeData() {
        urls = arrayListOf("https://ws1.sinaimg.cn/large/0065oQSqly1fw0vdlg6xcj30j60mzdk7.jpg",
                "https://ws1.sinaimg.cn/large/0065oQSqly1fvexaq313uj30qo0wldr4.jpg",
                "https://ww1.sinaimg.cn/large/0065oQSqly1ftu6gl83ewj30k80tites.jpg",
                "http://ww1.sinaimg.cn/large/0065oQSqgy1ftt7g8ntdyj30j60op7dq.jpg",
                "https://ww1.sinaimg.cn/large/0065oQSqgy1ftrrvwjqikj30go0rtn2i.jpg",
                "https://ww1.sinaimg.cn/large/0065oQSqly1ftf1snjrjuj30se10r1kx.jpg",
                "https://ww1.sinaimg.cn/large/0065oQSqly1ftdtot8zd3j30ju0pt137.jpg",
                "http://ww1.sinaimg.cn/large/0073sXn7ly1ft82s05kpaj30j50pjq9v.jpg",
                "https://ww1.sinaimg.cn/large/0065oQSqly1ft5q7ys128j30sg10gnk5.jpg",
                "https://ww1.sinaimg.cn/large/0065oQSqgy1ft4kqrmb9bj30sg10fdzq.jpg")

        val adapter = MyRecyclerAdapter()
        recyclerView?.adapter = adapter
        adapter.setData(urls)
        adapter.setOnItemClickListener(this)
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    fun reEnter(bundle: Bundle?) {
        bundle?.let {
            mReenterState = it

            val startPos = it.getInt("startPos")
            val curPos = it.getInt("curPos")

            if (startPos != curPos) {
                recyclerView?.scrollToPosition(curPos)
            }
            activity.postponeEnterTransition()

            recyclerView?.viewTreeObserver?.addOnPreDrawListener(object : ViewTreeObserver.OnPreDrawListener{
                override fun onPreDraw(): Boolean {
                    recyclerView?.viewTreeObserver?.removeOnPreDrawListener(this)
                    recyclerView?.requestLayout()

                    activity.startPostponedEnterTransition()
                    return true
                }
            })
        }
    }
}