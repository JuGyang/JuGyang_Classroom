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
     * 服务器地址
     */
    private static final String ROOT_URL = "http://jugyang.com/api";

    /**
     * 首页请求接口
     */
    public static String MAINPAGE_RECOMMAND = ROOT_URL + "/product/home_data.php";

    /**
     * 课程产品请求接口
     */
    public static String CLASSES_RECOMMAND = ROOT_URL + "/product/classes_data.json";

    /**
     * 直播页面请求接口
     */
    public static String LIVE_RECOMMAND = ROOT_URL + "/product/live_data.php";

    /**
     * 文章页面请求接口
     */
    public static String ARTICLES_RECOMMAND = ROOT_URL + "/product/articles_data.json";

    /**
     * 课程详情接口
     */
    public static String COURSE_DETAIL = ROOT_URL + "/product/classes_data.php?id=";


}
