package com.miles.ccit.adapter;

import java.util.List;
import java.util.Map;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;

import com.miles.ccit.duomo.R;
import com.miles.ccit.util.BaseMapObject;

public class ContactAdapter extends BaseAdapter
{

	private List<BaseMapObject> contactlist;
	private Context mContext;
	public ContactAdapter(Context contex,List<BaseMapObject> list)
	{
		this.mContext = contex;
		this.contactlist = list;
	}
	
	@Override
	public int getCount()
	{
		// TODO Auto-generated method stub
		return contactlist.size();
	}

	@Override
	public Object getItem(int position)
	{
		// TODO Auto-generated method stub
		return contactlist.get(position);
	}

	@Override
	public long getItemId(int position)
	{
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		// TODO Auto-generated method stub
		final BaseMapObject map = contactlist.get(position);
		View view = null;

		LayoutInflater mInflater = LayoutInflater.from(mContext);
		view = mInflater.inflate(R.layout.listitem_contact, null);
		((TextView)  view.findViewById(R.id.text_name)).setText(map.get("name").toString());
		((TextView) view.findViewById(R.id.text_number)).setText(map.get("number").toString());
		CheckBox checkDel = (CheckBox)view.findViewById(R.id.check_del);
		if(map.get("exp1")!=null&&map.get("exp1").toString().equals("0"))
		{
			checkDel.setVisibility(View.VISIBLE);
		}
		else
		{
			checkDel.setVisibility(View.INVISIBLE);
		}
		checkDel.setOnCheckedChangeListener(new OnCheckedChangeListener()
		{
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
			{
				// TODO Auto-generated method stub
				if(isChecked)
				{
					map.put("exp2", "1");
				}
				else
				{
					map.put("exp2", null);
				}
			}
		});
		return view;
	}

}