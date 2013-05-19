package cn.edu.bistu.bistumap;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import cn.edu.bistu.bistumap.Util.Beatles;

import com.amap.api.location.LocationManagerProxy;
import com.amap.api.maps.AMap;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.SupportMapFragment;

public class MapView extends FragmentActivity implements LocationSource {
    
    private AMap amap;
    private LocationManagerProxy locationManager;
    
    @Override
    protected void onCreate(Bundle bundle) {
        // TODO Auto-generated method stub
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
                .getInstance(MapView.this);
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
        
    }
    
    private void exitMap(){
        
    }
    
    @Override
    public void activate(OnLocationChangedListener arg0) {
        
    }

    @Override
    public void deactivate() {
        
    }
    
}
