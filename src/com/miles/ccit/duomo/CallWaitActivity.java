package com.miles.ccit.duomo;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

import com.miles.ccit.database.GetData4DB;
import com.miles.ccit.util.AbsBaseActivity;
import com.miles.ccit.util.AbsToCallActivity;
import com.miles.ccit.util.BaseMapObject;

public class CallWaitActivity extends AbsBaseActivity
{

	private TextView text_Num;
	String code = "";
	MediaPlayer player;
	AudioManager audioManager;
	public static boolean iswait = false;
	private MyBroadcastReciver broad = null;
	int type = AbsToCallActivity.TOCALLVOICE;
	String filepath = null;
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_call_wait);
		code = getIntent().getStringExtra("code");
		type = getIntent().getIntExtra("type", 0);
		filepath = getIntent().getStringExtra("filepath");
		iswait = true;
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(broad_recvvoicecode_Action);
		intentFilter.addAction(broad_wiredvoice_Action);
		broad = new MyBroadcastReciver();
		this.registerReceiver(broad, intentFilter);
		// AssetFileDescriptor afd =.openFd("callbeep.mp3");
		// player = new MediaPlayer();
		try
		{
			player = MediaPlayer.create(mContext, R.raw.callbeep);
			audioManager = (AudioManager) this.getSystemService(mContext.AUDIO_SERVICE);
			audioManager.setMode(AudioManager.MODE_IN_CALL);// 把模式调成听筒放音模式

			player.stop();
			player.prepare();
			player.start();
			player.setOnCompletionListener(new OnCompletionListener()
			{

				@Override
				public void onCompletion(MediaPlayer mp)
				{
					// TODO Auto-generated method stub
					audioManager.setMode(AudioManager.MODE_NORMAL);
					player.release();
					player = null;
					CallWaitActivity.this.finish();
				}
			});
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.call_wait, menu);
		return true;
	}

	@Override
	public void onClick(View v)
	{
		// TODO Auto-generated method stub
		switch (v.getId())
		{
		case R.id.bt_cut:
			this.finish();
			break;
		}
	}
	
	public class MyBroadcastReciver extends BroadcastReceiver
	{
		@Override
		public void onReceive(Context context, Intent intent)
		{
			// TODO Auto-generated method stub
//			hideProgressDlg();
			String action = intent.getAction();

			if (action.equals(broad_recvvoicecode_Action))
			{
				if (intent.getSerializableExtra("data").equals("true"))
				{
					startActivity(new Intent(mContext, VoicecodeConnetActivity.class).putExtra("code", code).putExtra("filepath", filepath).putExtra("type", type));
					CallWaitActivity.this.finish();
				}
				else
				{
					CallWaitActivity.this.finish();
				}
			}
			else if(action.equals(broad_wiredvoice_Action))
			{
				if (intent.getSerializableExtra("data").equals("true"))
				{
					startActivity(new Intent(mContext, VoicecodeConnetActivity.class).putExtra("code", code).putExtra("filepath", filepath).putExtra("type", type));
					CallWaitActivity.this.finish();
				}
				else
				{
					CallWaitActivity.this.finish();
				}
			}
		}

	}
	

	@Override
	protected void onDestroy()
	{
		// TODO Auto-generated method stub
		iswait = false;
		if (player!=null && player.isPlaying())
		{
			player.stop();
			player.release();
			player = null;
		}
		super.onDestroy();
	}

	@Override
	public void initView()
	{
		// TODO Auto-generated method stub
		findViewById(R.id.bt_cut).setOnClickListener(this);
		text_Num = (TextView) findViewById(R.id.text_number);
		if (code == null || code.equals(""))
		{
			this.finish();
		} else
		{
			BaseMapObject map = GetData4DB.getObjectByRowName(mContext, "contact", "number", code);
			if (map != null && map.get("name") != null)
			{
				text_Num.setText(map.get("name").toString() + "\r\n" + code);
			} else
			{
				text_Num.setText(code);
			}
		}

	}

}
