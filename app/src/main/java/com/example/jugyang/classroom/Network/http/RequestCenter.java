package com.example.jugyang.classroom.Network.http;

import com.example.jugyang.classroom.entity.course.BaseCourseModel;
import com.example.jugyang.classroom.entity.recommand.BaseRecommandModel;
import com.example.jugyang.classroom.okHttp.CommonOkHttpClient;
import com.example.jugyang.classroom.okHttp.listener.DisposeDataHandle;
import com.example.jugyang.classroom.okHttp.listener.DisposeDataListener;
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
     * 真正的发送我们首页请求
     * @param listener
     */

    public static void requestRecommandData(DisposeDataListener listener) {

        RequestCenter.postRequest(HttpConstants.HOME_RECOMMAND, null,
                listener, BaseRecommandModel.class);
    }

    /**
     * 请求课程详情
     *
     * @param listener
     */
    public static void requestCourseDetail(String courseId, DisposeDataListener listener) {
        RequestParams params = new RequestParams();
        params.put("courseId", courseId);
        RequestCenter.postRequest(HttpConstants.COURSE_DETAIL, params, listener, BaseCourseModel.class);
    }

}
