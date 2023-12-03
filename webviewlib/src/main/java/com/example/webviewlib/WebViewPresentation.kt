package com.example.webviewlib

import android.annotation.SuppressLint
import android.app.Presentation
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.Display
import android.webkit.WebView


@SuppressLint("MissingInflatedId")
class WebViewPresentation(context: Context, display: Display) : Presentation(context, display) {

    companion object {
        private const val TAG = "WebViewPresentation"
    }

    var binder: IClientAidlInterface? = null
        set(value) {
            field = value
            value?.success()
        }

    val pendingAction = mutableListOf<Runnable>()

    private val webView: WebView? by lazy {
        findViewById(R.id.webview)
    }
    private var isCreated = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.i(TAG, "WebViewPresentation onCreate")
        setContentView(R.layout.persentation_webview)
        pendingAction.forEach {
            it.run()
        }
        pendingAction.clear()
        isCreated = true
    }

    fun loadUrl(url: String) = runAfterCreate {
        webView?.loadUrl(url)
    }

    private fun runAfterCreate(action: Runnable) {
        if (isCreated) {
            action.run()
        } else {
            pendingAction.add(action)
        }
    }
}