package com.example.jugyang.classroom.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import com.example.jugyang.classroom.ImagerLoader.ImageLoaderManager;

import java.util.ArrayList;

/**
 * Project Name:     Classroom_Toy_v1.0
 * Package Name:     com.example.jugyang.classroom.adapter
 * File Name:        PhotoPagerAdapter
 * Description:      Created by jugyang on 4/14/17.
 * Function:         function
 */

public class PhotoPagerAdapter extends PagerAdapter {

    private Context mContext;

    private boolean mIsMatch;
    private ArrayList<String> mData;
    private ImageLoaderManager mLoader;

    public PhotoPagerAdapter(Context context, ArrayList<String> list, boolean isMatch) {
        mContext = context;
        mData = list;
        mIsMatch = isMatch;
        mLoader = ImageLoaderManager.getInstance(mContext);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        return super.instantiateItem(container, position);
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }
}
