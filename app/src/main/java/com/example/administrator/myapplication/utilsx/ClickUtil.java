package com.example.administrator.myapplication.utilsx;

import android.app.Activity;
import android.view.MotionEvent;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

/**
 *
 * Author:Administrator
 * Date:2018/newhomepageone/5
 * 佛祖保佑       永无BUG     永不修改
 */


public class ClickUtil {
    private static long lastClickTime;


    /**
     * 禁止实例化本工具类，因为实例化本工具类没有任何意义
     */
    private ClickUtil() {
        throw new AssertionError();
    }

    /**
     * 判断点击频率过快，即是否双击
     *
     * @return
     */
    public static boolean isFastDoubleClick() {
        long time = System.currentTimeMillis();
        long timeD = time - lastClickTime;
        if (0 < timeD && timeD < 500) {
            return true;
        }
        lastClickTime = time;
        return false;
    }

    /**
     * 触发屏幕点击事件
     *
     * @param x 点击位置的x值
     * @param y 点击位置的y值
     */
    public static void setMouseClick(Activity activity, int x, int y) {
        MotionEvent evenDown = MotionEvent.obtain(System.currentTimeMillis(),
                System.currentTimeMillis() + 100, MotionEvent.ACTION_DOWN, x, y, 0);
        activity.dispatchTouchEvent(evenDown);
        MotionEvent eventUp = MotionEvent.obtain(System.currentTimeMillis(),
                System.currentTimeMillis() + 100, MotionEvent.ACTION_UP, x, y, 0);
        activity.dispatchTouchEvent(eventUp);
        evenDown.recycle();
        eventUp.recycle();
    }

    /**
     * 转换get请求参数   集合转换字符串工具
     *
     * @param map
     * @return
     */
    public static String getGet(HashMap<String, String> map) {
        StringBuffer sb = new StringBuffer();
        String key = null;
        Set<String> set = map.keySet();
        Iterator<String> it = set.iterator();

        while (it.hasNext()) {
            key = it.next();
            sb.append(key + "=" + map.get(key) + "&");
        }
        String sbss=sb.substring(0,sb.length()-1);//减去最后一个字符号&

        L.i("sbss",sbss.toString());
        return sbss.toString();


    }

}