package com.example.jugyang.classroom.adapter;

import android.content.Context;
import android.media.Image;
import android.provider.Settings;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.jugyang.classroom.ImagerLoader.ImageLoaderManager;
import com.example.jugyang.classroom.R;
import com.example.jugyang.classroom.entity.recommand.RecommandBodyValue;
import com.example.jugyang.classroom.utils.MyLog;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Project Name:     Classroom_Toy_v1.0
 * Package Name:     com.example.jugyang.classroom.adapter
 * File Name:        CourseAdapter
 * Description:      Created by jugyang on 4/13/17.
 * Function:         function
 */

public class    CourseAdapter extends BaseAdapter {

    /**
     *  ListView不同类型的item标识
     */
    private static final int CARD_COUNT = 4;
    private static final int VIDEO_TYPE = 0x00;
    private static final int CARD_SIGNAL_PIC = 0x02;
    private static final int CARD_MULTI_PIC = 0x01;
    private static final int CARD_VIEW_PAGER = 0x03;

    private Context mContext;
    private ViewHolder mViewHolder;
    private LayoutInflater mInflate;

    private ArrayList<RecommandBodyValue> mData;

    /**
     * 异步图片加载工具类
     */
    private ImageLoaderManager mImageLoader;

    /**
     * 构造方法
     * @param context
     * @param data
     */
    public CourseAdapter(Context context, ArrayList<RecommandBodyValue> data) {
        mContext = context;
        mData = data;
        mInflate = LayoutInflater.from(mContext);
        mImageLoader = ImageLoaderManager.getInstance(mContext);
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Object getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getViewTypeCount() {
        return CARD_COUNT;
    }

    /**
     * 通知Adapter使用哪种类型的Item去加载数据
     * @param position
     * @return
     */
    @Override
    public int getItemViewType(int position) {
        RecommandBodyValue value = (RecommandBodyValue) getItem(position);
        return value.type;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //1.获取数据的type类型
        int type = getItemViewType(position);
        final RecommandBodyValue value = (RecommandBodyValue) getItem(position);
        //System.out.println("fuck:" + type);

        //为空表明当前没有可使用的缓存View
        if (convertView == null) {
            switch (type) {
                case CARD_SIGNAL_PIC:
                    mViewHolder = new ViewHolder();
                    convertView = mInflate.inflate(R.layout.item_product_card_two_layout, parent, false);
                    //初始化ViewHolder中所用到的控件
                    mViewHolder.mLogoView = (CircleImageView) convertView.findViewById(R.id.item_logo_view);
                    mViewHolder.mTitleView = (TextView) convertView.findViewById(R.id.item_title_view);
                    mViewHolder.mInfoView = (TextView) convertView.findViewById(R.id.item_info_view);
                    mViewHolder.mFooterView = (TextView) convertView.findViewById(R.id.item_footer_view);
                    mViewHolder.mPriceView = (TextView) convertView.findViewById(R.id.item_price_view);
                    mViewHolder.mFromView = (TextView) convertView.findViewById(R.id.item_from_view);
                    mViewHolder.mZanView = (TextView) convertView.findViewById(R.id.item_zan_view);
                    mViewHolder.mProductView = (ImageView) convertView.findViewById(R.id.product_photo_view);
                    break;
            }

            if (mViewHolder == null){
                MyLog.d("fuck5");
            } else {
                MyLog.d("fuck6");
            }

            convertView.setTag(mViewHolder);
        }
        //有可用的ConvertView
        else {
            mViewHolder = (ViewHolder) convertView.getTag();
        }
        //开始绑定数据
        switch (type) {
            case CARD_SIGNAL_PIC:
                mViewHolder.mTitleView.setText(value.title);
                mViewHolder.mInfoView.setText(value.info.concat("days ago"));
                mViewHolder.mFooterView.setText(value.text);
                mViewHolder.mPriceView.setText(value.price);
                mViewHolder.mFromView.setText(value.from);
                mViewHolder.mZanView.setText("like".concat(value.zan));
                /**
                 * 为ImageView完成图片的加载
                 */
                mImageLoader.displayImage(mViewHolder.mLogoView, value.logo);
                mImageLoader.displayImage(mViewHolder.mProductView, value.url.get(0));
                break;
        }
        return convertView;
    }

    /**
     * 用来缓存我们已经创建好的Item
     */
    private static class ViewHolder {
        //所有Card共有属性
        private CircleImageView mLogoView;
        private TextView mTitleView;
        private TextView mInfoView;
        private TextView mFooterView;
        //Video Card特有属性
        private RelativeLayout mVideoContentLayout;
        private ImageView mShareView;

        //Video Card外所有Card具有属性
        private TextView mPriceView;
        private TextView mFromView;
        private TextView mZanView;
        //Card One特有属性
        private LinearLayout mProductLayout;
        //Card Two特有属性
        private ImageView   mProductView;
        //Card Three特有属性
        private ViewPager mViewPager;
    }
}
