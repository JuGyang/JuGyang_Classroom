package com.example.jugyang.classroom.core;

/**
 * Project Name:     Classroom_Toy_v1.0
 * Package Name:     com.example.jugyang.classroom.core
 * File Name:        AdContextInterface
 * Description:      Created by jugyang on 4/23/17.
 * Function:         最终通知应用层广告是否成功
 */

public interface AdContextInterface {

    void onAdSuccess();

    void onAdFailed();

    void onClickVideo(String url);
}
