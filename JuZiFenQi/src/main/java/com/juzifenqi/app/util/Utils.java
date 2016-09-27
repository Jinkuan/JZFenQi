package com.juzifenqi.app.util;

import android.animation.ObjectAnimator;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationCompat.Builder;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.juzifenqi.app.R;
import com.juzifenqi.app.common.Global;
import com.juzifenqi.app.common.model.TextValueObj;
import com.juzifenqi.app.constant.Const;
import com.juzifenqi.app.interfaces.IndexInteface;
import com.juzifenqi.app.interfaces.JsonInterface;
import com.juzifenqi.app.interfaces.JsonStrInterface;
import com.juzifenqi.app.other.SystemBarTintManager;
import com.juzifenqi.app.readwrite.PreferManager;
import com.juzifenqi.app.readwrite.SDCardUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

/**
 * Desc:   一些常用的静态方法工具类
 * Time:   2016-09-23 12:54
 * Author: chende
 */
public class Utils {

    private static long lastClickTime;
    private static View lastView;
    private static final String KEY_MIUI_VERSION_NAME = "ro.miui.ui.version.name";
    private static final long ONE_SECOND = 1000;//秒
    private static final long ONE_MINUTE = 60;// 5分钟
    private static final long THREE_MINUTE = 3 * 60;// 3分钟
    private static final long ONE_HOUR = 60 * 60;// 1小时
    private static final long ONE_DAY = 24 * 60 * 60;// 1天


    public static boolean netWorkAvaliable(Context context) {
        if (context != null) {
            final ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            final NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            return networkInfo != null && networkInfo.isConnected();
        }
        return false;
    }

    public synchronized static boolean isFastClick(View currentView) {
        long time = System.currentTimeMillis();
        if ((time - lastClickTime < 1000) && (lastView == currentView)) {
            return true;
        }
        lastClickTime = time;
        lastView = currentView;
        return false;
    }

    public static void initScreenSize(Context mContext) {// 获取当前手机的屏幕大小及状态栏高度
        DisplayMetrics curMetrics = mContext.getResources().getDisplayMetrics();
        PreferManager.setInt(PreferManager.SCREEN_WIDTH, curMetrics.widthPixels);
        PreferManager.setInt(PreferManager.SCREEN_HEIGHT, curMetrics.heightPixels);
        PreferManager.setInt(PreferManager.STATUS_BAR_HEIGHT, Utils.getStatusBarHeight(mContext));
    }

    public static HashMap<String, String> addParam(HashMap<String, Object> paramsTmp, Context mContext) {
        HashMap<String, String> params = new HashMap<>();
        Global global = Global.getInstance(mContext);
        for (String key : paramsTmp.keySet()) {//将对象转为字符串
            params.put(key, paramsTmp.get(key).toString());
        }
        if (TextUtils.isEmpty(global.token())) {//若当前用户的Token不为空，则添加token到参数中
            params.put("token", global.token());
        }
        params.put("os", "android");
        params.put("build", Build.MODEL);
        params.put("versionCode", String.valueOf(Utils.getVersionCode(mContext)));
        return params;
    }

    public static void JSonArrayStr(JSONArray jsonArray, JsonStrInterface jsonStrInterface) {
        if (jsonArray != null && jsonArray.length() > 0) {
            for (int i = 0; i < jsonArray.length(); i++) {
                String str = jsonArray.optString(i);
                if (!TextUtils.isEmpty(str)) {
                    jsonStrInterface.parse(str, i);
                }
            }
        }
    }


