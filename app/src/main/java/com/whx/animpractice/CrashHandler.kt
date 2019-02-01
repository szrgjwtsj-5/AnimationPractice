package com.whx.animpractice

import android.annotation.SuppressLint
import android.content.Context

@SuppressLint("StaticFieldLeak")
object CrashHandler : Thread.UncaughtExceptionHandler {

    private var oldHandler: Thread.UncaughtExceptionHandler? = null
    private lateinit var mContext: Context

    fun newInstance(context: Context): CrashHandler {
        mContext = context
        return this
    }

    fun init() {

        if (oldHandler == null) {
            oldHandler = Thread.getDefaultUncaughtExceptionHandler()

            Thread.setDefaultUncaughtExceptionHandler(this)
        }
    }

    override fun uncaughtException(t: Thread?, e: Throwable?) {
        e?.printStackTrace()
    }
}