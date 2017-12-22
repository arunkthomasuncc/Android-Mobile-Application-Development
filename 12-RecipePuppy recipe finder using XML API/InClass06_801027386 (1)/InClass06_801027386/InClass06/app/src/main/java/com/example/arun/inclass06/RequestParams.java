package com.example.arun.inclass06;

import android.util.Log;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;

/**
 * Created by Arun on 10/2/2017.
 */

public class RequestParams {


    String method, baseURL;

    HashMap<String ,String> params = new HashMap<String, String>();

    public RequestParams(String method, String url) {
        this.method = method;
        this.baseURL = url;
    }

    public  void addParam(String key, String value){
        params.put(key,value);
    }

    public String getEncodedParams(){

        StringBuilder stringBuilder = new StringBuilder();
        for (String key: params.keySet()){
            try {
                String value = URLEncoder.encode(params.get(key), "UTF-8");
                if (stringBuilder.length() > 0){
                    stringBuilder.append("&");
                }
                stringBuilder.append(key+"="+value);
            } catch (IOException e){
                e.printStackTrace();
            }
        }
        return stringBuilder.toString();
    }

    public String getEncodedURL(){
        Log.d("demoFound",this.baseURL + "?" + getEncodedParams());
        return this.baseURL + "?" + getEncodedParams();
    }

    public HttpURLConnection setupConnection(){
        if (method.equals("GET")){
            try {
                URL url = new URL(getEncodedURL());
                HttpURLConnection connection = (HttpURLConnection)url.openConnection();
                connection.setRequestMethod("GET");
                return  connection;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        return null;
    }
}

