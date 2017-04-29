package com.example.jugyang.classroom.core.video;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.example.jugyang.classroom.core.AdParameters;
import com.example.jugyang.classroom.entity.AdValue;
import com.example.jugyang.classroom.report.ReportManager;
import com.example.jugyang.classroom.ui.AdBrowserActivity;
import com.example.jugyang.classroom.util.Util;
import com.example.jugyang.classroom.utils.StaticClass;
import com.example.jugyang.classroom.utils.Utils;
import com.example.jugyang.classroom.widget.CustomVideoView;
import com.example.jugyang.classroom.widget.VideoFullDialog;

/**
 * Project Name:     Classroom_Toy_v1.0
 * Package Name:     com.example.jugyang.classroom.core.video
 * File Name:        VideoAdSlot
 * Description:      Created by jugyang on 4/22/17.
 * Function:         function
 */

public class VideoAdSlot implements CustomVideoView.ADVideoPlayerListener {

    private Context mContext;
    /**
     * UI
     */
    private CustomVideoView mVideoView;
    private ViewGroup mParentView;
    /**
     * Data
     */
    private AdValue mXAdInstance;
    private AdSDKSlotListener mSlotListener;
    private boolean canPause = false; //是否可自动暂停标志位
    private int lastArea = 0; //防止将要滑入滑出时播放器的状态改变

    public VideoAdSlot(AdValue adInstance, AdSDKSlotListener slotListener, CustomVideoView.ADFrameImageLoadListener frameLoadListener) {
        mXAdInstance = adInstance;
        mSlotListener = slotListener;
        mParentView = slotListener.getAdParent();
        mContext = mParentView.getContext();
        initVideoView(frameLoadListener);
    }

    private void initVideoView(CustomVideoView.ADFrameImageLoadListener frameImageLoadListener) {
        mVideoView = new CustomVideoView(mContext, mParentView);
        if (mXAdInstance != null) {
            mVideoView.setDataSource(mXAdInstance.resource);
            mVideoView.setFrameURI(mXAdInstance.thumb);
            mVideoView.setFrameLoadListener(frameImageLoadListener);
            mVideoView.setListener(this);
        }
        RelativeLayout paddingView = new RelativeLayout(mContext);
        paddingView.setBackgroundColor(mContext.getResources().getColor(android.R.color.black));
        paddingView.setLayoutParams(mVideoView.getLayoutParams());
        mParentView.addView(paddingView);
        mParentView.addView(mVideoView);
    }

    /**
     * 实现滑入播放,滑出暂停功能
     */
    public void updateVideoInScrollView() {
        int currentArea = Utils.getVisiblePercent(mParentView);
        //还未出现在屏幕上,不做任何处理
        if (currentArea <= 0) {
            return;
        }
        //刚要滑入和滑出的异常情况处理
        if(Math.abs(currentArea - lastArea) >= 100) {
            return;
        }
        //滑动没有超过50%
        if (currentArea <= StaticClass.VIDEO_SCREEN_PERCENT) {
            if (canPause) {
                pauseVideo(true);
                canPause = false; //滑动事件过滤
            }
            lastArea = 0;
            mVideoView.setIsComplete(false);
            mVideoView.setIsRealPause(false);
            return;
        }

        //当视频进入真正的暂停状态时走入此case
        if (isRealPause() || isComplete()) {
            pauseVideo(false);
            canPause = false;
        }

        //满足用户的视频播放设置条件
        if (Utils.canAutoPlay(mContext, AdParameters.getCurrentSetting())
                || isPlaying()) {
            lastArea = currentArea;
            //真正去播放视频
            resumeVideo();
            canPause = true;
            mVideoView.setIsRealPause(false);
        } else {
            //不满足用户条件设置
            pauseVideo(false);
            mVideoView.setIsRealPause(true);
        }

    }

    /**
     * 实现从小屏到全屏播放功能接口
     */
    @Override
    public void onClickFullScreenBtn() {
        try {
            ReportManager.fullScreenReport(mXAdInstance.event.full.content, getPosition());
        } catch (Exception e) {
            e.printStackTrace();
        }
        //获得videoview在当前界面的属性
        Bundle bundle = Utils.getViewProperty(mParentView);
        //将播放器从view树中移除
        mParentView.removeView(mVideoView);
        //创建全屏播放Dialog
        VideoFullDialog dialog = new VideoFullDialog(mContext, mVideoView, mXAdInstance, mVideoView.getCurrentPosition());
        dialog.setListener(new VideoFullDialog.FullToSmallListener() {

            @Override
            public void getCurrentPlayPosition(int position) {
                //在全屏视频播放的时候点击了返回
                backToSmallMode(position);
            }

            @Override
            public void playComplete() {
                //全屏播放完成以后的事件回调
                bigPlayComplete();

            }
        });
        dialog.setViewBundle(bundle); //为Dialog设置播放器数据Bundle对象
        dialog.setSlotListener(mSlotListener);
        dialog.show(); //显示全屏Dialog
    }

