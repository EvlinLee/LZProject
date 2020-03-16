package com.by.lizhiyoupin.app.user.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.by.lizhiyoupin.app.R;
import com.by.lizhiyoupin.app.io.bean.TracesBean;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * @ jyx
 * Summary: 自营订单查询
 */
public class OrderLogiticsAdapter extends RecyclerView.Adapter<OrderLogiticsAdapter.ViewHolder> {
    Context context;
    List<TracesBean> traces;

    public OrderLogiticsAdapter(Context context, List<TracesBean> traces) {
        this.context = context;
        this.traces = traces;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View root =
                LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order_logitics_layout, parent, false);

        return new OrderLogiticsAdapter.ViewHolder(root);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String acceptTime = traces.get(position).getAcceptTime();
        String hour = acceptTime.substring(acceptTime.indexOf(":") - 2).trim();
        String year = acceptTime.substring(0, acceptTime.indexOf(":") - 2).trim();
        holder.order_time.setText(year + "\n" + hour);
        holder.order_content.setText(traces.get(position).getAcceptStation());

        if (position == traces.size() - 1) {
            holder.order_view.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return traces.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView order_time;//时间
        TextView order_content;//地址
        View order_view;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            order_time = itemView.findViewById(R.id.order_time);
            order_content = itemView.findViewById(R.id.order_content);
            order_view = itemView.findViewById(R.id.order_view);
        }
    }


}
