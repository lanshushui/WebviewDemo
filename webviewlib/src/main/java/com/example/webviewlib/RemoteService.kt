package com.example.webviewlib

import android.app.Service
import android.content.Intent
import android.hardware.display.DisplayManager
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.os.Process
import android.util.Log
import android.view.MotionEvent
import android.view.Surface
import java.lang.Exception
import java.util.concurrent.atomic.AtomicInteger


class RemoteService : Service() {

    companion object {
        private const val TAG = "RemoteService"
        private val WEBVIEW_ID = AtomicInteger(1)
    }


    val displayManager by lazy {
        getSystemService(DISPLAY_SERVICE) as DisplayManager
    }
    val handler by lazy {
        Handler(Looper.getMainLooper())
    }

    //存储所有WebViewPresentation
    private val webViewMap = HashMap<Int, WebViewPresentation>()

    private val binder = object : IWebviewAidlInterface.Stub() {
        override fun bindSurface(view: Surface?, width: Int, height: Int, url: String): Int {
            val surfaceId = WEBVIEW_ID.getAndIncrement()
            val pid = Process.myPid()
            Log.d(TAG, "bindSurface $view, 当前进程的PID是：$pid")
            Log.i(TAG, "bindSurface ${width}--${height}, surfaceId is $surfaceId")
            if (view == null) {
                Log.d(TAG, "bindSurface error,is null")
                return surfaceId
            }

            handler.post {
                createVirtualAndShowPresentation(view, surfaceId, width, height, url)
            }
            return surfaceId
        }

        override fun bindClientBinder(surfaceId: Int, binder: IBinder) {
            handler.post {
                this@RemoteService.bindClientBinder(surfaceId, binder)
            }
        }

        override fun dispatchTouchEvent(surfaceId: Int, event: MotionEvent?) {
            if (event != null) {
                handler.post {
                    webViewMap[surfaceId]?.dispatchTouchEvent(event)
                }
            }
        }

        override fun testCrash() {
            handler.post {
                throw Exception("多进程崩溃")
            }
        }
    }

    private fun bindClientBinder(surfaceId: Int, binder: IBinder) {
        Log.i(TAG, "bindClientBinder $surfaceId ,$binder")
        webViewMap[surfaceId]?.bindClientBinder(IClientAidlInterface.Stub.asInterface(binder))
    }

    private fun createVirtualAndShowPresentation(
        surface: Surface,
        surfaceId: Int,
        width: Int,
        height: Int,
        url: String
    ) {
        val virtualDisplay =
            displayManager.createVirtualDisplay(
                "webViewContainer",
                width,
                height,
                applicationContext.resources.displayMetrics.densityDpi,
                surface,
                DisplayManager.VIRTUAL_DISPLAY_FLAG_PRESENTATION
            )
        val presentation = WebViewPresentation(this, virtualDisplay.display)
        webViewMap[surfaceId] = presentation
        presentation.loadUrl(url)
        presentation.show()
    }

    override fun onCreate() {
        super.onCreate()
        Log.i(TAG, "$this onCreate")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.i(TAG, "$this onDestroy")
    }


    override fun onBind(intent: Intent) = binder
}