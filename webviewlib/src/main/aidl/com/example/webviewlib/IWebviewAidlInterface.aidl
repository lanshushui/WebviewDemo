// IWebviewAidlInterface.aidl
package com.example.webviewlib;

// Declare any non-default types here with import statements

interface IWebviewAidlInterface {
      //Surface变量不能用inout，否则binder通讯完会 重写surface到主进程，
      //导致出现 java.lang.IllegalStateException: Surface has already been released
     int bindSurface(in Surface view,int width,int height,String url);

     void dispatchTouchEvent(int surfaceId,in MotionEvent event);

     void testCrash();
}