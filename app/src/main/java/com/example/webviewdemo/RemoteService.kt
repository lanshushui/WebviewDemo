package com.example.webviewdemo

import android.app.Service
import android.content.Intent
import android.graphics.Rect
import android.hardware.display.DisplayManager
import android.os.Debug
import android.os.Handler
import android.os.Looper
import android.os.Process
import android.util.Log
import android.view.Surface
import android.view.WindowManager


class RemoteService : Service() {

    companion object {
        private const val TAG = "RemoteService"
    }

    private var surface: Surface? = null
    private var width = 0
    private var height = 0
    val displayManager by lazy {
        getSystemService(DISPLAY_SERVICE) as DisplayManager
    }
    val handler by lazy {
        Handler(Looper.getMainLooper())
    }

    private val binder = object : IWebviewAidlInterface.Stub() {
        override fun bindSurface(view: Surface?, width: Int, height: Int) {
            surface = view
            this@RemoteService.width = width
            this@RemoteService.height = height

            val pid = Process.myPid()
            Log.d(TAG, "RemoteService 当前进程的PID是：$pid")
            Log.i(TAG, "bindSurface ${width}--${height}")



            handler.post {
                createVirtualAndShowPresentation()
            }
        }
    }

    private fun createVirtualAndShowPresentation() {


        val flags = DisplayManager.VIRTUAL_DISPLAY_FLAG_PRESENTATION
        val virtualDisplay =
            displayManager.createVirtualDisplay(
                "webViewContainer",
                width,
                height,
                applicationContext.resources.displayMetrics.densityDpi,
                surface,
                flags
            )
        val presentation = WebViewPresentation(this, virtualDisplay.display)
        presentation.show()
    }


    override fun onBind(intent: Intent) = binder
}