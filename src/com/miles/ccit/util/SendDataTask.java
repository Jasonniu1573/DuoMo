package com.miles.ccit.util;

import android.content.Intent;
import android.os.AsyncTask;

import com.miles.ccit.net.APICode;
import com.miles.ccit.net.ComposeData;
import com.miles.ccit.net.SocketConnection;

public class SendDataTask extends AsyncTask<String, Void, byte[]>
{

	@Override
	protected byte[] doInBackground(String... parm)
	{
		// TODO Auto-generated method stub
		try
		{
			switch (Byte.parseByte(parm[0]))
			{
			case APICode.SEND_Login:
				SocketConnection.getInstance().readReqMsg(new ComposeData().sendLogin(parm[1], parm[2]));
				break;
			case APICode.SEND_Logout:
				break;
			case APICode.SEND_ChangePwd:
				break;
			case APICode.SEND_ShortTextMsg:
				SocketConnection.getInstance().readReqMsg(new ComposeData().sendShortTextmsg(parm[1], parm[2], parm[3]));
				break;
			case APICode.SEND_ShortVoiceMsg:
				SocketConnection.getInstance().readReqMsg(new ComposeData().sendShortVoicemsg(parm[1], parm[2], parm[3]));
				break;
			case APICode.SEND_VoiceCode:
				SocketConnection.getInstance().readReqMsg(new ComposeData().sendStartVoicecode(parm[1], parm[2]));
				break;
			case APICode.SEND_TalkVoiceCode:
				SocketConnection.getInstance().readReqMsg(new ComposeData().sendVoiceTalk(parm[2]));
				
				break;
			case APICode.SEND_Email:
				SocketConnection.getInstance().readReqMsg(new ComposeData().sendEMail(parm[1], parm[2],parm[3],parm[4],parm[5],parm[6]));
				break;
			case APICode.BACK_RECV_VoiceCode:
				SocketConnection.getInstance().readReqMsg(new ComposeData().sendRecvVoicecode(parm[2]));
				break;
			case APICode.SEND_Broadcast:
				SocketConnection.getInstance().readReqMsg(new ComposeData().sendBroadcast(parm[2]));
				break;
			case APICode.SEND_CodeDirec:
				
				break;
			case APICode.SEND_SpecialVoice:
				SocketConnection.getInstance().readReqMsg(new ComposeData().sendSpecialVoice(parm[2]));
				
				break;
			case APICode.SEND_TalkSpecialVoice:
				SocketConnection.getInstance().readReqMsg(new ComposeData().sendSpecialVoiceTalk(parm[2]));
				
				break;
			case APICode.SEND_WiredVoice:
				break;
			case APICode.SEND_WiredFile:
				break;
			case APICode.SEND_NormalInteraput:
				SocketConnection.getInstance().readReqMsg(new ComposeData().sendNormalInteraput());
				
				break;

			}

		} catch (Exception e)
		{
			e.printStackTrace();
			SocketConnection.getInstance().canleSocket();
			Intent intent = new Intent();
			intent.setAction(AbsBaseActivity.broad_login_Action);
//			intent.putExtra("data", null);
			MyApplication.getAppContext().sendBroadcast(intent);
		}
		return null;
	}

	@Override
	protected void onPostExecute(byte[] result)
	{
		// TODO Auto-generated method stub
		System.out.print("aa");
		super.onPostExecute(result);
	}

}
