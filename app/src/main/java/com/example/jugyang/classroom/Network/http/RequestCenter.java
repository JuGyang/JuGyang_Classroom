package com.example.jugyang.classroom.Network.http;

import com.example.jugyang.classroom.entity.course.BaseCourseModel;
import com.example.jugyang.classroom.entity.recommand.BaseRecommandModel;
import com.example.jugyang.classroom.okHttp.CommonOkHttpClient;
import com.example.jugyang.classroom.okHttp.HttpConstant;
import com.example.jugyang.classroom.okHttp.listener.DisposeDataHandle;
import com.example.jugyang.classroom.okHttp.listener.DisposeDataListener;
import com.example.jugyang.classroom.okHttp.listener.DisposeDownloadListener;
import com.example.jugyang.classroom.okHttp.request.CommonRequest;
import com.example.jugyang.classroom.okHttp.request.RequestParams;

/**
 * Project Name:     Classroom_Toy_v1.0
 * Package Name:     com.example.jugyang.classroom.Network.http
 * File Name:        RequestCenter
 * Description:      Created by jugyang on 4/13/17.
 */

public class RequestCenter {
    //根据参数发送所有post请求
    public static void postRequest(String url, RequestParams params, DisposeDataListener listener, Class<?> clazz) {
        CommonOkHttpClient.get(CommonRequest.
                createGetRequest(url, params), new DisposeDataHandle(listener, clazz));
    }

    /**
     * 真正的发送首页请求MainpageFragment
     * @param listener
     */

    public static void requestMainpageFragmentRecommandData(DisposeDataListener listener) {

        RequestCenter.postRequest(HttpConstants.MAINPAGE_RECOMMAND, null,
                listener, BaseRecommandModel.class);
    }

    /**
     * 真正发送课程页面请求ClassesFragment
     */
    public static void requestClassesFragmentRecommandData(DisposeDataListener listener) {

        RequestCenter.postRequest(HttpConstants.CLASSES_RECOMMAND, null,
                listener, BaseRecommandModel.class);
    }

    /**
     * 真正发送直播页面请求LiveFragment
     */
    public static void requestLiveFragmentRecommandData(DisposeDataListener listener) {

        RequestCenter.postRequest(HttpConstants.LIVE_RECOMMAND, null,
                listener, BaseRecommandModel.class);
    }

    /**
     * 真正发送直播页面请求ArticlesFragment
     */
    public static void requestArticlesFragmentRecommandData(DisposeDataListener listener) {

        RequestCenter.postRequest(HttpConstants.ARTICLES_RECOMMAND, null,
                listener, BaseRecommandModel.class);
    }

    /**
     * 请求课程详情
     *
     * @param listener
     */
    public static void requestCourseDetail(String courseId, DisposeDataListener listener) {
        RequestParams params = new RequestParams();
        //params.put("courseId", courseId);
        String class_url = HttpConstants.COURSE_DETAIL + courseId;
        RequestCenter.postRequest(class_url, params, listener, BaseCourseModel.class);
    }

    public static void downloadFile(String url, String path, DisposeDownloadListener listener) {
        CommonOkHttpClient.downloadFile(CommonRequest.createGetRequest(url, null),
                new DisposeDataHandle(listener, path));
    }


}
