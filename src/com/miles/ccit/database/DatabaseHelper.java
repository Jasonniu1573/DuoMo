package com.miles.ccit.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

//DatabaseHelper作为一个访问SQLite的助手类，提供两个方面的功能，
//第一，getReadableDatabase(),getWritableDatabase()可以获得SQLiteDatabse对象，通过该对象可以对数据库进行操作
//第二，提供了onCreate()和onUpgrade()两个回调函数，允许我们在创建和升级数据库时，进行自己的操作

public class DatabaseHelper extends SQLiteOpenHelper
{

	private static final int VERSION = 1; 	// 数据库版本/2013/10/22
	private static final String DATABASE_NAME = "DuoMo.db"; // 数据库名称

	// 在SQLiteOepnHelper的子类当中，必须有该构造函数
	public DatabaseHelper(Context context, String name, CursorFactory factory, int version)
	{
		// 必须通过super调用父类当中的构造函数
		super(context, name, factory, version);
		// TODO Auto-generated constructor stub
	}

	public DatabaseHelper(Context context)
	{
		this(context, DATABASE_NAME, VERSION);
	}

	public DatabaseHelper(Context context, String name, int version)
	{
		this(context, DATABASE_NAME, null, version);
	}

	// 该函数是在第一次创建数据库的时候执行,实际上是在第一次得到SQLiteDatabse对象的时候，才会调用这个方法
	@Override
	public void onCreate(SQLiteDatabase db)
	{
		// TODO Auto-generated method stub
		CtreteTables(db);
		initData(db);
		Log.v("Jason", "Create Database");
//		CtreteTables(db);
//		initData(db);
	}

	private void CtreteTables(SQLiteDatabase db)
	{
		// execSQL函数用于执行SQL语句,创建表
		db.execSQL("CREATE TABLE shortmsg(id INTEGER PRIMARY KEY AUTOINCREMENTNOT NULL,number TEXT NOT NULL,sendtype INTEGER NOT NULL,status INTEGER NOT NULL,msgtype INTEGER NOT NULL,msgcontent TEXT NOT NULL,creattime TEXT NOT NULL,priority INTEGER NOT NULL,acknowledgemen INTEGER NOT NULL,exp1 TEXT,exp2 TEXT");
		db.execSQL("CREATE TABLE emailmsg(id PRIMARY KEY AUTOINCREMENTNOT INTEGER NOT NULL,sendtype INTEGER NOT NULL,number TEXT NOT NULL,subject TEXT NOT NULL,mailcontent TEXT NOT NULL,haveattachments INTEGER NOT NULL,attachmentsname TEXT,attachmentspath TEXT,creattime TEXT NOT NULL,priority INTEGER NOT NULL,acknowledgemen INTEGER NOT NULL,exp1 TEXT,exp2 TEXT)");
		
		
		//		db.execSQL("create table rooms(roomid INTEGER PRIMARY KEY AUTOINCREMENT,myroomid varchar(10) NOT NULL,roomname varchar(20) NOT NULL,roomaddr varchar(20) NOT NULL,floorid INTEGER NOT NULL,roominfo varchar(10),info1 varchar(10),info2 varchar(10),info3 varchar(10))");
//		db.execSQL("create table equips(equipid INTEGER PRIMARY KEY AUTOINCREMENT,myequipid varchar(10) NOT NULL,equipname varchar(20) NOT NULL,equipaddr varchar(20) NOT NULL,equipstyle varchar(20) NOT NULL,equipstatus varchar(20) NOT NULL,roomid varchar(10) NOT NULL,equipinfo varchar(10),info1 varchar(10),info2 varchar(10),info3 varchar(10))");
//		db.execSQL("create table scenes(sceneid INTEGER PRIMARY KEY AUTOINCREMENT,scenename varchar(20) NOT NULL,sceneinfo varchar(20),info1 varchar(10),info2 varchar(10),info3 varchar(10))");
//		db.execSQL("create table voices(voiceid INTEGER PRIMARY KEY AUTOINCREMENT,voicename varchar(20) NOT NULL,sceneid INTEGER NOT NULL,voiceinfo varchar(20),info1 varchar(10),info2 varchar(10),info3 varchar(10))");
//		
//		//新增表-场景设备表
//		db.execSQL("create table scene_equips(relationid INTEGER PRIMARY KEY AUTOINCREMENT,sceneid INTEGER NOT NULL,equipid INTEGER NOT NULL,scenename varchar(20) NOT NULL,equipname varchar(20) NOT NULL,advancevalue varchar(20) NOT NULL,info1 varchar(10),info2 varchar(10),info3 varchar(10))");
//		db.execSQL("create table cameras(camera INTEGER PRIMARY KEY AUTOINCREMENT,cameraname varchar(20) NOT NULL,cameraip varchar(20) NOT NULL,cameraport varchar(20) NOT NULL,camerauser varchar(20) NOT NULL,camerapwd varchar(20) NOT NULL,info1 varchar(10),info2 varchar(10),info3 varchar(10))");
		
	}

	// 初始化密码表中数据，
	private void initData(SQLiteDatabase db)
	{
//		String Id = null;
//		ContentValues values = new ContentValues();
//		values.put("floorid", Id);
//		values.put("myfloorname", "0");
//		values.put("floorname", "一楼");
//		db.insert("floors", null, values);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
	{
		// TODO Auto-generated method stub
		if(newVersion > oldVersion)
		{
			//版本升级后的操作
		}
	}
}