package cn.edu.bistu.bistumap;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Environment;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import com.amap.api.maps.MapsInitializer;
import com.amap.api.offlinemap.DownCity;
import com.amap.api.offlinemap.MOfflineMapStatus;
import com.amap.api.offlinemap.OfflineMapManager;
import com.amap.api.offlinemap.OfflineMapManager.OfflineMapDownloadListener;

public class MapOffline extends Service implements OfflineMapDownloadListener {
    
    private OfflineMapManager mOffline = null;
    private String city = "北京";
    private SharedPreferences sharedPref;
    private DownCity downCity = null;
    private String cityVersion = null;
    private String old_version = null;
    private int cityState = 0;
    private int old_state = 0;
    private long citySize = 0;
    private long old_size = 0;
    
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        MapsInitializer.sdcardDir = getSdCacheDir(this);
        mOffline = new OfflineMapManager(this, this);
        sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        boolean start = false;
        downCity = mOffline.getItemByCityName(city);
        
        boolean shouldDown = judgeVersion();
        
        if(shouldDown){
            start = mOffline.downloadByCityName(city);
            if(!start){
                Toast.makeText(MapOffline.this, "开启下载失败，请检查网络是否开启！",
                        Toast.LENGTH_SHORT).show();
                Log.d("bistu", "offline map downloads error!");
            }
            else {
                Log.d("bistu", "offline map downloading!");
            }
        }
        else {
            Toast.makeText(MapOffline.this, "离线地图已下载！",
                    Toast.LENGTH_SHORT).show();
            Log.d("bistu", "offline map has downloaded!");
        }
    }
    
    private boolean judgeVersion(){
        
        boolean result = false;
        
        try{
            cityVersion = downCity.getVersion();
            cityState = downCity.getmState();
            citySize = downCity.getSize();
        } catch (Error e){
            Log.d("bistu", "DownCity get properties error!");
        }
        
        try {
            old_version = sharedPref.getString("cityVersion", null);
            old_state = sharedPref.getInt("cityState", 0);
            old_size = sharedPref.getLong("citySize", 0);
        } catch(Error e){
            Log.d("bistu", "SharedPref get properties error!");
        }
        
        if(cityVersion == old_version && cityState == old_state && citySize == old_size){
            result = false;
        }
        else {
            result = true;
        }
        
        return result;
    }
    
    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
    }
    
    private String getSdCacheDir(Context context) {
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            java.io.File fExternalStorageDirectory = Environment
                    .getExternalStorageDirectory();
            java.io.File autonaviDir = new java.io.File(
                    fExternalStorageDirectory, "bistu");
            boolean result = false;
            if (!autonaviDir.exists()) {
                result = autonaviDir.mkdir();
            }
            java.io.File minimapDir = new java.io.File(autonaviDir,
                    "xMap");
            if (!minimapDir.exists()) {
                result = minimapDir.mkdir();
            }
            
            if(result){
                Log.d("bistu", "offline map dir done!");
            }
            
            return minimapDir.toString() + "/";
        } else {
            return null;
        }
    }

    @Override
    public void onDownload(int status, int completeCode) {
        switch (status) {
        case MOfflineMapStatus.LOADING:
            Toast.makeText(this, "正在下载," + "已完成：" + completeCode + "%",
                    Toast.LENGTH_SHORT).show();
            break;
        case MOfflineMapStatus.PAUSE:
            Toast.makeText(this, "暂停",
                    Toast.LENGTH_SHORT).show();
            break;
        case MOfflineMapStatus.STOP:
            Toast.makeText(this, "停止",
                    Toast.LENGTH_SHORT).show();
            break;
        case MOfflineMapStatus.ERROR:
            Toast.makeText(this, "下载出错",
                    Toast.LENGTH_SHORT).show();
            break;
        default:
            break;
        }
    }
    
}
