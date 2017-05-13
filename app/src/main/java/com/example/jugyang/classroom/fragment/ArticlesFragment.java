package com.example.jugyang.classroom.fragment;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import com.example.jugyang.classroom.Network.http.RequestCenter;
import com.example.jugyang.classroom.R;
import com.example.jugyang.classroom.adapter.CourseAdapter;
import com.example.jugyang.classroom.entity.recommand.BaseRecommandModel;
import com.example.jugyang.classroom.entity.recommand.RecommandBodyValue;
import com.example.jugyang.classroom.okHttp.listener.DisposeDataListener;
import com.example.jugyang.classroom.ui.PhotoViewActivity;
import com.example.jugyang.classroom.utils.MyLog;
import com.example.jugyang.classroom.view.home.HomeHeaderLayout;

/**
 * Project Name:     Classroom_Toy_v1.0
 * Package Name:     com.example.jugyang.classroom.fragment
 * File Name:        LiveFragment
 * Description:      Created by jugyang on 4/9/17.
 */

public class ArticlesFragment extends Fragment implements AdapterView.OnItemClickListener{

    private Activity mContext;
    /**
     * UI
     */
    private View mContentView;
    private ListView mListView;
    private ImageView mLoadingView;

    /**
     * data
     */
    private BaseRecommandModel mRecommandData;
    private CourseAdapter mAdapter;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestRecommandData();
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mContext = getActivity();
        mContentView = inflater.inflate(R.layout.fragment_mainpage, null);
        initView();
        return mContentView;
    }

    private void initView() {
        mListView = (ListView) mContentView.findViewById(R.id.list_view);
        mListView.setOnItemClickListener(this);
        mLoadingView = (ImageView) mContentView.findViewById(R.id.loading_view);
        AnimationDrawable anim = (AnimationDrawable) mLoadingView.getDrawable();
        anim.start();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        RecommandBodyValue value = (RecommandBodyValue) mAdapter.getItem(position - mListView.getHeaderViewsCount());
        if (value.type != 0) {
            Intent intent = new Intent(mContext, PhotoViewActivity.class);
            intent.putStringArrayListExtra(PhotoViewActivity.PHOTO_LIST, value.url);
            startActivity(intent);
        }
    }

    /**
     * 发送首页列表数据请求
     */
    private void requestRecommandData() {

        RequestCenter.requestRecommandData(new DisposeDataListener() {
            @Override
            public void onSuccess(Object reponseObj) {
                //完成我们真正的功能逻辑
                MyLog.e("onSuccess: " + reponseObj.toString());
                /**
                 * 获取到数据后更新我们的UI
                 */
                mRecommandData = (BaseRecommandModel) reponseObj;
                showSuccessView();

            }

            @Override
            public void onFailure(Object reasonObj) {
                //提示用户网络有问题
                MyLog.e("onFailure" + reasonObj.toString());
            }
        });
    }

    /**
     * 请求成功后执行的方法
     */
    private void showSuccessView() {
        //判断数据是否为空
        if (mRecommandData.data.list != null && mRecommandData.data.list.size() > 0) {

            mLoadingView.setVisibility(View.GONE);
            mListView.setVisibility(View.VISIBLE);

            //为ListView添加列表头
            mListView.addHeaderView(new HomeHeaderLayout(mContext, mRecommandData.data.head));

            //创建我们的Adapter
            mAdapter = new CourseAdapter(mContext, mRecommandData.data.list);
            mListView.setAdapter(mAdapter);

            mListView.setOnScrollListener(new AbsListView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(AbsListView view, int scrollState) {
                }

                @Override
                public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                    MyLog.d("showSuccessView");
                    mAdapter.updateAdInScrollView();
                }
            });

        } else {
            showErrorView();
        }
    }
    /**
     * 请求失败后执行的方法
     */
    private void showErrorView() {

    }

}
