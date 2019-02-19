package com.whx.animpractice.transition.share_element

import android.support.v4.view.PagerAdapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import com.whx.animpractice.R

class MyPageAdapter(private val imageLoadComplete: OnImageLoadComplete) : PagerAdapter() {

    interface OnImageLoadComplete {
        fun startEnterTransition()
    }

    private val data = arrayListOf<String>()
    private var itemView: ImageView? = null
    private var currentView: ImageView? = null

    override fun getCount() = data.size

    override fun isViewFromObject(view: View?, obj: Any?) = view == obj

    override fun instantiateItem(container: ViewGroup?, position: Int): Any {
//        val imageView = ImageView(container?.context)
//        imageView.scaleType = ImageView.ScaleType.CENTER_CROP

        val view = LayoutInflater.from(container?.context).inflate(R.layout.single_image, container, false)
        val imageView = view.findViewById<ImageView>(R.id.single_image)
        imageView.transitionName = data[position]
        itemView = imageView

        container?.addView(view)

        Picasso.get().load(data[position]).into(imageView, object : Callback {
            override fun onSuccess() {
                imageLoadComplete.startEnterTransition()
            }

            override fun onError(e: Exception?) {
                imageLoadComplete.startEnterTransition()
            }
        })
        return view
    }

    override fun destroyItem(container: ViewGroup?, position: Int, `object`: Any?) {
        if (`object` is View) {
            container?.removeView(`object`)
        }
    }

    override fun setPrimaryItem(container: ViewGroup?, position: Int, `object`: Any?) {
        super.setPrimaryItem(container, position, `object`)
        currentView = (`object` as View).findViewById(R.id.single_image)
    }

    fun getCurrentView(): ImageView? = currentView

    fun getItemView() = itemView

    fun setData(data: List<String>?) {
        data?.let {
            this.data.clear()
            this.data.addAll(it)
            notifyDataSetChanged()
        }
    }
}