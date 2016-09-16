package com.chhd.mobliebutler.util;

import android.animation.AnimatorInflater;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.Interpolator;

import com.chhd.mobliebutler.R;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;

import java.text.SimpleDateFormat;

/**
 * Created by CWQ on 2016/8/11.
 */
public class Tools {

    private static Animation animation;
    private static HanyuPinyinOutputFormat hanyuPinyinOutputFormat;
    private static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd hh:mm");

    private Tools() {
    }

    public static void loadColorAnimator(Context context, View view, int animRes) {
        ObjectAnimator objectAnimator = (ObjectAnimator) AnimatorInflater.loadAnimator(context, animRes);
        objectAnimator.setEvaluator(new ArgbEvaluator());
        objectAnimator.setTarget(view);
        objectAnimator.start();
    }

    public static String getLetter(String name) {
        try {
            if (hanyuPinyinOutputFormat == null) {
                hanyuPinyinOutputFormat = new HanyuPinyinOutputFormat();
                hanyuPinyinOutputFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
            }
            String letter = "" + PinyinHelper.toHanyuPinyinString(name, hanyuPinyinOutputFormat, "").toUpperCase().charAt(0);
            if (!letter.matches("[A-Z]+")) {
                letter = "#";
            }
            return letter;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Animation getShakeAimation(Context context) {
        if (animation == null) {
            animation = AnimationUtils.loadAnimation(context, R.anim.set_shake);
            animation.setInterpolator(new InnerInterpolator());
        }
        return animation;
    }

    public static String formatTime(long date) {
        return simpleDateFormat.format(date);
    }

    private static class InnerInterpolator implements Interpolator {
        @Override
        public float getInterpolation(float input) {
            return (float) (Math.sin(2 * 6 * Math.PI * input));
        }
    }
}
