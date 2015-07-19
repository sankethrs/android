package com.sample.activities;

import com.hacker.taxiforsure.R;

import android.app.Activity;
import android.os.Bundle;

public class RideInfo extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.page_rideinfo);
		
		getActionBar().setDisplayHomeAsUpEnabled(true);

		
	}

	
}
