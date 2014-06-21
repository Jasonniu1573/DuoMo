package com.miles.ccit.ui;

import com.miles.ccit.duomo.R;
import com.miles.ccit.duomo.R.layout;
import com.miles.ccit.duomo.R.menu;
import com.miles.ccit.util.BaseActivity;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class ContactActivity extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_contact);
		initBaseView("通讯录");
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.contact, menu);
		return true;
	}

}