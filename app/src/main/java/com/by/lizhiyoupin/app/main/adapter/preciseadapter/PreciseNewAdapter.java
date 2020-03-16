package com.by.lizhiyoupin.app.main.adapter.preciseadapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.by.lizhiyoupin.app.R;
import com.by.lizhiyoupin.app.common.log.LZLog;
import com.by.lizhiyoupin.app.component_ui.weight.recyclerview.CommonRecyclerViewAdapter;
import com.by.lizhiyoupin.app.main.holder.PreciseStickTabHolder;
import com.by.lizhiyoupin.app.weight.rv.ChildRv;

import java.util.List;

import androidx.viewpager.widget.ViewPager;

/**
 * (Hangzhou)
 *
 * @author: wzm
 * @date :  2019/12/26 17:08
 * Summary: 精选  adapter
 */
public class PreciseNewAdapter extends CommonRecyclerViewAdapter {
    private List<ChildRv> mChildRVimplList;
    private ViewPager mViewPager;

    public PreciseNewAdapter(Context context, List<ChildRv> childRVimplList) {
        super(context);
        mChildRVimplList = childRVimplList;
    }

    @Override
    protected CommonViewHolder createNormalViewHolder(ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(mContext).inflate(R.layout.precise_viewpager_layout, parent, false);
        return new ViewHolder(inflate);
    }


    class ViewHolder extends CommonViewHolder {
        PreciseStickTabHolder preciseStickTabHolder;
        PreciseNewViewPagerAdapter pagerAdapter;

        public ViewHolder(View itemView) {
            super(itemView);
            LZLog.i("PreciseNewAdapter", "create ViewHolder");
            mViewPager = itemView.findViewById(R.id.viewPager);
            preciseStickTabHolder = new PreciseStickTabHolder();
            pagerAdapter = new PreciseNewViewPagerAdapter(mContext, mChildRVimplList);
            mViewPager.setOffscreenPageLimit(4);
            mViewPager.setAdapter(pagerAdapter);
            preciseStickTabHolder.getOnCreateView(mContext, itemView.findViewById(R.id.rootLL), mViewPager);
            mViewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
                @Override
                public void onPageSelected(int position) {
                    super.onPageSelected(position);
                    preciseStickTabHolder.setSelectColor(position);
                }
            });
        }

    }

    public ChildRv getCurrentChildRecyclerView() {
        if (mViewPager == null) {
            return null;
        }
        return mChildRVimplList.get(mViewPager.getCurrentItem());
    }
}
