package com.juzifenqi.app.main.base;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.BaseAdapter;
import android.widget.Toast;

import com.juzifenqi.app.common.Global;
import com.juzifenqi.app.readwrite.PreferManager;

import java.util.ArrayList;
import java.util.List;


/**
 * Desc:   公共BaseAdapter基类， 子类只需要实现getView()和构造方法
 * Time:   2016-09-23 15:08
 * Author: chende
 */
public abstract class TBaseAdapter<E> extends BaseAdapter {

    protected Context mContext;
    protected Global global;
    protected LayoutInflater inflater;
    protected List<E> list = new ArrayList<E>();
    protected int screen_width;
    private Toast toast;

    public TBaseAdapter(Context mContext, List<E> list) {
        this.mContext = mContext;
        global = Global.getInstance(mContext);
        toast = global.getToast();
        if (list != null && list.size() > 0) {
            this.list = list;
        }
        this.screen_width = PreferManager.getInt(PreferManager.SCREEN_WIDTH, 1080);
        this.inflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        return list == null ? 0 : list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void refreshData(List<E> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    protected void toshow(String text) {
        toast.setText(text);
        toast.show();
    }

    protected void toshow(int resId) {
        toast.setText(mContext.getString(resId));

        toast.show();
    }

    public void addData(List<E> list) {
        this.list.addAll(list);
        notifyDataSetChanged();
    }

}
