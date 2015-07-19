package com.ssthouse.gpstest.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.UiSettings;
import com.baidu.mapapi.model.LatLng;
import com.ssthouse.gpstest.R;
import com.ssthouse.gpstest.util.LogHelper;

/**
 * 用于选址的Activity
 * Created by ssthouse on 2015/7/17.
 */
public class MarkerActivity extends AppCompatActivity{

    private static final String TAG = "MarkerActivity";

    // 定位相关
    private LocationClient mLocClient;

    //ui设置
    private UiSettings uiSettings;

    //经纬度的输入框
    private EditText etLatitude, etLongitude;

    //确定按钮
    private Button btnSubmit;


    //视图中间的marker
    private ImageView ivMark;

    //控制状态的button
    private ImageButton imageButton;


    //跟随----普通---罗盘---三种定位方式
    private MyLocationConfiguration.LocationMode mCurrentMode =
            MyLocationConfiguration.LocationMode.NORMAL;

    private MapView mMapView;

    private BaiduMap mBaiduMap;

    //用于Activity开启时的第一次定位
    private boolean isFistIn = true;

    //用于判断是否已经定位
    private boolean isLocated = true;

    //定位监听器---每秒触发
    public BDLocationListener myListener = new BDLocationListener() {
        @Override
        public void onReceiveLocation(BDLocation location) {
            // map view 销毁后不在处理新接收的位置
            if (location == null || mMapView == null) {
                return;
            }
            //TODO---这里其实没有每秒都要做的事--就先空着
            //locate(location);
            if (isFistIn) {
                locate(location);
                isFistIn = false;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mark);

        // 定位初始化
        mLocClient = new LocationClient(this);
        mLocClient.registerLocationListener(myListener);
        //初始化定位---设置
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true);// 打开gps
        option.setCoorType("bd09ll"); // 设置坐标类型
        option.setScanSpan(1000);
        mLocClient.setLocOption(option);
        //开启定位
        mLocClient.start();

        //初始化视图
        initView();
    }

    private void initView() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.id_tb);
        toolbar.setTitle("选址");
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        toolbar.setLogo(R.mipmap.ic_launcher);
        toolbar.setBackgroundDrawable(getResources().getDrawable(R.drawable.action_bar_background));
        setSupportActionBar(toolbar);

        mMapView = (MapView) findViewById(R.id.id_map_view);
        mBaiduMap = mMapView.getMap();
        uiSettings = mBaiduMap.getUiSettings();
        //开启指南针
        uiSettings.setCompassEnabled(true);
        // 开启定位图层
        mBaiduMap.setMyLocationEnabled(true);

        //地图的触摸监听事件
        mBaiduMap.setOnMapTouchListener(new BaiduMap.OnMapTouchListener() {
            @Override
            public void onTouch(MotionEvent motionEvent) {
                //一旦摸到---就表示未定位
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    isLocated = false;
                    imageButton.setImageResource(R.drawable.locate1);
                    LogHelper.Log(TAG, "我摸到了---地图");
                }

            }
        });

        //地图状态变化监听---用于监听选取的Marker位置
        mBaiduMap.setOnMapStatusChangeListener(new BaiduMap.OnMapStatusChangeListener() {
            @Override
            public void onMapStatusChangeStart(MapStatus mapStatus) {
                //TODO---markIcon的放大动画
            }

            @Override
            public void onMapStatusChange(MapStatus mapStatus) {
            }

            @Override
            public void onMapStatusChangeFinish(MapStatus mapStatus) {
                //TODO---markIcon的缩小动画

                //更新输入框经纬度数据
                LatLng latlng = mBaiduMap.getMapStatus().target;
                etLatitude.setText(latlng.latitude + "");
                etLongitude.setText(latlng.longitude + "");
            }
        });

        //TODO---测试用
        mBaiduMap.setOnMapClickListener(new BaiduMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                LogHelper.Log(TAG, latLng.latitude + " : " + latLng.longitude);
            }

            @Override
            public boolean onMapPoiClick(MapPoi mapPoi) {
                return false;
            }
        });

        ivMark = (ImageView) findViewById(R.id.id_iv_mark_icon);

        etLatitude = (EditText) findViewById(R.id.id_et_latitude);
        etLongitude = (EditText) findViewById(R.id.id_et_longitude);

        btnSubmit = (Button) findViewById(R.id.id_btn_submit);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO---判断输入框中的数据是否可用---然后提交数据
            }
        });

        //模式切换按钮---兼定位按钮
        imageButton = (ImageButton) findViewById(R.id.id_btn_locate);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isLocated) {
                    //如果已经定位了---切换视图
                    if (mCurrentMode == MyLocationConfiguration.LocationMode.NORMAL) {
                        LogHelper.Log(TAG, "change to compass mode");
                        mCurrentMode = MyLocationConfiguration.LocationMode.COMPASS;
                        imageButton.setImageResource(R.drawable.location_mode_2);
                        enableEagle();
                    } else if (mCurrentMode == MyLocationConfiguration.LocationMode.COMPASS) {
                        LogHelper.Log(TAG, "change to normal mode");
                        mCurrentMode = MyLocationConfiguration.LocationMode.NORMAL;
                        imageButton.setImageResource(R.drawable.location_mode_1);
                        disableEagle();
                    }
                } else {
                    //手动定位
                    locate(mLocClient.getLastKnownLocation());
//                    uiSettings.setCompassEnabled(true);
                    LogHelper.Log(TAG, "located!!!");
                    isLocated = true;
                    //判断当前状态---切换图标
                    if (mCurrentMode == MyLocationConfiguration.LocationMode.NORMAL) {
                        imageButton.setImageResource(R.drawable.location_mode_1);
                    } else if (mCurrentMode == MyLocationConfiguration.LocationMode.COMPASS) {
                        imageButton.setImageResource(R.drawable.location_mode_2);
                    }
                }
            }
        });
    }

    /**
     * 根据BDLocation定位
     *
     * @param location
     */
    private void locate(BDLocation location) {
        MyLocationData locData = new MyLocationData.Builder()
                .accuracy(location.getRadius())
                        // 此处设置开发者获取到的方向信息，顺时针0-360
                .direction(100).latitude(location.getLatitude())
                .longitude(location.getLongitude()).build();
        mBaiduMap.setMyLocationData(locData);
        LatLng ll = new LatLng(location.getLatitude(),
                location.getLongitude());
        MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(ll);
        mBaiduMap.animateMapStatus(u);
    }

    private void enableEagle() {
        //改变可视角度
        MapStatus ms = new MapStatus.Builder(mBaiduMap.getMapStatus()).overlook(-100).build();
        MapStatusUpdate u = MapStatusUpdateFactory.newMapStatus(ms);
        mBaiduMap.animateMapStatus(u);
    }

    private void disableEagle() {
        //改变可视角度
        MapStatus ms = new MapStatus.Builder(mBaiduMap.getMapStatus()).overlook(0).build();
        MapStatusUpdate u = MapStatusUpdateFactory.newMapStatus(ms);
        mBaiduMap.animateMapStatus(u);
    }

    public static void start(Context context) {
        Intent intent = new Intent(context, MarkerActivity.class);
        context.startActivity(intent);
    }

    //生命周期----------------------------------------------
    @Override
    protected void onPause() {
        mMapView.onPause();
        super.onPause();
    }

    @Override
    protected void onResume() {
        mMapView.onResume();
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        // 退出时销毁定位
        mLocClient.stop();
        // 关闭定位图层
        mBaiduMap.setMyLocationEnabled(false);
        mMapView.onDestroy();
        mMapView = null;
        super.onDestroy();
    }
}
