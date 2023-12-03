package com.example.webviewlib

import android.content.ComponentName
import android.content.Context
import android.content.Context.BIND_AUTO_CREATE
import android.content.Intent
import android.content.ServiceConnection
import android.graphics.SurfaceTexture
import android.os.IBinder
import android.os.Process
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.Surface
import android.view.TextureView

class RemoveWebView
@JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) :
    TextureView(context, attrs, defStyleAttr), TextureView.SurfaceTextureListener {

    var surface: Surface? = null
    var surfaceWidth = 0
    var surfaceHeight = 0
    var iWebViewAidlInterface: IWebviewAidlInterface? = null
    var surfaceId: Int = -1
    val binder = object : IClientAidlInterface.Stub() {
        override fun success() {
            Log.i(TAG, "$this success")
        }

    }

    companion object {
        private const val TAG = "RemoveWebView"
    }

    val connect = object : ServiceConnection {
        override fun onServiceConnected(p0: ComponentName?, p1: IBinder?) {
            Log.i(TAG, "onServiceConnected")
            iWebViewAidlInterface = IWebviewAidlInterface.Stub.asInterface(p1)
            surfaceId = iWebViewAidlInterface?.bindSurface(
                surface,
                surfaceWidth,
                surfaceHeight,
                "https://www.baidu.com"
            ) ?: -1
            iWebViewAidlInterface?.bindClientBinder(surfaceId, binder)
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            Log.i(TAG, "${this@RemoveWebView}  ,$name onServiceDisconnected")
            iWebViewAidlInterface = null
        }

        override fun onBindingDied(name: ComponentName?) {
            super.onBindingDied(name)
            Log.i(TAG, "${this@RemoveWebView}  ,$name onBindingDied")
            iWebViewAidlInterface = null
            bindService()
        }
    }

    init {
        val pid = Process.myPid()
        Log.d(TAG, "RemoveWebView 当前进程的PID是：$pid")
        surfaceTextureListener = this
    }

    override fun onSurfaceTextureAvailable(
        surfaceTexture: SurfaceTexture,
        width: Int,
        height: Int
    ) {
        Log.i(TAG, "onSurfaceTextureAvailable $surfaceTexture")
        if (surface == null) {
            surface = Surface(surfaceTexture)
            surfaceWidth = width
            surfaceHeight = height
        }
        bindService()
    }

    private fun bindService() {
        val intent = Intent(context, RemoteService::class.java)
        context.bindService(intent, connect, BIND_AUTO_CREATE)
    }

    override fun onSurfaceTextureSizeChanged(
        surfaceTexture: SurfaceTexture,
        width: Int,
        height: Int
    ) {
        Log.i(TAG, "onSurfaceTextureSizeChanged $surfaceTexture")
    }

    override fun onSurfaceTextureDestroyed(surfaceTexture: SurfaceTexture): Boolean {
        Log.i(TAG, "onSurfaceTextureDestroyed $surfaceTexture")
        return false
    }

    override fun onSurfaceTextureUpdated(surfaceTexture: SurfaceTexture) {
        Log.i(TAG, "onSurfaceTextureUpdated $surfaceTexture")
    }

    override fun dispatchTouchEvent(event: MotionEvent?): Boolean {
        iWebViewAidlInterface?.dispatchTouchEvent(surfaceId, event)
        return true
    }

    fun crash() {
        iWebViewAidlInterface?.testCrash()
    }

}