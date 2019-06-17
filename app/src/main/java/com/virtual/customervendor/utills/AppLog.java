package com.virtual.customervendor.utills;

import android.util.Log;

import com.virtual.customervendor.BuildConfig;

public class AppLog {

    public static void e(String tag, String value) {
        if (BuildConfig.DEBUG) {
            Log.e(tag, value);
        }
    }

    public static void i(String tag, String value) {
        if (BuildConfig.DEBUG) {
            Log.i(tag, value);
        }
    }

    public static void d(String tag, String value) {
        if (BuildConfig.DEBUG) {
            Log.d(tag, value);
        }
    }

    public static void w(String tag, String value) {
        if (BuildConfig.DEBUG) {
            Log.w(tag, value);
        }
    }

    public static void v(String tag, String value) {
        if (BuildConfig.DEBUG) {
            Log.v(tag, value);
        }
    }


}
