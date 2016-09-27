package com.juzifenqi.app.main.base;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
 * Desc:   Fragment基类
 * Time:   2016-09-23 15:08
 * Author: chende
 */
public abstract class BaseFragment extends Fragment {

    protected Intent intent;
    protected Context mContext;
    protected View contentView;
    protected EventBus eventBus;
    protected PullToRefreshListView refresh_list;
    protected PullToRefreshGridView gridView;
    protected PullToRefreshScrollView scrollview;
    protected PullToRefreshWebView webView;
    protected BaseFragment lastFragment;
    protected int current_page = 1;// 当前面码
    protected int current_page_up = 1;// 当前面码（上拉加载使用的页面）
    protected int records_of_page = 10;// 每一页的记录的条数
    protected int screen_width;// 屏幕宽度
    protected int screen_height;// 屏幕宽度
    protected boolean has_more = true;// 标记获取列表接口是否有更多数据
    protected boolean pull_down = true;// 标记列表是上拉还是下拉
    protected Global global;
    private int layoutResId;
    private Toast toast;
    protected HashMap<String, Object> params = new HashMap<String, Object>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mContext = getActivity();
        global = Global.getInstance(mContext);
        screen_width = PreferManager.getInt(PreferManager.SCREEN_WIDTH, 1080);
        screen_height = PreferManager.getInt(PreferManager.SCREEN_HEIGHT, 1920);
        toast = global.getToast();
        eventBus = EventBus.getDefault();
        if (contentView == null) {
            contentView = inflater.inflate(layoutResId, container, false);
        }
        ViewGroup parent = (ViewGroup) contentView.getParent();
        if (parent != null) {
            parent.removeView(contentView);
        }
        initViewAfterOnCreate();
        initDataAfterOnCreate();
        return contentView;
    }

    @SuppressWarnings({"unchecked"})
    protected <T> T findById(int id) {
        T view = (T) contentView.findViewById(id);
        return view;
    }

    public void setContentLayout(int layoutResId) {
        this.layoutResId = layoutResId;
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

    public boolean isRefreshing() {// 判断当前的可刷新的View是否处于刷新中
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

    protected Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            if (refresh_list != null) {
                refresh_list.onRefreshComplete();
            }
            if (gridView != null) {
                gridView.onRefreshComplete();
            }
            if (scrollview != null) {
                scrollview.onRefreshComplete();
            }
            if (webView != null) {
                webView.onRefreshComplete();
            }
            super.handleMessage(msg);
        }
    };

    /**
     * 用于从xml文件中inflate控件View
     */
    public abstract void initViewAfterOnCreate();

    /**
     * 给View填充数据
     */
    public abstract void initDataAfterOnCreate();

}
