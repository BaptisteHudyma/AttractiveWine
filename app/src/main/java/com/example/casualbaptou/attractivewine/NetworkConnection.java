package com.example.casualbaptou.attractivewine;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;


public class NetworkConnection {

    private static NetworkConnection instance = new NetworkConnection();
    private static Context context;
    private boolean connected = false;
    private ConnectivityManager connectivityManager;

    public static NetworkConnection getInstance(Context ctx) {
        context = ctx.getApplicationContext();
        return instance;
    }

    public boolean isAvailable() {

        try {
            connectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);

            if(connectivityManager == null)
                return false;
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            connected = networkInfo != null && networkInfo.isAvailable() &&
                    networkInfo.isConnected();
            return connected;


        } catch (Exception e) {
            System.out.println("CheckConnectivity Exception: " + e.getMessage());
            Log.v("connectivity", e.toString());
        }
        return connected;
    }

    public  boolean isWifi() {
        if(connectivityManager == null)
            return false;
        NetworkInfo netInfo = connectivityManager.getActiveNetworkInfo();
        String connecName = netInfo.getTypeName();

        return connecName!=null && "WIFI".equalsIgnoreCase( connecName );
    }

    public boolean isMobile() {
        if(connectivityManager == null)
            return false;
        NetworkInfo netInfo = connectivityManager.getActiveNetworkInfo();
        String connecName = netInfo.getTypeName();

        return connecName!=null && "MOBILE".equalsIgnoreCase( connecName );
    }

}