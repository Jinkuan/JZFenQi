package com.juzifenqi.app.main.base;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.handmark.pulltorefresh.library.PullToRefreshGridView;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.handmark.pulltorefresh.library.PullToRefreshWebView;
import com.juzifenqi.app.R;
import com.juzifenqi.app.common.Global;
import com.juzifenqi.app.constant.Const;
import com.juzifenqi.app.http.AsyncRequest;
import com.juzifenqi.app.http.TRequestCallBack;
import com.juzifenqi.app.readwrite.PreferManager;
import com.juzifenqi.app.util.Utils;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;


/**
 * Desc:   Activity基类
 * Time:   2016-09-23 15:08
 * Author: chende
 */
public abstract class BaseActivity extends FragmentActivity implements View.OnClickListener {

    protected Intent intent;
    protected Context mContext;
    private Toast toast;
    protected Global global;
    protected EventBus eventBus;
    protected BaseFragment lastFragment;
    protected PullToRefreshListView refresh_list;// 可上下拉刷新列表
    protected PullToRefreshGridView gridView;// 可上下拉刷新列表
    protected PullToRefreshScrollView scrollview;// 可下拉刷新的ScrollView
    protected PullToRefreshWebView webView;// 可下拉刷新的WebView
    protected int current_page = 1;// 当前面码
    protected int records_of_page = 20;// 每页记录的条数
    protected int screen_width;// 屏幕宽度
    protected int screen_height;// 屏幕高度
    protected int status_bar_height;// 状态栏高度
    protected boolean has_more = true;// 标记获取列表接口是否有更多数据
    protected boolean pull_down = true;// 标记列表是上拉还是下拉
    protected HashMap<String, Object> params = new HashMap<String, Object>();
    protected boolean whiteFont = false;//针对小米手机作的处理
    protected int statusBgColor = R.color.transparent;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        eventBus = EventBus.getDefault();
        global = Global.getInstance(mContext);
        screen_width = PreferManager.getInt(PreferManager.SCREEN_WIDTH, 1080);
        screen_height = PreferManager.getInt(PreferManager.SCREEN_HEIGHT, 1920);
        status_bar_height = PreferManager.getInt(PreferManager.STATUS_BAR_HEIGHT, 25);
        toast = global.getToast();
        intent = getIntent() == null ? new Intent() : getIntent();
        initViewAfterOnCreate();
        initDataAfterOnCreate();
        Utils.statusBarColor(this, null, whiteFont, statusBgColor, global);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.iv_left) {
            Utils.toRightAnim(mContext);
        }
    }

    @SuppressWarnings({"unchecked"})
    protected <T> T findById(int id) {
        T view = (T) findViewById(id);
        return view;
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {// 用户点击"返回键"的时候，
        if (event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
            if (viewIsRefreshing()) {// 判断若当前有View在刷新，则返回true
                return true;
            }
        }
        return super.dispatchKeyEvent(event);
    }

    protected boolean viewIsRefreshing() { // 先判断当前Activity的子fragment是否有刷新中的列表，再判断当前是否有刷新中的View
        if ((lastFragment != null && lastFragment.isRefreshing()) || isRefreshing()) {
            return true;
        } else {
            return false;
        }
    }

    private boolean isRefreshing() {// 若当前View正在刷新，则先取消刷新
        if (refresh_list != null && refresh_list.isRefreshing()) {
            refresh_list.onRefreshComplete();
            return true;
        }
        if (gridView != null && gridView.isRefreshing()) {
            gridView.onRefreshComplete();
            return true;
        }
        if (scrollview != null && scrollview.isRefreshing()) {
            scrollview.onRefreshComplete();
            return true;
        }
        if (webView != null && webView.isRefreshing()) {
            webView.onRefreshComplete();
            return true;
        }
        return false;
    }


    protected void requestData(String action_url, String toast, TRequestCallBack requestCallBack) {// 请接口获取数据
        if (!Utils.netWorkAvaliable(mContext)) {
            toShow(R.string.net_work_disable);
            handler.sendEmptyMessage(0);
            return;
        }
        AsyncRequest.post().url(Const.BASE_URL + action_url).params(Utils.addParam(params, mContext)).build().execute(requestCallBack);
    }

    protected void requestFirstData(String action_url, TRequestCallBack requestCallBack) {// 列表下拉，获取第一页数据
        current_page = 1;
        params.put("page", current_page);
        params.put("pagecount", records_of_page);
        AsyncRequest.post().url(Const.BASE_URL + action_url).params(Utils.addParam(params, mContext)).build().execute(requestCallBack);
    }


    protected void requestMoreData(String action_url, TRequestCallBack requestCallBack) {// 列表上拉，获取更多数据
        if (has_more) {
            params.put("page", ++current_page);
            params.put("pagecount", records_of_page);
            AsyncRequest.post().url(Const.BASE_URL + action_url).params(Utils.addParam(params, mContext)).build().execute(requestCallBack);
        } else {
            toShow(R.string.no_more_data);
            handler.sendEmptyMessage(0);
        }
    }


    protected void registerOnClickListenter(BaseActivity mActivity, View... views) {//注册点击事件
        for (View view : views) {
            view.setOnClickListener(mActivity);
        }
    }

    protected void toShow(String str) {
        if (!TextUtils.isEmpty(str)) {
            toast.setText(str);
            toast.show();
        }
    }

    protected void toShow(int strId) {
        toShow(getString(strId));
    }

    protected void setTitleStrId(int resId) {// 设置标题栏文本
        TextView tv = (TextView) findViewById(R.id.tv_title);
        if (resId != 0 && tv != null) {
            tv.setText(getString(resId));
        }
    }

    protected View setTitleStr(String text) {// 设置标题栏文本
        TextView tv = (TextView) findViewById(R.id.tv_title);
        if (!TextUtils.isEmpty(text) && tv != null) {
            tv.setText(text);
        }
        return tv;
    }

    protected void backWithTitile(String text) {
        TextView tv = (TextView) findViewById(R.id.tv_title);
        if (!TextUtils.isEmpty(text) && tv != null) {
            tv.setText(text);
        }
        getLeftIV().setOnClickListener(this);
    }

    protected ImageView getLeftIV() {// 显示返回按钮并取得引用
        ImageView iv = (ImageView) findViewById(R.id.iv_left);
        if (iv != null) {
            iv.setVisibility(View.VISIBLE);
        }
        return iv;
    }

    protected ImageView getRightIV(int resId) {// 显示右侧按钮并取得引用
        ImageView iv = (ImageView) findViewById(R.id.iv_right);
        if (iv != null) {
            iv.setImageResource(resId);
            iv.setVisibility(View.VISIBLE);
        }
        return iv;
    }


    protected TextView getRightTV(int resId) {// 显示右侧按钮（文本）并取得引用
        return getRightTV(getString(resId));
    }

    protected TextView getRightTV(String text) {// 显示右侧按钮（文本）并取得引用
        TextView tv = (TextView) findViewById(R.id.tv_right);
        if (!TextUtils.isEmpty(text) && tv != null) {
            tv.setVisibility(View.VISIBLE);
            tv.setText(text);
        }
        return tv;
    }

    protected int getSize(int resId) {
        return getResources().getDimensionPixelSize(resId);
    }

    protected Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            if (refresh_list != null && refresh_list.isRefreshing()) {
                refresh_list.onRefreshComplete();
            }
            if (gridView != null && gridView.isRefreshing()) {
                gridView.onRefreshComplete();
            }
            if (scrollview != null && scrollview.isRefreshing()) {
                scrollview.onRefreshComplete();
            }
            if (webView != null && webView.isRefreshing()) {
                webView.onRefreshComplete();
            }
            super.handleMessage(msg);
        }
    };

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        InputMethodManager imeManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imeManager.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        return super.onTouchEvent(event);
    }

    /**
     * 用于从xml文件中inflate控件View
     */
    public abstract void initViewAfterOnCreate();

    /**
     * 给View填充数据
     */
    public abstract void initDataAfterOnCreate();

}
