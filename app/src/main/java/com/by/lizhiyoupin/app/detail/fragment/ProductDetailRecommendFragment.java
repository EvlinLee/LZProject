package com.by.lizhiyoupin.app.detail.fragment;

import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.by.lizhiyoupin.app.R;
import com.by.lizhiyoupin.app.common.utils.DeviceUtil;
import com.by.lizhiyoupin.app.component_ui.fragment.BaseFragment;
import com.by.lizhiyoupin.app.detail.adapter.ProductRecommendListAdapter;
import com.by.lizhiyoupin.app.io.bean.PreciseListBean;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * (Hangzhou)
 *
 * @author: wzm
 * @date :  2019/10/9 17:45
 * Summary: 商品详情页的 三方数据 荔枝推荐
 */
public class ProductDetailRecommendFragment extends BaseFragment {

    private Context mContext;
    private RecyclerView mRecyclerView;
    private ProductRecommendListAdapter mAdapter;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mContext = context;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_product_detail_recommend_layout, container, false);
        initView(root);

        return root;
    }

    private void initView(View root) {

        mRecyclerView = root.findViewById(R.id.detail_recommend_recyclerView);
        mAdapter = new ProductRecommendListAdapter(mContext);

        mRecyclerView.setLayoutManager(new GridLayoutManager(mContext,1,RecyclerView.HORIZONTAL,false));

        int space = DeviceUtil.dip2px(mContext, 10);
        mRecyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {

                if (parent.getChildLayoutPosition(view)==0){
                    outRect.set(space, 0,space, 0);
                }else {
                    outRect.set(0, 0, space, 0);
                }
            }
        });

        mRecyclerView.setAdapter(mAdapter);
    }

    public void updateData( List<PreciseListBean> recommend ){
        mAdapter.getListData().clear();
        mAdapter.setListData(recommend);
        mAdapter.notifyDataSetChanged();
    }
}
