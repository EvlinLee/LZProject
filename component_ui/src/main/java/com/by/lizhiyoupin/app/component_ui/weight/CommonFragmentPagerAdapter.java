package com.by.lizhiyoupin.app.component_ui.weight;

import android.view.ViewGroup;


import com.by.lizhiyoupin.app.common.log.LZLog;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class CommonFragmentPagerAdapter extends FragmentPagerAdapter {
    private static final String TAG = CommonFragmentPagerAdapter.class.getSimpleName();

    private final int mCount;
    public final Fragment[] mChildFragments;
    private final FragmentFactoryCallback mCallback;
    private int mCurrentIndex = 0;

    private boolean isIgnoreParentItemId = false;
    private int[] itemIds;//解决同一个fragmentmanager需要创建多个同一个类的fragment

    public CommonFragmentPagerAdapter(final FragmentManager fm, final int count, final FragmentFactoryCallback callback) {
        super(fm);
        mCount = count;
        mChildFragments = new Fragment[count];
        mCallback = callback;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = mChildFragments[position];
        if (fragment == null) {
            fragment = mCallback.createFragment(position);
            mChildFragments[position] = fragment;
        }
        return fragment;
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        return super.getItemPosition(object);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        Fragment fragment = (Fragment) super.instantiateItem(container, position);
        mChildFragments[position] = fragment;
        return fragment;
    }

    @Override
    public long getItemId(int position) {
        if (isIgnoreParentItemId) {
            return itemIds[position];
        }
        return super.getItemId(position);
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return super.getPageTitle(position);
    }

    public void setItemIds(int[] itemIds) {
        this.itemIds = itemIds;
    }

    public void setIgnoreParentItemId(boolean ignoreParentItemId) {
        isIgnoreParentItemId = ignoreParentItemId;
    }

    @Override
    public int getCount() {
        return mChildFragments.length;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
       LZLog.d(TAG, "hook parent destroyItem.");
        // 此处不需要调用父类的方法，避免被销毁
//        super.destroyItem(container, position, object);
    }

    public void setCurrentIndex(int index) {
        this.mCurrentIndex = index;
    }

    public void setChildUserVisibleHint(boolean isVisibleToUser) {
        if (mCurrentIndex > -1 && mCurrentIndex < mCount) {
            final Fragment currentFragment = mChildFragments[mCurrentIndex];
            if (currentFragment != null) {
                currentFragment.setUserVisibleHint(isVisibleToUser);
            }
        }
    }

    public interface FragmentFactoryCallback {
        Fragment createFragment(int index);
    }
}
