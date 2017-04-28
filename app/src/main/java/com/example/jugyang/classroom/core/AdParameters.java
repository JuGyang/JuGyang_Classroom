package com.example.jugyang.classroom.core;

import com.example.jugyang.classroom.utils.StaticClass;

/**
 * Project Name:     Classroom_Toy_v1.0
 * Package Name:     com.example.jugyang.classroom.core
 * File Name:        AdParameters
 * Description:      Created by jugyang on 4/22/17.
 * Function:         function
 */

public final class AdParameters {

    //用来记录可自动播放的条件
    private static StaticClass.AutoPlaySetting currentSetting = StaticClass.AutoPlaySetting.AUTO_PLAY_3G_4G_WIFI; //默认都可以自动播放

    public static void setCurrentSetting(StaticClass.AutoPlaySetting setting) {
        currentSetting = setting;
    }

    public static StaticClass.AutoPlaySetting getCurrentSetting() {
        return currentSetting;
    }

    /**
     * 获取sdk当前版本号
     */
    public static String getAdSDKVersion() {
        return "1.0.0";
    }
}