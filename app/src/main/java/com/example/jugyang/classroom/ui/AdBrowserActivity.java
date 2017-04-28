package com.example.jugyang.classroom.ui;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.Button;

import com.example.jugyang.classroom.core.video.VideoAdSlot;
import com.example.jugyang.classroom.utils.MyLog;
import com.example.jugyang.classroom.utils.Utils;
import com.example.jugyang.classroom.widget.adbrowser.AdBrowserLayout;
import com.example.jugyang.classroom.widget.adbrowser.AdBrowserWebViewClient;
import com.example.jugyang.classroom.widget.adbrowser.Base64Drawables;
import com.example.jugyang.classroom.widget.adbrowser.BrowserWebView;

/**
 * Project Name:     Classroom_Toy_v1.0
 * Package Name:     com.example.jugyang.classroom.ui
 * File Name:        AdBrowserActivity
 * Description:      Created by jugyang on 4/23/17.
 * Function:         广告页面View
 */

public class AdBrowserActivity extends Activity {

    public static final String KEY_URL = "url";

    private BrowserWebView mAdBrowserWebView;
    private AdBrowserLayout mLayout;

    private boolean mIsBackFromMarket = false;

    private View mProgress;
    private Button mBackButton;

    private String mUrl;

    private AdBrowserWebViewClient.Listener mWebClientListener;

    private Base64Drawables mBase64Drawables = new Base64Drawables();

    @Override
    public final void onCreate(Bundle bundle) {
        super.onCreate(bundle);

        if (isValidExtras()) {
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

            mLayout = new AdBrowserLayout(this.getApplicationContext());
            setContentView(mLayout);

            mProgress = mLayout.getProgressBar();
            mBackButton = mLayout.getBackButton();

            mAdBrowserWebView= mLayout.getWebView();
            initWebView(mAdBrowserWebView);

            if (bundle != null) {
                mAdBrowserWebView.restoreState(bundle);
            } else {
                mAdBrowserWebView.loadUrl(mUrl);
            }
            initButtonListeners(mAdBrowserWebView);
        } else {
            finish();
        }
    }

    private void initButtonListeners(final WebView webView) {
        mLayout.getBackButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (webView.canGoBack()) {
                    mLayout.getProgressBar().setVisibility(View.VISIBLE);
                    webView.goBack();
                }
            }
        });

        mLayout.getCloseButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mLayout.getRefreshButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mLayout.getProgressBar().setVisibility(View.VISIBLE);
                webView.reload();
            }
        });

        mLayout.getNativeButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String uriString = webView.getUrl();
                if (uriString != null) {
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(uriString));

                    boolean isActivityResolved = getPackageManager()
                            .resolveActivity(browserIntent, PackageManager.MATCH_DEFAULT_ONLY) != null
                            ? true : false;
                    if (isActivityResolved) {
                        startActivity(browserIntent);
                    }
                }
            }
        });
    }

    private void initWebView(BrowserWebView webView) {
        mWebClientListener = initAdBrowserClientListener();
        AdBrowserWebViewClient client = new AdBrowserWebViewClient(mWebClientListener);
        webView.setWebViewClient(client);
        webView.getSettings().setBuiltInZoomControls(false);
    }

    private AdBrowserWebViewClient.Listener initAdBrowserClientListener() {
        return new AdBrowserWebViewClient.Listener() {

            @Override
            public void onReceiveError() {
                finish();
            }

            @Override
            public void onPageStarted() {
                mProgress.setVisibility(View.VISIBLE);
            }

            @Override
            @SuppressLint("NewApi")
            public void onPageFinished(boolean canGoBack) {
                mProgress.setVisibility(View.INVISIBLE);
                if (canGoBack) {
                    setImage(mBackButton, mBase64Drawables.getBackActive());
                } else {
                    setImage(mBackButton, mBase64Drawables.getBackInactive());
                }
            }

            @Override
            public void onLeaveApp() {

            }
        };
    }

    @SuppressLint("NewApi")
    private void setImage(Button button, String imageString) {
        if (Build.VERSION.SDK_INT < 16) {
            button.setBackgroundDrawable(Utils.decodeImage(imageString));
        } else {
            button.setBackground(Utils.decodeImage(imageString));
        }
    }

    private boolean isValidExtras() {
        mUrl = getIntent().getStringExtra(KEY_URL);
        return !TextUtils.isEmpty(mUrl);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MyLog.d("onPause");
        if (mAdBrowserWebView != null) {
            mAdBrowserWebView.onPause();
        }
    }

    @Override
    protected void onDestroy() {
        MyLog.i("onDestroy");
        if (mAdBrowserWebView != null) {
            mAdBrowserWebView.clearCache(true);
        }
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        MyLog.i("onResume");
        if (mIsBackFromMarket) {

        }

        mIsBackFromMarket = true;
        mLayout.getProgressBar().setVisibility(View.INVISIBLE);
    }

    @Override
    public final boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (mAdBrowserWebView.canGoBack()) {
                mAdBrowserWebView.goBack();
                return true;
            }
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        mIsBackFromMarket = false;
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        mAdBrowserWebView.saveState(outState);
        super.onSaveInstanceState(outState);
    }
}
