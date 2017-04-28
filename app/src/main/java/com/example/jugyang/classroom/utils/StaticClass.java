package com.example.jugyang.classroom.utils;

/**
 * Project Name:     Classroom_Toy_v1.0
 * Package Name:     com.example.jugyang.classroom.utils
 * File Name:        StaticClass
 * Description:      Created by jugyang on 4/9/17.
 */

public class StaticClass {

    //闪屏页延时
    public static final int HANDLER_SPLASH = 1001;
    //判断程序是否是第一次运行
    public static final String SHARE_IS_FIRST = "isFirst";
    //Bugly key
    public static final String BUGLY_APP_ID = "b54ac0c8b0";
    //Bmob key
    public static final String BMOB_APP_ID = "d2b96fb5e47f9af7dbf3b26b5d4964cb";
    //
    public static final String PHOTO_IMAGE_FILE_NAME = "fileImg.jpg";
    //
    public static final int CAMERA_REQUEST_CODE = 100;
    //
    public static final int IMAGE_REQUEST_CODE = 101;
    //
    public static final int RESULT_REQUEST_CODE = 102;
    //超时参数
    public static final int TIME_OUT = 30;
    //
    public static final int QRCODE_REQUEST_CODE = 103;

    //毫秒单位
    public static int MILLION_UNIT = 1000;

    //自动播放阈值
    public static int VIDEO_SCREEN_PERCENT = 50;

    public static float VIDEO_HEIGHT_PERCENT = 9 / 16.0f;

    //自动播放条件
    public enum AutoPlaySetting {
        AUTO_PLAY_ONLY_WIFI,
        AUTO_PLAY_3G_4G_WIFI,
        AUTO_PLAY_NEVER
    }


    //素材类型
    public static final String MATERIAL_IMAGE = "image";
    public static final String MATERIAL_HTML = "html";
}

