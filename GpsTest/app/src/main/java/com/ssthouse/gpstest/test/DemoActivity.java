package com.ssthouse.gpstest.test;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMap.OnMapLoadedCallback;
import com.baidu.mapapi.map.BaiduMap.OnMapStatusChangeListener;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.poi.PoiSearch;
import com.ssthouse.gpstest.R;

public class DemoActivity extends Activity {

	private final String POIWord = "��ҵ";
	
	
	private MapView mMapView;
	private BaiduMap mBaiduMap;
	private PoiSearch mPoiSearch;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		mMapView = (MapView) findViewById(R.id.mapview);
		mBaiduMap = mMapView.getMap();
		LatLng cenpt = new LatLng(39.912897, 116.402724); 
        //�����ͼ״̬
        MapStatus mMapStatus = new MapStatus.Builder()
        .target(cenpt)
        .zoom(14)
        .build();
        MapStatusUpdate mMapStatusUpdate = MapStatusUpdateFactory.newMapStatus(mMapStatus);
        //�ı��ͼ״̬
        mBaiduMap.setMapStatus(mMapStatusUpdate);
        InitLocation();
        completeLis();
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		mPoiSearch.destroy();
	}

	private void InitLocation(){
		LocationClientOption option = new LocationClientOption();
		option.setLocationMode(LocationMode.Hight_Accuracy);//���ö�λģʽ
		option.setCoorType("gcj02");//���صĶ�λ����ǰٶȾ�γ�ȣ�Ĭ��ֵgcj02
		option.setScanSpan(5000);//���÷���λ����ļ��ʱ��Ϊ5000ms
//		option.setIsNeedAddress(checkGeoLocation.isChecked());
	}
	
	/**
	 * ʵ��ʵλ�ص�����
	 */
	public class MyLocationListener implements BDLocationListener {

		@Override
		public void onReceiveLocation(BDLocation location) {
			//Receive Location 
			StringBuffer sb = new StringBuffer(256);
			sb.append("time : ");
			sb.append(location.getTime());
			sb.append("\nerror code : ");
			sb.append(location.getLocType());
			sb.append("\nlatitude : ");
			sb.append(location.getLatitude());
			sb.append("\nlontitude : ");
			sb.append(location.getLongitude());
			sb.append("\nradius : ");
			sb.append(location.getRadius());
			if (location.getLocType() == BDLocation.TypeGpsLocation){
				sb.append("\nspeed : ");
				sb.append(location.getSpeed());
				sb.append("\nsatellite : ");
				sb.append(location.getSatelliteNumber());
				sb.append("\ndirection : ");
				sb.append("\naddr : ");
				sb.append(location.getAddrStr());
				sb.append(location.getDirection());
			} else if (location.getLocType() == BDLocation.TypeNetWorkLocation){
				sb.append("\naddr : ");
				sb.append(location.getAddrStr());
				//��Ӫ����Ϣ
				sb.append("\noperationers : ");
				sb.append(location.getOperators());
			}
			Log.i("BaiduLocationApiDem", sb.toString());
			LatLng cenpt = new LatLng(location.getLatitude(), location.getLongitude()); 
	        //�����ͼ״̬
	        MapStatus mMapStatus = new MapStatus.Builder()
	        .target(cenpt)
	        .zoom(14)
	        .build();
	        MapStatusUpdate mMapStatusUpdate = MapStatusUpdateFactory.newMapStatus(mMapStatus);
	        //�ı��ͼ״̬
	        mBaiduMap.setMapStatus(mMapStatusUpdate);
		}

	}
	
	public void completeLis(){
		mBaiduMap.setOnMapLoadedCallback(new OnMapLoadedCallback() {
			
			@Override
			public void onMapLoaded() {
				// TODO Auto-generated method stub
				Toast.makeText(DemoActivity.this, "�������", Toast.LENGTH_SHORT).show();
			}
		});
		mBaiduMap.setOnMapStatusChangeListener(new OnMapStatusChangeListener() {
			
			@Override
			public void onMapStatusChangeStart(MapStatus arg0) {
				// TODO Auto-generated method stub
			}
			
			@Override
			public void onMapStatusChangeFinish(MapStatus arg0) {
				// TODO Auto-generated method stub
				LatLng latlng = mBaiduMap.getMapStatus().target;
				System.out.println("*****************lat = " + latlng.latitude);
				System.out.println("*****************lng = " + latlng.longitude);
			}
			
			@Override
			public void onMapStatusChange(MapStatus arg0) {
				// TODO Auto-generated method stub
				
			}
		});
	}
	
}
