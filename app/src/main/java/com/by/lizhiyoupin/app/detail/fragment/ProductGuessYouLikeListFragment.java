package com.by.lizhiyoupin.app.detail.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.by.lizhiyoupin.app.R;
import com.by.lizhiyoupin.app.common.utils.DeviceUtil;
import com.by.lizhiyoupin.app.component_ui.fragment.BaseFragment;
import com.by.lizhiyoupin.app.component_ui.weight.recyclerview.SpaceItemDecoration;
import com.by.lizhiyoupin.app.io.bean.PreciseListBean;
import com.by.lizhiyoupin.app.loader.LoadMoreHelperRx;
import com.by.lizhiyoupin.app.main.adapter.DetailSelectionListAdapter;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * (Hangzhou)
 *
 * @author: wzm
 * @date :  2019/9/27 18:03
 * Summary: 商详页的猜你喜欢 是三方数据
 */
public class ProductGuessYouLikeListFragment extends BaseFragment {

    private Context mContext;
    private RecyclerView mRecyclerView;
    private DetailSelectionListAdapter mAdapter;

    private List<PreciseListBean> mList;
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.fragment_guess_you_like_layout, container, false);
        initView(inflate);
        return inflate;
    }

    private void initView(View root) {
        mList=new ArrayList<>();

        mRecyclerView = root.findViewById(R.id.guess_you_like_list_rv);
        mAdapter = new DetailSelectionListAdapter(mContext);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(mContext, 2);
        mRecyclerView.setLayoutManager(gridLayoutManager);

        mRecyclerView.addItemDecoration(new SpaceItemDecoration(DeviceUtil.dip2px(mContext, 5), 2));
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                //每行item数据=spanCount/getSpanSize
                return mAdapter.getListData().size()==position?2:1;
            }
        });
        mAdapter.setListData(mList);
        mRecyclerView.setAdapter(mAdapter);
    }


    public void updateData(List<PreciseListBean> guessYouLike){
        mAdapter.getListData().clear();
        mAdapter.setListData(guessYouLike);
        mAdapter.notifyDataSetChanged();
    }
}
