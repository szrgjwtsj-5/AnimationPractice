package com.whx.animpractice.transition.share_element

import android.os.Build
import android.support.annotation.RequiresApi
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.squareup.picasso.Picasso
import com.whx.animpractice.R

class MyRecyclerAdapter : RecyclerView.Adapter<MyRecyclerAdapter.MyViewHolder>() {
    interface ItemClickListener {
        fun onItemClick(view: View, url: String, position: Int)
    }

    //        private val weakRef = WeakReference<RecyclerFragment>(ref)
    private var clickListener: ItemClickListener? = null

    private val data = arrayListOf<String>()

    override fun getItemCount() = data.size

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): MyViewHolder {
        return MyViewHolder(LayoutInflater.from(parent?.context).inflate(R.layout.recycler_item, parent, false), clickListener)
    }

    override fun onBindViewHolder(holder: MyViewHolder?, position: Int) {
        if (position in data.indices) {
            holder?.bindView(data[position], position)
        }
    }

    fun setData(data: List<String>?) {
        data?.let {
            this.data.clear()
            this.data.addAll(data)
            notifyDataSetChanged()
        }
    }

    fun setOnItemClickListener(clickListener: ItemClickListener) {
        this.clickListener = clickListener
    }

    class MyViewHolder(view: View, private val clickListener: ItemClickListener?) : RecyclerView.ViewHolder(view) {
        private val imageView = view.findViewById<ImageView>(R.id.item_image)

        @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
        fun bindView(url: String, position: Int) {
            Picasso.get()
                    .load(url)
                    .into(imageView)

            imageView.transitionName = url
            imageView.tag = url

            imageView.setOnClickListener { clickListener?.onItemClick(imageView, url, position) }
        }
    }
}