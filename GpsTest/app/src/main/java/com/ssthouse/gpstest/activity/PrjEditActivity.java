package com.ssthouse.gpstest.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.model.LatLng;
import com.ssthouse.gpstest.Constant;
import com.ssthouse.gpstest.R;
import com.ssthouse.gpstest.model.PrjItem;
import com.ssthouse.gpstest.util.ToastHelper;
import com.ssthouse.gpstest.util.gps.LocateHelper;
import com.ssthouse.gpstest.util.gps.MarkHelper;
import com.ssthouse.gpstest.util.gps.NavigateHelper;

/**
 * 开启时会接收到一个PrjItem---intent中
 */
public class PrjEditActivity extends AppCompatActivity implements View.OnClickListener{
    private static final String TAG = "PrjEditActivity";

    /**
     * 接收到的数据
     */
    private PrjItem prjItem;

    private BaiduMap baiduMap;

    private MapView mapView;

    private LocationClient locationClient;

    private BDLocationListener locationListener = new BDLocationListener() {
        @Override
        public void onReceiveLocation(BDLocation bdLocation) {
            ToastHelper.show(PrjEditActivity.this, bdLocation);
        }
    };

    /**
     * 用于更加方便的开启Activity
     * 后面几个参数可以用来传递-----放入intent 的数据
     * @param context
     */
    public static void start(Context context, PrjItem prjItem){
        if(context == null || prjItem == null){
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

        prjItem = (PrjItem) getIntent().getSerializableExtra(Constant.EXTRA_KEY_PRJ_ITEM);
        initView();

        //TODO-------------------------测试代码

        MarkHelper.mark(baiduMap, new LatLng(30.5, 114.4));

        LocateHelper.addListener(this, locationListener);
        LocateHelper.start(this);

        NavigateHelper.initNavi(this);

//        PictureHelper.showPictureInAlbum(this, "/storage/sdcard0/DCIM/Camera/IMG_20150712_104954.jpg");
//        PictureHelper.showPictureInAlbum(this, "/storage/sdcard0/picture/a21.jpg");
//        PictureHelper.showPictureInAlbum(this, "/sdcard/storage/adcard0/picture/a21.jpg");
    }


    private void initView() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.id_tb);
        setSupportActionBar(toolbar);
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            getSupportActionBar().setLogo(R.mipmap.ic_launcher);
            actionBar.setDisplayShowTitleEnabled(false);
            actionBar.setBackgroundDrawable(getResources().getDrawable(R.drawable.action_bar_background));
            //设置Title
            toolbar.setTitle(prjItem.getPrjName());
            toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        }


        mapView = (MapView)findViewById(R.id.id_baidu_map);
        baiduMap = mapView.getMap();
        initMap(baiduMap);

        //四个按钮的监听事件
        findViewById(R.id.id_btn_route).setOnClickListener(this);
        findViewById(R.id.id_btn_guide).setOnClickListener(this);
        findViewById(R.id.id_btn_mark).setOnClickListener(this);
        findViewById(R.id.id_btn_take_photo).setOnClickListener(this);
    }

    /**
     * 初始化地图
     */
    private void initMap(BaiduMap baiduMap){
        baiduMap.setMyLocationEnabled(true);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.id_btn_route:
                RoutePlanActivity.start(this);
                ToastHelper.show(this, "路线");
                break;
            case R.id.id_btn_guide:

                ToastHelper.show(this, "导航");
                break;
            case R.id.id_btn_mark:

                ToastHelper.show(this, "标记");
                break;
            case R.id.id_btn_take_photo:

                ToastHelper.show(this, "拍照");
                break;
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_activity_prj_edit, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.id_action_setting:

                break;
        }
        return super.onOptionsItemSelected(item);
    }



    @Override
    protected void onPause() {
        super.onPause();
        // activity 暂停时同时暂停地图控件
        mapView.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // activity 恢复时同时恢复地图控件
        mapView.onResume();
    }

    @Override
    protected void onDestroy() {
        // activity 销毁时同时销毁地图控件
        mapView.onDestroy();
        super.onDestroy();
    }
}
