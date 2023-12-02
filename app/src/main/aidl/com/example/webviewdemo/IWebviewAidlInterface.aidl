// IWebviewAidlInterface.aidl
package com.example.webviewdemo;
import android.view.Surface;
import android.view.MotionEvent;

// Declare any non-default types here with import statements

interface IWebviewAidlInterface {
      //Surface变量不能用inout，否则binder通讯完会 重写surface到主进程，
      //导致出现 java.lang.IllegalStateException: Surface has already been released
     void bindSurface(in Surface view,int width,int height);

     void dispatchTouchEvent(in MotionEvent event);
}