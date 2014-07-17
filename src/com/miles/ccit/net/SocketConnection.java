package com.miles.ccit.net;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Array;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.locks.ReentrantLock;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Message;

import com.miles.ccit.database.GetData4DB;
import com.miles.ccit.duomo.R;
import com.miles.ccit.duomo.ShortmsgListActivity;
import com.miles.ccit.util.AbsBaseActivity;
import com.miles.ccit.util.AbsMsgRecorderActivity;
import com.miles.ccit.util.BaseMapObject;
import com.miles.ccit.util.ByteUtil;
import com.miles.ccit.util.MyApplication;
import com.miles.ccit.util.MyLog;
import com.miles.ccit.util.OverAllData;
import com.miles.ccit.util.UnixTime;

public class SocketConnection
{
	private volatile Socket socket;

	private boolean isNetworkConnect = false; // 网络是否已连接
	// private static String host;
	// private static int port;
	static InputStream inStream = null;
	static OutputStream outStream = null;

	private static SocketConnection socketConnection = null;
	private static Timer heartTimer = null;

	private static Thread receiveThread = null;
	private final ReentrantLock lock = new ReentrantLock();
	private String result;
	public static boolean isSocketRun = false;

	private SocketConnection()
	{
		// Properties conf = new Properties();
		try
		{
			init(OverAllData.Ipaddress, OverAllData.Port);
		} catch (IOException e)
		{
			// log.fatal("socket初始化异常!",e);
			// throw new RuntimeException("socket初始化异常,请检查配置参数");
			isSocketRun = false;
			e.printStackTrace();
		}
	}
	
