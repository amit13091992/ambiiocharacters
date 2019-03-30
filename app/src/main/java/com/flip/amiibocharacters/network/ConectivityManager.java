package com.flip.amiibocharacters.network;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

/**
 * Created by Amit on 29-Mar-19.
 */
public class ConectivityManager extends BroadcastReceiver {

    public static ConnectivityReceiverListener connectivityReceiverListener;
    Context mContext;
    private String TAG = ConectivityManager.class.getSimpleName();

    public ConectivityManager(Context mContext) {
        this.mContext = mContext;
    }

    public ConectivityManager() {
    }

    public boolean isConnected() {
        ConnectivityManager connectivity = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null)
                for (int i = 0; i < info.length; i++)
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }  //Toast.makeText(mContext, "No internet, Please connect to Internet.", Toast.LENGTH_SHORT).show();

        }
        return false;
    }

    @SuppressLint("UnsafeProtectedBroadcastReceiver")
    @Override
    public void onReceive(Context context, Intent intent) {
        final ConnectivityManager connMgr = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        final android.net.NetworkInfo wifi = connMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        final android.net.NetworkInfo mobile = connMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

        if (wifi.isAvailable() || mobile.isAvailable()) {
            boolean isConnected = (wifi.isConnected() || mobile.isConnected() || mobile.isAvailable());
            if (connectivityReceiverListener != null) {
                connectivityReceiverListener.onNetworkConnectionChanged(isConnected);
            }
            Log.d("Netowk Available ", "Flag No 1");
        }
    }

    public interface ConnectivityReceiverListener {
        void onNetworkConnectionChanged(boolean isConnected);
    }
}
