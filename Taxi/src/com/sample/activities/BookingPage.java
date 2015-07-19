package com.sample.activities;

import java.util.ArrayList;

import org.w3c.dom.Document;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.hacker.taxiforsure.R;
import com.http.manager.HttpConnection;
import com.http.manager.TaskOnComplete;
import com.sample.adapter.GMapV2Direction;
import com.sample.adapter.GPSTracker;
import com.sample.adapter.PlacesAutoCompleteAdapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.transition.Visibility;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class BookingPage extends Activity implements TaskOnComplete, OnItemClickListener{

	private GoogleMap mMap;
	static final LatLng SUPERMARKET = new LatLng(12.933099, 77.612183);
	private LatLng currentLocation;
	private LatLng destinationLocation;
	private HttpConnection connection;
	private ProgressDialog dialog = null;
	private Context context;
	Button drop;
	private AlertDialog alertDialog;
	private GMapV2Direction md;
	private Document doc;
	
	private TextView distance;
	private TextView time;
	
	GPSTracker gpsTracker;
	
	private EditText currentPlace;
	private EditText destinationPlace;
	
	private Button now;
	private Button later;
	
	private Button hackerback;
	private Button sedan;
	private Button suv;
	
	private NotificationManager mNotificationManager;
	
	private TextView pageTitle2;
	private RelativeLayout divider;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.page_booking);
		
		drop = (Button) findViewById(R.id.button2);
		currentPlace = (EditText) findViewById(R.id.editText1);
		destinationPlace = (EditText) findViewById(R.id.editText2);
		
		pageTitle2 = (TextView) findViewById(R.id.pageTitle2);
		pageTitle2.setVisibility(View.GONE);
		divider = (RelativeLayout) findViewById(R.id.divider);
		divider.setVisibility(View.GONE);
		
		now = (Button) findViewById(R.id.button3);
		later = (Button) findViewById(R.id.button4);
		
		hackerback = (Button) findViewById(R.id.button5);
		sedan = (Button) findViewById(R.id.button6);
		suv = (Button) findViewById(R.id.button7);
		
		context = this;
		
		dialog = new ProgressDialog(this);
		
		distance = (TextView) findViewById(R.id.distance);
		time = (TextView) findViewById(R.id.time);
		
		distance.setVisibility(View.GONE);
		time.setVisibility(View.GONE);
		
		mMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
		
		md = new GMapV2Direction();
		
		gpsTracker = new GPSTracker(context);
		
		if(gpsTracker.canGetLocation()){
        	currentLocation	 = new LatLng(gpsTracker.getLatitude(), gpsTracker.getLongitude());
        	mMap.addMarker(new MarkerOptions()
            .position(currentLocation)
            .title("Current Location")
            );
        	mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(currentLocation.latitude, currentLocation.longitude), 12.0f));
        }else{
        	currentLocation = new LatLng(12.933099, 77.612183);
        	 mMap.addMarker(new MarkerOptions()
             .position(currentLocation)
             .title("Dummy Location")
             );
        	 mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(currentLocation.latitude, currentLocation.longitude), 12.0f));
        }
		        
        /*dialog.setTitle("Taxi for sure");
        dialog.setMessage("Getting list");
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();*/
		
        /*connection = new HttpConnection(this);
        connection.setListener(this);
        connection.execute("c");*/
        
       /* AutoCompleteTextView autoCompView = (AutoCompleteTextView) findViewById(R.id.autocomplete);
        autoCompView.setAdapter(new PlacesAutoCompleteAdapter(this, R.layout.list_item));*/

       drop.setOnClickListener(new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			
			LayoutInflater layout = LayoutInflater.from(context);
			
			View promptView = layout.inflate(R.layout.prompt_place, null);
			
			AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
			alertDialogBuilder.setView(promptView);
			
			AutoCompleteTextView autoCompView = (AutoCompleteTextView) promptView.findViewById(R.id.autocomplete);
	        autoCompView.setAdapter(new PlacesAutoCompleteAdapter(context, R.layout.list_item));
	        autoCompView.setOnItemClickListener((OnItemClickListener) BookingPage.this);
	        autoCompView.requestFocus();
	        
	        alertDialogBuilder.setCancelable(false)
	        .setPositiveButton("Drop", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {															
					
				}
			})
	        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					
				}
			});
	        
	        alertDialog = alertDialogBuilder.create();
	        alertDialog.show();
				
			}
		});
       
       hackerback.setOnClickListener(new OnClickListener() {
		
			@Override
			public void onClick(View v) {
				hackerback.setBackgroundResource(R.drawable.rounded_white);
				sedan.setBackgroundResource(R.drawable.rounded_blue);
				suv.setBackgroundResource(R.drawable.rounded_blue);
			}
       });
       
       sedan.setOnClickListener(new OnClickListener() {
		
			@Override
			public void onClick(View v) {
				hackerback.setBackgroundResource(R.drawable.rounded_blue);
				sedan.setBackgroundResource(R.drawable.rounded_white);
				suv.setBackgroundResource(R.drawable.rounded_blue);
				
			}
		});
       
       suv.setOnClickListener(new OnClickListener() {
		
			@Override
			public void onClick(View v) {
				hackerback.setBackgroundResource(R.drawable.rounded_blue);
				sedan.setBackgroundResource(R.drawable.rounded_blue);
				suv.setBackgroundResource(R.drawable.rounded_white);
				
			}
		});
	}


	@Override
	public void onResponseReceived(ArrayList<Double> response) {
				
		Log.d("BookingPage", "Autocomplete completed");
		for(double a : response){
	        	System.out.println(a);
	    }
		
        dialog.dismiss();
        destinationLocation = new LatLng(response.get(0), response.get(1));
        GetDirections gd = new GetDirections();
        gd.execute(currentLocation.latitude,currentLocation.longitude,response.get(0),response.get(1));
	}


	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {

		
		alertDialog.dismiss();
		
		String reference = PlacesAutoCompleteAdapter.reference.get(position);
		
		//Log.d("BookingPage","This is reference "+ reference);
		
		destinationPlace.setHint(PlacesAutoCompleteAdapter.resultList.get(position));
		
		dialog.setTitle("Taxi for sure");
        dialog.setMessage("Calculating Route");
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
		
        connection = new HttpConnection(context);
        connection.setListener(BookingPage.this);
        connection.execute(reference);
	}

	public class GetDirections extends AsyncTask<Double, Integer, String>{

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			
			dialog.setTitle("Taxi for sure");
	        dialog.setMessage("Finding shortest path");
	        dialog.setCancelable(false);
	        dialog.setCanceledOnTouchOutside(false);
	        dialog.show();
		}

		@Override
		protected String doInBackground(Double... params) {
			
		
				
			doc = md.getDocument(new LatLng(params[0], params[1]), new LatLng(params[2], params[3]),
	                GMapV2Direction.MODE_DRIVING);
				
			
			return "success";
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			
			ArrayList<LatLng> directionPoint = md.getDirection(doc);
	        PolylineOptions rectLine = new PolylineOptions().width(3).color(
	                Color.RED);
	        
	        for (int i = 0; i < directionPoint.size(); i++) {
	            rectLine.add(directionPoint.get(i));
	        }
	        
	        mMap.clear();
	        
	        if(gpsTracker.canGetLocation()){
	        	currentLocation	 = new LatLng(gpsTracker.getLatitude(), gpsTracker.getLongitude());
	        	mMap.addMarker(new MarkerOptions()
	            .position(currentLocation)
	            .title("Current Location")
	            );
	        	mMap.addMarker(new MarkerOptions()
	            .position(destinationLocation)
	            .title("Destination Location")
	            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
	            );
	        	mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(currentLocation.latitude, currentLocation.longitude), 12.0f));
	        }else{
	        	 currentLocation = new LatLng(12.933099, 77.612183);
	        	 mMap.addMarker(new MarkerOptions()
	             .position(currentLocation)
	             .title("Dummy Location")
	             );
	        	 mMap.addMarker(new MarkerOptions()
	             .position(destinationLocation)
	             .title("Destination Location")
	             .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
	             );
	        	 mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(currentLocation.latitude, currentLocation.longitude), 12.0f));
	        }
	        
	        Polyline polylin = mMap.addPolyline(rectLine);
	        
	        Log.d("BookingPage", "Distance Text "+md.getDistanceText(doc));
	        Log.d("BookingPage", "Distance Value "+md.getDistanceValue(doc));
	        Log.d("BookingPage", "Duration Text "+md.getDurationText(doc));
	        Log.d("BookingPage", "Duration Value "+md.getDurationValue(doc));
	        Log.d("BookingPage", "End Address "+md.getEndAddress(doc));
	        Log.d("BookingPage", "Start Address "+md.getStartAddress(doc));
	        dialog.dismiss();
	        
	        distance.setVisibility(View.VISIBLE);
			time.setVisibility(View.VISIBLE);
			
			int dist_int = md.getDistanceValue(doc);
			double dist_double = (double) dist_int/1000;
	        distance.setText("("+String.valueOf(dist_double)+" km)");
	        time.setText("? min");
	        
	       /* currentPlace.setHint(md.getStartAddress(doc));
	        destinationPlace.setHint(md.getEndAddress(doc));*/
	        
	        now.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					
					LayoutInflater layoutInflater = LayoutInflater.from(context);
					
					View promptMobile = layoutInflater.inflate(R.layout.prompt_mobile, null);
					
					final EditText mobileNumber = (EditText) promptMobile.findViewById(R.id.editText1);
					
					AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
					alertDialogBuilder.setView(promptMobile);
					
					alertDialogBuilder.setCancelable(false)
			        .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {															
							mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
							PendingIntent contentIntent = PendingIntent.getActivity(context, 0, new Intent(context, BookingPage.class), 0);
							String mobileNo = mobileNumber.getText().toString();
							NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context)
							.setSmallIcon(R.drawable.ic_launcher)
							.setContentTitle("Taxi For Sure")
							.setStyle(new NotificationCompat.BigTextStyle()
							.bigText("Your cab is booked. A SMS will be sent to "+mobileNo+" mobile number soon with the driver details and timing."))
							.setContentText("Your cab is booked. A SMS will be sent to "+mobileNo+" mobile number soon with the driver details and timing.");
							
							
							mBuilder.setContentIntent(contentIntent);
							mNotificationManager.notify(1, mBuilder.build());
							
						}
					})
			        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							
						}
					});
			        
			        alertDialog = alertDialogBuilder.create();
			        alertDialog.show();
					
				}
			});
	        
	        later.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					LayoutInflater layoutInflater = LayoutInflater.from(context);
					
					View promptMobile = layoutInflater.inflate(R.layout.prompt_mobile, null);
					
					AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
					alertDialogBuilder.setView(promptMobile);
					
					final EditText mobileNumber = (EditText) promptMobile.findViewById(R.id.editText1); 
					
					alertDialogBuilder.setCancelable(false)
			        .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {															
							mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
							PendingIntent contentIntent = PendingIntent.getActivity(context, 0, new Intent(context, BookingPage.class), 0);
							String mobileNo = mobileNumber.getText().toString();
							NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context)
							.setSmallIcon(R.drawable.ic_launcher)
							.setContentTitle("Taxi For Sure")
							.setStyle(new NotificationCompat.BigTextStyle()
							.bigText("Your cab is booked. A SMS will be sent to "+mobileNo+" mobile number soon with the driver details and timing."))
							.setContentText("Your cab is booked. A SMS will be sent to "+mobileNo+" mobile number soon with the driver details and timing.");
							
							mBuilder.setContentIntent(contentIntent);
							mNotificationManager.notify(1, mBuilder.build());
						}
					})
			        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							
						}
					});
			        
			        alertDialog = alertDialogBuilder.create();
			        alertDialog.show();
					
				}
			});
	        
	        currentPlace.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					LayoutInflater layoutInflater = LayoutInflater.from(context);
					
					View promptLocation = layoutInflater.inflate(R.layout.prompt_location, null);
					
					TextView location = (TextView) promptLocation.findViewById(R.id.location); 
					TextView title = (TextView) promptLocation.findViewById(R.id.title);
					
					title.setText("Current");
					location.setText(md.getStartAddress(doc));
					location.setTextColor(Color.RED);
					
					AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
					alertDialogBuilder.setView(promptLocation);
					
					alertDialogBuilder.setCancelable(false)
			        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {															
							
						}
					})
			        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							
						}
					});
			        
			        alertDialog = alertDialogBuilder.create();
			        alertDialog.show();
					
				}
			});
			
	        
	        destinationPlace.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					LayoutInflater layoutInflater = LayoutInflater.from(context);
					
					View promptLocation = layoutInflater.inflate(R.layout.prompt_location, null);
					
					TextView location = (TextView) promptLocation.findViewById(R.id.location); 
					TextView title = (TextView) promptLocation.findViewById(R.id.title);
					
					title.setText("Destination");
					location.setText(md.getEndAddress(doc));
					location.setTextColor(Color.BLUE);
					
					AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
					alertDialogBuilder.setView(promptLocation);
					
					alertDialogBuilder.setCancelable(false)
			        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {															
							
						}
					})
			        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							
						}
					});
			        
			        alertDialog = alertDialogBuilder.create();
			        alertDialog.show();
					
				}
			});

		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		
		MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.activity_actions, menu);

		return super.onCreateOptionsMenu(menu);
	}


	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		
		switch(item.getItemId()){
		case R.id.details : Intent intent = new Intent(context,RideInfo.class);
							startActivity(intent);
							return true;
      
		case R.id.history : Intent intent1 = new Intent(context,BookingHistory.class);
							startActivity(intent1);
							return true;
							
							
        default: return super.onOptionsItemSelected(item);							
		}
		 
	}

	

}
