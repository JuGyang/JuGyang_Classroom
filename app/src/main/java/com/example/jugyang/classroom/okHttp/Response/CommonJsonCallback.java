package com.example.jugyang.classroom.okHttp.Response;

import android.os.Handler;
import android.os.Looper;

import com.example.jugyang.classroom.okHttp.exception.OkHttpException;
import com.example.jugyang.classroom.okHttp.listener.DisposeDataHandle;
import com.example.jugyang.classroom.okHttp.listener.DisposeDataListener;
import com.example.jugyang.classroom.utils.ResponseEntityToModule;

import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Project Name:     Classroom_Toy_v1.0
 * Package Name:     com.example.jugyang.classroom.okHttp.Response
 * File Name:        CommonJsonCallback
 * Description:      Created by jugyang on 4/12/17.
 */

public class CommonJsonCallback implements Callback{

    /**
     * the logic layer exception, may alter in different app
     */
    protected final String RESULT_CODE = "ecode"; //有返回则对于http请求是成功的，但还有可能是业务逻辑上的错误
    protected final int RESULT_CODE_VALUE = 0;
    protected final String ERROR_MSG = "emsg";
    protected final String EMPTY_MSG = "";

    /**
     * the java layer exception, do not same to the logic error
     */
    protected final int NETWORK_ERROR = -1; //the network relative error
    protected final int JSON_ERROR = -2; //the JSON relative error
    protected final int OTHER_ERROR = -3; //the unknown error

    private Handler mDeliveryHandler; //进行消息的转发
    private DisposeDataListener mListener;
    private Class<?> mClass;

    public CommonJsonCallback(DisposeDataHandle handle) {
        this.mListener = handle.mListener;
        this.mClass = handle.mClass;
        this.mDeliveryHandler = new Handler(Looper.getMainLooper());
    }


    //请求失败处理
    @Override
    public void onFailure(final Call call, final IOException ioexception) {

        mDeliveryHandler.post(new Runnable() {
            @Override
            public void run() {

                mListener.onFailure(new OkHttpException(NETWORK_ERROR, ioexception));
            }
        });
    }


    //响应处理函数
    @Override
    public void onResponse(Call call, Response response) throws IOException {
        final String result = response.body().string();
        mDeliveryHandler.post(new Runnable() {
            @Override
            public void run() {

                handleResponse(result);
            }


        });
    }

    /**
     * 处理服务器返回的响应数据
     */
    private void handleResponse(Object responseObj) {
        //为了保证代码的健壮性
        if (responseObj == null || responseObj.toString().trim().equals("")) {
            mListener.onFailure(new OkHttpException(NETWORK_ERROR, EMPTY_MSG));
            return;
        }

        try {
            JSONObject result = new JSONObject(responseObj.toString());
            //开始尝试解析json
            if (result.has(RESULT_CODE)) {
                //从json对象中取出我们的响应码，若为0，则是正确的响应
                if (result.getInt(RESULT_CODE) == RESULT_CODE_VALUE) {
                    if (mClass == null) {
                        mListener.onSuccess(responseObj);
                    } else {
                        //需要我们将json对象转化为实体对象
                        Object obj = ResponseEntityToModule.parseJsonObjectToModule(result, mClass);
                        //表明正确的转为了实体对象
                        if (obj != null) {
                            mListener.onSuccess(obj);
                        } else {
                            //返回的不是合法的json
                            mListener.onFailure(new OkHttpException(JSON_ERROR,
                                    EMPTY_MSG));
                        }
                    }
                } else {
                    //将服务器返回给我们的异常回调到应用层去处理
                    mListener.onFailure(new OkHttpException(OTHER_ERROR, result.get(RESULT_CODE)));
                }
            }

        } catch (Exception e) {
            mListener.onFailure(new OkHttpException(OTHER_ERROR, e.getMessage()));
            e.printStackTrace();
        }
    }
}
