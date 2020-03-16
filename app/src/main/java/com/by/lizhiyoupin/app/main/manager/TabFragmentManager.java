package com.by.lizhiyoupin.app.main.manager;

import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.RadioButton;

import com.by.lizhiyoupin.app.LiZhiApplication;
import com.by.lizhiyoupin.app.R;
import com.by.lizhiyoupin.app.common.CycQueue;
import com.by.lizhiyoupin.app.common.log.LZLog;
import com.by.lizhiyoupin.app.common.utils.DeviceUtil;
import com.by.lizhiyoupin.app.component_ui.web.CommonSchemeJump;
import com.by.lizhiyoupin.app.main.MainActivity;
import com.by.lizhiyoupin.app.main.fragment.TabFragmentEntrance;
import com.by.lizhiyoupin.app.main.fragment.TabFragmentFind;
import com.by.lizhiyoupin.app.main.fragment.TabFragmentHome;
import com.by.lizhiyoupin.app.main.fragment.TabFragmentUser;
import com.by.lizhiyoupin.app.main.fragment.TabFragmentVip;
import com.by.lizhiyoupin.app.manager.AccountManager;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

/**
 * (Hangzhou)
 *
 * @author: wzm
 * @date :  2019/9/26 14:22
 * Summary:
 */
public class TabFragmentManager implements CompoundButton.OnCheckedChangeListener {
    public static final String TAG = TabFragmentManager.class.getSimpleName();

    public static final String MAIN_HOME_PAGE_TAB_NAME = "main_home_page_tab";//首页
    public static final String MAIN_ENTRANCE_TAB_NAME = "main_navigation_tab";//超级购
    public static final String MAIN_VIP_TAB_NAME = "main_vip_tab";//超级会员
    public static final String MAIN_FIND_TAB_NAME = "main_circle_tab";//发圈
    public static final String MAIN_USER_TAB_NAME = "main_user_tab";//我的
    private static final int MAX_TRACE = 2;
    public static final int TAB_HOME = 0;
    public static final int TAB_ENTRANCE = 1;
    public static final int TAB_VIP = 2;
    public static final int TAB_FIND = 3;
    public static final int TAB_USER= 4;
    private MainActivity mMainActivity;
    private RadioButton[] mTabButtons;
    public final Fragment[] mTabFragments;

    // fragment的显示区域id
    private int mTabFragmentContentId;
    private String[] routerNames;
    private View content;
    // fragment stack
    private CycQueue<Integer> fragmentStack = new CycQueue(MAX_TRACE);
    // 当前tab页面索引
    private int mCurrentTab = -1;

    private boolean onBackPressed;

    public TabFragmentManager(MainActivity mainActivity, int fragmentContentId) {
        LZLog.d(TAG, "new TabFragmentManager fragmentActivity=" + mainActivity + ", fragmentContentId=" + fragmentContentId);

        this.mMainActivity = mainActivity;
        this.mTabFragmentContentId = fragmentContentId;

        final int TAB_SIZE = 5;

        mTabButtons = new RadioButton[TAB_SIZE];
        mTabButtons[TAB_HOME] = mMainActivity.findViewById(R.id.tab_home_tv);
        mTabButtons[TAB_ENTRANCE] = mMainActivity.findViewById(R.id.tab_entrance_tv);
        mTabButtons[TAB_VIP] = mMainActivity.findViewById(R.id.tab_vip_tv);
        mTabButtons[TAB_FIND] = mMainActivity.findViewById(R.id.tab_find_tv);
        mTabButtons[TAB_USER] = mMainActivity.findViewById(R.id.tab_user_tv);
        content = mMainActivity.findViewById(mTabFragmentContentId);

        routerNames = new String[TAB_SIZE];
        routerNames[TAB_HOME] = MAIN_HOME_PAGE_TAB_NAME;//首页
        routerNames[TAB_ENTRANCE] = MAIN_ENTRANCE_TAB_NAME;//超级购
        routerNames[TAB_VIP] = MAIN_VIP_TAB_NAME;//超级会员
        routerNames[TAB_FIND] = MAIN_FIND_TAB_NAME;//发圈
        routerNames[TAB_USER] = MAIN_USER_TAB_NAME;//我的

        RadioButton radioButton = null;
        for (int i = 0; i < TAB_SIZE; i++) {
            radioButton = mTabButtons[i];
            radioButton.setChecked(false);
            radioButton.setOnCheckedChangeListener(TabFragmentManager.this);
            // tag里保存index
            radioButton.setTag(i);
        }

        mTabFragments = new Fragment[TAB_SIZE];
        //首页
        mTabFragments[0] = new TabFragmentHome();
        //超级购
        mTabFragments[1] = new TabFragmentEntrance();
        //超级会员
        mTabFragments[TAB_VIP] = new TabFragmentVip();
        //发圈
        mTabFragments[3] = new TabFragmentFind();
        //我的
        mTabFragments[4] = new TabFragmentUser();

        // 启动默认显示第一页
        switchTab(0);
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        //tab切换
        final Integer checkedIndex = (Integer) buttonView.getTag();
        if (isChecked) {
            if (checkedIndex == TAB_VIP) {
                AccountManager accountManager = LiZhiApplication.getApplication().getAccountManager();
                if (!accountManager.isLogined()) {
                    CommonSchemeJump.showLoginActivity(mMainActivity);
                    mTabButtons[checkedIndex].setChecked(false);
                    return;
                }
                //中间Vip选中变大
                //定义底部标签图片大小和位置
                Drawable drawable_live = mMainActivity.getResources().getDrawable(R.drawable.main_tab_vip_selector);
                //当这个图片被绘制时，给他绑定一个矩形 ltrb规定这个矩形
                int boundBom = DeviceUtil.dip2px(mMainActivity, 45);
                int bound = DeviceUtil.dip2px(mMainActivity, 57);
                drawable_live.setBounds(0, 0, bound, boundBom);
                //设置图片在文字的哪个方向
                buttonView.setCompoundDrawables(null, drawable_live, null, null);
            }
            final int lastTab = mCurrentTab;
            if (lastTab >= 0) {
                mTabButtons[lastTab].setChecked(false);
            }

            showFragment(checkedIndex, lastTab);
            mCurrentTab = checkedIndex;
            if (!onBackPressed) {
                fragmentStack.add(mCurrentTab);
            }
            onBackPressed = false;

        } else {
            if (checkedIndex == TAB_VIP) {
                //定义底部标签图片大小和位置
                Drawable drawable_live = mMainActivity.getResources().getDrawable(R.drawable.main_tab_vip_selector);
                //设置图片在文字的哪个方向
                buttonView.setCompoundDrawablesWithIntrinsicBounds(null, drawable_live, null, null);
            }
        }

    }


