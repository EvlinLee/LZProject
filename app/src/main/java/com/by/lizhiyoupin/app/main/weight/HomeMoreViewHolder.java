package com.by.lizhiyoupin.app.main.weight;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.by.lizhiyoupin.app.R;
import com.by.lizhiyoupin.app.io.bean.CommonCategoryBean;
import com.by.lizhiyoupin.app.main.adapter.HomeMoreAdapter;
import com.by.lizhiyoupin.app.message_box.MessageBox;
import com.by.lizhiyoupin.app.message_box.holder.IContentHolder;

import java.util.List;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * (Hangzhou)
 *
 * @author: wzm
 * @date :  2019/11/21 20:32
 * Summary:
 */
public class HomeMoreViewHolder implements IContentHolder, View.OnClickListener {
    private MessageBox messageBox;
    private RecyclerView mRecyclerView;
    private Context mContext;
    private GridLayoutManager mGridLayoutManager;
    private HomeMoreAdapter mHomeMoreAdapter;
    private  List<CommonCategoryBean> mList;
    private float top;// dialog顶部需要空出的距离
    public HomeMoreViewHolder(Context context, List<CommonCategoryBean> list,float top) {
        this.mContext = context;
        mList=list;
        this.top=top;
    }
    @Override
    public View contentView(MessageBox messageBox, ViewGroup parent) {
        this.messageBox = messageBox;
        View root = LayoutInflater.from(mContext).inflate(R.layout.messagebox_home_more_layout, parent, false);
        initView(root);
        return root;
    }

    private void initView(View root) {
        mRecyclerView = root.findViewById(R.id.recyclerView);
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) mRecyclerView.getLayoutParams();
        layoutParams.setMargins(0, (int) top,0,0);
        root.findViewById(R.id.container_ll).setOnClickListener(this);
        mGridLayoutManager = new GridLayoutManager(mContext, 4);
        mRecyclerView.setLayoutManager(mGridLayoutManager);
        mHomeMoreAdapter = new HomeMoreAdapter(mContext,messageBox);
        mRecyclerView.setAdapter(mHomeMoreAdapter);
        mHomeMoreAdapter.setListData(mList);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.container_ll:
                messageBox.dismiss();
                break;
        }
    }


}
