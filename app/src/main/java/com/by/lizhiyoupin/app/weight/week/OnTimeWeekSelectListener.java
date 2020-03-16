package com.by.lizhiyoupin.app.weight.week;

import android.view.View;

/**
 * (Hangzhou)
 *
 * @author: wzm
 * @date :  2019/11/8 14:19
 * Summary:
 */
public interface OnTimeWeekSelectListener {
      /**
       *
       * @param date "yyyy-MM-dd"
       * @param number  月数，第几周，日数
       * @param v
       */
      void onTimeWeekSelect(String date, int number,View v);
}
