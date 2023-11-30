// IWebviewAidlInterface.aidl
package com.example.webviewdemo;
import android.view.Surface;

// Declare any non-default types here with import statements

interface IWebviewAidlInterface {
     void bindSurface(inout Surface view,int width,int height)       ;
}