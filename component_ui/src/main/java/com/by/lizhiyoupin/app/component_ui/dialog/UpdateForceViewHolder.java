package com.by.lizhiyoupin.app.component_ui.dialog;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.by.lizhiyoupin.app.common.log.LZLog;
import com.by.lizhiyoupin.app.common.utils.PackageUtil;
import com.by.lizhiyoupin.app.component_ui.R;
import com.by.lizhiyoupin.app.component_ui.weight.IndicateProgressBar;
import com.by.lizhiyoupin.app.component_ui.weight.download.DownloadInstaller;
import com.by.lizhiyoupin.app.component_ui.weight.download.DownloadProgressCallBack;
import com.by.lizhiyoupin.app.io.bean.UpdateViersionBean;
import com.by.lizhiyoupin.app.message_box.MessageBox;
import com.by.lizhiyoupin.app.message_box.MessageToast;
import com.by.lizhiyoupin.app.message_box.holder.IContentHolder;

import java.util.List;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * (Hangzhou)
 *
 * @author: wzm
 * @date :  2019/12/5 16:32
 * Summary: 强制更新
 */
public class UpdateForceViewHolder implements IContentHolder, View.OnClickListener {
    private Context mContext;
    private MessageBox mMessageBox;
    private RecyclerView mRecyclerView;
    private UpdateViersionBean mVersionBean;
    private IndicateProgressBar mProgressBar;
    private TextView mUpdateTv;

    public UpdateForceViewHolder(Context context, UpdateViersionBean versionBean) {
        super();
        this.mContext = context;
        this.mVersionBean = versionBean;
    }

    @Override
    public View contentView(MessageBox messageBox, ViewGroup parent) {
        mMessageBox = messageBox;
        View content = LayoutInflater.from(mContext).inflate(R.layout.messagebox_update_force_layout, parent, false);
        initView(content);
        return content;
    }

    private void initView(View root) {
        mRecyclerView = root.findViewById(R.id.recyclerView);
        mProgressBar = root.findViewById(R.id.loading_progress);
        mUpdateTv = root.findViewById(R.id.update_tv);
        mUpdateTv.setOnClickListener(this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        UpdateAdapter updateAdapter = new UpdateAdapter(mContext);
        if (mVersionBean != null) {
            List<String> updateLogs = mVersionBean.getUpdateLogs();
            updateAdapter.setListData(updateLogs);
            mRecyclerView.setAdapter(updateAdapter);
        }
        mProgressBar.setCanTouch(false);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.update_tv) {
            //开始更新
            mUpdateTv.setVisibility(View.GONE);
            mProgressBar.setVisibility(View.VISIBLE);
            if (mVersionBean != null) {
                update(mVersionBean.getDownloadUrl());
            } else {
                if (mContext != null) {
                    MessageToast.showToast(mContext, "下载出错，请到应用市场更新!");
                    PackageUtil.evaluateThisApp(mContext);
                }
            }

        }
    }


    private void update(String downloadUrl) {
        //一般的弹出对话框提示升级
        //如果是企业内部应用升级，肯定是要这个权限; 其他情况不要太流氓，TOAST 提示
        new DownloadInstaller(mContext, downloadUrl, true, new DownloadProgressCallBack() {
            @Override
            public void downloadProgress(int progress) {
                LZLog.i("PROGRESS", "Progress==" + progress + Thread.currentThread());
                mProgressBar.setProgressInvalidate(progress);
            }

            @Override
            public void downloadException(Exception e) {
                e.printStackTrace();
                MessageToast.showToast(mContext, "下载出错，请到应用市场更新!");
                if (mContext != null) {
                    PackageUtil.evaluateThisApp(mContext);
                }
            }


            @Override
            public void onInstallStart() {

            }
        }).start();

    }
}
