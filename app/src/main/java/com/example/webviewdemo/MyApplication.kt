package com.example.webviewdemo

import android.app.Application
import android.content.Context
import android.os.Build
import android.webkit.WebView


class MyApplication : Application() {

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        //java.lang.RuntimeException
        //Using WebView from more than one process at once with the same data directory is not supported
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            val processName = getProcessName()
            WebView.setDataDirectorySuffix(processName)
        }
    }
    override fun onCreate() {
        super.onCreate()
    }
}