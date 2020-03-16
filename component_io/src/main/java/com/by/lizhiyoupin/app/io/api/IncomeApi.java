package com.by.lizhiyoupin.app.io.api;

import com.by.lizhiyoupin.app.io.BaseBean;
import com.by.lizhiyoupin.app.io.bean.IncomeDetailsVO;
import com.by.lizhiyoupin.app.io.bean.IncomeReport;
import com.by.lizhiyoupin.app.io.bean.MyIncomeVO;
import com.by.lizhiyoupin.app.io.bean.SaveMoneyBean;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;

/**
 * (Hangzhou)
 *
 * @author: wzm
 * @date :  2019/11/7 13:41
 * Summary:
 */
public interface IncomeApi {
    /**
     * 会员的综合佣金收益
     * 我的收益记录
     *
     * @return
     */
    @GET("income/v1/user-income")
    @Headers({"Content-Type: application/x-www-form-urlencoded;charset=UTF-8"})
    Observable<BaseBean<MyIncomeVO>> requestIncomeProfitRecord();

    /**
     * 会员的佣金明细列表
     *
     * @param desc  排序 0降序 1升序
     * @param start 起始页1
     * @param limit 每页显示多少条，默认10条
     * @param fansType 粉丝类型 0全部 1专属 2普通
     * @param platformType 平台类型 0 全部 1淘宝 2京东 3拼多多
     * @param profitType 收益类型 1礼包 2导购
     * @return
     */
    @GET("income/v1/income-list")
    @Headers({"Content-Type: application/x-www-form-urlencoded;charset=UTF-8"})
    Observable<BaseBean<List<IncomeDetailsVO>>> requestIncomeDetail(@Query("desc") int desc,
                                                                    @Query("start") int start,
                                                                    @Query("limit") int limit,
                                                                    @Query("fansType") int fansType,
                                                                    @Query("platformType") int platformType,
                                                                    @Query("profitType") int profitType);

    /**
     * 会员的预估收益报表
     *
     * @param date 2015-12-13
     * @param type 数据类型 0--单日 1--单周 2--单月 ,3--前几周 4--前几月
     * @param num   type类型012第几周、几月，type类型34向前几周或几月
     * @return
     */
    @GET("income/v1/report")
    @Headers({"Content-Type: application/x-www-form-urlencoded;charset=UTF-8"})
    Observable<BaseBean<List<IncomeReport>>> requestIncomeReport(@Query("date") String date,
                                                                 @Query("type") int type,
                                                                 @Query("num") int num);

    /**
     * 会员的优惠券 省钱明细 列表
     * @param desc 排序 0降序 1升序
     * @param start
     * @param limit
     * @return
     */
    @GET("income/v1/save-list")
    @Headers({"Content-Type: application/x-www-form-urlencoded;charset=UTF-8"})
    Observable<BaseBean<SaveMoneyBean>> requestSaveMoneyList(@Query("desc") int desc,
                                                            @Query("start") int start,
                                                            @Query("limit") int limit);
}
