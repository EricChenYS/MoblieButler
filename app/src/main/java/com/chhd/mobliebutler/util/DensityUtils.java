package com.chhd.mobliebutler.util;

import android.content.Context;

/**
 * Created by CWQ on 2016/8/26.
 */
public class DensityUtils {

    public static int dp2px(Context context, float dp) {
        float density = context.getResources().getDisplayMetrics().density;
        return (int) (dp * density + 0.5);
    }

    public static double px2dp(Context context, int px) {
        float density = context.getResources().getDisplayMetrics().density;
        return px / density;
    }
}
