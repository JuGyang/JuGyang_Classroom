package com.example.jugyang.classroom.core.video;

import android.content.Intent;
import android.view.ViewGroup;

import com.example.jugyang.classroom.core.AdContextInterface;
import com.example.jugyang.classroom.entity.AdValue;
import com.example.jugyang.classroom.okHttp.HttpConstant;
import com.example.jugyang.classroom.report.ReportManager;
import com.example.jugyang.classroom.ui.AdBrowserActivity;
import com.example.jugyang.classroom.utils.ResponseEntityToModule;
import com.example.jugyang.classroom.utils.Utils;
import com.example.jugyang.classroom.widget.CustomVideoView;

/**
 * Project Name:     Classroom_Toy_v1.0
 * Package Name:     com.example.jugyang.classroom.core.video
 * File Name:        VideoAdContext
 * Description:      Created by jugyang on 4/22/17.
 * Function:         管理Slot,与外界进行通信
 */

public class VideoAdContext implements VideoAdSlot.AdSDKSlotListener {

    //the ad container
    private ViewGroup mParentView;

    private VideoAdSlot mAdSlot;
    private AdValue mInstance = null;

    //the listener to the app layer
    private AdContextInterface mListener;
    private CustomVideoView.ADFrameImageLoadListener mFrameLoadListener;

    public VideoAdContext(ViewGroup parentView, String instance, CustomVideoView.ADFrameImageLoadListener frameLoadListener) {
        this.mParentView = parentView;
        this.mInstance = (AdValue) ResponseEntityToModule.parseJsonToModule(instance, AdValue.class);
        this.mFrameLoadListener = frameLoadListener;
        load();
    }

    /**
     * 创建Slot业务逻辑类,不调用则不会创建最底层的CustomVideoView
     */
    private void load() {
        if (mInstance != null && mInstance.resource != null) {
            mAdSlot = new VideoAdSlot(mInstance, this, mFrameLoadListener);
            //发送解析成功事件
            sendAnalizeReport(HttpConstant.Params.ad_analize, HttpConstant.AD_DATA_SUCCESS);
        } else {
            mAdSlot = new VideoAdSlot(null, this, mFrameLoadListener); //创建空的slot,不响应任何事件
            if (mListener != null) {
                mListener.onAdFailed();
            }
            sendAnalizeReport(HttpConstant.Params.ad_analize, HttpConstant.AD_DATA_FAILED);
        }
    }

    /**
     * 根据滑动距离来判断是否可以自动播放,出现超过50%自动播放,离开超过50%,自动暂停
     */
    public void updateAdInScrollView() {
        if (mAdSlot != null) {
            mAdSlot.updateVideoInScrollView();
        }
    }

    /**
     * release the ad
     */
    public void destroy() {
        mAdSlot.destroy();
    }

    public void setAdResultListener(AdContextInterface listener) {
        this.mListener = listener;
    }


    @Override
    public ViewGroup getAdParent() {
        return mParentView;
    }

    @Override
    public void onAdVideoLoadSuccess() {
        if (mListener != null) {
            mListener.onAdSuccess();
        }
        sendAnalizeReport(HttpConstant.Params.ad_load, HttpConstant.AD_PLAY_SUCCESS);
    }

    @Override
    public void onAdVideoLoadFailed() {
        if (mListener != null) {
            mListener.onAdFailed();
        }
        sendAnalizeReport(HttpConstant.Params.ad_load, HttpConstant.AD_PLAY_FAILED);
    }

    @Override
    public void onAdVideoLoadComplete() {

    }

    @Override
    public void onClickVideo(String url) {
        if (mListener != null) {
            mListener.onClickVideo(url);
        } else {
            Intent intent = new Intent(mParentView.getContext(), AdBrowserActivity.class);
            intent.putExtra(AdBrowserActivity.KEY_URL, url);
            mParentView.getContext().startActivity(intent);
        }
    }

    /**
     * 发送广告数据解析成功监测
     */
    private void sendAnalizeReport(HttpConstant.Params step, String result) {
        try {
            ReportManager.sendAdMonitor(Utils.isPad(mParentView.getContext().
                            getApplicationContext()), mInstance == null ? "" : mInstance.resourceID,
                    (mInstance == null ? null : mInstance.adid), Utils.getAppVersion(mParentView.getContext()
                            .getApplicationContext()), step, result);
        } catch (Exception e) {

        }
    }
}
