// IWebviewAidlInterface.aidl
package com.example.webviewlib;

// aidl 参数默认数据流向是 in

interface IWebviewAidlInterface {
      //Surface变量不能用inout，否则binder通讯完会 重写surface到主进程，
      //导致出现 java.lang.IllegalStateException: Surface has already been released
     int bindSurface(in Surface view,int width,int height,String url);

     oneway void changSurfaceWH(int surfaceId,in Surface view, int width , int height);

     oneway void bindClientBinder(int surfaceId ,IBinder binder);

     oneway void dispatchTouchEvent(int surfaceId,in MotionEvent event);

     oneway void testCrash();
}