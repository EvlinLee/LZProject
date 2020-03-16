package com.by.lizhiyoupin.app.main.adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.by.lizhiyoupin.app.R;
import com.by.lizhiyoupin.app.common.CommonConst;
import com.by.lizhiyoupin.app.common.utils.DeviceUtil;
import com.by.lizhiyoupin.app.common.utils.StringUtils;
import com.by.lizhiyoupin.app.component_ui.utils.GlideImageLoader;
import com.by.lizhiyoupin.app.component_ui.web.CommonSchemeJump;
import com.by.lizhiyoupin.app.component_ui.weight.MoneyTextView;
import com.by.lizhiyoupin.app.io.bean.LimitSkillTimeBean;
import com.by.lizhiyoupin.app.io.bean.LimitedTimeSkillBean;
import com.by.lizhiyoupin.app.utils.ViewUtil;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

/**
 * data:2019/11/12
 * author:jyx
 * function: 限时秒杀 viewpager
 */
public class LimitskillAdapter extends RecyclerView.Adapter<LimitskillAdapter.ViewHolder> {
     private  List<LimitedTimeSkillBean> list;
    private Context mContext;

    public LimitskillAdapter(Context context, List<LimitedTimeSkillBean> lists) {
        this.mContext = context;
       this.list = lists;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View root = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_limitskill, parent, false);

        return new ViewHolder(root);

    }

    @Override
    public void onBindViewHolder(@NonNull LimitskillAdapter.ViewHolder holder, int position) {
        if (list.size()==0){
            holder.mChild01.setVisibility(View.INVISIBLE);
            holder.mChild02.setVisibility(View.INVISIBLE);
            holder.mChild03.setVisibility(View.INVISIBLE);
            return;
        }
       position=position%list.size();
        int finalPosition = position;
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle=new Bundle();
                bundle.putInt(CommonConst.KEY_LIMIT_TIME_TYPE,list.get(finalPosition).getBatch());
                CommonSchemeJump.showActivity(mContext, "/app/LimitedTimeSpikeActivity",bundle);
            }
        });

        List<LimitSkillTimeBean> commodityList = list.get(position).getCommodityList();
        int size = commodityList.size();
        if (size<1){
            holder.mChild01.setVisibility(View.INVISIBLE);
            holder.mChild02.setVisibility(View.INVISIBLE);
            holder.mChild03.setVisibility(View.INVISIBLE);
            return;
        }
        holder.mChild01.setVisibility(View.VISIBLE);
        LimitSkillTimeBean limitSkillTimeBean = commodityList.get(0);
        new GlideImageLoader(mContext, limitSkillTimeBean.getCommodityImg())
                .placeholder(R.drawable.empty_pic_list)
                .transform(new RoundedCornersTransformation(holder.corner,0,RoundedCornersTransformation.CornerType.ALL))
                .error(R.drawable.empty_pic_list)
                .into(holder.store_img_o);//主图

       holder.store_title_one.setText(limitSkillTimeBean.getCommodityName());//标题
        ViewUtil.setTextViewFormat(mContext, holder.store_money_one, R.string.pay_order_price_text,
                StringUtils.getFormattedDoubleOrInt(limitSkillTimeBean.getRealPrice()));
        if (size<2){
            holder.mChild01.setVisibility(View.VISIBLE);
            holder.mChild02.setVisibility(View.INVISIBLE);
            holder.mChild03.setVisibility(View.INVISIBLE);
            return;
        }
        holder.mChild02.setVisibility(View.VISIBLE);
        LimitSkillTimeBean limitSkillTimeBean1 = commodityList.get(1);
        new GlideImageLoader(mContext, limitSkillTimeBean1.getCommodityImg())
                .placeholder(R.drawable.empty_pic_list)
                .transform(new RoundedCornersTransformation(holder.corner,0,RoundedCornersTransformation.CornerType.ALL))
                .error(R.drawable.empty_pic_list)
                .into(holder.img_t);//主图

        holder.store_title_two.setText(limitSkillTimeBean1.getCommodityName());//标题
        ViewUtil.setTextViewFormat(mContext, holder.store_money_two, R.string.pay_order_price_text,
                StringUtils.getFormattedDoubleOrInt(limitSkillTimeBean1.getRealPrice()));
        if (size<3){
            holder.mChild01.setVisibility(View.VISIBLE);
            holder.mChild02.setVisibility(View.VISIBLE);
            holder.mChild03.setVisibility(View.INVISIBLE);
            return;
        }
        holder.mChild03.setVisibility(View.VISIBLE);
        LimitSkillTimeBean limitSkillTimeBean2 = commodityList.get(2);
        new GlideImageLoader(mContext, limitSkillTimeBean2.getCommodityImg())
                .placeholder(R.drawable.empty_pic_list)
                .transform(new RoundedCornersTransformation(holder.corner,0,RoundedCornersTransformation.CornerType.ALL))
                .error(R.drawable.empty_pic_list)
                .into(holder.store_img_t);//主图

        holder.store_title_three.setText(limitSkillTimeBean2.getCommodityName());//标题
        ViewUtil.setTextViewFormat(mContext, holder.store_money_three, R.string.pay_order_price_text,
                StringUtils.getFormattedDoubleOrInt(limitSkillTimeBean2.getRealPrice()));



    }

    @Override
    public int getItemCount() {
        if (list==null||list.size()==0){
            return 0;
        }
        return Integer.MAX_VALUE;
    }



    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView store_img_o;
        private ImageView img_t;
        private ImageView store_img_t;
        private TextView store_title_one,store_title_two,store_title_three;
        private MoneyTextView store_money_one,store_money_two,store_money_three;
        private int corner;
        private  View mChild01,mChild02,mChild03;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            corner = DeviceUtil.dip2px(mContext, 8);
            mChild01 = itemView.findViewById(R.id.child_01);
            mChild02 = itemView.findViewById(R.id.child_02);
            mChild03 = itemView.findViewById(R.id.child_03);
            store_img_o = itemView.findViewById(R.id.store_img_o);//商品首图
            store_title_one = itemView.findViewById(R.id.store_title_one);//返红包
            store_money_one = itemView.findViewById(R.id.store_money_one);//商品价格
            img_t = itemView.findViewById(R.id.img_t);//商品首图
            store_title_two = itemView.findViewById(R.id.store_title_two);//返红包
            store_money_two = itemView.findViewById(R.id.store_money_two);//商品价格
            store_img_t = itemView.findViewById(R.id.store_img_t);//商品首图
            store_title_three = itemView.findViewById(R.id.store_title_three);//返红包
            store_money_three = itemView.findViewById(R.id.store_money_three);//商品价格
        }
    }

}
