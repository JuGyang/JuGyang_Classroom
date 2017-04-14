package com.example.jugyang.classroom.Network.http;

/**
 * Project Name:     Classroom_Toy_v1.0
 * Package Name:     com.example.jugyang.classroom.Network.http
 * File Name:        HttpConstants
 * Description:      Created by jugyang on 4/13/17.
 *                   定义所有请求地址
 */


public class HttpConstants {
    /**
     * 方便切换服务器地址
     */
    private static final String ROOT_URL = "http://jugyang.com/api";

    /**
     * 首页产品请求接口
     */
    public static String HOME_RECOMMAND = ROOT_URL + "/product/home_data_two.json   ";

}
