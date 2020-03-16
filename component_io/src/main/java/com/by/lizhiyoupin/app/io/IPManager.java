package com.by.lizhiyoupin.app.io;

import androidx.annotation.IntRange;

/**
 * (Hangzhou)
 *
 * @author: wzm
 * @date :  2019/9/20 17:00
 * Summary:
 */
public class IPManager {

    private static final String BASE_IP = "http://test.api.lizhiyoupin.com/user/";//http://47.99.89.136:8080/
    private static final String TEST_BASE_IP = "http://test.api.lizhiyoupin.com/user/";
    private static final String DEV_BASE_IP = "http://47.99.136.159:80/";
    private static final String VIP_IP = "http://test.api.lizhiyoupin.com/user/";
    private static final String AVATAR_IP = "http://test.api.lizhiyoupin.com/user/";

    private static final String HOME_IP = "http://test.api.lizhiyoupin.com/goods/";//http://47.99.89.136:7080/
    private static final String SHARE_IP = "http://test.api.lizhiyoupin.com/goods/";
    private static final String ORDER_IP = "http://test.api.lizhiyoupin.com/goods/";
    private static final String FANS_IP = "http://test.api.lizhiyoupin.com/goods/";
    private static final String TAOBAO_PIC_IP = "https://h5api.m.taobao.com/";


    /**
     * 正式 测试  开发 环境 ip
     */
    //测试
    private static final String TEST_USER_IP = "http://test.api.lizhiyoupin.com/user/";
    private static final String TEST_GOODS_IP = "http://test.api.lizhiyoupin.com/goods/";
    private static final String TEST_OTHER_IP = "http://test.api.lizhiyoupin.com/other/";

    //正式
    private static final String PROD_USER_IP = "http://api.lizhiyoupin.com/user/";
    private static final String PROD_GOODS_IP = "http://api.lizhiyoupin.com/goods/";
    private static final String PROD_OTHER_IP = "http://api.lizhiyoupin.com/other/";

    // 测试环境ip
    private int serverType = PROD;
    public static final int PROD = 0;
    public static final int TEST = 1;
    public static final int DEV = 2;
    private static IPManager instance;

    private IPManager() {
    }

    public synchronized static IPManager getInstance() {
        if (instance == null) {
            instance = new IPManager();
        }
        return instance;
    }

    /**
     * 设置服务ip切换环境
     *
     * @param serverType
     */
    public void setServerType(@IntRange(from = 0, to = 2) int serverType) {
        this.serverType = serverType;
    }

    public String getServerTypeName(){
        switch (serverType){
            case PROD:
                return "正式环境";
            case TEST:
                return "测试环境";
            case DEV:
                return "开发";
        }
        return "正式";
    }
    /**
     * 是否测试环境
     * @return
     */
    public boolean isTestIp() {
        return serverType == TEST;
    }

    /**
     * 是否正式环境
     * @return
     */
    public boolean isProdIp() {
        return serverType == PROD;
    }



    public String getTaobaoPicIp() {
        return TAOBAO_PIC_IP;
    }


    /**
     * 得到user服务的ip
     *
     * @return
     */
    public String getUserIp() {
        switch (serverType) {
            case PROD:
                return PROD_USER_IP;
            case TEST:
                return TEST_USER_IP;
            case DEV:
                return TEST_USER_IP;
        }
        return PROD_USER_IP;
    }

    /**
     * goods服务的ip
     *
     * @return
     */
    public String getGoodsIp() {
        switch (serverType) {
            case PROD:
                return PROD_GOODS_IP;
            case TEST:
                return TEST_GOODS_IP;
            case DEV:
                return TEST_GOODS_IP;
        }
        return PROD_GOODS_IP;
    }

    public String getOtherIp() {
        switch (serverType) {
            case PROD:
                return PROD_OTHER_IP;
            case TEST:
                return TEST_OTHER_IP;
            case DEV:
                return TEST_OTHER_IP;
        }
        return PROD_OTHER_IP;
    }
}
