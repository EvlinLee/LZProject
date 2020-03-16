package com.by.lizhiyoupin.app.popup;


import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.by.lizhiyoupin.app.R;

import java.util.List;

/**
 * (Hangzhou)
 *
 * @author: wzm
 * @date :  2019/11/6 17:11
 * Summary:
 */
public class InComeDetailPopupWindow extends PopupWindow implements AdapterView.OnItemClickListener {
    private Context mContext;
    public interface  OnItemClickInterface{
        void itemClick(String txt,int position);
    }
    private OnItemClickInterface mInterface;
    public void setOnItemClickPopListerner(OnItemClickInterface listener){
        this.mInterface=listener;
    }
    public InComeDetailPopupWindow(Context context,List<String> list,int width) {
        mContext=context;
        //设置背景，使点击外面pop可以消失
        setFocusable(true);
        setBackgroundDrawable(new ColorDrawable());
        setWidth(width);
        setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        setContentView(createContentView(list));

    }

    /**
     * 创建PopupWindow要显示的内容
     */
    private View createContentView(List<String> list) {
        View view = View.inflate(mContext, R.layout.income_detail_popupwindow, null);
        //recyclerView 在pop内会测量失败
        ListView listView = view.findViewById(R.id.listview);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(mContext, R.layout.item_incom_popup_layout, list);
        listView.setAdapter(arrayAdapter);
        listView.setOnItemClickListener(this);
        return view;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        TextView textView = view.findViewById(R.id.text_tv);
        if (mInterface!=null){
            mInterface.itemClick(textView.getText().toString(),position);
        }
        dismiss();
    }
}
