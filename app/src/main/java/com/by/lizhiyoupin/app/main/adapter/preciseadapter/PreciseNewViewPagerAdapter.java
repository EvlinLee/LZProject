package com.by.lizhiyoupin.app.main.adapter.preciseadapter;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.by.lizhiyoupin.app.weight.rv.ChildRv;
import com.by.lizhiyoupin.app.weight.rv.RvVpInterface;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.PagerAdapter;

/**
 * (Hangzhou)
 *
 * @author: wzm
 * @date :  2019/12/26 19:29
 * Summary: 精选 viewpager adapter
 */
public class PreciseNewViewPagerAdapter extends PagerAdapter {
    public static final String TAG=PreciseNewViewPagerAdapter.class.getSimpleName();
    private Context mContext;
    private List<ChildRv> mChildRVimplList;

    public PreciseNewViewPagerAdapter(Context context,List<ChildRv> childRVimplList) {
        mContext = context;
        mChildRVimplList=childRVimplList;
    }

    @Override
    public int getCount() {
        return mChildRVimplList.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view==object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View view = mChildRVimplList.get(position);
        if (container == view.getParent()) {
            container.removeView(view);
            Log.i(TAG,"removeView");
        }
        if (view instanceof RvVpInterface){
            Log.i(TAG,"instantiateItem");
        }
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        //super.destroyItem(container, position, object);
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return "title"+position;
    }
}
