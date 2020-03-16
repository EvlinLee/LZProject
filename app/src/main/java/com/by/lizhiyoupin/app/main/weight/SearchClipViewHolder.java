package com.by.lizhiyoupin.app.main.weight;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.by.lizhiyoupin.app.R;
import com.by.lizhiyoupin.app.common.CommonConst;
import com.by.lizhiyoupin.app.common.utils.TimeUtils;
import com.by.lizhiyoupin.app.common.utils.storage.SPUtils;
import com.by.lizhiyoupin.app.component_ui.web.CommonSchemeJump;
import com.by.lizhiyoupin.app.message_box.MessageBox;
import com.by.lizhiyoupin.app.message_box.holder.IContentHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * (Hangzhou)
 *
 * @author: wzm
 * @date :  2019/10/17 19:35
 * Summary: 粘切板弹框
 */
public class SearchClipViewHolder implements IContentHolder, View.OnClickListener {
    // View值
    private TextView mTitleTv;
    private TextView contentMesTv;
    private TextView taobaoTv;
    private TextView pingddTv;
    private TextView jindTv;
    private MessageBox messageBox;

    private Context context;
    private String dialogTitle;
    private String contentText;
    private String goodsId = null;
    private List<String> mHistoryList;

    public SearchClipViewHolder(Context context, String dialogTitle, String contentMessage, Long goodsId) {
        this.context = context;
        this.dialogTitle = dialogTitle;
        this.contentText = contentMessage;
        if (goodsId != null) {
            this.goodsId = String.valueOf(goodsId);
        }
    }

    @Override
    public View contentView(MessageBox messageBox, ViewGroup parent) {
        this.messageBox = messageBox;
        View tipsContent = LayoutInflater.from(context).inflate(R.layout.messagebox_search_clip_layout, parent, false);
        mTitleTv = tipsContent.findViewById(R.id.dialog_title_tv);
        contentMesTv = tipsContent.findViewById(R.id.content_Message_tv);
        taobaoTv = tipsContent.findViewById(R.id.search_taobao_tv);
        pingddTv = tipsContent.findViewById(R.id.search_pingdd_tv);
        jindTv = tipsContent.findViewById(R.id.search_jind_tv);
        taobaoTv.setOnClickListener(this);
        pingddTv.setOnClickListener(this);
        jindTv.setOnClickListener(this);
        updateTitle(dialogTitle);
        updateContentMessage(contentText);
        mHistoryList = SPUtils.getDefault().getListData(CommonConst.KEY_SAVE_SEARCH_HISTORY);
        if (mHistoryList == null) {
            mHistoryList = new ArrayList<>();
        }
        return tipsContent;
    }

    /**
     * 更新内容信息
     *
     * @param content
     */
    public void updateContentMessage(String content) {
        this.contentText = content;
        if (!TextUtils.isEmpty(content)) {
            contentMesTv.setText(content);
        }
    }

    /**
     * 更新标题信息
     *
     * @param title
     */
    public void updateTitle(String title) {
        this.dialogTitle = title;
        if (!TextUtils.isEmpty(title)) {
            mTitleTv.setText(title);
        }
    }

    @Override
    public void onClick(View v) {
        if (TimeUtils.isFrequentOperation()) {
            return;
        }
        Bundle bundle = new Bundle();
        switch (v.getId()) {
            case R.id.search_taobao_tv:
                bundle.putString(CommonConst.KEY_CURRENT_SEARCH_PLATFORM, CommonConst.SOURCE_TAO_BAO);
                break;
            case R.id.search_pingdd_tv:
                bundle.putString(CommonConst.KEY_CURRENT_SEARCH_PLATFORM, CommonConst.SOURCE_PING_DUO_DUO);
                break;
            case R.id.search_jind_tv:
                bundle.putString(CommonConst.KEY_CURRENT_SEARCH_PLATFORM, CommonConst.SOURCE_JING_DONG);
                break;
            default:
                bundle.putString(CommonConst.KEY_CURRENT_SEARCH_PLATFORM, CommonConst.SOURCE_TAO_BAO);
                return;
        }

        bundle.putString(CommonConst.KEY_SEARCH_TITLE, contentText);
        bundle.putString(CommonConst.KEY_SEARCH_ID, goodsId);
        CommonSchemeJump.showSearchResultActivity(context, bundle);
        if (!TextUtils.isEmpty(contentText) && !mHistoryList.contains(contentText)) {
            mHistoryList.add(0, contentText);
            SPUtils.getDefault().putListData(CommonConst.KEY_SAVE_SEARCH_HISTORY, mHistoryList);
        }
        if (messageBox != null) {
            messageBox.dismiss();
        }
    }
}
