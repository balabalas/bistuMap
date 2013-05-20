package cn.edu.bistu.bistumap;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import cn.edu.bistu.bistumap.Util.Beatles;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.location.LocationManagerProxy;
import com.amap.api.location.LocationProviderProxy;
import com.amap.api.maps.AMap;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.SupportMapFragment;

public class MyMapView extends FragmentActivity implements LocationSource, AMapLocationListener {
    
    private AMap amap;
    private LocationManagerProxy locationManager;
    private OnLocationChangedListener pChangeListener;
    
    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.mapview);
        init();
    }
    
    private void init(){
        if(null == amap){
            amap = ((SupportMapFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.theMap)).getMap();
            
            if(null != amap){
                setUpMap();
            }
            
        }
    }
    
    private void setUpMap(){
        locationManager = LocationManagerProxy
                .getInstance(MyMapView.this);
        amap.setLocationSource(this);
        amap.setMyLocationEnabled(true);
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.mapview, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.onlineUser: getOnlineUsers(); break;
            case R.id.myPosition: getMyPosiotn(); break;
            case R.id.satellite: satelliteView(); break;
            case R.id.clearTrace: clearTrace(); break;
            case R.id.offlineMap: offlineMap(); break;
            case R.id.exit: exitMap(); break;
            default:
                return super.onOptionsItemSelected(item);
        }
        
        return true;
    }
    
    private void getOnlineUsers(){
        
    }
    
    private void getMyPosiotn(){
        
        if(!Beatles.GET_MY_POSITION){
            Beatles.GET_MY_POSITION = true;
            
            locationManager.requestLocationUpdates(
                LocationProviderProxy.AMapNetwork, 5000, 10, this);
        }
        else {
            Beatles.GET_MY_POSITION = false;
            deactivate();
        }
        
    }
    
    private void satelliteView(){
        if(!Beatles.VIEW){
            Beatles.VIEW = true;
            amap.setMapType(AMap.MAP_TYPE_SATELLITE);
        }
        else {
            Beatles.VIEW = false;
            amap.setMapType(AMap.MAP_TYPE_NORMAL);
        }
    }
    
    private void clearTrace(){
        
    }
    
    private void offlineMap(){
        Intent map = new Intent(MyMapView.this, MapOffline.class);
        if(!Beatles.OFFLINE_MAP_DOWNLOADING){
            Beatles.OFFLINE_MAP_DOWNLOADING = true;
            startService(map);
        }
        else {
            Beatles.OFFLINE_MAP_DOWNLOADING = false;
            stopService(map);
        }
    }
    
    private void exitMap(){
        Intent it = new Intent(this, MainActivity.class);
        it.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        it.putExtra("exit", true);
        startActivity(it);
        finish();
    }
    
    @Override
    public void activate(OnLocationChangedListener listener) {
        pChangeListener = listener;
        if (locationManager == null) {
            locationManager = LocationManagerProxy.getInstance(this);
        }
        locationManager.requestLocationUpdates(
                LocationProviderProxy.AMapNetwork, 5000, 10, this);
    }

    @Override
    public void deactivate() {
        pChangeListener = null;
        if (locationManager != null) {
            locationManager.removeUpdates(this);
            locationManager.destory();
        }
        locationManager = null;
    }

    @Override
    public void onLocationChanged(Location location) {
        
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        
    }

    @Override
    public void onProviderEnabled(String provider) {
        
    }

    @Override
    public void onProviderDisabled(String provider) {
        
    }

    @Override
    public void onLocationChanged(AMapLocation alocation) {
        if (pChangeListener != null) {
            pChangeListener.onLocationChanged(alocation);
        }
        
        if(alocation != null && Beatles.GET_MY_POSITION){
            Double geoLat = alocation.getLatitude();
            Double geoLng = alocation.getLongitude();
            Log.d("bistu", "Latitude: " + geoLat + "  Longitude: " + geoLng);
        }
    }
    
}
