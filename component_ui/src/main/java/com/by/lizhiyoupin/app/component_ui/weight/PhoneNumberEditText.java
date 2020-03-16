package com.by.lizhiyoupin.app.component_ui.weight;

import android.content.Context;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.widget.EditText;

/**
 * 手机号输入
 *
 * 136 1654 4587 由于中间多了2个空格所以长度是11+2=13
 */
public class PhoneNumberEditText extends EditText {

    private int beforeLen = 0;
    private int afterLen = 0;

    public PhoneNumberEditText(Context context) {
        super(context);
        init();
    }

    public PhoneNumberEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public PhoneNumberEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init();
    }

    public void setPhoneNumber(String text) {
        setText(text);
        if (!TextUtils.isEmpty(text)) {
            setSelection(text.length());
        }
    }

    private void init() {
        addTextChangedListener(
                new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                        beforeLen = s.length();
                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        String txt = getText().toString();
                        afterLen = txt.length();

                        if (afterLen > beforeLen) {
                            if (txt.length() == 4 || txt.length() == 9) {
                                setText(new StringBuffer(txt).insert(txt.length() - 1, " ").toString());
                                setSelection(getText().length());
                            }
                        } else {
                            if (txt.startsWith(" ")) {
                                setText(new StringBuffer(txt).delete(afterLen - 1, afterLen).toString());
                                setSelection(getText().length());
                            }
                        }
                    }
                }
        );
    }

    public String getPhoneNumber() {
        String text = getText().toString();
        text.trim();
        return text.replaceAll(" ", "");
    }
}
