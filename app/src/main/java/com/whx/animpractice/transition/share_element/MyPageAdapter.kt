package com.whx.animpractice.transition.share_element

import android.support.v4.view.PagerAdapter
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso

class MyPageAdapter(private val imageLoadComplete: OnImageLoadComplete) : PagerAdapter() {

    interface OnImageLoadComplete {
        fun startEnterTransition()
    }

    private val data = arrayListOf<String>()
    private var itemView: ImageView? = null

    override fun getCount() = data.size

    override fun isViewFromObject(view: View?, obj: Any?) = view == obj

    override fun instantiateItem(container: ViewGroup?, position: Int): Any {
        val imageView = ImageView(container?.context)
        imageView.scaleType = ImageView.ScaleType.CENTER_CROP
        imageView.transitionName = data[position]

        itemView = imageView

        container?.addView(imageView)

        Picasso.get().load(data[position]).into(imageView, object : Callback {
            override fun onSuccess() {
                imageLoadComplete.startEnterTransition()
            }

            override fun onError(e: Exception?) {
                imageLoadComplete.startEnterTransition()
            }
        })
        return imageView
    }

    override fun destroyItem(container: ViewGroup?, position: Int, `object`: Any?) {
        if (`object` is View) {
            container?.removeView(`object`)
        }
    }

    fun getItemView(): ImageView? {
        return itemView
    }

    fun setData(data: List<String>?) {
        data?.let {
            this.data.clear()
            this.data.addAll(it)
            notifyDataSetChanged()
        }
    }
}