    /**
     * 全屏播放结束时的事件回调
     */
    private void bigPlayComplete() {
        if (mVideoView.getParent() == null) {
            mParentView.addView(mVideoView);
        }
        mVideoView.setTranslationY(0); //防止动画导致偏离父容器
        mVideoView.isShowFullBtn(true);
        mVideoView.mute(true);
        mVideoView.setListener(this);
        mVideoView.seekAndPause(0);
        canPause = false;
    }

    /**
     * 返回小屏的时候
     * @param position
     */
    private void backToSmallMode(int position) {
        if (mVideoView.getParent() == null) {

            mParentView.addView(mVideoView);
        }
        mVideoView.setTranslationY(0); //防止动画导致偏离父容器
        mVideoView.isShowFullBtn(true);
        mVideoView.mute(true); //小屏静音播放
        mVideoView.setListener(this); //重新设置监听为我们的业务逻辑层
        mVideoView.seekAndResume(position); //使播放器跳到指定位置并且播放
        canPause = true;
    }

    @Override
    public void onClickVideo() {
        String desationUrl = mXAdInstance.clickUrl;
        if (mSlotListener != null) {
            if (mVideoView.isFrameHidden() && !TextUtils.isEmpty(desationUrl)) {
                mSlotListener.onClickVideo(desationUrl);
                try {
                    ReportManager.pauseVideoReport(mXAdInstance.clickMonitor, mVideoView.getCurrentPosition()
                            / StaticClass.MILLION_UNIT);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } else {
            //走默认样式
            if (mVideoView.isFrameHidden() && !TextUtils.isEmpty(desationUrl)) {
                Intent intent = new Intent(mContext, AdBrowserActivity.class);
                intent.putExtra(AdBrowserActivity.KEY_URL, mXAdInstance.clickUrl);
                mContext.startActivity(intent);
                try {
                    ReportManager.pauseVideoReport(mXAdInstance.clickMonitor, mVideoView.getCurrentPosition()
                            / StaticClass.MILLION_UNIT);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void onClickBackBtn() {

    }

    @Override
    public void onClickPlay() {
        sendSUSReport(false);
    }


    @Override
    public void onAdVideoLoadSuccess() {
        if (mSlotListener != null) {
            mSlotListener.onAdVideoLoadSuccess();
        }
    }

    @Override
    public void onAdVideoLoadFailed() {
        if (mSlotListener != null) {
            mSlotListener.onAdVideoLoadFailed();
        }
        //加载失败全部回到初始状态
        canPause = false;
    }

    @Override
    public void onAdVideoLoadComplete() {
        try {
            ReportManager.sueReport(mXAdInstance.endMonitor, false, getDuration());
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (mSlotListener != null) {
            mSlotListener.onAdVideoLoadComplete();
        }
        mVideoView.setIsRealPause(true);
    }

    @Override
    public void onBufferUpdate(int time) {
        try {
            ReportManager.suReport(mXAdInstance.middleMonitor, time / StaticClass.MILLION_UNIT);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取播放器当前秒数
     * @return
     */
    private int getPosition() {
        return mVideoView.getCurrentPosition() / StaticClass.MILLION_UNIT;
    }

    private int getDuration() {
        return mVideoView.getDuration() / StaticClass.MILLION_UNIT;
    }

    private boolean isPlaying() {
        if (mVideoView != null) {
            return mVideoView.isPlaying();
        }
        return false;
    }

    private boolean isRealPause() {
        if (mVideoView != null) {
            return mVideoView.isRealPause();
        }
        return false;
    }

    private boolean isComplete() {
        if (mVideoView != null) {
            return mVideoView.isComplete();
        }
        return false;
    }

    public void destroy() {
        mVideoView.destroy();
        mVideoView = null;
        mContext = null;
        mXAdInstance = null;
    }


    //pause the video
    private void pauseVideo(boolean isAuto) {
        if (mVideoView != null) {
            if (isAuto) {
                //发自动暂停检测
                if (!isRealPause() && isPlaying()) {
                    try {
                        ReportManager.pauseVideoReport(mXAdInstance.event.pause.content, getPosition());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        mVideoView.seekAndPause(0);
    }

    //resume the video
    private void resumeVideo() {
        if (mVideoView != null) {
            mVideoView.resume();
            if (isPlaying()) {
                sendSUSReport(true);
            }
        }
    }

    /**
     * 发送视频开始播放监测
     *
     * @param isAuto
     */
    private void sendSUSReport(boolean isAuto) {
        try {
            ReportManager.susReport(mXAdInstance.startMonitor, isAuto);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }





    //传递消息到appcontext层
    public interface AdSDKSlotListener {

        public ViewGroup getAdParent();

        public void onAdVideoLoadSuccess();

        public void onAdVideoLoadFailed();

        public void onAdVideoLoadComplete();

        public void onClickVideo(String url);
    }
}
