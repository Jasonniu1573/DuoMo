package com.miles.ccit.duomo;

import com.miles.ccit.util.AbsBaseActivity;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;

public class CreatcodeApplyresActivity extends AbsBaseActivity
{

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_creatcode_applyres);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.creatcode_applyres, menu);
		return true;
	}
	
	@Override
	public void onClick(View v)
	{
		// TODO Auto-generated method stub
		switch(v.getId())
		{
		case R.id.bt_left:
			this.finish();
			break;
		}
	}

	@Override
	public void initView()
	{
		// TODO Auto-generated method stub
		initBaseView("资源申请");
		Btn_Left.setOnClickListener(this);
		Btn_Right.setBackgroundResource(R.drawable.sendmail);
		Btn_Right.setOnClickListener(this);
	}

}