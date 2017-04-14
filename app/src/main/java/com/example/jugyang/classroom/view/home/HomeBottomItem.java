package com.example.jugyang.classroom.view.home;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.jugyang.classroom.ImagerLoader.ImageLoaderManager;
import com.example.jugyang.classroom.R;
import com.example.jugyang.classroom.entity.recommand.RecommandFooterValue;

/**
 * Project Name:     Classroom_Toy_v1.0
 * Package Name:     com.example.jugyang.classroom.view.home
 * File Name:        HomeBottomItem
 * Description:      Created by jugyang on 4/14/17.
 * Function:         function
 */

public class HomeBottomItem extends RelativeLayout{

    private Context mContext;
    /**
     * UI
     */
    private RelativeLayout mRootView;
    private TextView mTitleView;
    private TextView mInfoView;
    private TextView mInterestingView;
    private ImageView mImageOneView;
    private ImageView mImageTwoView;

    /**
     * Data
     */
    private RecommandFooterValue mData;
    private ImageLoaderManager mImageLoader;

    public HomeBottomItem(Context context, RecommandFooterValue data) {
        this(context, null, data);
    }

    public HomeBottomItem(Context context, AttributeSet attrs, RecommandFooterValue data) {
        super(context, attrs);
        mContext = context;
        mData = data;
        mImageLoader = ImageLoaderManager.getInstance(mContext);
        initView();
    }

    private void initView() {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        mRootView = (RelativeLayout) inflater.inflate(R.layout.item_home_recommand_layout, this); //添加到当前布局中
        mTitleView = (TextView) mRootView.findViewById(R.id.title_view);
        mInfoView = (TextView) mRootView.findViewById(R.id.info_view);
        mInterestingView = (TextView) mRootView.findViewById(R.id.interesting_view);
        mImageOneView = (ImageView) mRootView.findViewById(R.id.icon_1);
        mImageTwoView = (ImageView) mRootView.findViewById(R.id.icon_2);
//        mRootView.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(mContext, AdBrowserActivity.class);
//                intent.putExtra(AdBrowserActivity.KEY_URL, mData.destationUrl);
//                mContext.startActivity(intent);
//            }
//        });
        mTitleView.setText(mData.title);
        mInfoView.setText(mData.info);
        mInterestingView.setText(mData.from);
        mImageLoader.displayImage(mImageOneView, mData.imageOne);
        mImageLoader.displayImage(mImageTwoView, mData.imageTwo);
    }}
