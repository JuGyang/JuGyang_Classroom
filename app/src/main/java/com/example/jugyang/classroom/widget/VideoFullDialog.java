package com.example.jugyang.classroom.widget;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.example.jugyang.classroom.R;
import com.example.jugyang.classroom.core.video.VideoAdSlot;
import com.example.jugyang.classroom.entity.AdValue;
import com.example.jugyang.classroom.report.ReportManager;
import com.example.jugyang.classroom.utils.MyLog;
import com.example.jugyang.classroom.utils.StaticClass;

/**
 * Project Name:     Classroom_Toy_v1.0
 * Package Name:     com.example.jugyang.classroom.widget
 * File Name:        VideoFullDialog
 * Description:      Created by jugyang on 4/22/17.
 * Function:         function
 */

public class VideoFullDialog extends Dialog implements CustomVideoView.ADVideoPlayerListener{

    private static final String TAG = VideoFullDialog.class.getSimpleName();

    /**
     * UI
     */
    private CustomVideoView mVideoView;
    private ViewGroup mParentView;
    private ImageView mBackButton;

    /**
     * Data
     */
    private AdValue mXAdInstance;
    private FullToSmallListener mListener;
    private int mPostion; //从小屏到全屏时视频的播放位置
    private boolean isFirst = true;
    private Bundle mStartBundle;
    private VideoAdSlot.AdSDKSlotListener mSlotListener;

    public VideoFullDialog(Context context, CustomVideoView videoView, AdValue instance, int position) {

        super(context, R.style.dialog_full_screen); //通过style的设置,保证我们的Dialog全屏
        mXAdInstance = instance;
        mVideoView = videoView;
        mPostion = position;

    }

    /**
     * 用来初始化控件
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.xadsdk_dialog_video_layout);
        initVideoView();
    }

    private void initVideoView() {
        mParentView = (RelativeLayout) findViewById(R.id.content_layout);
        mBackButton = (ImageView) findViewById(R.id.xadsdk_player_close_btn);
        mBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickBackBtn();
            }
        });

        mVideoView.setListener(this); //设置事件监听为当前对话框
        mVideoView.mute(false);
        mParentView.addView(mVideoView);
    }

    /**
     * 全屏返回关闭按钮点击事件
     */
    public void onClickBackBtn() {
        dismiss();
        if (mListener != null) {
            mListener.getCurrentPlayPosition(mVideoView.getCurrentPosition());
        }
    }

    /**
     * Back键按下事件监听
     */
    @Override
    public void onBackPressed() {
        onClickBackBtn();
        super.onBackPressed();
    }

    /**
     * 焦点状态改变时的回调
     * @param hasFocus
     */
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        if (!hasFocus) {
            //未取得焦点逻辑
            mPostion = mVideoView.getCurrentPosition();
            mVideoView.pause();
        } else {
            //表明我们的dialog是首次创建且首次获得焦点
            if (isFirst) {
                mVideoView.seekAndResume(mPostion);
            } else {
                //取得焦点逻辑
                mVideoView.resume(); //恢复视频播放
            }
        }
        isFirst = false;
    }

    /**
     * dialog销毁的时候调用
     */
    @Override
    public void dismiss() {
        MyLog.e("dismiss");
        mParentView.removeView(mVideoView);
        super.dismiss();
    }

    /**
     * 实现了ADVideoPlayerListener接口中的方法
     */

    @Override
    public void onBufferUpdate(int time) {
        try {
            if (mXAdInstance != null) {
                ReportManager.suReport(mXAdInstance.middleMonitor, time / StaticClass   .MILLION_UNIT);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClickFullScreenBtn() {

    }

    @Override
    public void onClickVideo() {

    }


    @Override
    public void onAdVideoLoadSuccess() {
        if (mVideoView != null) {
            mVideoView.resume();
        }
    }

    @Override
    public void onAdVideoLoadFailed() {

    }

    @Override
    public void onAdVideoLoadComplete() {
        dismiss();
        if (mListener != null) {
            mListener.playComplete();
        }
    }

    /**
     * 注入事件监听类
     * @param listener
     */
    public void setListener(FullToSmallListener listener) {
        mListener = listener;
    }

    /**
     * 与我们的业务逻辑层(VideoAdSlot)进行通信
     */
    public interface FullToSmallListener {
        void getCurrentPlayPosition(int position); //全屏播放中点击关闭按钮或者back键时回调
        void playComplete(); //全屏播放结束时回调
    }

    @Override
    public void onClickPlay() {

    }

    public void setViewBundle(Bundle bundle) {
        mStartBundle = bundle;
    }

    public void setSlotListener(VideoAdSlot.AdSDKSlotListener slotListener) {
        this.mSlotListener = slotListener;
    }
}
