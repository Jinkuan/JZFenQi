package com.juzifenqi.app.main.base;

import android.app.ActionBar;
import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

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
 * Desc:   基础Dialog类
 * Time:   2016-09-23 15:08
 * Author: chende
 */
public abstract class BaseDialog extends Dialog implements View.OnClickListener {

    protected Context mContext;
    protected Global global;
    protected HashMap<String, Object> params = new HashMap<String, Object>();
    protected EventBus eventBus;
    protected int screen_width;
    protected int screen_height;
    protected Toast toast;

    protected BaseDialog(Context mContext, int layoutResId) {
        super(mContext, R.style.dialog_style);
        this.mContext = mContext;
        setContentView(layoutResId);
        initDlgData();
        initViewAfterOnCreate();
    }

    private void initDlgData() {//初始化Dialog数据
        global = Global.getInstance(mContext);
        eventBus = EventBus.getDefault();
        screen_width = PreferManager.getInt(PreferManager.SCREEN_WIDTH, 1080);
        screen_height = PreferManager.getInt(PreferManager.SCREEN_HEIGHT, 1920);
        getWindow().setLayout(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.WRAP_CONTENT);
        toast = global.getToast();
    }

    protected void requestData(String action_url, String toast, TRequestCallBack requestCallBack) {// 请接口获取数据
        if (!Utils.netWorkAvaliable(mContext)) {
            toShow(R.string.net_work_disable);
            return;
        }
        AsyncRequest.post().url(Const.BASE_URL + action_url).params(Utils.addParam(params, mContext)).build().execute(requestCallBack);
    }

    @SuppressWarnings({"unchecked"})
    protected <T> T findById(int id) {
        T view = (T) findViewById(id);
        return view;
    }

    protected void toShow(String str) {
        if (!TextUtils.isEmpty(str)) {
            toast.setText(str);
            toast.show();
        }
    }

    protected void toShow(int strId) {
        toShow(mContext.getString(strId));
    }

    @Override
    public void onClick(View v) {

    }

    /**
     * 用于从xml文件中inflate控件View
     */
    public abstract void initViewAfterOnCreate();
}