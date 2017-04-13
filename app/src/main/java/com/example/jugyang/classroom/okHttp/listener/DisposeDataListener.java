package com.example.jugyang.classroom.okHttp.listener;

/**
 * Project Name:     Classroom_Toy_v1.0
 * Package Name:     com.example.jugyang.classroom.okHttp.listener
 * File Name:        DisposeDataListener
 * Description:      Created by jugyang on 4/12/17.
 */

public interface DisposeDataListener  {

    /**
     * 请求成功回调时间处理
     */
    public void onSuccess(Object reponseObj);

    /**
     * 请求失败回调事件处理
     */
    public void onFailure(Object reasonObj);
}
