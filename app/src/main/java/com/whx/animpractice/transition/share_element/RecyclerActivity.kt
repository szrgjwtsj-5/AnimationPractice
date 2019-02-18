package com.whx.animpractice.transition.share_element

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.whx.animpractice.R

class RecyclerActivity : AppCompatActivity() {

    private var fragment: RecyclerFragment? = null

    private var mReenterState: Bundle? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.empty_layout)

        fragment = RecyclerFragment.getInstance()

        supportFragmentManager.beginTransaction().replace(R.id.container, fragment).commit()
    }

    override fun onActivityReenter(resultCode: Int, data: Intent?) {
        super.onActivityReenter(resultCode, data)
        mReenterState = Bundle(data?.extras)
        fragment?.reEnter(mReenterState)
    }
}