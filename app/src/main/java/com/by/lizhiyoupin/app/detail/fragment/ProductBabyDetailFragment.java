package com.by.lizhiyoupin.app.detail.fragment;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.by.lizhiyoupin.app.R;
import com.by.lizhiyoupin.app.common.CommonConst;
import com.by.lizhiyoupin.app.common.utils.TimeUtils;
import com.by.lizhiyoupin.app.component_ui.fragment.BaseFragment;
import com.by.lizhiyoupin.app.component_ui.web.CommonWebJump;
import com.by.lizhiyoupin.app.detail.activity.PreciseDetailActivity;
import com.by.lizhiyoupin.app.io.bean.HandPickDetailBean;
import com.by.lizhiyoupin.app.main.adapter.VipImageAdapter;
import com.by.lizhiyoupin.app.utils.ViewUtil;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * (Hangzhou)
 *
 * @author: wzm
 * @date :  2019/10/9 15:13
 * Summary: 宝贝详情  {@see #PreciseDetailActivity}
 */
public class ProductBabyDetailFragment extends BaseFragment implements View.OnClickListener {
    public static final String TAG = ProductBabyDetailFragment.class.getSimpleName();
    //店铺名
    private TextView mStoreNameTv;
    //进入商店
    private TextView mStoreIntoTv;
    //店铺商品评级
    private RecyclerView mStoreServicesRv;
    //展开收起
    private TextView mBabyDetailsExpandTv;
    //宝贝详情 图
    private RecyclerView mBabyDetailsImgRv;
    private  View mRootView;

    private Context mContext;
    private boolean babyExpand = false;//是否展开
    private VipImageAdapter mVipImageAdapter;
    private HandPickDetailBean mDetailBean;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_product_baby_detail_layout, container, false);
        initView(root);
        return root;
    }

    private void initView(View root) {
        mRootView= root.findViewById(R.id.root_cl);
        mStoreNameTv = root.findViewById(R.id.detail_current_store_name_tv);
        mStoreIntoTv = root.findViewById(R.id.detail_current_store_into_tv);
        mStoreServicesRv = root.findViewById(R.id.detail_current_store_services_Rv);
        mBabyDetailsExpandTv = root.findViewById(R.id.detail_baby_details_expand_tv);
        mBabyDetailsImgRv = root.findViewById(R.id.detail_baby_details_img_rv);
        mStoreIntoTv.setOnClickListener(this);
        mBabyDetailsExpandTv.setOnClickListener(this);
        mStoreIntoTv.setVisibility(View.INVISIBLE);


        mBabyDetailsImgRv.setLayoutManager(new LinearLayoutManager(mContext, RecyclerView.VERTICAL, false));
        mVipImageAdapter = new VipImageAdapter(mContext);
        mBabyDetailsImgRv.setNestedScrollingEnabled(false);
        mBabyDetailsImgRv.setAdapter(mVipImageAdapter);
        mBabyDetailsImgRv.setVisibility(View.VISIBLE);
        babyExpand = false;
        mBabyDetailsExpandTv.setText(R.string.product_expand_baby_details_text);

        mStoreNameTv.setText("店面名称");
        ViewUtil.setDrawableOfTextView(mStoreNameTv, R.drawable.temp_detail_tao_big_pic, ViewUtil.DrawableDirection.LEFT);

    }


    @Override
    public void onClick(View v) {
        if (TimeUtils.isFrequentOperation()) {
            return;
        }
        switch (v.getId()) {
            case R.id.detail_current_store_into_tv:
                //进入店铺
                if (mDetailBean != null) {
                    CommonWebJump.showInterceptOtherWebActivity(mContext, mDetailBean.getShopUrl());
                }
                break;
            case R.id.detail_baby_details_expand_tv:
                //展开 收起 宝贝详情
                changeProductPic();
                break;
        }
    }

    /**
     * 改变商品详情图 显示隐藏
     */
    private void changeProductPic() {
        ViewUtil.setDrawableOfTextView(mBabyDetailsExpandTv,
                babyExpand ? R.drawable.product_baby_details_down : R.drawable.product_baby_details_up
                , ViewUtil.DrawableDirection.RIGHT);
        mBabyDetailsExpandTv.setText(babyExpand ? R.string.product_expand_baby_details_text : R.string.product_close_baby_details_text);
        mBabyDetailsImgRv.setVisibility(babyExpand ? View.GONE : View.VISIBLE);
        babyExpand = !babyExpand;
    }

    public void updateShopStore(HandPickDetailBean detailBean) {
        mDetailBean = detailBean;
        mStoreNameTv.setText(detailBean.getShopTitle());
        int iconType = detailBean.getIcon();
        int drawRes;
        switch (iconType) {
            case CommonConst.PLATFORM_TAO_BAO:
                if (!TextUtils.isEmpty(detailBean.getShopUrl())) {
                    mStoreIntoTv.setVisibility(View.VISIBLE);
                }
                drawRes = R.drawable.temp_detail_tao_big_pic;
                break;
            case CommonConst.PLATFORM_JING_DONG:
                drawRes = R.drawable.temp_detail_jingd_pic;
                break;
            case CommonConst.PLATFORM_PIN_DUO_DUO:
                drawRes = R.drawable.temp_detail_pingdd_big_pic;
                break;
            case CommonConst.PLATFORM_TIAN_MAO:
                if (!TextUtils.isEmpty(detailBean.getShopUrl())) {
                    mStoreIntoTv.setVisibility(View.VISIBLE);
                }
                drawRes = R.drawable.temp_detail_tian_mao_pic;
                break;
            case CommonConst.PLATFORM_KAO_LA:
                //考拉不显示店铺 商品介绍图
                mRootView.setVisibility(View.GONE);
                drawRes = R.drawable.temp_detail_kaola_pic;
                break;
            default:
                drawRes = R.drawable.temp_detail_tao_big_pic;
                break;
        }
        ViewUtil.setDrawableOfTextView(mStoreNameTv, drawRes, ViewUtil.DrawableDirection.LEFT);
    }

    public void updateShopDecPic(List<String> picList) {
        mVipImageAdapter.setListData(picList);
        mVipImageAdapter.notifyDataSetChanged();
        mBabyDetailsImgRv.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (getActivity() instanceof PreciseDetailActivity) {
                    //首次会加载部分，所以先显示在隐藏
                    mBabyDetailsImgRv.setVisibility(View.GONE);
                }
            }
        }, 300);
    }
}