    /**
     * @param tabIndex
     */
    public void switchTab(final int tabIndex) {
        if (tabIndex < mTabButtons.length) {
            mTabButtons[tabIndex].setChecked(true);
        }
    }

    /**
     * @param tabIndex    mainActivity tab位置
     * @param subPageIndex 当前tab内fragment内部的一级tab位置
     * @param childIndex  当前tab内fragment内部的二级tab位置
     */
    public void switchTab(final String tabIndex,  final int subPageIndex,final int childIndex) {
        int position = getIndexByKey(tabIndex);
        mTabButtons[position].setChecked(true);
        if (mTabFragments[position] instanceof SetSubPageIndex) {
            ((SetSubPageIndex) mTabFragments[position]).setSubPageIndex(subPageIndex,childIndex);
        }
    }

    private int getIndexByKey(String key) {
        for (int i = 0; i < routerNames.length; i++) {
            if (routerNames[i].equals(key)) {
                return i;
            }
        }
        return mCurrentTab;
    }

    public int getCurrentTab() {
        return mCurrentTab;
    }

    /**
     * tab切换显示隐藏 fragment
     *
     * @param currentIndex
     * @param lastIndex
     */
    private void showFragment(final int currentIndex, final int lastIndex) {
        if (mMainActivity.isDestroy()) {
            return;
        }
        final FragmentTransaction ft = mMainActivity.getSupportFragmentManager().beginTransaction();
        if (lastIndex >= 0) {
            final Fragment lastFragment = mTabFragments[lastIndex];
            if (lastFragment != null) {
                ft.hide(lastFragment);
            }
        }
        final Fragment currentFragment = mTabFragments[currentIndex];

        /*
         * 防止快速切换的时候，isAdded方法判断不准确，可能已经add后isAdded返回还是false，
         * 所以这里在add之前把没有提交的事务都在主线程同步先提交掉
         */
        mMainActivity.getSupportFragmentManager().executePendingTransactions();
        if (null == currentFragment) {
            return;
        }
        if (currentFragment.isAdded()) {
            ft.show(currentFragment);
        } else {
            ft.add(mTabFragmentContentId, currentFragment);
            currentFragment.setUserVisibleHint(true);
        }
        ft.commitAllowingStateLoss();
    }


    /**
     * 主页面tab 处理返回逻辑
     *
     * @return 是否还可以回退
     */
    public boolean onBackPressed() {
        Integer topTab = fragmentStack.poll();
        Log.d(TAG, "======================>>>>>>> onBackPressed " + topTab);
        if (topTab == null) {
            if (mCurrentTab != 0) {
                switchTab(0);
                return true;
            }
            return false;
        } else {
            if (topTab == 0) {
                fragmentStack.clearQueue();
                onBackPressed = false;
                return false;
            } else {
                onBackPressed = true;
                Integer newTag = fragmentStack.peek();
                switchTab(newTag == null ? 0 : newTag);
                return true;
            }
        }
    }


    /**
     * 支持二级tab跳转的实现aging接口
     */
    public interface SetSubPageIndex {
        void setSubPageIndex(int subPageIndex,int childIndex);
    }
}
