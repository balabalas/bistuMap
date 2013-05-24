package cn.edu.bistu.bistumap;

import org.json.JSONObject;

import io.socket.IOAcknowledge;
import io.socket.IOCallback;
import io.socket.SocketIO;
import io.socket.SocketIOException;

//import org.json.JSONArray;
//import org.json.JSONException;
//import org.json.JSONObject;

import cn.edu.bistu.bistumap.Util.Beatles;

import android.util.Log;

public class OnlineUser {
    
    private SocketIO socket = null;
    
    public boolean connect() throws Exception {
        socket  = new SocketIO("http://dmdgeeker.com:7887/");
        socket.connect(new IOCallback(){

            @Override
            public void on(String evt, IOAcknowledge ack, Object... args) {
                Log.d(Beatles.LOG_TAG, "Connect the server. " + evt);
                if(evt == "callback"){
                    Log.d(Beatles.LOG_TAG, "CB:" + args.length);
                }
                else {
                    Log.d(Beatles.LOG_TAG, "CB:" + args.length);
                    Log.d(Beatles.LOG_TAG, "CB is not callback.");
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
            public void onError(SocketIOException arg0) {
                Log.d(Beatles.LOG_TAG, "Connection error!.");
            }

            @Override
            public void onMessage(String arg, IOAcknowledge arg1) {
                Log.d(Beatles.LOG_TAG, "Connection String." + arg);
            }

            @Override
            public void onMessage(JSONObject arg, IOAcknowledge arg1) {
                Log.d(Beatles.LOG_TAG, "Connection jsonObject.");
            }
            
        });
        
        if(socket != null) return true;
        else return false;
    }
    
    public void disconnect(){
        Log.d(Beatles.LOG_TAG, "Connection disconnect.");
        socket.disconnect();
    }
    
    public void receive(){
        
    }
    /**
     * @param obj (String) the JSON string.
     *      like {"latitude":"45.12163", "longitude":"122.2135"}
     * **/
    public void send(String obj){
        socket.emit("update", obj);
    }
    
}