package cn.edu.bistu.bistumap;

import io.socket.IOAcknowledge;
import io.socket.IOCallback;
import io.socket.SocketIO;
import io.socket.SocketIOException;

import java.net.MalformedURLException;
import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.graphics.Color;
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
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.Polyline;
import com.amap.api.maps.model.PolylineOptions;

public class MyMapView extends FragmentActivity implements LocationSource, AMapLocationListener {
    
    private AMap amap;
    private LocationManagerProxy locationManager;
    private OnLocationChangedListener pChangeListener;
    private UiSettings uiSetting;
    private LatLng lastLatIng;
    private PolylineOptions polylineOptions;
    private Polyline polyline = null;
    private Thread userThread = null;
    private SocketIO socket = null;
    private boolean SOCKET_STATUS = false;
    
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
                polylineOptions = new PolylineOptions();
                polylineOptions.color(Color.BLUE).width(5);
                setUpMap();
            }
            
        }
    }
    
    private void setUpMap(){
        locationManager = LocationManagerProxy
                .getInstance(MyMapView.this);
        uiSetting = amap.getUiSettings();
        amap.setLocationSource(this);
        amap.setMyLocationEnabled(true);
        uiSetting.setMyLocationButtonEnabled(false);
        uiSetting.setZoomControlsEnabled(false);
        amap.addMarker(new MarkerOptions().position(Beatles.Bistu).title("Main ")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
        amap.addMarker(new MarkerOptions().position(Beatles.BistuJXQ).title("JXQ ")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
        amap.addMarker(new MarkerOptions().position(Beatles.BistuQH).title("QH ")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
        polylineOptions.add(new LatLng(39.9588, 116.3181), Beatles.BEIJING,
                        new LatLng(39.9588, 116.5181)).color(Color.RED).width(5);
        polyline = amap.addPolyline(polylineOptions);
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
        
        if(userThread == null){
            userThread = new Thread(new Runnable(){
                @Override
                public void run() {
                    try {
                        SOCKET_STATUS = onlineUser();
                        Log.d(Beatles.LOG_TAG, "L114 connect server: " + SOCKET_STATUS);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            userThread.start();
        }
        else {
            Log.d(Beatles.LOG_TAG, "L128 thread has exist: " + SOCKET_STATUS);
        }
        
    }
    
    private boolean onlineUser(){
        
        boolean connected = false;
        
        try {
            socket  = new SocketIO("http://dmdgeeker.com:7887/");
            socket.connect(new IOCallback(){
                @Override
                public void on(String evt, IOAcknowledge ack, Object... args) {
                    Log.d(Beatles.LOG_TAG, "Server evt:" + evt + "  params: " + args.length);
                    if(evt.equals("callback")){
                        Log.d(Beatles.LOG_TAG, "CB callback: " + args[0].toString());
                    }
                    else if(evt.equals("id")){
                        Log.d(Beatles.LOG_TAG, "CB evt is id." + args[0].toString());
                    }
                }

                @Override
                public void onConnect() {
                    Log.d(Beatles.LOG_TAG, "Connect the server.");
                }

                @Override
                public void onDisconnect() {
                    Log.d(Beatles.LOG_TAG, "Connection terminated.");
                }

                @Override
                public void onError(SocketIOException err) {
                    Log.d(Beatles.LOG_TAG, "Connection error!.");
                }

                @Override
                public void onMessage(String arg, IOAcknowledge ack) {
                    Log.d(Beatles.LOG_TAG, "Connection String." + arg);
                }

                @Override
                public void onMessage(JSONObject arg0, IOAcknowledge arg1) {
                    Log.d(Beatles.LOG_TAG, "Connection jsonObject.");
                }
                
            });
            
            
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        
        if(socket != null){
            connected = true;
        }
        else{
            connected = false;
        }
        
        return connected;
    }
    
    private void getMyPosiotn(){
        
//        if(!Beatles.GET_MY_POSITION){
//            Beatles.GET_MY_POSITION = true;
            
            locationManager.requestLocationUpdates(
                LocationProviderProxy.AMapNetwork, 5000, 10, this);
//        }
//        else {
//            Beatles.GET_MY_POSITION = false;
//            deactivate();
//        }
        
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
        if(polyline != null){
            polyline.remove();
        }
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
        Log.d(Beatles.LOG_TAG, "Location changed.");
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        Log.d(Beatles.LOG_TAG, "Status changed.");
    }

    @Override
    public void onProviderEnabled(String provider) {
        Log.d(Beatles.LOG_TAG, "Provider is enable.");
    }

    @Override
    public void onProviderDisabled(String provider) {
        Log.d(Beatles.LOG_TAG, "Provider is not enable.");
    }

    @Override
    public void onLocationChanged(AMapLocation alocation) {
        
        LatLng tLatLng = null;
        boolean drawLine = false;
        
        if (pChangeListener != null) {
            pChangeListener.onLocationChanged(alocation);
        }
        
        /**
         * can draw line here. but can't draw blow if section.
         * **/
//        amap.addPolyline((new PolylineOptions())
//                .add(new LatLng(39.9588, 116.3181), Beatles.BEIJING,
//                        new LatLng(39.9588, 116.5181)).color(Color.RED)
//                .width(5));
        
        if(alocation != null){
            
            Double geoLat = alocation.getLatitude();
            Double geoLng = alocation.getLongitude();
            tLatLng = new LatLng(geoLat, geoLng);
            drawLine = compareLatLngs(lastLatIng, tLatLng);
            lastLatIng = tLatLng;
            
            if(drawLine){
                if(polyline != null){
                    polyline.remove();
                }
                polylineOptions.add(lastLatIng, tLatLng).color(Color.BLUE).width(5);
                polyline = amap.addPolyline(polylineOptions);
                
                if(SOCKET_STATUS){
                    JSONObject obj = new JSONObject();
                    try {
                        obj.put("latitude", geoLat);
                        obj.put("longitude", geoLng);
                        
                        if(socket != null){
                            socket.emit("update", obj.toString());
                        }
                        else {
                            Log.d(Beatles.LOG_TAG, "socket is null.");
                        }
                        
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                
                ArrayList<LatLng> li = (ArrayList<LatLng>)polylineOptions.getPoints();
                
                Log.d(Beatles.LOG_TAG, "Position changed!!! and " + li.size());
            }
            else {
//                Log.d(Beatles.LOG_TAG, "Position doesn't change!!!");
            }
            
//            Log.d(Beatles.LOG_TAG, "Latitude: " + geoLat + "  Longitude: " + geoLng);
        }
    }
    
    private boolean compareLatLngs(LatLng old, LatLng fresh){
        
        boolean result = false;
        
        if(old != null && fresh != null){
            if(old.latitude != fresh.latitude || old.longitude != fresh.longitude){
                result = true;
            }
        }
        else{
            old = fresh;
            result = false;
        }
        
        return result;
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(SOCKET_STATUS){
//            online.disconnect();
            socket = null;
            SOCKET_STATUS = false;
        }
    }
    
    
    
}
