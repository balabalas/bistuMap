package cn.edu.bistu.bistumap.Util;

import com.amap.api.maps.model.LatLng;

public class Beatles {
    public static final int POISEARCH = 1000;
    public static final String LOG_TAG = "bistu";
    
    // set satellite view or normal view.
    public static boolean VIEW = false;
    
    // offline map download status
    public static boolean OFFLINE_MAP_DOWNLOADING = false;
    
    // set Get my position or not.
    public static boolean GET_MY_POSITION = false;
    
    public static final int ERROR = 1001;
    public static final int FIRST_LOCATION = 1002;

    public static final int ROUTE_START_SEARCH = 2000;
    public static final int ROUTE_END_SEARCH = 2001;
    public static final int ROUTE_SEARCH_RESULT = 2002;
    public static final int ROUTE_SEARCH_ERROR = 2004;

    public static final int REOCODER_RESULT = 3000;
    public static final int DIALOG_LAYER = 4000;
    public static final int POISEARCH_NEXT = 5000;

    public static final int BUSLINE_RESULT = 6000;
    public static final int BUSLINE_DETAIL_RESULT = 6001;
    public static final int BUSLINE_ERROR_RESULT = 6002;
    //116.388755,39.995114
    public static final LatLng Bistu = new LatLng(40.038951, 116.346443); //北信小营坐标
    public static final LatLng BistuJXQ = new LatLng(39.988707, 116.382025); //北信健翔桥校区
    public static final LatLng BistuQH = new LatLng(40.043468, 116.342753); //北信清河校区
    public static final LatLng BEIJING = new LatLng(39.90403, 116.407525);// 北京市经纬度
    public static final LatLng ZHONGGUANCUN = new LatLng(39.983456, 116.3154950);// 北京市中关村经纬度
    public static final LatLng SHANGHAI = new LatLng(31.239879, 121.499674);// 上海市经纬度
    public static final LatLng CHENGDU = new LatLng(29.339879, 104.384855);// 成都市经纬度
    public static final LatLng XIAN = new LatLng(34.341568, 108.940174);// 西安市经纬度
}