	public void canleSocket()
	{
		if(socket!=null)
		{
			try
			{
				socketConnection = null;	
				socket.close();
				socket = null;
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
			
		}
		
	}

	public Socket getsocket()
	{
		return socket;
	}

	/**
	 * 单态模式
	 */
	public static SocketConnection getInstance()
	{
		if (socketConnection == null)
		{
			synchronized (SocketConnection.class)
			{
				if (socketConnection == null)
				{
					socketConnection = new SocketConnection();
					return socketConnection;
				}
			}
		}
		return socketConnection;
	}

	private void init(String host, int port) throws IOException
	{
		InetSocketAddress addr = new InetSocketAddress(host, port);
		socket = new Socket();
		synchronized (this)
		{
			MyLog.SystemOut("【准备与" + addr + "建立连接】");
			socket.connect(addr, OverAllData.networktimeout);
			MyLog.SystemOut("【与" + addr + "连接已建立】");
			inStream = socket.getInputStream();
			outStream = socket.getOutputStream();
			socket.setTcpNoDelay(true); 			// 数据不作缓冲，立即发送
			socket.setSoLinger(true, 0); 			// socket关闭时，立即释放资源
			socket.setKeepAlive(true);
			socket.setTrafficClass(0x04 | 0x10); 	// 高可靠性和最小延迟传输
			isNetworkConnect = true;
			receiveThread = new Thread(new ReceiveWorker());
			receiveThread.start();
			isSocketRun = true;
		}
	}

	/**
	 * 重连与目标IP建立重连
	 */
	public void reConnectToCTCC()
	{
		MyApplication.handle.sendMessage(new Message());
		if (heartTimer != null)
			heartTimer.cancel();
		isNetworkConnect = false;
		if (receiveThread != null && receiveThread.isAlive())
			receiveThread.interrupt();
		canleSocket();
//		new Thread(new Runnable()
//		{
//			public void run()
//			{
//				// MyLog.SystemOut("重新建立与" + host + ":" + port + "的连接");
//				MyApplication.handle.sendMessage(new Message());
//				// 清理工作，中断计时器，中断接收线程，恢复初始变量
//				if (heartTimer != null)
//					heartTimer.cancel();
//				isNetworkConnect = false;
//				if (receiveThread != null && receiveThread.isAlive())
//					receiveThread.interrupt();
//				try
//				{
//					socket.close();
//				} catch (IOException e1)
//				{
//				}
//				synchronized (this)
//				{
//					for (int i = 0; i < 3; i++)// 重连三次
//					{
//						try
//						{
//							Thread.currentThread();
//							Thread.sleep(1000 * 1);
//							init(OverAllData.Ipaddress, OverAllData.Port);
//							// launchHeartcheck();
//							this.notifyAll();
//							break;
//						} catch (Exception e)
//						{
//
//						}
//						i++;
//					}
//				}
//			}
//		}).start();
	}

	/**
	 * 发送命令并接受响应
	 * 
	 * @param requestMsg
	 * @return
	 * @throws SocketTimeoutException
	 * @throws IOException
	 */
	public String readReqMsg(byte[] requestMsg) throws IOException
	{
		if (requestMsg == null)
		{
			return null;
		}
		if (!isNetworkConnect)
		{
			synchronized (this)
			{
				try
				{
					this.wait(1000 * 5); // 等待5秒，如果网络还没有恢复，抛出IO流异常
					if (!isNetworkConnect)
					{
						canleSocket();
						throw new IOException("网络连接中断！");
					}
				} catch (InterruptedException e)
				{

				}
			}
		}
		outStream = socket.getOutputStream();
		outStream.write(requestMsg);
		outStream.flush();

		return "ok";
	}

	/**
	 * 心跳包检测
	 */
	public void launchHeartcheck()
	{
		if (SocketConnection.getInstance().getsocket() == null)
			throw new IllegalStateException("socket is not 	established!");
		heartTimer = new Timer();
		heartTimer.schedule(new TimerTask()
		{
			@SuppressLint("SimpleDateFormat")
			public void run()
			{
				SimpleDateFormat dateformate = new SimpleDateFormat("yyyyMMddHHmmss");
				String msgDateTime = dateformate.format(new Date());
				MyLog.SystemOut("心跳检测包 -> IVR " + msgDateTime);
				int reconnCounter = 1;
				while (true)
				{
					try
					{
						result = SocketConnection.getInstance().readReqMsg(new ComposeData().sendHeartbeat());
					} catch (IOException e)
					{
						MyLog.SystemOut("IO流异常" + e.toString());
						reconnCounter++;
					}
					if (result != null)
					{
						MyLog.SystemOut("心跳响应包 <- IVR " + result);
						reconnCounter = 1;
						break;
					} else
					{
						reconnCounter++;
					}
					if (reconnCounter > 3)
					{
						// 重连次数已达三次，判定网络连接中断，重新建立连接。连接未被建立时不释放锁
						reConnectToCTCC();
						break;
					}
				}
			}

		}, 1000, OverAllData.HeartbeatTime);
	}

	public void finalize()
	{
		if (socket != null)
		{
			try
			{
				socket.close();
			} catch (IOException e)
			{
				e.printStackTrace();
			}
		}
	}

	// 消息接收线程
	private class ReceiveWorker implements Runnable
	{
		public void run()
		{
			byte[] heart = new byte[20480];
			while (!Thread.interrupted())
			{
				try
				{
					byte[] b = new byte[1024]; // 缓冲区20K
					while (inStream.read(heart) == -1)
					{
						// log.warn("读到流未尾，对方已关闭流!");
						reConnectToCTCC();// 读到流未尾，对方已关闭流
						return;
					}

					MyLog.SystemOut("接收到消息：" + heart);

					AnalysisRecvData analyUtil = new AnalysisRecvData();

					switch (heart[4])
					{
					case APICode.BACK_Login:
						analyUtil.analyLogin(heart);
						// intent.setAction(AbsBaseActivity.broad_login_Action);
						// intent.putExtra("data", heart);
						// MyApplication.getAppContext().sendBroadcast(intent);
						break;
					case APICode.BACK_ShortTextMsg:
					case APICode.BACK_ShortVoiceMsg:
						analyUtil.analyBackTextMsg(heart);
						break;
					case APICode.RECV_ShortTextMsg:
						analyUtil.analyTextMsg(heart);
						break;
					case APICode.RECV_ShortVoiceMsg:
						analyUtil.analyVoiceMsg(heart);
						break;
					case APICode.RECV_Email:
						analyUtil.analyEmail(heart);
						break;
					case APICode.BACK_VoiceCode:
						analyUtil.analyBackVoiceCode(heart);
						break;
					case APICode.RECV_VoiceCode:
						analyUtil.analyRecvVoicecode(heart);
						break;
					case APICode.RECV_BroadcastFile:
						analyUtil.analyBroadcast(heart);
						break;
					case APICode.BACK_SpecialVoice:
						analyUtil.analyBackSpecialVoice(heart);
						break;
					}

					try
					{
						lock.lock();
					} finally
					{
						lock.unlock();
					}
				} catch (SocketException e)
				{
					MyLog.SystemOut("服务端关闭socket" + e.toString());
					reConnectToCTCC();
				} catch (IOException e)
				{
					MyLog.SystemOut("接收线程读取响应数据时发生IO流异常" + e.toString());
				}

			}
		}

	}

}
