package com.miles.ccit.ui;

import java.util.HashMap;
import java.util.List;
import java.util.Vector;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.miles.ccit.database.GetData4DB;
import com.miles.ccit.duomo.R;
import com.miles.ccit.util.BaseMapObject;
import com.miles.ccit.util.AbsMsgRecorderActivity;

public class ShortmsgListActivity extends AbsMsgRecorderActivity
{

	private BaseMapObject map = null;
	private List<BaseMapObject> shortList = new Vector<BaseMapObject>();
	private ListView list_Content;
	private MediaPlayer mp;

	@SuppressWarnings("unchecked")
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_shortmsg_list);
		if (getIntent().getSerializableExtra("item") != null)
		{
			map = BaseMapObject
					.HashtoMyself((HashMap<String, Object>) getIntent()
							.getSerializableExtra("item"));
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.shortmsg_list, menu);
		return true;
	}

	@Override
	public void onClick(View v)
	{
		// TODO Auto-generated method stub
		stopMediaplayer();
		switch (v.getId())
		{
		case R.id.bt_left:
			this.finish();
			break;
		case R.id.bt_addcontact:
			break;
		case R.id.bt_swicthvoice:
			switchVoice();
			break;
		case R.id.bt_send:
			sendTextmsg(map.get("number").toString());
			edit_inputMsg.setText("");
			refreshList();
			break;
		}
	}

	private void refreshList()
	{
		shortList = GetData4DB.getObjectListData(mContext, "shortmsg",
				"number", map.get("number").toString());

		if (shortList == null)
		{
			Toast.makeText(mContext, "暂无消息记录...", 0).show();
			return;
		}

		list_Content.setAdapter(new MessageListAdapter(mContext, shortList,
				list_Content));

		list_Content.setSelection(shortList.size() - 1);

	}

	@Override
	public void initView()
	{
		// TODO Auto-generated method stub
		initBaseView(map.get("name") == null ? map.get("number").toString()
				: map.get("name").toString());
		Btn_Left.setText("返回");
		Btn_Right.setVisibility(View.INVISIBLE);
		edit_inputMsg = (EditText) findViewById(R.id.edit_inputmsg);
		Btn_switchVoice = (Button) findViewById(R.id.bt_swicthvoice);
		Btn_Send = (Button) findViewById(R.id.bt_send);
		Btn_Talk = (Button) findViewById(R.id.bt_talk);
		Btn_switchVoice.setOnClickListener(this);
		Btn_Send.setOnClickListener(this);
		list_Content = (ListView) findViewById(R.id.listView_contect);
		Btn_Talk.setOnTouchListener(new OnTouchListener()
		{

			@Override
			public boolean onTouch(View v, MotionEvent event)
			{
				// TODO Auto-generated method stub
				stopMediaplayer();
				switch (event.getAction())
				{
				case MotionEvent.ACTION_DOWN:
					talkTouchDown(map.get("number").toString());

					break;
				case MotionEvent.ACTION_UP:
					talkTouchUp(event);
					refreshList();
					break;
				}
				return false;
			}
		});
		refreshList();
	}

	@Override
	protected void onDestroy()
	{
		// TODO Auto-generated method stub
		stopMediaplayer();
		super.onDestroy();
	}
	
	private void stopMediaplayer()
	{
		if(mp!=null && mp.isPlaying())
		{
			mp.stop();
			mp.release();
			mp=null;
		}
	}

	private class MessageListAdapter extends BaseAdapter
	{

		private List<BaseMapObject> items;
		private Context context;
		private ListView adapterList;

		public MessageListAdapter(Context context, List<BaseMapObject> items,
				ListView adapterList)
		{
			this.context = context;
			this.items = items;
			this.adapterList = adapterList;
		}

		@Override
		public int getCount()
		{
			return items.size();
		}

		@Override
		public Object getItem(int position)
		{
			return items.get(position);
		}

		@Override
		public long getItemId(int position)
		{
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent)
		{
			final BaseMapObject message = items.get(position);

			View talkView = null;// LayoutInflater.from(ChatActivity.this).inflate(R.layout.child,
									// null);

			switch (Integer.parseInt(message.get("sendtype").toString()))
			{
			case 1:
				talkView = LayoutInflater.from(mContext).inflate(
						R.layout.outcometalk, null);

				break;
			case 2:
				talkView = LayoutInflater.from(mContext).inflate(
						R.layout.incometalk, null);

				break;
			case 3:
				break;
			}

			TextView text = (TextView) talkView.findViewById(R.id.textcontent);

			if (message.get("msgcontent") == null
					|| message.get("msgcontent").equals("null"))
			{
				text.setText("无效信息");
			}
			else
			{
				if (message.get("msgtype").toString().equals("0"))
				{
					text.setText(message.get("msgcontent").toString());
				}
				else if (message.get("msgtype").toString().equals("1"))
				{
					text.setText("(((");
					text.setOnClickListener(new OnClickListener()
					{

						@Override
						public void onClick(View v)
						{
							// TODO Auto-generated method stub
							try
							{
								stopMediaplayer();

								mp = new MediaPlayer();
								mp.setDataSource(message.get("msgcontent")
										.toString());
								mp.prepare();
								mp.start();
								
							}
							catch (Exception e)
							{
								e.printStackTrace();
							}
						}
					});
				}
			}
			return talkView;

		}

	}

}