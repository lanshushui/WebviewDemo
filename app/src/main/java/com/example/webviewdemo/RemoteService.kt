package com.example.webviewdemo

import android.app.Service
import android.content.Intent
import android.graphics.Rect
import android.os.Process
import android.util.Log
import android.view.Surface


class RemoteService : Service() {

    companion object{
        private const val TAG="RemoteService"
    }

    private var surface:Surface?=null

    private val binder=object :IWebviewAidlInterface.Stub(){
        override fun bindSurface(view: Surface?,width:Int,height:Int) {
            val pid = Process.myPid()
            Log.d(TAG, "RemoteService 当前进程的PID是：$pid")
            Log.i(TAG,"bindSurface ${width}--${height}")
            surface=view
            val lockCanvas = surface?.lockCanvas(Rect(0, 0, width, height))
            lockCanvas?.drawARGB(200,55,45,200)
            surface?.unlockCanvasAndPost(lockCanvas)
        }

    }

    override fun onBind(intent: Intent)=binder
}