package com.example.jugyang.classroom.share;

import android.app.Dialog;
import android.content.Context;
import android.icu.text.LocaleDisplayNames;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jugyang.classroom.Network.http.RequestCenter;
import com.example.jugyang.classroom.R;
import com.example.jugyang.classroom.okHttp.listener.DisposeDownloadListener;
import com.example.jugyang.classroom.utils.StaticClass;

import java.util.HashMap;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;

/**
 * Created by jugyang on 5/14/17.
 */

public class ShareDialog extends Dialog implements View.OnClickListener {

    private Context mContext;
    private DisplayMetrics dm;

    /**
     * UI
     */
    private RelativeLayout mWeixinLayout;
    private RelativeLayout mWeixinMomentLayout;
    private RelativeLayout mQQLayout;
    private RelativeLayout mQZoneLayout;
    private RelativeLayout mDownloadLayout;
    private TextView mCancelView;

    /**
     * share relative
     */
    private int mShareType; //指定分享类型
    private String mShareTitle; //指定分享内容标题
    private String mShareText; //指定分享内容文本
    private String mSharePhoto; //指定分享本地图片
    private String mShareTileUrl;
    private String mShareSiteUrl;
    private String mShareSite;
    private String mUrl;
    private String mResourceUrl;

    private boolean isShowDownload;



    public ShareDialog(Context context, boolean isShowDownload) {
        super(context, R.style.SheetDialogStyle);
        mContext = context;
        dm = mContext.getResources().getDisplayMetrics();
        this.isShowDownload = false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_share_layout);
        initView();
    }

    private void initView() {

        Window dialogWindow = getWindow();
        dialogWindow.setGravity(Gravity.BOTTOM);
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.width = dm.widthPixels; //设置宽度
        dialogWindow.setAttributes(lp);

        mWeixinLayout = (RelativeLayout) findViewById(R.id.weixin_layout);
        mWeixinLayout.setOnClickListener(this);
        mWeixinMomentLayout = (RelativeLayout) findViewById(R.id.moment_layout);
        mWeixinMomentLayout.setOnClickListener(this);
        mQQLayout = (RelativeLayout) findViewById(R.id.qq_layout);
        mQQLayout.setOnClickListener(this);
        mQZoneLayout = (RelativeLayout) findViewById(R.id.qzone_layout);
        mQZoneLayout.setOnClickListener(this);
        mDownloadLayout = (RelativeLayout) findViewById(R.id.download_layout);
        mDownloadLayout.setOnClickListener(this);
        if (isShowDownload) {
            mDownloadLayout.setVisibility(View.VISIBLE);
        }
        mCancelView = (TextView) findViewById(R.id.cancel_view);
        mCancelView.setOnClickListener(this);
    }

    public void setResourceUrl(String resourceUrl) {
        mResourceUrl = resourceUrl;
    }

    public void setShareTitle(String title) {
        mShareTitle = title;
    }

    public void setImagePhoto(String photo) {
        mSharePhoto = photo;
    }

    public void setShareType(int type) {
        mShareType = type;
    }

    public void setShareSite(String site) {
        mShareSite = site;
    }

    public void setShareTitleUrl(String titleUrl) {
        mShareTileUrl = titleUrl;
    }

    public void setUrl(String url) {
        mUrl = url;
    }

    public void setShareSiteUrl(String siteUrl) {
        mShareSiteUrl = siteUrl;
    }

    public void setShareText(String text) {
        mShareText = text;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.weixin_layout:
                shareData(ShareManager.PlatformType.WeChat);
                break;
            case R.id.moment_layout:
                shareData(ShareManager.PlatformType.WeChatMoments);
                break;
            case R.id.qq_layout:
                shareData(ShareManager.PlatformType.QQ);
                break;
            case R.id.qzone_layout:
                shareData(ShareManager.PlatformType.QZone);
                break;
            case R.id.cancel_view:
                dismiss();
                break;
            case R.id.download_layout:
                //检查文件夹是否存在
                RequestCenter.downloadFile(mResourceUrl,
                        StaticClass.APP_PHOTO_DIR.concat(String.valueOf(System.currentTimeMillis())),
                        new DisposeDownloadListener() {
                            @Override
                            public void onSuccess(Object responseObj) {
                                Toast.makeText(mContext,
                                        "下载成功",
                                        Toast.LENGTH_SHORT)
                                        .show();
                            }

                            @Override
                            public void onFailure(Object reasonObj) {
                                Toast.makeText(mContext,
                                        "下载失败" + mResourceUrl,
                                        Toast.LENGTH_SHORT)
                                        .show();
                            }

                            @Override
                            public void onProgress(int progrss) {
                                Log.e("dialog", progrss + "XX");
                            }
                        });
                break;
        }
    }

    /**
     * 分享结果时间监听
     */
    private PlatformActionListener mListener = new PlatformActionListener() {
        @Override
        public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
        }

        @Override
        public void onError(Platform platform, int i, Throwable throwable) {
        }

        @Override
        public void onCancel(Platform platform, int i) {
        }
    };


    /**
     * 完成分享的方法
     * @param platform
     */
    private void shareData(ShareManager.PlatformType platform) {
        ShareData mData = new ShareData();
        Platform.ShareParams params = new Platform.ShareParams();
        params.setShareType(mShareType);
        params.setTitle(mShareTitle);
        params.setTitleUrl(mShareTileUrl);
        params.setSite(mShareSite);
        params.setSiteUrl(mShareSiteUrl);
        params.setText(mShareText);
        params.setImagePath(mSharePhoto);
        params.setUrl(mUrl);
        mData.type = platform;
        mData.params = params;
        ShareManager.getInstance().shareData(mData, mListener);
    }
}
