package com.by.lizhiyoupin.app.main.adapter;

import com.by.lizhiyoupin.app.component_ui.weight.CommonFragmentPagerAdapter;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;

/**
 * (Hangzhou)
 *
 * @author: wzm
 * @date :  2019/9/26 17:34
 * Summary: 首页内二级tab adapter
 */
public class HomeFragmentPagerAdapter extends CommonFragmentPagerAdapter {
    private List<String> titles;

    public HomeFragmentPagerAdapter(@NonNull FragmentManager fm, int count, List<String> title, FragmentFactoryCallback factoryCallback) {
        super(fm, count, factoryCallback);
        this.titles = title;
    }


    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        if (titles == null || titles.size() == 0) {
            return null;
        } else {
            return titles.get(position);
        }
    }


}
