package com.example.webviewdemo

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.webkit.WebView
import android.widget.Button
import com.example.webviewlib.RemoveWebView

class MainActivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        findViewById<WebView>(R.id.localWebView).loadUrl("https://www.bilibili.com")
        findViewById<Button>(R.id.crash).setOnClickListener {
            findViewById<RemoveWebView>(R.id.webview1).crash()
        }
        findViewById<Button>(R.id.changeSize).setOnClickListener {
            val remoteWebView = findViewById<View>(R.id.webview1)
            val lp = remoteWebView.layoutParams
            lp.height += 50
            remoteWebView.layoutParams = lp
        }
    }
}