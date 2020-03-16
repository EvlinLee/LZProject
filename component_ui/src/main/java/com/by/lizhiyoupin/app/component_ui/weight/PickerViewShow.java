package com.by.lizhiyoupin.app.component_ui.weight;

import android.content.Context;
import android.content.res.Resources;
import android.text.TextUtils;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.by.lizhiyoupin.app.component_ui.R;

import java.util.ArrayList;

/**
 * 选择器弹框处理
 */
public class PickerViewShow {





    public static OptionsPickerView<String> showPlacePicker(Context context, OnOptionsSelectListener listener, ArrayList<String> options1Items, final ArrayList<ArrayList<String>> options2Items, final String provice, final String city) {
        final int size = options1Items.size();
        int options1 = 0;
        int options2 = 0;
        for (int i = 0; i < size; i++) {
            if (TextUtils.equals(provice, options1Items.get(i))) {
                final ArrayList<String> cites = options2Items.get(i);
                final int subSize = cites.size();
                for (int j = 0; j < subSize; j++) {
                    if (TextUtils.equals(city, cites.get(j))) {
                        options1 = i;
                        options2 = j;
                    }
                }
            }
        }
        Resources resources = context.getResources();
        OptionsPickerView<String> view = new OptionsPickerBuilder(context, listener)
                .setBgColor(resources.getColor(R.color.default_content_bg))
                .setTextColorCenter(resources.getColor(R.color.color_333333))
                .setTextColorOut(resources.getColor(R.color.color_999999))
                .setCancelText(resources.getString(R.string.cancel_txt))
                .setCancelColor(resources.getColor(R.color.color_999999))
                .setSubmitText(resources.getString(R.string.confirm_txt))
                .setSubmitColor(resources.getColor(R.color.color_D0C481))
                //.setTitleText("选择城市")
               // .setTitleBgColor(resources.getColor(R.color.default_white_background))
                .setDividerColor(resources.getColor(R.color.list_divider))
                .setCyclic(false, false, false)
                .build();
        view.setSelectOptions(options1, options2);
        view.show();
        return view;
    }
}
