package com.ssthouse.gpstest.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.ssthouse.gpstest.Constant;
import com.ssthouse.gpstest.R;
import com.ssthouse.gpstest.model.MarkerItem;
import com.ssthouse.gpstest.model.PrjItem;
import com.ssthouse.gpstest.util.ToastHelper;
import com.ssthouse.gpstest.util.gps.LocateHelper;
import com.ssthouse.gpstest.util.gps.MapHelper;

/**
 * 开启时会接收到一个PrjItem---intent中
 */
public class PrjEditActivity extends AppCompatActivity {
    private static final String TAG = "PrjEditActivity";

    private static final int REQUEST_CODE_ROUTE_ACTIVITY = 1000;
    private static final int REQUEST_CODE_MARKER_ACTIVITY = 1001;
    private static final int REQUEST_CODE_PICTURE_ACTIVITY = 1002;

    /**
     * 接收到的数据
     */
    private PrjItem prjItem;

    private BaiduMap mBaiduMap;
    private MapView mMapView;
    private LocationClient mLocationClient;
    private boolean isLocated = false;

    private ImageButton ibLocate;

    /**
     * 用于更加方便的开启Activity
     * 后面几个参数可以用来传递-----放入intent 的数据
     *
     * @param context
     */
    public static void start(Context context, PrjItem prjItem) {
        if (context == null || prjItem == null) {
            return;
        }
        Intent intent = new Intent(context, PrjEditActivity.class);
        intent.putExtra(Constant.EXTRA_KEY_PRJ_ITEM, prjItem);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prj_edit);

        //获取intent中数据
        prjItem = (PrjItem) getIntent().getSerializableExtra(Constant.EXTRA_KEY_PRJ_ITEM);

        //初始化数据
        mLocationClient = new LocationClient(this);
        LocateHelper.initLocationClient(mLocationClient);
        mLocationClient.registerLocationListener(new BDLocationListener() {
            @Override
            public void onReceiveLocation(BDLocation bdLocation) {
            }
        });
        mLocationClient.start();

        initView();

        //TODO-------------------------测试代码
//        MarkerHelper.mark(mBaiduMap, new LatLng(30.5, 114.4));

//        NavigateHelper.initNavi(this);

//        PictureHelper.showPictureInAlbum(this, "/storage/sdcard0/DCIM/Camera/IMG_20150712_104954.jpg");
//        PictureHelper.showPictureInAlbum(this, "/storage/sdcard0/picture/a21.jpg");
//        PictureHelper.showPictureInAlbum(this, "/sdcard/storage/adcard0/picture/a21.jpg");
    }


    private void initView() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.id_tb);
        //设置Title
        toolbar.setTitle(prjItem.getPrjName());
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        //logo--background
        toolbar.setLogo(R.mipmap.ic_launcher);
        toolbar.setBackgroundDrawable(getResources().getDrawable(R.drawable.action_bar_background));
        //init ActionBar
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mMapView = (MapView) findViewById(R.id.id_baidu_map);
        mBaiduMap = mMapView.getMap();
        mBaiduMap.setMyLocationEnabled(true);
        mBaiduMap.setMyLocationConfigeration(new MyLocationConfiguration(
                MyLocationConfiguration.LocationMode.NORMAL, true, null));

        mBaiduMap.setOnMapTouchListener(new BaiduMap.OnMapTouchListener() {
            @Override
            public void onTouch(MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    isLocated = false;
                    ibLocate.setImageResource(R.drawable.locate2);
                }
            }
        });

        ibLocate = (ImageButton) findViewById(R.id.id_ib_locate);
        ibLocate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isLocated) {
                    //有可能还没有初始化完成---没有前一个点
                    if (mLocationClient.getLastKnownLocation() == null) {
                        return;
                    }
                    locate(mLocationClient.getLastKnownLocation());
                    isLocated = true;
                    ibLocate.setImageResource(R.drawable.locate1);
                }
            }
        });

        //路线
        findViewById(R.id.id_btn_route).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RoutePlanActivity.start(PrjEditActivity.this);
                ToastHelper.show(PrjEditActivity.this, "路线");
            }
        });

        //选址
        findViewById(R.id.id_btn_mark).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //先保存进数据库---然后传递
                MarkerItem markerItem = new MarkerItem(prjItem);
                markerItem.save();
                MarkerActivity.start(PrjEditActivity.this, markerItem, REQUEST_CODE_MARKER_ACTIVITY);
                ToastHelper.show(PrjEditActivity.this, "标记");
            }
        });

        //拍照---拍照前要选中一个Marker
        findViewById(R.id.id_btn_take_photo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO
            }
        });
    }

    private void locate(BDLocation location) {
        //更新我的位置
        MyLocationData locData = new MyLocationData.Builder()
                .accuracy(location.getRadius())
                .latitude(location.getLatitude())
                .longitude(location.getLongitude()).build();
        mBaiduMap.setMyLocationData(locData);
        //更新地图中心点
        LatLng ll = new LatLng(location.getLatitude(),
                location.getLongitude());
        MapHelper.animatToPoint(mBaiduMap, ll);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_activity_prj_edit, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            //加载铁路地图
            case R.id.id_action_load_map:
                //TODO---在MapHelper中实现
                break;
            //测量工具
            case R.id.id_action_measure_tool:
                MeasureActivity.start(PrjEditActivity.this, prjItem);
                break;
            //设置
            case R.id.id_action_setting:

                break;
            //返回
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_CODE_MARKER_ACTIVITY:
                if (resultCode == Constant.RESULT_CODE_OK) {
                    //TODO---重新获取MarkerItem--重新画图
                }
                break;
            case REQUEST_CODE_ROUTE_ACTIVITY:
                break;
            case REQUEST_CODE_PICTURE_ACTIVITY:
                break;
        }
        //TODO----处理各种回调时间
        super.onActivityResult(requestCode, resultCode, data);
    }

    //生命周期***********************************************************
    @Override
    protected void onPause() {
        super.onPause();
        // activity 暂停时同时暂停地图控件
        mMapView.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // activity 恢复时同时恢复地图控件
        mMapView.onResume();
    }

    @Override
    protected void onDestroy() {
        // 退出时销毁定位
        mLocationClient.stop();
        // activity 销毁时同时销毁地图控件
        mMapView.onDestroy();
        super.onDestroy();
    }
}
