package com.example.jugyang.classroom.ui;

import android.content.Intent;
import android.content.pm.ProviderInfo;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;

import com.example.jugyang.classroom.Network.http.RequestCenter;
import com.example.jugyang.classroom.R;
import com.example.jugyang.classroom.adapter.CourseCommentAdapter;
import com.example.jugyang.classroom.entity.course.BaseCourseModel;
import com.example.jugyang.classroom.entity.course.CourseFooterValue;
import com.example.jugyang.classroom.entity.course.CourseHeaderValue;
import com.example.jugyang.classroom.okHttp.listener.DisposeDataListener;
import com.example.jugyang.classroom.view.course.CourseDetailFooterView;
import com.example.jugyang.classroom.view.course.CourseDetailHeaderView;

import javax.crypto.BadPaddingException;

/**
 * Created by jugyang on 5/9/17.
 * @function 课程详情Activity
 */

public class CourseDetailActivity extends BaseActivity implements View.OnClickListener{

    public static final String COURSE_ID = "coureseID";

    /**
     * UI
     */
    private ImageView mBackView;
    private ListView mListView;
    private ImageView mLoadingView;
    private CourseDetailHeaderView headerView;
    private CourseDetailFooterView footerView;

    /**
     * Data
     */
    private String mCourseID;
    private BaseCourseModel mData;
    private String tempHint = "";
    private CourseCommentAdapter mAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_detail_layout);
        initData();
        initView();
        requestDetail();
    }

    /**
     * 复用Activity时走的生命周期回调
     * @param intent
     */
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent); //更新当前页面Intent
        initData();
        initView();
        requestDetail();
    }

    /**
     * 发送我们的请求
     */
    private void requestDetail() {
        RequestCenter.requestCourseDetail(mCourseID, new DisposeDataListener() {

            @Override
            public void onSuccess(Object reponseObj) {
                mData = (BaseCourseModel) reponseObj;
                updateUI();
            }


            @Override
            public void onFailure(Object reasonObj) {

            }
        });

    }



    /**
     * 根据服务器返回的数据来更新我们的UI
     */
    private void updateUI() {
        mLoadingView.setVisibility(View.GONE);
        mListView.setVisibility(View.VISIBLE);
        mAdapter = new CourseCommentAdapter(this, mData.data.body);
        mListView.setAdapter(mAdapter);

        //主要为了防止headerView多次添加
        if (headerView != null) {
            mListView.removeHeaderView(headerView);
        }
        headerView = new CourseDetailHeaderView(this, mData.data.head);
        mListView.addHeaderView(headerView);
        if (footerView != null) {
            mListView.removeFooterView(footerView);
        }
        footerView = new CourseDetailFooterView(this, mData.data.footer);
        mListView.addFooterView(footerView);
    }


    private void initView() {
        mBackView = (ImageView) findViewById(R.id.back_view);
        mBackView.setOnClickListener(this);
        mListView = (ListView) findViewById(R.id.comment_list_view);
        //mListView.setOnItemClickListener(this);
        mListView.setVisibility(View.GONE);
        mLoadingView = (ImageView) findViewById(R.id.loading_view);
        mLoadingView.setVisibility(View.VISIBLE);
        /**
         * 加载我们的loading动画
         */
        AnimationDrawable anim = (AnimationDrawable) mLoadingView.getDrawable();
        anim.start();
    }

    private void initData() {
        mCourseID = getIntent().getStringExtra(COURSE_ID);
    }


    @Override
    public void onClick(View v) {

    }
}
