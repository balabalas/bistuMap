package cn.edu.bistu.bistumap;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.os.IBinder;
import android.widget.Toast;

import com.amap.api.maps.MapsInitializer;
import com.amap.api.offlinemap.MOfflineMapStatus;
import com.amap.api.offlinemap.OfflineMapManager;
import com.amap.api.offlinemap.OfflineMapManager.OfflineMapDownloadListener;

public class MapOffline extends Service implements OfflineMapDownloadListener {
    
    private OfflineMapManager mOffline = null;
    private String city = "北京";
    
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        MapsInitializer.sdcardDir = getSdCacheDir(this);
        mOffline = new OfflineMapManager(this, this);
        boolean start = mOffline.downloadByCityName(city);
        if(!start){
            Toast.makeText(MapOffline.this, "开启下载失败，请检查网络是否开启！",
                    Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
    }
    
    private  String getSdCacheDir(Context context) {
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
