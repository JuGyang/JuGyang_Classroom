package com.example.jugyang.classroom.utils;

import android.util.Log;

import java.util.Locale;

/**
 * Project Name:     Classroom_Toy_v1.0
 * Package Name:     com.example.jugyang.classroom.utils
 * File Name:        MyLog
 * Description:      Created by jugyang on 4/10/17.
 */

public class MyLog {
    //Switch (default turn on)
    public static final boolean DEBUG = true;
    //TAG
    public static final String TAG = "Classroom";
    //Level DIWEF
    public static void d(String text) {
        if (DEBUG) {
            Log.d(TAG, text);
        }
    }

    public static void i(String text) {
        if(DEBUG) {
            Log.i(TAG, text);
        }
    }

    public static void w(String text) {
        if(DEBUG) {
            Log.w(TAG, text);
        }
    }

    public static void e(String text) {
        if(DEBUG) {
            Log.e(TAG, text);
        }
    }
}
