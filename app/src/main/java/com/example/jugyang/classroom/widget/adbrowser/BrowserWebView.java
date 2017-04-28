package com.example.jugyang.classroom.widget.adbrowser;

import android.content.Context;
import android.webkit.WebSettings;
import android.webkit.WebView;

/**
 * Project Name:     Classroom_Toy_v1.0
 * Package Name:     com.example.jugyang.classroom.widget.adbrowser
 * File Name:        BrowserWebView
 * Description:      Created by jugyang on 4/23/17.
 * Function:         自定义WebView,设置一些能用的参数
 */

public class BrowserWebView extends WebView {

    public BrowserWebView(Context context) {
        super(context);
        init();
    }

    private void init() {
        WebSettings webSettings = getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setPluginState(WebSettings.PluginState.ON);

        webSettings.setBuiltInZoomControls(true);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setUseWideViewPort(true);
        setInitialScale(1);
    }
}
