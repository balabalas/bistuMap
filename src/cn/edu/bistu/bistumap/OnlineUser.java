package cn.edu.bistu.bistumap;

import org.json.JSONObject;

import io.socket.IOAcknowledge;
import io.socket.IOCallback;
import io.socket.SocketIO;
import io.socket.SocketIOException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cn.edu.bistu.bistumap.Util.Beatles;

import android.util.Log;

public class OnlineUser {
    
    private SocketIO socket;
    
    
    public boolean connect() throws Exception {
        socket  = new SocketIO("http://bistumap.dmdgeeker.com/");
        socket.connect(new IOCallback(){

            @Override
            public void on(String evt, IOAcknowledge ack, Object... args) {
                Log.d(Beatles.LOG_TAG, "Connect the server." + evt);
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
                
            }

            @Override
            public void onMessage(String arg0, IOAcknowledge arg1) {
                
            }

            @Override
            public void onMessage(JSONObject arg0, IOAcknowledge arg1) {
                
            }
            
        });
        
        if(socket != null) return true;
        else return false;
    }
    
    public void disconnect(){
        socket.disconnect();
    }
    
    public void send(){
        socket.send("");
    }
    
}