package com.alex.atour.models;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import static android.content.Context.CONNECTIVITY_SERVICE;

public class NetworkStateChangeReceiver extends BroadcastReceiver {
    private NetworkStateChangeListener listener;

    public interface NetworkStateChangeListener{
        void onNetworkStateChanged(boolean isConnected);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (listener != null){
            listener.onNetworkStateChanged(isConnectedToInternet(context));
        }
    }

    public void attach(NetworkStateChangeListener listener){ this.listener = listener; }
    public void detach(){ listener = null; }

    private boolean isConnectedToInternet(Context context) {
        try {
            if (context != null) {
                ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(CONNECTIVITY_SERVICE);
                NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
                return networkInfo != null && networkInfo.isConnectedOrConnecting();
            }
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("TAG", e.getMessage());
            return false;
        }
    }
}