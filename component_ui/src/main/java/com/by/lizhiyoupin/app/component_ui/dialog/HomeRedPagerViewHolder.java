package com.by.lizhiyoupin.app.component_ui.dialog;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.by.lizhiyoupin.app.common.ComponentManager;
import com.by.lizhiyoupin.app.common.utils.TimeUtils;
import com.by.lizhiyoupin.app.component_ui.R;
import com.by.lizhiyoupin.app.component_ui.web.CommonSchemeJump;
import com.by.lizhiyoupin.app.io.manager.IAccountManager;
import com.by.lizhiyoupin.app.message_box.MessageBox;
import com.by.lizhiyoupin.app.message_box.holder.IContentHolder;

/**
 * (Hangzhou)
 *
 * @author: wzm
 * @date :  2020/1/10 15:49
 * Summary: 首页 签到红包
 */
public class HomeRedPagerViewHolder implements IContentHolder, View.OnClickListener {

    private Context context;
    private String msg;
    private MessageBox mMessageBox;
    private TextView conMsgTv;


    public HomeRedPagerViewHolder(Context context) {
        this.context = context;

    }

    @Override
    public View contentView(MessageBox messageBox, ViewGroup parent) {
        mMessageBox = messageBox;
        View root = LayoutInflater.from(context).inflate(R.layout.dialog_home_red_pager_layout, parent, false);
        initView(root);
        return root;
    }

    private void initView(View root) {
        root.findViewById(R.id.red_root).setOnClickListener(this);
        conMsgTv = root.findViewById(R.id.content_tips_tv);
    }


    @Override
    public void onClick(View v) {
        if (TimeUtils.isFrequentOperation()) {
            return;
        }
        if (v.getId() == R.id.red_root) {
            final IAccountManager accountManager = (IAccountManager) ComponentManager.getInstance()
                    .getManager(IAccountManager.class.getName());
            if (accountManager != null) {
                if (accountManager.isLogined()) {
                    CommonSchemeJump.showSignInActivity(context);
                } else {
                    CommonSchemeJump.showLoginActivity(context);
                }
            }

            mMessageBox.dismiss();
        }

    }
}
