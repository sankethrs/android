package com.http.manager;


import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

public class HttpConnection extends AsyncTask<String, Integer, ArrayList<Double>> {

	private TaskOnComplete mListener;
	private Context mContext;
	private HttpURLConnection conn;
	
	private static final String LOG_TAG = "BookingPage";

	private static final String PLACES_API_BASE = "https://maps.googleapis.com/maps/api/place";
	private static final String TYPE_AUTOCOMPLETE = "/autocomplete";
	private static final String TYPE_DETAILS = "/details";
	private static final String OUT_JSON = "/json";

	private static final String API_KEY = "AIzaSyDR6j6mGSwQoetn3hR-o3PayA1gq26Z-DE";
	
	public void setListener(TaskOnComplete listener){
		mListener = listener;
	}
	
	public HttpConnection(Context context){
		mContext = context;
	}
	
	@Override
	protected ArrayList<Double> doInBackground(String... params) {
		
		ArrayList<Double> response = autocomplete(params[0]);
		
		return response;
	}

	private ArrayList<Double> autocomplete(String input) {
	    ArrayList<Double> resultList = new ArrayList<>(2);

	    HttpURLConnection conn = null;
	    StringBuilder jsonResults = new StringBuilder();
	    try {
	        StringBuilder sb = new StringBuilder(PLACES_API_BASE + TYPE_DETAILS + OUT_JSON);
	        sb.append("?key=" + API_KEY);
	        sb.append("&reference="+URLEncoder.encode(input, "utf8"));
	        sb.append("&sensor=" + URLEncoder.encode("true", "utf8"));

	        URL url = new URL(sb.toString());
	        conn = (HttpURLConnection) url.openConnection();
	        InputStreamReader in = new InputStreamReader(conn.getInputStream());

	        // Load the results into a StringBuilder
	        int read;
	        char[] buff = new char[1024];
	        while ((read = in.read(buff)) != -1) {
	            jsonResults.append(buff, 0, read);
	        }
	    } catch (MalformedURLException e) {
	        Log.e(LOG_TAG, "Error processing Places API URL", e);
	        return resultList;
	    } catch (IOException e) {
	        Log.e(LOG_TAG, "Error connecting to Places API", e);
	        return resultList;
	    } finally {
	        if (conn != null) {
	            conn.disconnect();
	        }
	    }

	    try {
	        // Create a JSON object hierarchy from the results
	        JSONObject jsonObj = new JSONObject(jsonResults.toString());
	        JSONObject result = jsonObj.getJSONObject("result");
	        
	        JSONObject geometry = result.getJSONObject("geometry");

	        JSONObject location = geometry.getJSONObject("location");
	        
	        resultList.add((double) location.get("lat"));
	        resultList.add((double )location.get("lng"));
	        
	    } catch (JSONException e) {
	        Log.e(LOG_TAG, "Cannot process JSON results", e);
	    }

	    return resultList;
	}
	
	
	@Override
	protected void onPostExecute(ArrayList<Double> result) {
		super.onPostExecute(result);
		
		mListener.onResponseReceived(result);
	}

}
