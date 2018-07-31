package com.huntersdevs.www.gmessngr.app;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class NetworkConnection {

    private static int[] NETWORK_TYPES = { ConnectivityManager.TYPE_WIFI,
            ConnectivityManager.TYPE_ETHERNET, ConnectivityManager.TYPE_MOBILE };

    public boolean isConnected(Context mContext) {
        ConnectivityManager connManager =
                (ConnectivityManager) mContext.getSystemService( Context.CONNECTIVITY_SERVICE );
        for( int networkType : NETWORK_TYPES ) {
            NetworkInfo info = null;
            if (connManager != null) {
                info = connManager.getNetworkInfo( networkType );
            }
            if( info != null && info.isConnectedOrConnecting() ) {
                return true;
            }
        }
        return false;
    }
}
