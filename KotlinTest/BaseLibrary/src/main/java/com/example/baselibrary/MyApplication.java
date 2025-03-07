package com.example.baselibrary;


import android.content.Context;
import android.content.res.Resources;
import android.support.multidex.MultiDexApplication;

import com.alibaba.android.arouter.launcher.ARouter;
import com.example.baselibrary.widgets.Density;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;

public  class MyApplication extends MultiDexApplication {
 public  static   Context cxt;
 public  static Resources resources;
  private static  boolean isDebug = true;
    private RefWatcher refWatcher;
    @Override
    public void onCreate() {
        super.onCreate();
        cxt = getApplicationContext();
        resources = cxt.getResources();
        if (LeakCanary.isInAnalyzerProcess(this)) {
                        return;
                     }
        refWatcher = LeakCanary.install(this);
        Density.INSTANCE.setDensity(this);

        if (isDebug) {
            // 崩溃日志的收集,便于测试发现崩溃后处理
            CrashHandler.Companion.getInstance().init(this);
            ARouter.openLog();     // 打印日志
            ARouter.openDebug();  // 开启调试模式(如果在InstantRun模式下运行，必须开启调试模式！线上版本需要关闭,否则有安全风险)
        }

        ARouter.init(this); // 尽可能早，推荐在Application中初始化

    }

    public static RefWatcher getRefWatcher (Context context){
        MyApplication application =(MyApplication) context . getApplicationContext ();
        return application.refWatcher;
    }


}
