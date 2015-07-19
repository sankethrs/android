package com.http.manager;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
	 
	public class ConnectionDetector {
	     
	    private Context _context;
	     
	    public ConnectionDetector(Context context){
	        this._context = context;
	    }
	 
	    public boolean isConnectingToInternet(){
	        ConnectivityManager connectivity = (ConnectivityManager) _context.getSystemService(Context.CONNECTIVITY_SERVICE);
	          if (connectivity != null) 
	          {
	              NetworkInfo[] info = connectivity.getAllNetworkInfo();
	              if (info != null) 
	                  for (int i = 0; i < info.length; i++) 
	                      if (info[i].getState() == NetworkInfo.State.CONNECTED)
	                      {
	                          return true;
	                      }
	 
	          }
	          return false;
	    }
	    public boolean isConnectedtoInternet()
	    {
	    	ConnectivityManager connManager = (ConnectivityManager) _context.getSystemService(Context.CONNECTIVITY_SERVICE);
	    	NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
	    	NetworkInfo mMobileNetwork = connManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

	    	if (mWifi.isConnected() || mMobileNetwork.isConnected() ) {
	    		return true;
	    	}
	    	else
	    	{
	    		return false;
	    	}
	    }
	}