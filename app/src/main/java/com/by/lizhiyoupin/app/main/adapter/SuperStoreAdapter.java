package com.by.lizhiyoupin.app.main.adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.by.lizhiyoupin.app.R;
import com.by.lizhiyoupin.app.common.CommonConst;
import com.by.lizhiyoupin.app.common.utils.DeviceUtil;
import com.by.lizhiyoupin.app.common.utils.TimeUtils;
import com.by.lizhiyoupin.app.component_ui.web.CommonSchemeJump;
import com.by.lizhiyoupin.app.component_ui.weight.recyclerview.SpaceItemDecoration;
import com.by.lizhiyoupin.app.io.bean.ShopKindBean;
import com.by.lizhiyoupin.app.user.adapter.CommissionRecyclerViewAdapter;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

/**
 * data:2019/11/12
 * author:jyx
 * function:
 */
public class SuperStoreAdapter extends CommissionRecyclerViewAdapter {
    private Context context;


    public SuperStoreAdapter(Context context) {
        super(context);
        this.context = context;
    }

    @Override
    protected CommonViewHolder createNormalViewHolder(ViewGroup parent, int viewType) {
        View root = mInflater.inflate(R.layout.item_superstore_details, parent, false);
        return new ViewHolder(root);
    }

    class ViewHolder extends CommonViewHolder {
        private RecyclerView store_list;
        private ImageView shop_into, store_logo;
        private TextView shop_name, shop_content;
        private int corner;
        private LinearLayoutManager mLinearLayoutManager;
        private StoredetaisAdapter adapter;

        public ViewHolder(View itemView) {
            super(itemView);
            corner = DeviceUtil.dip2px(mContext, 4);
            store_list = itemView.findViewById(R.id.store_list);
            shop_into = itemView.findViewById(R.id.shop_into);//进入店铺
            store_logo = itemView.findViewById(R.id.store_logo);//店铺图标
            shop_name = itemView.findViewById(R.id.shop_name);//店铺名称
            shop_content = itemView.findViewById(R.id.shop_content);//店铺描述

            adapter = new StoredetaisAdapter(context);
            //适配器
            SpaceItemDecoration mDividerItemDecoration = new SpaceItemDecoration(DeviceUtil.dip2px(mContext, 10), 3);

            store_list.addItemDecoration(mDividerItemDecoration);
            mLinearLayoutManager = new GridLayoutManager(context, 3);
            store_list.setLayoutManager(mLinearLayoutManager);

            store_list.setAdapter(adapter);
            store_list.setNestedScrollingEnabled(false);
        }

        @Override
        public void bindData(Object itemData) {
            super.bindData(itemData);
            ShopKindBean bean = (ShopKindBean) itemData;
            Glide.with(mContext).load(bean.getShopImg())
                    .transform(new RoundedCornersTransformation(corner, 0, RoundedCornersTransformation.CornerType.ALL))
                    .error(R.drawable.empty_pic_list)
                    .placeholder(R.drawable.empty_pic_list)
                    .into(store_logo);
            shop_name.setText(bean.getShopName());
            shop_content.setText(bean.getShopActivities());
            adapter.setListData(bean.getLzCommodityInfoVOs());
            adapter.notifyDataSetChanged();

            //禁止recycleview滑动


            shop_into.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (TimeUtils.isFrequentOperation()) {
                        return;
                    }
                    Bundle bundle = new Bundle();
                    bundle.putString(CommonConst.SUPER_SHOPIMG, bean.getShopImg());
                    bundle.putString(CommonConst.SUPER_SHOPNAME, bean.getShopName());
                    bundle.putString(CommonConst.SUPER_SHOPCONTENT, bean.getShopActivities());
                    bundle.putString(CommonConst.SUPER_SHOPDESCRIBLE, bean.getDescribe());
                    bundle.putString(CommonConst.SUPER_SHOPURL, bean.getUrl());
                    bundle.putString(CommonConst.SUPER_SHOPID, bean.getId() + "");
                    bundle.putString(CommonConst.SUPER_SHOPSTATUS, bean.getFollowStatus() + "");
                    CommonSchemeJump.showActivity(context, "/app/SuperStoreActivity", bundle);//详细店铺信息
                }
            });
        }
    }


}
