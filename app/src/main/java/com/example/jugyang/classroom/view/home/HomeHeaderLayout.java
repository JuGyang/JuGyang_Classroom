package com.example.jugyang.classroom.view.home;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.jugyang.classroom.ImagerLoader.ImageLoaderManager;
import com.example.jugyang.classroom.R;
import com.example.jugyang.classroom.adapter.PhotoPagerAdapter;
import com.example.jugyang.classroom.entity.recommand.RecommandFooterValue;
import com.example.jugyang.classroom.entity.recommand.RecommandHeadValue;
import com.example.jugyang.classroom.view.viewpagerindictor.CirclePageIndicator;

import cn.trinea.android.view.autoscrollviewpager.AutoScrollViewPager;

/**
 * Project Name:     Classroom_Toy_v1.0
 * Package Name:     com.example.jugyang.classroom.view.home
 * File Name:        HomeHeaderLayout
 * Description:      Created by jugyang on 4/14/17.
 * Function:         function
 */

public class HomeHeaderLayout extends RelativeLayout {

    private Context mContext;

    /**
     * UI
     */
    private RelativeLayout mRootView;
    private AutoScrollViewPager mViewPager;
    private CirclePageIndicator mPagerIndicator;
    private TextView mHotView;
    private PhotoPagerAdapter mAdapter;
    private ImageView[] mImageViews = new ImageView[4];
    private LinearLayout mFootLayout;

    /**
     * Data
     */
    private RecommandHeadValue mHeaderValue;
    private ImageLoaderManager mImageLoader;

    public HomeHeaderLayout(Context context, RecommandHeadValue headerValue) {
        this(context, null, headerValue);
    }

    public HomeHeaderLayout(Context context, AttributeSet attrs, RecommandHeadValue headerValue) {
        super(context, attrs);
        mContext = context;
        mHeaderValue = headerValue;
        mImageLoader = ImageLoaderManager.getInstance(mContext);
        initView();
    }

    private void initView() {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        mRootView = (RelativeLayout) inflater.inflate(R.layout.listview_home_head_layout, this);
        mViewPager = (AutoScrollViewPager) mRootView.findViewById(R.id.pager);
        mPagerIndicator = (CirclePageIndicator) mRootView.findViewById(R.id.pager_indicator_view);
        mHotView = (TextView) mRootView.findViewById(R.id.zuixing_view);
        mImageViews[0] = (ImageView) mRootView.findViewById(R.id.head_image_one);
        mImageViews[1] = (ImageView) mRootView.findViewById(R.id.head_image_two);
        mImageViews[2] = (ImageView) mRootView.findViewById(R.id.head_image_three);
        mImageViews[3] = (ImageView) mRootView.findViewById(R.id.head_image_four);
        mFootLayout = (LinearLayout) mRootView.findViewById(R.id.content_layout);

        //为我们的View填充数据
        mAdapter = new PhotoPagerAdapter(mContext, mHeaderValue.ads, true);
        mViewPager.setAdapter(mAdapter);
        mViewPager.startAutoScroll(3000);
        mPagerIndicator.setViewPager(mViewPager);

        for (int i = 0; i < mImageViews.length; i++) {
            mImageLoader.displayImage(mImageViews[i], mHeaderValue.middle.get(i));
        }

        for (RecommandFooterValue value : mHeaderValue.footer) {
            mFootLayout.addView(createItem(value));
        }
        mHotView.setText("-------->Latest News<---------");
    }

    private HomeBottomItem createItem(RecommandFooterValue value) {
        HomeBottomItem item = new HomeBottomItem(mContext, value);
        return item;
    }

}
