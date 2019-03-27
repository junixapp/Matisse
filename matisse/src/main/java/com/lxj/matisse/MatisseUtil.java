package com.lxj.matisse;

import android.content.Context;

/**
 * Description:
 * Create by dance, at 2019/3/27
 */
public class MatisseUtil {
    public static int dp2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }
}
