package com.by.lizhiyoupin.app.component_ui.dialog;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bigkoo.pickerview.adapter.ArrayWheelAdapter;
import com.by.lizhiyoupin.app.component_ui.R;
import com.by.lizhiyoupin.app.component_ui.impl.SelectCallback;
import com.by.lizhiyoupin.app.message_box.MessageBox;
import com.by.lizhiyoupin.app.message_box.holder.IContentHolder;
import com.contrarywind.view.WheelView;

import java.util.List;

import androidx.annotation.NonNull;

/**
 * (Hangzhou)
 *
 * @author: wzm
 * @date :  2019/12/31 15:24
 * Summary: 运营商推送管理 选择推送内容标签
 */
public class PushTagsSelectViewHolder implements IContentHolder, View.OnClickListener {
    public static final String TAG = PushTagsSelectViewHolder.class.getSimpleName();
    private Context context;
    private MessageBox mMessageBox;
    private SelectCallback<String> selectCallback;
    private WheelView mWheelView;
    private List<String> list;

    public PushTagsSelectViewHolder(@NonNull Context context, @NonNull List<String> list, SelectCallback<String> selectCallback) {
        this.context = context;
        this.selectCallback = selectCallback;
        this.list = list;
    }

    @Override
    public View contentView(MessageBox messageBox, ViewGroup parent) {
        mMessageBox = messageBox;
        View content = LayoutInflater.from(context).inflate(R.layout.messagebox_push_tags_dialog_layout, parent, false);
        initView(content);
        return content;
    }

    private void initView(View root) {
        mWheelView = root.findViewById(R.id.wheelView);
        root.findViewById(R.id.confirm_tv).setOnClickListener(this);
        root.findViewById(R.id.cancel_tv).setOnClickListener(this);
        if (list != null) {
            ArrayWheelAdapter<String> arrayWheelAdapter = new ArrayWheelAdapter<>(list);
            mWheelView.setAdapter(arrayWheelAdapter);
        }

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.confirm_tv) {
            if (selectCallback != null && list != null && list.size() > mWheelView.getCurrentItem()) {
                selectCallback.selectBack(list.get(mWheelView.getCurrentItem()));
            }
            mMessageBox.dismiss();
        } else if (v.getId() == R.id.cancel_tv) {
            mMessageBox.dismiss();
        }
    }

}
