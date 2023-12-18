package com.example.webviewlib

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.webkit.WebView


class PresentationController(context: Context) {
    companion object {
        private const val TAG = "PresentationController"
    }

    var binder: IClientAidlInterface? = null
        set(value) {
            field = value
            value?.success()
        }

    private val rootView = LayoutInflater.from(context).inflate(R.layout.persentation_webview, null)

    private val webView: WebView? by lazy {
        rootView.findViewById(R.id.webview)
    }

    fun loadUrl(url: String) {
        webView?.loadUrl(url)
    }

    fun addWebview(container: ViewGroup?) {
        val parent = rootView.parent
        if (parent is ViewGroup) {
            parent.removeView(rootView)
        }
        container?.addView(rootView)
    }
}