package com.whx.animpractice

import android.app.Application
import android.content.Context

class MyApplication : Application() {

    companion object {
        private lateinit var mAppContext: Context

        @JvmStatic
        fun getAppContext(): Context {
            return mAppContext
        }
    }

    override fun onCreate() {
        super.onCreate()
        mAppContext = this

        CrashHandler.newInstance(this.applicationContext).init()
    }
}