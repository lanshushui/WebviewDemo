package com.example.webviewdemo

import android.annotation.SuppressLint
import android.app.Presentation
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.Display
import android.webkit.WebSettings
import android.webkit.WebView


@SuppressLint("MissingInflatedId")
class WebViewPresentation(context: Context, display: Display) : Presentation(context, display) {

    companion object {
        private const val TAG = "WebViewPresentation"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.i(TAG,"WebViewPresentation onCreate")
        setContentView(R.layout.persentation_webview)
        val webView:WebView = findViewById(R.id.webview)

        webView.loadUrl("https://www.baidu.com")
    }
}