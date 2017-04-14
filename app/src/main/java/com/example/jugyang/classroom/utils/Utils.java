package com.example.jugyang.classroom.utils;

import android.content.Context;

/**
 * Project Name:     Classroom_Toy_v1.0
 * Package Name:     com.example.jugyang.classroom.utils
 * File Name:        Utils
 * Description:      Created by jugyang on 4/14/17.
 * Function:         function
 */

public class Utils {

    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale);
    }

}
