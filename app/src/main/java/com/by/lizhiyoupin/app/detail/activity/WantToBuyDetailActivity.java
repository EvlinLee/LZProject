package com.by.lizhiyoupin.app.detail.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.bumptech.glide.Glide;
import com.by.lizhiyoupin.app.R;
import com.by.lizhiyoupin.app.common.CommonConst;
import com.by.lizhiyoupin.app.common.log.LZLog;
import com.by.lizhiyoupin.app.common.utils.DeviceUtil;
import com.by.lizhiyoupin.app.component_ui.activity.BaseActivity;
import com.by.lizhiyoupin.app.component_ui.weight.recyclerview.DividerItemDecoration2;
import com.by.lizhiyoupin.app.detail.adapter.WantToBuyDetailAdapter;
import com.by.lizhiyoupin.app.io.BaseBean;
import com.by.lizhiyoupin.app.io.DefaultRx2Subscribe;
import com.by.lizhiyoupin.app.io.bean.GuideArticleDetailBean;
import com.by.lizhiyoupin.app.io.service.ApiService;
import com.by.lizhiyoupin.app.utils.ViewUtil;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * (Hangzhou)
 *
 * @author: wzm
 * @date :  2019/12/25 15:31
 * Summary: 种草详情
 */
@Route(path = "/app/WantToBuyDetailActivity")
public class WantToBuyDetailActivity extends BaseActivity implements View.OnClickListener {
    public static final String TAG = WantToBuyDetailActivity.class.getSimpleName();
    private RecyclerView mRecyclerView;
    private ImageView mAvatarIv;
    private TextView mNameTv;
    private TextView mTimeTv;
    private TextView mSeeNumberTv;
    private TextView mContentTv;
    private View loadingView;
    private ImageView loadingGifIv;
    private WantToBuyDetailAdapter mWantToBuyDetailAdapter;
    private int articleType;
    private long articleId;
    private Disposable mDisposable;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_want_to_buy_detail_layout);
        initImmersionBar(Color.WHITE,true);
        Intent intent = getIntent();
        articleType = intent.getIntExtra(CommonConst.WANT_TO_BUY_FROM_TYPE, 0);
        articleId = intent.getLongExtra(CommonConst.WANT_TO_BUY_ID, 0L);
        initView();
    }

    private void initView() {
        mRecyclerView = findViewById(R.id.recyclerView);
        loadingView = findViewById(R.id.loading_view);
        loadingGifIv = findViewById(R.id.loading_gif_iv);
        Glide.with(this).load(R.drawable.loading_dialog_g).into(loadingGifIv);
        mAvatarIv = findViewById(R.id.item_daily_avatar_iv);
        mNameTv = findViewById(R.id.item_daily_name_tv);
        mTimeTv = findViewById(R.id.item_daily_time_tv);
        mSeeNumberTv = findViewById(R.id.item_daily_see_number_tv);
        mContentTv = findViewById(R.id.daily_content_tv);
         findViewById(R.id.actionbar_back_tv).setOnClickListener(this);
        //底部推荐商品
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        DividerItemDecoration2 dividerItemDecoration2 = new DividerItemDecoration2(this, RecyclerView.VERTICAL);
        dividerItemDecoration2.setDividerHeight(DeviceUtil.dip2px(this,10));
        dividerItemDecoration2.setDividerColor(Color.WHITE);
        mRecyclerView.addItemDecoration(dividerItemDecoration2);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.setFocusable(false);
        mRecyclerView.setNestedScrollingEnabled(false);
        mWantToBuyDetailAdapter = new WantToBuyDetailAdapter(this);
        mRecyclerView.setAdapter(mWantToBuyDetailAdapter);
        initRequest();
    }

    private void setData(GuideArticleDetailBean detailBean) {
        Glide.with(this).load(detailBean.getIssueHeadImg()).error(R.drawable.default_face).into(mAvatarIv);
        mNameTv.setText(detailBean.getIssueName());
        ViewUtil.setTextViewFormat(this,mSeeNumberTv,R.string.precise_want_to_buy_detail_see_text,detailBean.getViewCount());
        Spanned spanned = Html.fromHtml(detailBean.getContext());
        mContentTv.setText(Html.fromHtml(spanned.toString()));
        mWantToBuyDetailAdapter.setListData(detailBean.getCommodityList());
        mWantToBuyDetailAdapter.notifyDataSetChanged();
    }

    private void initRequest() {
        ApiService.getHomeApi().requestGetGuideArticleDetail(articleType, articleId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new DefaultRx2Subscribe<BaseBean<GuideArticleDetailBean>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        super.onSubscribe(d);
                        mDisposable = d;
                    }

                    @Override
                    public void onNext(BaseBean<GuideArticleDetailBean> bean) {
                        super.onNext(bean);
                        LZLog.i(TAG, "requestGetGuideArticleDetail success");
                        if (bean.success() && bean.getResult() != null) {
                            GuideArticleDetailBean result = bean.getResult();
                            setData(result);
                        } else {
                            onError(new Throwable(bean.msg));
                        }
                        loadingView.setVisibility(View.GONE);
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                        LZLog.i(TAG, "requestGetGuideArticleDetail error==" + throwable);
                        loadingView.setVisibility(View.GONE);
                    }
                });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mDisposable != null && !mDisposable.isDisposed()) {
            mDisposable.dispose();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.actionbar_back_tv:
                finish();
                break;
        }
    }
}
