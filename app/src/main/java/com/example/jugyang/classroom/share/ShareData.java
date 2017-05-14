package com.example.jugyang.classroom.share;

import cn.sharesdk.framework.Platform;

/**
 * Created by jugyang on 5/14/17.
 * @function 封装到要分享到的平台和数据
 */

public class ShareData {

    /**
     * 要分享到的平台的参数
     */
    public Platform.ShareParams params;
    /**
     * 要分享到的平台
     */
    public ShareManager.PlatformType type;

}
