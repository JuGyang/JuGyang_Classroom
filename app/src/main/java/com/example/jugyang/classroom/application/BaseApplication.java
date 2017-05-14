package com.example.jugyang.classroom.application;

import android.app.Application;

import com.example.jugyang.classroom.share.ShareManager;
import com.example.jugyang.classroom.utils.StaticClass;
import com.example.jugyang.classroom.widget.adbrowser.Base64Drawables;
import com.mob.commons.SHARESDK;
import com.tencent.bugly.crashreport.CrashReport;

import cn.bmob.v3.Bmob;
import cn.sharesdk.framework.ShareSDK;

/**
 * Projeck Name:     Classroom_Toy_v1.0
 * Package Name:     com.example.jugyang.classroom.application
 * File Name:        BaseApplication
 * Description:      Created by jugyang on 4/9/17.
 */

public class BaseApplication extends Application{

    private static BaseApplication mApplication = null;

    @Override
    public void onCreate() {
        super.onCreate();
        mApplication = this;
        //Initialization Bugly
        CrashReport.initCrashReport(getApplicationContext(), StaticClass.BUGLY_APP_ID, true);
        //Initialization Bmob
        Bmob.initialize(this, StaticClass.BMOB_APP_ID);
        initShareSDk();
    }

    //初始化ShareSDK
    public void initShareSDk() {
        ShareManager.init(this);
    }


}
