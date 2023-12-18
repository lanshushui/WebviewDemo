package com.example.webviewlib

import android.annotation.SuppressLint
import android.app.Presentation
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.Display
import android.view.ViewGroup

/**
 * 因为surface宽高变化时需要重新创建 虚拟屏幕和Presentation，但webview的内容不应该变化
 * 所以该类不应该存有任何数据，只单纯作为一个中间类
 */
@SuppressLint("MissingInflatedId")
class WebViewPresentation(
    context: Context,
    display: Display,
    val controller: PresentationController = PresentationController(context)
) : Presentation(context, display) {

    companion object {
        private const val TAG = "WebViewPresentation"
    }


    private var isCreated = false

    val pendingAction = mutableListOf<Runnable>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.i(TAG, "WebViewPresentation onCreate")
        setContentView(R.layout.persentation_container)
        val container = findViewById<ViewGroup>(R.id.container)
        container.post {
            controller.addWebview(container)
        }
        pendingAction.forEach {
            it.run()
        }
        pendingAction.clear()
        isCreated = true
    }

    private fun runAfterCreate(action: Runnable) {
        if (isCreated) {
            action.run()
        } else {
            pendingAction.add(action)
        }
    }

    fun loadUrl(url: String) = runAfterCreate {
        controller.loadUrl(url)
    }

    fun bindClientBinder(binder: IClientAidlInterface?) {
        controller.binder = binder
    }
}