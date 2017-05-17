package com.example.jugyang.classroom.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v4.view.ViewPager;
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
import com.example.jugyang.classroom.core.AdContextInterface;
import com.example.jugyang.classroom.core.video.VideoAdContext;
import com.example.jugyang.classroom.entity.recommand.RecommandBodyValue;
import com.example.jugyang.classroom.share.ShareDialog;
import com.example.jugyang.classroom.ui.AdBrowserActivity;
import com.example.jugyang.classroom.ui.PhotoViewActivity;
import com.example.jugyang.classroom.util.Util;
import com.example.jugyang.classroom.utils.MyLog;
import com.example.jugyang.classroom.utils.Utils;
import com.google.gson.Gson;

import java.util.ArrayList;

import cn.sharesdk.framework.Platform;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Project Name:     Classroom_Toy_v1.0
 * Package Name:     com.example.jugyang.classroom.adapter
 * File Name:        CourseAdapter
 * Description:      Created by jugyang on 4/13/17.
 * Function:         function
 */

public class CourseAdapter extends BaseAdapter {

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
    private VideoAdContext mAdsdkContext;


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
        //为空表明当前没有可使用的缓存View
        if (convertView == null) {
            switch (type) {
                case VIDEO_TYPE:
                    System.out.println("Classroom Type: " + type);
                    mViewHolder = new ViewHolder();
                    convertView = mInflate.inflate(R.layout.item_product_video_type_layout, parent, false);
                    mViewHolder.mVideoContentLayout = (RelativeLayout)
                            convertView.findViewById(R.id.video_ad_layout);
                    mViewHolder.mLogoView = (CircleImageView) convertView.findViewById(R.id.item_logo_view);
                    mViewHolder.mTitleView = (TextView) convertView.findViewById(R.id.item_title_view);
                    mViewHolder.mInfoView = (TextView) convertView.findViewById(R.id.item_info_view);
                    mViewHolder.mFooterView = (TextView) convertView.findViewById(R.id.item_footer_view);
                    mViewHolder.mShareView = (ImageView) convertView.findViewById(R.id.item_share_view);
                    //为对应布局创建播放器
                    mAdsdkContext = new VideoAdContext(mViewHolder.mVideoContentLayout,
                            new Gson().toJson(value), null);
                    mAdsdkContext.setAdResultListener(new AdContextInterface() {
                        @Override
                        public void onAdSuccess() {
                        }

                        @Override
                        public void onAdFailed() {
                        }

                        @Override
                        public void onClickVideo(String url) {
                            Intent intent = new Intent(mContext, AdBrowserActivity.class);
                            intent.putExtra(AdBrowserActivity.KEY_URL, url);
                            mContext.startActivity(intent);
                        }
                    });
                    break;
                case CARD_SIGNAL_PIC:
                    //单图模式
                    mViewHolder = new ViewHolder();
                    convertView = mInflate.inflate(R.layout.item_product_card_signal_picture_layout, parent, false);
                    //初始化单图ViewHolder中所用到的控件
                    mViewHolder.mLogoView = (CircleImageView) convertView.findViewById(R.id.item_logo_view);
                    mViewHolder.mTitleView = (TextView) convertView.findViewById(R.id.item_title_view);
                    mViewHolder.mInfoView = (TextView) convertView.findViewById(R.id.item_info_view);
                    mViewHolder.mFooterView = (TextView) convertView.findViewById(R.id.item_footer_view);
                    mViewHolder.mPriceView = (TextView) convertView.findViewById(R.id.item_price_view);
                    mViewHolder.mFromView = (TextView) convertView.findViewById(R.id.item_from_view);
                    mViewHolder.mZanView = (TextView) convertView.findViewById(R.id.item_zan_view);
                    mViewHolder.mProductView = (ImageView) convertView.findViewById(R.id.product_photo_view);
                    break;
                case CARD_MULTI_PIC:
                    //多图模式
                    mViewHolder = new ViewHolder();
                    convertView = mInflate.inflate(R.layout.item_product_card_multi_picture_layout, parent, false);
                    //初始化多图ViewHolder中所用到的控件
                    mViewHolder.mLogoView = (CircleImageView) convertView.findViewById(R.id.item_logo_view);
                    mViewHolder.mTitleView = (TextView) convertView.findViewById(R.id.item_title_view);
                    mViewHolder.mInfoView = (TextView) convertView.findViewById(R.id.item_info_view);
                    mViewHolder.mFooterView = (TextView) convertView.findViewById(R.id.item_footer_view);
                    mViewHolder.mPriceView = (TextView) convertView.findViewById(R.id.item_price_view);
                    mViewHolder.mFromView = (TextView) convertView.findViewById(R.id.item_from_view);
                    mViewHolder.mZanView = (TextView) convertView.findViewById(R.id.item_zan_view);
                    mViewHolder.mProductLayout = (LinearLayout) convertView.findViewById(R.id.product_photo_layout);
                    break;
                case CARD_VIEW_PAGER:
                    mViewHolder = new ViewHolder();
                    convertView = mInflate.inflate(R.layout.item_product_card_view_pager_layout, parent, false);

                    mViewHolder.mViewPager = (ViewPager) convertView.findViewById(R.id.pager);
                    mViewHolder.mViewPager.setPageMargin(Utils.dip2px(mContext, 12));
                    //为我们的VIewPager填充数据
                    ArrayList<RecommandBodyValue> recommandList = Util.handleData(value);
                    //设置热卖Adapter
                    mViewHolder.mViewPager.setAdapter(new HotSaleAdapter(mContext, recommandList));
                    //一开始就让我们的ViewPager处于一个比较靠中间的Item项
                    mViewHolder.mViewPager.setCurrentItem(recommandList.size() * 100);
                    break;

            }
            convertView.setTag(mViewHolder);
        }
        //有可用的ConvertView
        else {
            mViewHolder = (ViewHolder) convertView.getTag();
        }
        //开始绑定数据
        switch (type) {
            case VIDEO_TYPE:
                mImageLoader.displayImage(mViewHolder.mLogoView, value.logo);
                mViewHolder.mTitleView.setText(value.title);
                mViewHolder.mInfoView.setText(value.info.concat("days ago"));
                mViewHolder.mFooterView.setText(value.text);
                mViewHolder.mShareView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ShareDialog dialog = new ShareDialog(mContext, false);
                        dialog.setShareType(Platform.SHARE_VIDEO);
                        dialog.setShareTitle(value.title);
                        dialog.setShareTitleUrl(value.site);
                        dialog.setShareText(value.text);
                        dialog.setShareSite(value.title);
                        dialog.setShareTitle(value.site);
                        dialog.setUrl(value.resource);
                        dialog.show();
                    }
                });
                break;
            case CARD_SIGNAL_PIC:
                //单图模式
                System.out.println("Classroom Type: " + type);
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
            case CARD_MULTI_PIC:
                //多图模式
                //为我们的LogoView加载异步图片
                mImageLoader.displayImage(mViewHolder.mLogoView, value.logo);
                mViewHolder.mTitleView.setText(value.title);
                mViewHolder.mInfoView.setText(value.info.concat("days ago"));
                mViewHolder.mFooterView.setText(value.text);
                mViewHolder.mPriceView.setText(value.price);
                mViewHolder.mFromView.setText(value.from);
                mViewHolder.mZanView.setText("like ".concat(value.zan));
                mViewHolder.mProductLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(mContext, PhotoViewActivity.class);
                        intent.putStringArrayListExtra(PhotoViewActivity.PHOTO_LIST, value.url);
                        mContext.startActivity(intent);
                    }
                });
                //动态添加我们的ImageView到我们的水平ScrollView中
                mViewHolder.mProductLayout.removeAllViews();//删除已有的图片
                for (String url: value.url) {
                    mViewHolder.mProductLayout.addView(createImageView(url));
                }
                break;
            case CARD_VIEW_PAGER:
                break;

        }
        return convertView;
    }

    //自动播放方法
    public void updateAdInScrollView() {
        if (mAdsdkContext != null) {
            mAdsdkContext.updateAdInScrollView();
        }
    }

    /**
     * 动态创建我们的ImageView
     * @return
     */
    private ImageView createImageView(String url) {
        ImageView imageView = new ImageView(mContext);
        //与要添加到的ViewGroup保持一致
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                Utils.dip2px(mContext, 100), LinearLayout.LayoutParams.MATCH_PARENT);
        params.leftMargin = Utils.dip2px(mContext, 5);
        imageView.setLayoutParams(params);
        mImageLoader.displayImage(imageView, url);

        return imageView;
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
