package com.by.lizhiyoupin.app.component_ui.weight;

import android.content.Context;
import android.database.DataSetObservable;
import android.database.DataSetObserver;
import android.os.Handler;
import android.os.Looper;
import android.util.AttributeSet;

import com.by.lizhiyoupin.app.component_ui.R;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;

/**
 * (Hangzhou)
 *
 * @author: wzm
 * @date :  2020/1/10 10:47
 * Summary: 12:45:62 样式 倒计时
 */
public class SignCountTimeView extends AppCompatTextView implements Runnable {
    public static final String TAG = CountTimeView.class.getSimpleName();

    private long mTime = 0;
    private Handler mHandler;
    private String time_10_60;
    private final DataSetObservable mDataSetObservable = new DataSetObservable();

    public SignCountTimeView(Context context) {
        this(context, null);
    }

    public SignCountTimeView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SignCountTimeView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        time_10_60 = getResources().getString(R.string.count_time_10_60_text_format);
        mHandler = new Handler(Looper.getMainLooper());
    }

    @Override
    public void run() {
        mTime = mTime - 1;
        if (mTime <= 0) {
            notifyDataSetChanged();
            if (countTime0Listener!=null){
                countTime0Listener.notifyTimeChanged();
            }
        } else {
            mHandler.postDelayed(this, 1000);
        }
        long[] timeDuring = getTimeDuring(mTime);
        String time = String.format(time_10_60, timeDuring[0] < 10 ? "0" + timeDuring[0] : timeDuring[0],
                timeDuring[1] < 10 ? "0" + timeDuring[1] : timeDuring[1],
                timeDuring[2] < 10 ? "0" + timeDuring[2] : timeDuring[2]);
        setText(time);
    }


    /**
     * 倒计时
     *
     * @param time 秒
     */
    public void startLimitedTime(long time) {
        mTime = time;
        if (mTime > 0) {
            mHandler.removeCallbacksAndMessages(null);
            mHandler.post(this);
        }
    }

    /**
     * 距离秒杀还剩多少时间
     *
     * @return
     */
    public long getLimitedTime() {
        return mTime;
    }

    public final void registerDataSetObserver(DataSetObserver observer) {
        mDataSetObservable.registerObserver(observer);
    }

    public final void unregisterDataSetObserver(DataSetObserver observer) {
        mDataSetObservable.unregisterObserver(observer);
    }

    public final void notifyDataSetChanged() {
        mDataSetObservable.notifyChanged();
    }

    public final void notifyDataSetInvalidated() {
        mDataSetObservable.notifyInvalidated();
    }

    public void removeHandlerMessage(){
        if (mHandler!=null){
            mHandler.removeCallbacksAndMessages(null);
        }
    }
    private long[] getTimeDuring(long second) {
        long hours = (second % (60 * 60 * 24)) / (60 * 60);
        long minutes = (second % (60 * 60)) / (60);
        long seconds = second % 60;
        return new long[]{hours, minutes, seconds};
    }

    public   interface  CountTime0Interface{
        void notifyTimeChanged();
    }
    CountTime0Interface countTime0Listener;
    public void  setCountTime0Listener(CountTime0Interface countTime0Listener){
        this.countTime0Listener=countTime0Listener;
    }
}
