package com.example.jugyang.classroom.ImagerLoader;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.widget.ImageView;

import com.example.jugyang.classroom.R;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

/**
 * Project Name:     Classroom_Toy_v1.0
 * Package Name:     com.example.jugyang.classroom.ImagerLoader
 * File Name:        ImageLoaderManager
 * Description:      Created by jugyang on 4/13/17.
 *                   初始化UniverImageLoader，并用来加载网络图片
 */

public class ImageLoaderManager {

    private static final int THREAD_COUNT = 4; //表明我们的UIL最多可以有多少条线程
    private static final int PRIORITY = 2; //表明我们图片加载的优先级
    private static final int DISK_CACHE_SIZE = 50 * 1024; //表明UIL可以最多缓存多少图片
    private static final int CONNECTION_TIME_OUT = 5* 1000; //连接的超时时间
    private static final int READ_TIME_OUT = 30 * 1000; //读取的超时时间

    private static ImageLoader mImageLoader = null;
    private static ImageLoaderManager mInstance = null;

    private static ImageLoaderManager getInstance(Context context) {

        if (mInstance == null) {
            synchronized (ImageLoaderManager.class) {
                if (mInstance == null) {

                    mInstance = new ImageLoaderManager(context);
                }
            }
        }

        return mInstance;
    }

    /**
     * 单例模式的私有构造方法
     */
    private ImageLoaderManager(Context context) {
        ImageLoaderConfiguration configuration = new ImageLoaderConfiguration.
                Builder(context)
                .threadPoolSize(THREAD_COUNT) //配置图片下载线程的最大数量
                .threadPriority(Thread.NORM_PRIORITY - PRIORITY).
                        denyCacheImageMultipleSizesInMemory(). //防止缓存多套尺寸图片到我们的内存中
                        memoryCache(new WeakMemoryCache()) //使用弱引用内存缓存
                        .diskCacheSize(DISK_CACHE_SIZE) //分配硬盘缓存大小
                        .diskCacheFileNameGenerator(new Md5FileNameGenerator()) //使用MD5命名文件
                        .tasksProcessingOrder(QueueProcessingType.LIFO)//图片下载顺序
                        .defaultDisplayImageOptions(getDefaultOptions())//默认的图片加载Options
                        .imageDownloader(new BaseImageDownloader(context, CONNECTION_TIME_OUT, READ_TIME_OUT)) //设置图片下载器
                        .writeDebugLogs() //debug环境下会输出日志
                        .build();

        ImageLoader.getInstance().init(configuration);
        mImageLoader = ImageLoader.getInstance();
    }

    /**
     * 实现我们默认的Options
     * @return
     */
    private DisplayImageOptions getDefaultOptions() {

        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showImageForEmptyUri(R.drawable.xadsdk_img_error) //在我们图片地址为空的时候,加载我们指定的图片
                .showImageOnFail(R.drawable.xadsdk_img_error) //图片下载失败的时候显示的图片
                .cacheInMemory(true) //设置图片可以缓存在内存
                .cacheOnDisk(true) //设置图片可以缓存在硬盘
                .bitmapConfig(Bitmap.Config.RGB_565) //使用的图片解码类型
                .decodingOptions(new BitmapFactory.Options()) //图片解码配置
                .build();

        return options;
    }

    /**
     * 我们加载图片API
     * @param imageView
     * @param url
     * @param options
     * @param listener
     */
    public void displayImage(ImageView imageView, String url,
                             DisplayImageOptions options,
                             ImageLoadingListener listener) {
        if (mImageLoader != null) {
            mImageLoader.displayImage(url, imageView, options, listener);
        }
    }

    public void displayImage(ImageView imageView, String url, ImageLoadingListener listener) {
        displayImage(imageView, url, null, listener);
    }
    public void displayImage(ImageView imageView, String url) {
        displayImage(imageView, url, null, null);
    }
}
