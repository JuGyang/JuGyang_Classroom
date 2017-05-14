package com.example.jugyang.classroom.share;

import android.content.Context;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.tencent.qzone.QZone;
import cn.sharesdk.wechat.friends.Wechat;
import cn.sharesdk.wechat.moments.WechatMoments;

/**
 * Created by jugyang on 5/14/17.
 * @function 分享功能的统一入口，负责发送数据到指定平台，采用单例模式
 */

public class ShareManager {

    private static ShareManager mShareManager = null;

    /**
     * 线程安全的单例模式
     */
    public static ShareManager getInstance() {
        if (mShareManager == null) {
            synchronized (ShareManager.class) {
                if (mShareManager == null) {
                    mShareManager = new ShareManager();
                }
            }
        }
        return mShareManager;
    }

    private ShareManager() {

    }

    public static void init(Context context) {
        ShareSDK.initSDK(context);
    }

    private Platform mCurrentPlatform;

    /**
     * 分享数据入口
     * @param data
     * @param listener
     */
    public void shareData(ShareData data, PlatformActionListener listener) {

        switch (data.type) {
            case QQ:
                mCurrentPlatform = ShareSDK.getPlatform(QQ.NAME);
                break;
            case QZone:
                mCurrentPlatform = ShareSDK.getPlatform(QZone.NAME);
                break;
            case WeChat:
                mCurrentPlatform = ShareSDK.getPlatform(Wechat.NAME);
                break;
            case WeChatMoments:
                mCurrentPlatform = ShareSDK.getPlatform(WechatMoments.NAME);
                break;
        }
        mCurrentPlatform.setPlatformActionListener(listener);
        mCurrentPlatform.share(data.params);
    }

    /**
     * 应用程序需要使用的平台
     */
    public enum PlatformType {
        QQ, QZone, WeChat, WeChatMoments;
    }

}
