package com.sample.activities;

import com.hacker.taxiforsure.R;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class BookingHistory extends Activity {

	private ListView listView;
	
	private TextView pageTitle2;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {	
		super.onCreate(savedInstanceState);
	
		setContentView(R.layout.page_bookinghistory);
		
		listView = (ListView) findViewById(R.id.pastBooking);
		
		pageTitle2 = (TextView) findViewById(R.id.pageTitle2);
		pageTitle2.setText("booking History");
		
		String[] places = new String[]{"UB CIty, Bangalore","Chaianti, Koramangala","Embassy Golf Links","The Park, Chennai","Chennai Airport",
				"U.S Consulate Chennai","TaxiforSure, BLR","Om made cafe, BLR","Marine Drive, Mumbai","Marriot, Juhu Road","Howrah Bridge","Chandni Chowk, DL"};
		
		ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.booking_singleitem, R.id.checkBox1, places);
		
		listView.setAdapter(adapter);
		
	}

	
}
