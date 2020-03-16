package com.by.lizhiyoupin.app.splash;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.by.lizhiyoupin.app.R;
import com.by.lizhiyoupin.app.common.CommonConst;
import com.by.lizhiyoupin.app.component_ui.fragment.BaseFragment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * (Hangzhou)
 *
 * @author: wzm
 * @date :  2019/11/22 10:53
 * Summary:
 */
public class SplashFragment extends BaseFragment implements View.OnClickListener {
    private String title;
    private String dec;
    private int imgRes;
    private boolean isEnd;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.fragment_splash_layout, container, false);
        Bundle arguments = getArguments();
        if (arguments != null) {
            title = arguments.getString(CommonConst.KEY_SPLASH_TITLE, "");
            dec = arguments.getString(CommonConst.KEY_SPLASH_DESC, "");
            imgRes = arguments.getInt(CommonConst.KEY_SPLASH_IMG, 0);
            isEnd = arguments.getBoolean(CommonConst.KEY_SPLASH_END, false);
        }
        initView(inflate);
        return inflate;
    }

    private void initView(View inflate) {
        ImageView show_iv = inflate.findViewById(R.id.show_iv);
        TextView title_tv = inflate.findViewById(R.id.title_tv);
        TextView desc_tv = inflate.findViewById(R.id.desc_tv);
        View finishView = inflate.findViewById(R.id.finish_button);
        finishView.setOnClickListener(this);
        show_iv.setImageResource(imgRes);
        title_tv.setText(title);
        desc_tv.setText(dec);
        finishView.setVisibility(isEnd ? View.VISIBLE : View.INVISIBLE);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.finish_button:
                //进入主页
                if (getActivity() instanceof SplashActivity) {
                    SplashActivity activity = (SplashActivity) getActivity();
                    activity.jumpMainActivity();
                }
                break;
            default:
                break;
        }
    }


}
