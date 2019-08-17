package com.angelova.w510.radixapp.utils;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.WindowManager;

public class Utils {

    public static final String URL = "https://radix.bg:3000/";

    private static final String AES = "AES";
    public static final String ENCRYPTION_PASSWORD = "testPassword";
    public static final String ENCRYPTION_SALT = "12345678";
    public static final int ENCRYPTION_ITERATION_COUNT = 2048;
    public static final int ENCRYPTION_KEY_STRENGTH = 256;

    public static int getWidth(Context context) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        WindowManager windowmanager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        windowmanager.getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.widthPixels;
    }

    public static int getHeight(Context context) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        WindowManager windowmanager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        windowmanager.getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.heightPixels;
    }
}
