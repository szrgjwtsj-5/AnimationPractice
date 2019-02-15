package com.whx.animpractice.transition.change_activity

import android.os.Bundle
import android.webkit.WebView
import com.whx.animpractice.BaseActivity
import com.whx.animpractice.R

class SwitchAnimTargetActivity : BaseActivity() {

    private val webView by lazy { findViewById<WebView>(R.id.content_web) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.many_elements)

        webView.loadUrl("www.meituan.com")
    }
}