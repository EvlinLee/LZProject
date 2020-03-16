package com.by.lizhiyoupin.app.component_ui.dialog;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.by.lizhiyoupin.app.component_ui.R;
import com.by.lizhiyoupin.app.io.bean.RedGiftBean;
import com.by.lizhiyoupin.app.message_box.MessageBox;
import com.by.lizhiyoupin.app.message_box.holder.IContentHolder;

/**
 * (Hangzhou)
 *
 * @author: wzm
 * @date :  2019/11/28 21:41
 * Summary: 红包
 */
public class RedGiftPackageViewHolder implements IContentHolder, View.OnClickListener {
    public static final String TAG = RedGiftPackageViewHolder.class.getSimpleName();


    private Context context;
    private MessageBox mMessageBox;
    private TextView mNumberTv;
    private TextView mPriceTv;
    private RedGiftBean mRedGiftBean;

    public RedGiftPackageViewHolder(Context context, RedGiftBean redGiftBean) {
        this.context = context;
        mRedGiftBean = redGiftBean;
    }

    @Override
    public View contentView(MessageBox messageBox, ViewGroup parent) {
        mMessageBox = messageBox;
        View content = LayoutInflater.from(context).inflate(R.layout.messagebox_red_gift_package_layout, parent, false);
        initView(content);
        return content;
    }

    private void initView(View root) {
        mNumberTv = root.findViewById(R.id.order_number_tv);
        mPriceTv = root.findViewById(R.id.order_price_tv);
        mNumberTv.setText(String.format(context.getResources().getString(R.string.red_gift_package_order_number_text),
                String.valueOf(mRedGiftBean.getOrderCount())));
        mPriceTv.setText(String.valueOf(mRedGiftBean.getAllMoney()));
        root.findViewById(R.id.add_tv).setOnClickListener(this);
        showSoundPool();
    }
    /**
     * 播放撒钱声音
     */
    private void showSoundPool() {
        SoundPool soundPool;
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            soundPool = new SoundPool(1, AudioManager.STREAM_MUSIC, 0);
        } else {
            soundPool = new SoundPool.Builder().setMaxStreams(1).build();
        }
        int id = soundPool.load(context, R.raw.sign_in_money, 1);
        soundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
            @Override
            public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
                soundPool.play(id, 1, 1, 0, 0, 1);
            }
        });
    }
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.add_tv) {
            //收入钱包
            mMessageBox.dismiss();
        }
    }
}
