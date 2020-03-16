package com.by.lizhiyoupin.app.main.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.by.lizhiyoupin.app.R;
import com.by.lizhiyoupin.app.common.log.LZLog;
import com.by.lizhiyoupin.app.common.utils.ArraysUtils;
import com.by.lizhiyoupin.app.common.utils.StringUtils;
import com.by.lizhiyoupin.app.common.utils.TimeUtils;
import com.by.lizhiyoupin.app.component_ui.utils.GlideImageLoader;
import com.by.lizhiyoupin.app.component_ui.web.CommonSchemeJump;
import com.by.lizhiyoupin.app.component_ui.weight.MoneyTextView;
import com.by.lizhiyoupin.app.io.bean.ShopKindBean;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * data:2019/11/12
 * author:jyx
 * function:
 */
public class StoredetaisAdapter extends RecyclerView.Adapter<StoredetaisAdapter.ViewHolder> {
    private List<ShopKindBean.LzCommodityInfoVOsBean> list;
    private Context context;

    public StoredetaisAdapter(Context context) {
        this.context = context;
        this.list = new ArrayList<>();

    }

    public void setListData(List<ShopKindBean.LzCommodityInfoVOsBean> listData) {
        list.clear();
        if (!ArraysUtils.isListEmpty(listData)) {
            list.addAll(listData);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View root = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_superstore_sdetails, parent, false);

        return new ViewHolder(root);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        LZLog.i("ssss2", "po==" + position);
        holder.store_commoeny.setText("最高返" + StringUtils.getFormattedDoubleOrInt(list.get(position).getCommissionMoney()) + "元");
        holder.store_money.setText("¥" + StringUtils.getFormattedDoubleOrInt(list.get(position).getDiscountsPriceAfter()));
        new GlideImageLoader(context, list.get(position).getPictUrl())
                .error(R.drawable.empty_pic_list)
                .placeholder(R.drawable.empty_pic_list)
                .into(holder.store_img);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TimeUtils.isFrequentOperation()) {
                    return;
                }
                CommonSchemeJump.showPreciseDetailActivity(context, list.get(position).getItemId(), list.get(position).getIcon());
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView store_img;
        private TextView store_commoeny;
        private MoneyTextView store_money;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            store_img = itemView.findViewById(R.id.store_img);//商品首图
            store_commoeny = itemView.findViewById(R.id.store_commoeny);//返红包
            store_money = itemView.findViewById(R.id.store_money);//商品价格
        }
    }


}
