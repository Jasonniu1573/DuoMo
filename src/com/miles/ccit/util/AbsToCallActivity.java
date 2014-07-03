package com.miles.ccit.util;

import java.util.List;
import java.util.Vector;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListView;

import com.miles.ccit.adapter.ContactAdapter;
import com.miles.ccit.database.GetData4DB;
import com.miles.ccit.duomo.CallWaitActivity;
import com.miles.ccit.duomo.R;
import com.miles.ccit.ui.CreatContactActivity;

public abstract class AbsToCallActivity extends AbsBaseActivity
{
	private String strNumber ="";
	private EditText editInputFrom;
	private ListView listview;
	private ContactAdapter adapter;
	private List<BaseMapObject> all;
	public static final int TOCALLVOICE = 0;
	public static final int TOCALLWIRED = 1;
	

	public List<BaseMapObject> getContact(String code)
	{
		if(code.equals(""))
		{
			return new Vector<BaseMapObject>();
		}
		List<BaseMapObject> have = new Vector<BaseMapObject>();
		for(BaseMapObject item : all)
		{
			if(item.get("number").toString().indexOf(code)!=-1)
			{
				have.add(item);
			}
		}
		return have;
	}
	
	public void refreshList()
	{
		adapter = new ContactAdapter(mContext, getContact(strNumber),"name","name","number"); 
		listview.setAdapter(adapter);
		listview.setOnItemClickListener(new OnItemClickListener()
		{

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3)
			{
				// TODO Auto-generated method stub
//				toCall(TOCALLVOICE, getContact(strNumber).get(arg2).get("number").toString());
				insertRecord(getContact(strNumber).get(arg2).get("number").toString());
			}
		});
	}
	
	public void insertNum(String num)
	{
		strNumber+=num;
		editInputFrom.setText(strNumber);
		refreshList();
	}
	
	public boolean isHaveNum(String code)
	{
		for(BaseMapObject item : all)
		{
			if(item.get("number").toString().equals(code))
			{
				return true;
			}
		}
		return false;
	}
	
	public void delNum()
	{
		if(strNumber.length()<=0)
		{
			return;
		}
		strNumber = strNumber.substring(0, strNumber.length()-1);
		editInputFrom.setText(strNumber);
		refreshList();
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
		case R.id.button0:
			insertNum("0");
			break;
		case R.id.button1:
			insertNum("1");
			break;
		case R.id.button2:
			insertNum("2");
			break;
		case R.id.button3:
			insertNum("3");
			break;
		case R.id.button4:
			insertNum("4");
			break;
		case R.id.button5:
			insertNum("5");
			break;
		case R.id.button6:
			insertNum("6");
			break;
		case R.id.button7:
			insertNum("7");
			break;
		case R.id.button8:
			insertNum("8");
			break;
		case R.id.button9:
			insertNum("9");
			break;
		case R.id.buttonx:
			insertNum("*");
			break;
		case R.id.buttony:
			insertNum("#");
			break;
		case R.id.buttoncall:
			insertRecord(strNumber);
			break;
		case R.id.buttonadd:
			inserContact();
			break;
		case R.id.buttondel:
			delNum();
			break;

		}
	}
	
	public void insertRecord(String code)
	{
		if(code.equals(""))
		{
			return;
		}
		BaseMapObject record = new BaseMapObject();
		record.put("id",null);
		record.put("number",code);
		record.put("status","2");
		record.put("creattime", UnixTime.getStrCurrentUnixTime());
		record.put("priority", OverAllData.Priority);
		record.put("acknowledgemen", OverAllData.Acknowledgemen);
		
		record.InsertObj2DB(mContext, "voicecoderecord");
		toCall(TOCALLVOICE,code);
	}
	
	public void toCall(int voiceOrwired,String code)
	{
		if(voiceOrwired==TOCALLVOICE)
		{
			startActivity(new Intent(mContext, CallWaitActivity.class).putExtra("code", code));
		}
	}
	
	public void inserContact()
	{
		if(strNumber.equals(""))
		{
			return;
		}
		else if(isHaveNum(strNumber))
		{
			MyLog.showToast(mContext, "此号码已存在，不能添加...");
		}
		else
		{
			startActivity(new Intent(mContext, CreatContactActivity.class).putExtra("number", strNumber));
		}
	}

	@Override
	public void initView()
	{
		// TODO Auto-generated method stub
		initBaseView("拨号");
		Btn_Left.setOnClickListener(this);
		Btn_Right.setVisibility(View.INVISIBLE);
		editInputFrom = (EditText)findViewById(R.id.edit_form);
		listview = (ListView)findViewById(R.id.listview_content);
		findViewById(R.id.button0).setOnClickListener(this);
		findViewById(R.id.button1).setOnClickListener(this);
		findViewById(R.id.button2).setOnClickListener(this);
		findViewById(R.id.button3).setOnClickListener(this);
		findViewById(R.id.button4).setOnClickListener(this);
		findViewById(R.id.button5).setOnClickListener(this);
		findViewById(R.id.button6).setOnClickListener(this);
		findViewById(R.id.button7).setOnClickListener(this);
		findViewById(R.id.button8).setOnClickListener(this);
		findViewById(R.id.button9).setOnClickListener(this);
		findViewById(R.id.buttonx).setOnClickListener(this);
		findViewById(R.id.buttony).setOnClickListener(this);
		findViewById(R.id.buttoncall).setOnClickListener(this);
		findViewById(R.id.buttonadd).setOnClickListener(this);
		findViewById(R.id.buttondel).setOnClickListener(this);
		
		
		all = GetData4DB.getObjectListData(mContext, "contact", "type", "0");
		
	}
	
}