    public static boolean JSonArray(JSONArray jsonArray, int records_of_page, JsonInterface jsonInterface) {
        if (jsonArray != null && jsonArray.length() > 0) {
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.optJSONObject(i);
                if (jsonObject != null) {
                    jsonInterface.parse(jsonObject, i);
                }
            }
            return jsonArray.length() % records_of_page == 0;
        }
        return false;
    }

    public static boolean JSonArrayReverse(JSONArray jsonArray, int records_of_page, JsonInterface jsonInterface) {
        if (jsonArray != null && jsonArray.length() > 0) {
            for (int i = jsonArray.length() - 1; i >= 0; i--) {
                JSONObject jsonObject = jsonArray.optJSONObject(i);
                if (jsonObject != null) {
                    jsonInterface.parse(jsonObject, i);
                }
            }
            return jsonArray.length() % records_of_page == 0;
        }
        return false;
    }

    public static void hidenSoftInput(Context mContext, EditText... views) {//隐藏软键盘
        InputMethodManager inputManager = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
        for (EditText view : views) {
            inputManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public static void changeDirect(Context mContext, Intent intent, boolean finishSelf) {
        Activity mActivity = (Activity) mContext;
        mActivity.startActivity(intent);
        mActivity.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        if (finishSelf) {
            mActivity.finish();
        }
    }

    public static void toFadeIn(Context mContext, Intent intent, boolean finishSelf) {
        Activity mActivity = (Activity) mContext;
        mActivity.startActivity(intent);
        mActivity.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        if (finishSelf) {
            mActivity.finish();
        }
    }

    public static void toFadeOut(Context mContext) {
        Activity mActivity = (Activity) mContext;
        mActivity.finish();
        mActivity.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    public static void toFadeOut(Context mContext, Intent intent) {
        Activity mActivity = (Activity) mContext;
        if (intent != null) {
            mActivity.setResult(Activity.RESULT_OK, intent);
        } else {
            mActivity.setResult(Activity.RESULT_OK);
        }
        mActivity.finish();
        mActivity.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }


    public static void toLeftAnim(Context mContext, Intent intent, boolean finishSelf) {
        Activity mActivity = (Activity) mContext;
        mActivity.startActivity(intent);
        mActivity.overridePendingTransition(R.anim.right_to_current, R.anim.curent_to_left);
        if (finishSelf) {
            mActivity.finish();
        }
    }

    public static void toRightAnim(Context mContext, Intent intent, boolean finishSelf) {
        Activity mActivity = (Activity) mContext;
        mActivity.startActivity(intent);
        mActivity.overridePendingTransition(R.anim.left_to_current, R.anim.curent_to_right);
        if (finishSelf) {
            mActivity.finish();
        }
    }

    public static void toLeftAnimForResult(Context mContext, Intent intent, int requestCode) {
        Activity mActivity = (Activity) mContext;
        mActivity.startActivityForResult(intent, requestCode);
        mActivity.overridePendingTransition(R.anim.right_to_current, R.anim.curent_to_left);
    }

    public static void toFadeInForResult(Context mContext, Intent intent, int requestCode) {
        Activity mActivity = (Activity) mContext;
        mActivity.startActivityForResult(intent, requestCode);
        mActivity.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    public static void toRightForResult(Context mContext, Intent intent) {
        Activity mActivity = (Activity) mContext;
        if (intent != null) {
            mActivity.setResult(Activity.RESULT_OK, intent);
        } else {
            mActivity.setResult(Activity.RESULT_OK);
        }
        mActivity.finish();
        mActivity.overridePendingTransition(R.anim.left_to_current, R.anim.curent_to_right);
    }

    public static void toRightAnim(Context mContext) {
        Activity mActivity = (Activity) mContext;
        mActivity.finish();
        mActivity.overridePendingTransition(R.anim.left_to_current, R.anim.curent_to_right);
    }

    public static int getStatusBarHeight(Context context) {
        Class<?> c = null;
        Object obj = null;
        Field field = null;
        int x = 0;
        int sbarHeight = 0;
        try {
            c = Class.forName("com.android.internal.R$dimen");
            obj = c.newInstance();
            field = c.getField("status_bar_height");
            x = Integer.parseInt(field.get(obj).toString());
            sbarHeight = context.getResources().getDimensionPixelSize(x);
        } catch (Exception e1) {
            Log.i("TAG", "get status bar height fail");
            e1.printStackTrace();
        }
        return sbarHeight;
    }


    public static void addChangeListener(final ViewPager viewPager, final IndexInteface indexInteface, final View... view_tabs) {
        viewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                for (int i = 0; i < view_tabs.length; i++) {
                    if (i == position) {
                        view_tabs[i].setSelected(true);
                    } else {
                        view_tabs[i].setSelected(false);
                    }
                }
                if (indexInteface != null) {
                    indexInteface.callBack(position, null);
                }
                super.onPageSelected(position);
            }
        });
    }

    public static void addChangeListener(final ViewPager viewPager, final IndexInteface indexInteface, final LinearLayout linearLayout, final View... view_tabs) {
        viewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                ObjectAnimator.ofFloat(linearLayout, "TranslationX", Integer.valueOf(linearLayout.getTag().toString()) * linearLayout.getWidth(), position * linearLayout.getWidth()).setDuration(300).start();
                for (int i = 0; i < view_tabs.length; i++) {
                    if (i == position) {
                        view_tabs[i].setSelected(true);
                    } else {
                        view_tabs[i].setSelected(false);
                    }
                }
                if (indexInteface != null) {
                    indexInteface.callBack(position, null);
                }
                super.onPageSelected(position);
            }
        });
    }


    public static SpannableString getStringSpan(String rawStr, Context mContext, int colorResId, float rateMultiple, TextValueObj... valueObjs) {//将指定字符起始的文字设置为系统主题颜色
        SpannableString spannableString = new SpannableString(rawStr);
        for (TextValueObj obj : valueObjs) {
            if (colorResId == 0) {
                spannableString.setSpan(new ForegroundColorSpan(mContext.getResources().getColor(R.color.text_lighter)), rawStr.lastIndexOf(obj.text), rawStr.lastIndexOf(obj.text) + obj.value, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
            } else {
                spannableString.setSpan(new ForegroundColorSpan(mContext.getResources().getColor(colorResId)), rawStr.indexOf(obj.text), rawStr.lastIndexOf(obj.text) + obj.value, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
            }
            spannableString.setSpan(new RelativeSizeSpan(rateMultiple), rawStr.lastIndexOf(obj.text), rawStr.lastIndexOf(obj.text) + obj.value, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        return spannableString;
    }

    public static SpannableString getStringSpan(String rawStr, Context mContext, int colorResId, TextValueObj... valueObjs) {//将指定字符起始的文字设置为系统主题颜色
        SpannableString spannableString = new SpannableString(rawStr);
        for (TextValueObj obj : valueObjs) {
            if (colorResId == 0) {
                spannableString.setSpan(new ForegroundColorSpan(mContext.getResources().getColor(R.color.text_lighter)), rawStr.lastIndexOf(obj.text), rawStr.lastIndexOf(obj.text) + obj.value, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
            } else {
                spannableString.setSpan(new ForegroundColorSpan(mContext.getResources().getColor(colorResId)), rawStr.indexOf(obj.text), rawStr.lastIndexOf(obj.text) + obj.value, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
            }
        }
        return spannableString;
    }

    public static SpannableStringBuilder getStringSpan(String rawStr, String startStr, int offset, Context mContext, int colorResId) {
        int firstSign;
        int lastSign;
        SpannableStringBuilder stringBuilder = new SpannableStringBuilder(rawStr);
        firstSign = rawStr.indexOf(startStr);
        lastSign = firstSign + offset;
        if (colorResId != 0) {
            stringBuilder.setSpan(new ForegroundColorSpan(ContextCompat.getColor(mContext, colorResId)), firstSign, lastSign, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        } else {
            stringBuilder.setSpan(new ForegroundColorSpan(ContextCompat.getColor(mContext, R.color.text_lighter)), firstSign, lastSign, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        }
        return stringBuilder;
    }

    public static SpannableStringBuilder getInvetStringSpan(String rawStr, Context mContext) {
        SpannableStringBuilder stringBuilder = new SpannableStringBuilder(rawStr);
        stringBuilder.setSpan(new ForegroundColorSpan(ContextCompat.getColor(mContext, R.color.text_lighter_gray)), rawStr.length() - 1, rawStr.length(), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        return stringBuilder;
    }

    private static String getMetaDataValue(String name, Context mContext) {
        Object value = null;
        PackageManager packageManager = mContext.getPackageManager();
        ApplicationInfo applicationInfo;
        try {
            applicationInfo = packageManager.getApplicationInfo(mContext.getPackageName(), PackageManager.GET_META_DATA);
            if (applicationInfo != null && applicationInfo.metaData != null) {
                value = applicationInfo.metaData.get(name);
            }
        } catch (PackageManager.NameNotFoundException e) {
            throw new RuntimeException("Could not read the name in the manifest file.", e);
        }
        if (value == null) {
            throw new RuntimeException("The name '" + name + "' is not defined in the manifest file's meta data.");
        }
        return value.toString() == null ? "" : value.toString();
    }

    public static void statusBarColor(Activity activity, Dialog dialog, Boolean whiteFont, int bgColor, Global global) {// 是否白色字体
        try {
            if (isMiUIVX(global)) {//MIUI系统
                statusBarBlackText(activity, dialog, whiteFont ? 0 : 1, global);
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                setTranslucentStatus(true, activity, dialog);
                SystemBarTintManager tintManager = new SystemBarTintManager(activity, dialog);
                tintManager.setStatusBarTintEnabled(true);
                tintManager.setNavigationBarTintEnabled(true);
                tintManager.setStatusBarTintResource(bgColor);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @TargetApi(19)
    private static void setTranslucentStatus(boolean on, Activity activity, Dialog dialog) {
        Window win = activity != null ? activity.getWindow() : dialog.getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }

    /**
     * 只支持MIUI V6
     * 0--只需要状态栏透明 1-状态栏透明且黑色字体 2-清除黑色字体
     */
    public static void statusBarBlackText(Activity mActivity, Dialog dialog, int type, Global global) {
        if (!isMiUIVX(global)) {
            return;
        }
        Window window = mActivity != null ? mActivity.getWindow() : dialog.getWindow();
        Class clazz = window.getClass();
        try {
            int tranceFlag = 0;
            int darkModeFlag = 0;
            Class layoutParams = Class.forName("android.view.MiuiWindowManager$LayoutParams");
            Field field = layoutParams.getField("EXTRA_FLAG_STATUS_BAR_TRANSPARENT");
            tranceFlag = field.getInt(layoutParams);
            field = layoutParams.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE");
            darkModeFlag = field.getInt(layoutParams);
            Method extraFlagField = clazz.getMethod("setExtraFlags", int.class, int.class);
            if (type == 0) {
                extraFlagField.invoke(window, tranceFlag, tranceFlag);//只需要状态栏透明
            } else if (type == 1) {
                extraFlagField.invoke(window, tranceFlag | darkModeFlag, tranceFlag | darkModeFlag);//状态栏透明且黑色字体
            } else {
                extraFlagField.invoke(window, 0, darkModeFlag);//清除黑色字体
            }
        } catch (Exception e) {

        }
    }


    public static boolean isMiUIVX(Global global) {
        String name = global.getBuildProperties().getProperty(KEY_MIUI_VERSION_NAME, "");
        if (!TextUtils.isEmpty(name) && name.toLowerCase(Locale.CHINESE).matches("v[6-9]")) {
            return true;
        } else {
            return false;
        }
    }

    public static String[] amountFormat(double amount) {
        String[] result = new String[2];
        if (amount < 10000) {
            result[0] = new DecimalFormat("#.##").format(amount);
            result[1] = "元";
        } else if (amount < 10000000) {
            result[0] = new DecimalFormat("#.##").format(amount / 10000);
            result[1] = "万";
        } else {
            result[0] = new DecimalFormat("#.##").format(amount / 10000 / 1000);
            result[1] = "亿";
        }
        return result;
    }

    public static void sort(List<Double> list) {
        Collections.sort(list, new Comparator<Double>() {

            @Override
            public int compare(Double rate1, Double rate2) {
                return (int) (rate1 - rate2);
            }
        });
    }

    public static void showNotify(String title, String content, int flagId, Context mContext) {
        NotificationManager noticeManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder mBuilder = new Builder(mContext);
        mBuilder.setContentIntent(PendingIntent.getBroadcast(mContext, flagId, new Intent(Const.BrocastAction.ACTION_CLICK_NOTIFICATION_FRIEND), PendingIntent.FLAG_UPDATE_CURRENT))
                .setWhen(System.currentTimeMillis())// 通知产生的时间，会在通知信息里显示
                .setTicker("您有新的消息")
                .setContentTitle(title)
                .setContentText(content)
                .setPriority(Notification.PRIORITY_DEFAULT)// 设置该通知优先级
                .setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_ALL)
                .setSmallIcon(R.drawable.ic_launcher);
        Notification mNotification = mBuilder.build();
        noticeManager.notify(flagId, mNotification);
    }


    public static String getVersion(Context mContext) {
        return getPackageInfo(mContext).versionName;
    }

    public static int getVersionCode(Context mContext) {
        return getPackageInfo(mContext).versionCode;
    }


    private static PackageInfo getPackageInfo(Context context) {
        PackageInfo packageInfo = null;
        try {
            PackageManager pm = context.getPackageManager();
            packageInfo = pm.getPackageInfo(context.getPackageName(), PackageManager.GET_CONFIGURATIONS);
            return packageInfo;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return packageInfo;
    }

    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    public static String appendUrl(String action_url) {//给url拼接前缀
        String action_url_lower = action_url.toLowerCase(Locale.ENGLISH);
        if (action_url_lower.startsWith("http://") || action_url_lower.startsWith("https://")) {//若已经包含前缀,则不拼接
            return action_url;
        } else {
            return Const.BASE_URL + action_url;
        }
    }

    public static String getTimeDiff(long time, Context mContext) {
        Calendar calendarNow = Calendar.getInstance();
        Calendar calendarTime = Calendar.getInstance();
        String diffStr = null;
        long now = System.currentTimeMillis();
        long diff = (now - time) / 1000;
        if (diff <= THREE_MINUTE) {// 小于5分钟
            diffStr = mContext.getString(R.string.just_now);
        } else if (diff < ONE_HOUR) {// 小于1小时
            diffStr = mContext.getString(R.string.minutes_ago_ars, diff / ONE_MINUTE);
        } else if (diff < ONE_DAY) {// 小于1天
            diffStr = mContext.getString(R.string.hours_ago_ars, diff / ONE_HOUR);
        } else {
            if (calendarTime.get(Calendar.YEAR) < calendarNow.get(Calendar.YEAR)) {// 今年以前
                diffStr = TimeUtils.getDateStr(time);
            } else {// 今年，则只显示月日
                diffStr = getMonthDayInChinese(time);
            }
        }
        return diffStr;
    }

    private static String getMonthDayInChinese(long time) {// 返回标准的年月日时间格式
        SimpleDateFormat formator = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
        return formator.format(new Date(time));
    }

    public static float px2dp(Context context, float px) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (px / scale);
    }

    public static float dp2px(Context context, float dp) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (dp * scale);
    }

    public static void doCrop(String path, StringBuilder lastPath, Uri uri, Context mContext, int requestCode) {
        lastPath.delete(0, lastPath.length());
        lastPath.append(SDCardUtils.getHeadPortraitPath(mContext, String.valueOf(System.currentTimeMillis())));
        Intent intent = new Intent("com.android.camera.action.CROP");
        if (!TextUtils.isEmpty(path)) {
            intent.setDataAndType(Uri.fromFile(new File(path)), "image/*");
        } else {
            intent.setDataAndType(uri, "image/*");
        }
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", Const.USER_PORTRAIT_SIZE);
        intent.putExtra("outputY", Const.USER_PORTRAIT_SIZE);
        intent.putExtra("scale", true);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(lastPath.toString())));
        intent.putExtra("return-data", false);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("noFaceDetection", true);
        Utils.toLeftAnimForResult(mContext, intent, requestCode);
    }


    public static boolean isUri(String urlStr) {
        if (TextUtils.isEmpty(urlStr)) {
            return false;
        }
        String action_url = urlStr.toLowerCase(Locale.ENGLISH);
        return (action_url.startsWith("file://") || action_url.startsWith("drawable://")) || action_url.startsWith("http://") || action_url.startsWith("https://");
    }

    public static String getName(String path) {
        if (!TextUtils.isEmpty(path)) {
            return path.substring(path.lastIndexOf("/") + 1, path.length());
        }
        return "";
    }

    public static String getRealFilePath(final Context context, final Uri uri) {//通过uri获取图片路径
        if (null == uri) return null;
        final String scheme = uri.getScheme();
        String data = null;
        if (scheme == null)
            data = uri.getPath();
        else if (ContentResolver.SCHEME_FILE.equals(scheme)) {
            data = uri.getPath();
        } else if (ContentResolver.SCHEME_CONTENT.equals(scheme)) {
            Cursor cursor = context.getContentResolver().query(uri, new String[]{MediaStore.Images.ImageColumns.DATA}, null, null, null);
            if (null != cursor) {
                if (cursor.moveToFirst()) {
                    int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                    if (index > -1) {
                        data = cursor.getString(index);
                    }
                }
                cursor.close();
            }
        }
        return data;
    }
}

