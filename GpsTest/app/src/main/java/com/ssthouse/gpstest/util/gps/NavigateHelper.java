package com.ssthouse.gpstest.util.gps;

import android.app.Activity;
import android.content.Context;

import com.baidu.lbsapi.auth.LBSAuthManagerListener;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.navi.BaiduMapNavigation;
import com.baidu.mapapi.navi.NaviParaOption;
import com.baidu.navisdk.BNaviEngineManager;
import com.baidu.navisdk.BaiduNaviManager;
import com.ssthouse.gpstest.util.FileHelper;
import com.ssthouse.gpstest.util.LogHelper;
import com.ssthouse.gpstest.util.ToastHelper;

/**
 * 导航工具
 * Created by ssthouse on 2015/7/17.
 */
public class NavigateHelper {
    private static final String TAG = "NavigateHelper";


    /**
     * 初始化导航功能
     */
    public static  void initNavi(final Activity context) {
        BNaviEngineManager.NaviEngineInitListener naviListener = new BNaviEngineManager.NaviEngineInitListener() {

            @Override
            public void engineInitStart() {
                LogHelper.Log(TAG, "初始化导航-----");
            }

            @Override
            public void engineInitSuccess() {
                LogHelper.Log(TAG, "初始化成功");
            }

            @Override
            public void engineInitFail() {
                LogHelper.Log(TAG, "初始化失败");
            }
        };

        LBSAuthManagerListener authListener = new LBSAuthManagerListener() {

            @Override
            public void onAuthResult(int status, String msg) {
                if (0 == status) {
                    ToastHelper.show(context, "key校验成功-----");
                } else {
                    ToastHelper.show(context, "key校验失败-----");
                }
            }
        };

        BaiduNaviManager.getInstance().initEngine(context, FileHelper.getSDPath(),
                naviListener, authListener);
    }

    public static void startNavigate(Context context, BaiduMap baiduMap){
        BaiduMapNavigation.openBaiduMapNavi(new NaviParaOption(), context);
    }
}
