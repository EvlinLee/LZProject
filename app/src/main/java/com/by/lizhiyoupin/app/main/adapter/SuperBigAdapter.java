package com.by.lizhiyoupin.app.main.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.by.lizhiyoupin.app.R;
import com.by.lizhiyoupin.app.common.utils.DeviceUtil;
import com.by.lizhiyoupin.app.component_ui.web.CommonWebJump;
import com.by.lizhiyoupin.app.component_ui.weight.RoundImageView;
import com.by.lizhiyoupin.app.io.bean.ShopBannerBean;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * data:2019/11/10
 * author:jyx
 * function:
 */
public class SuperBigAdapter extends RecyclerView.Adapter<SuperBigAdapter.ViewHolder> {
    private Context context;
    private List<ShopBannerBean> list;
    public SuperBigAdapter(Context context, List<ShopBannerBean> list) {
        this.context = context;
        this.list=list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View root =
                LayoutInflater.from(parent.getContext()).inflate(R.layout.item_superbig_details,
                        parent, false);
        SuperBigAdapter.ViewHolder viewHolder = new SuperBigAdapter.ViewHolder(root);
        int parentWidth = parent.getWidth();
        ViewGroup.LayoutParams layoutParams = viewHolder.itemView.getLayoutParams();
        layoutParams.width = (parentWidth / 4);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Glide.with(context).load(list.get(position).getShopImg()).apply(RequestOptions.bitmapTransform(new CircleCrop())) .error(R.drawable.empty_pic_list)
                .placeholder(R.drawable.empty_pic_list)
                .into(holder.super_img);
        holder.super_name.setText(list.get(position).getShopName());
        holder.super_content.setText(list.get(position).getShopActivities());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CommonWebJump.showInterceptOtherWebActivity(context, list.get(position).getUrl());
            }
        });
        holder.super_img.setBorderColor(Color.parseColor("#E5e5e5"));
        holder.super_img.setBorderWidth(DeviceUtil.dip2px(context, 1));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView super_name, super_content;
        private RoundImageView super_img;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            super_content = itemView.findViewById(R.id.super_content);
            super_img = itemView.findViewById(R.id.super_img);
            super_name = itemView.findViewById(R.id.super_name);


        }
    }
}
