package com.juzifenqi.app.main.base;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;


/**
 * 类描述 : 通过Fragment实例数组构造PagerAdapter
 * <p/>
 * 创建时间 : 2015年6月18日 上午10:20:43
 * <p/>
 * 作者 :陈得
 */
public class FragPagerAdapter extends FragmentPagerAdapter {

    private BaseFragment[] fragments = null;

    public FragPagerAdapter(FragmentManager fm, BaseFragment[] fragments) {
        super(fm);
        this.fragments = fragments;
    }

    @Override
    public Fragment getItem(int position) {
        return fragments[position];
    }

    @Override
    public int getCount() {
        return fragments.length;
    }
}
