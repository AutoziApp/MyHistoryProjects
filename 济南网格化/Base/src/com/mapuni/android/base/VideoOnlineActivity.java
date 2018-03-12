package com.mapuni.android.base;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.MediaPlayer.PlayM4.Player;
import org.w3c.dom.NodeList;

import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.Toast;

import com.hikvision.netsdk.ExceptionCallBack;
import com.hikvision.netsdk.HCNetSDK;
import com.hikvision.netsdk.NET_DVR_CLIENTINFO;
import com.hikvision.netsdk.NET_DVR_DEVICEINFO_V30;
import com.hikvision.netsdk.RealPlayCallBack;
import com.mapuni.android.base.util.DisplayUitl;
import com.mapuni.android.netprovider.Net;

/**
 * FileName: VideoOnlineActivity.java
 * Description:视频监控
 * @author Liusy
 * @Version 1.3.4
 * @Copyright 中科宇图天下科技有限公司
 * Create at: 2012-12-3 下午3:58:51
 */
public class VideoOnlineActivity extends BaseActivity {
	private LinearLayout 	middleLayout;	
	
	//视频组件
	private Player 			m_oPlayerSDK			= null;
	private HCNetSDK		m_oHCNetSDK				= null;
	private SurfaceView 	m_osurfaceView			= null;
	private NET_DVR_DEVICEINFO_V30 m_oNetDvrDeviceInfoV30 = null;
	
	//播放状态描述
	private int				m_iLogID				= -1;				// return by NET_DVR_Login_v30
	private int 			m_iPlayID				= -1;				// return by NET_DVR_RealPlay_V30
	private int				m_iPlaybackID			= -1;				// return by NET_DVR_PlayBackByTime	
	//private byte			m_byGetFlag				= 1;				// 1-get net cfg, 0-set net cfg 
	private int				m_iPort					= -1;				// play port
	//private	NET_DVR_NETCFG_V30 NetCfg = new NET_DVR_NETCFG_V30();	//netcfg struct
	
	private  final String 	TAG				= "AllVideoOnlineActivity";
	
	private String videoIP = "121.22.34.202";
	private String videoUser = "admin";
	private int videoPort = 8000;
	private String videoPass = "12345";
	private int videoChannel = 33;
	
	private boolean channelFlag = true;
	
	private  List<String> listText = new ArrayList<String>();
	private  List<String> listIP = new ArrayList<String>();
	private  List<String> listChannel = new ArrayList<String>();
	private  List<HashMap<String, Object>> dataList;
	private String getDataPath = "sdcard";
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ui_mapuni);
		// 获取数据
        try {
        	if(getDataPath.equalsIgnoreCase("sdcard")) {
        		Log.i(TAG, "Read Data From Sdacrd");
        		dataList = DisplayUitl.getVideoData();
        	} 
        	if(getDataPath.equalsIgnoreCase("assest") 
        			|| dataList == null || dataList.size() == 0){
        		Log.i(TAG, "Read Data From Assest");
        		InputStream in = getResources().getAssets().open("video_config.xml");
    			dataList = getConfigInfoFromXml(in);
        	}
        } catch (IOException e) {
			Log.i(TAG, "Get Video Xml Error");
		}
		for (HashMap<String, Object> map : dataList) {
			String status = (String)map.get("status");
			if ("1".equals(status)) {
				listText.add((String)map.get("qymc"));
				listIP.add((String)map.get("ip"));
				listChannel.add((String)map.get("channel"));
				Log.i(TAG, (String)map.get("qymc") + (String)map.get("ip"));
			}
		}
		if (!initeSdk()) {
			this.finish();
			return;
		}
		if (!initeActivity()) {
			this.finish();
			return;
		}

    }

   
    
    /** 
     * @fn initeSdk
     * @author huyf
     * @brief SDK init
     * @param NULL [in]
     * @param NULL [out]
     * @return true - success;false - fail
     */
    private boolean initeSdk() {
		// get an instance and init net sdk
		m_oHCNetSDK = new HCNetSDK();
    	if (null == m_oHCNetSDK) {
    		Log.e(TAG, "m_oHCNetSDK new is failed!");
    		return false;
    	}
    	
    	if (!m_oHCNetSDK.NET_DVR_Init()) {
    		Log.e(TAG, "HCNetSDK init is failed!");
    		return false;
    	}
    	
    	// init player
    	m_oPlayerSDK = Player.getInstance();
    	if (m_oPlayerSDK == null)
    	{
    		Log.e(TAG,"PlayCtrl getInstance failed!");
    		return false;
    	}
    	return true;
	}
    

    // GUI init
    private boolean initeActivity() {
    	findViews();
    	return true;
    }
    
    /**
     * Description:获取界面组件
     * 
     * @author Administrator<br>
     * Create at: 2012-12-5 下午05:31:20
     */
    private void findViews() {
    	RelativeLayout rl = (RelativeLayout) findViewById(R.id.parentLayout);
    	SetBaseStyle(rl, "视频监控");
    	//10dp间隔去掉
    	LinearLayout divider = (LinearLayout)findViewById(R.id.ui_mapuni_divider);
    	divider.setVisibility(View.GONE);
    	//中间布局，视频显示区域
    	middleLayout = (LinearLayout)findViewById(R.id.middleLayout);
    	
    	LayoutInflater mInflater = LayoutInflater.from(this);
    	ScrollView videoLayout = (ScrollView) mInflater.inflate(R.layout.online_video, null);
    	LayoutParams layoutParams = new LayoutParams(LayoutParams.FILL_PARENT, 
    			LayoutParams.WRAP_CONTENT);
    	videoLayout.setLayoutParams(layoutParams);
    	middleLayout.addView(videoLayout);
    	
    	m_osurfaceView = (SurfaceView) findViewById(R.id.Sur_Player);    	
    	
    	Spinner spinner = (Spinner) findViewById(R.id.spinner1);
     	ArrayAdapter<String> mAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, listText);
     	//样式
     	
     	mAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
     	spinner.setAdapter(mAdapter);
     	spinner.setOnItemSelectedListener(new OnItemSelectedListener() {

 			@Override
 			public void onItemSelected(AdapterView<?> arg0, View arg1,
 					int index, long arg3) {
		        
 		        //下拉列表选择后操作
 				onSpinnerItemSelectedAction(index);
 			}

 			@Override
 			public void onNothingSelected(AdapterView<?> arg0) {
 				
 			}
 		});
     	
    }
    
    /**
     * Description:下拉列表选择
     * @param id
     * @author Administrator<br>
     * Create at: 2012-12-5 下午05:31:39
     */
	private void onSpinnerItemSelectedAction(int id) {
		//判断网络是否连接
		if (Net.checkNet(this)) {
			// 用户选择，获取对应IP
			Log.i(TAG, "选择" + listText.get(id) + listIP.get(id));

			videoIP = listIP.get(id);
			if ("33".equals(listChannel.get(id))) {
				channelFlag = true;
			} else {
				channelFlag = false;
			}
			videoIP = listIP.get(id);
			if(m_iPlayID < 0) {//没有播放
				if(m_iLogID == 0) {
					logout();					
					onLoginAction();//登录
				} else {
					onLoginAction();//登录
				}
				if(m_iLogID == 0) {
					onPreviewAction();//播放
				} /*else {
					onLoginAction();
					onPreviewAction();
				}*/
				//isPlay = true;
			} else {//正在播放
				stopPlay();
				logout();
				onLoginAction();//登录
				if(m_iLogID == 0) {
					onPreviewAction();//播放
				} /*else {
					onLoginAction();
					onPreviewAction();
				}*/
			}
	    	if(m_iLogID == -1 || m_iPlayID == -1) 
				Toast.makeText(this, "网络错误，请更换监控点！", Toast.LENGTH_SHORT).show();
		} else {
		//	Toast.makeText(this, "检测到网络未连接，无法观看视频！", Toast.LENGTH_SHORT).show();
		Net.OpenWirelessSettings(this);

		}
	}
    
	 
	/**
	 * Description:登录、登出
	 * 
	 * @author Administrator<br>
	 * Create at: 2012-12-5 下午05:31:58
	 */
	private void onLoginAction() {
		Toast.makeText(this, "视频初始化中，请稍后...", Toast.LENGTH_SHORT).show();
		try {
			if(m_iLogID < 0) {
				// login on the device
				m_iLogID = loginDevice();
				if (m_iLogID < 0) {
					//loginAgain();
					Log.e(TAG, "This device logins failed!");
					return;
				}
				// get instance of exception callback and set
				ExceptionCallBack oexceptionCbf = getExceptiongCbf();
				if (oexceptionCbf == null) {
					//loginAgain();
					Log.e(TAG, "ExceptionCallBack object is failed!");
				    return ;
				}
				
				if (!m_oHCNetSDK.NET_DVR_SetExceptionCallBack(oexceptionCbf)) {
					//loginAgain();
					Log.e(TAG, "NET_DVR_SetExceptionCallBack is failed!");
			        return;
			    }
				
				Log.i(TAG, "Login sucess ****************************1***************************");
			} else {
				// whether we have logout
				/*if (!m_oHCNetSDK.NET_DVR_Logout_V30(m_iLogID)) {//退出登录
					Log.e(TAG, " NET_DVR_Logout is failed!");
					return;
				}
				m_iLogID = -1;*/
			}		
		} 
		catch (Exception err) {
			Log.e(TAG, "error: " + err.toString());
		}
	}
	
	/**
	 * 退出登录
	 * @return
	 */
	private boolean logout(){
		if (!m_oHCNetSDK.NET_DVR_Logout_V30(m_iLogID)) {//退出登录
			Log.e(TAG, " NET_DVR_Logout is failed!");
			return false;
		}
		m_iLogID = -1;
		return true;
	}
	
	
	/**
	 * Description:预览、停止
	 * 
	 * @author Administrator<br>
	 * Create at: 2012-12-5 下午05:32:09
	 */
	private void onPreviewAction(){
		try {
			if(m_iLogID < 0) {
				Log.e(TAG,"please login on device first");
				return ;
			}
			if(m_iPlayID < 0) {	
				if(m_iPlaybackID >= 0) {
					Log.i(TAG, "Please stop palyback first");
					return;
				}
				RealPlayCallBack fRealDataCallBack = getRealPlayerCbf();
				if (fRealDataCallBack == null) {
				    Log.e(TAG, "fRealDataCallBack object is failed!");
		            return;
				}
				
				int iFirstChannelNo = m_oNetDvrDeviceInfoV30.byStartChan;// get start channel no
				
				Log.i(TAG, "iFirstChannelNo:" +iFirstChannelNo);
				
				NET_DVR_CLIENTINFO ClientInfo = new NET_DVR_CLIENTINFO();
		        //ClientInfo.lChannel =  iFirstChannelNo; 	// start channel no + preview channel
		       
				if(!channelFlag) {
					ClientInfo.lChannel =  iFirstChannelNo; 	// start channel no + preview channel
				} else {
					ClientInfo.lChannel =  videoChannel;
				}
				
				ClientInfo.lLinkMode = 0;//(1<<31);  			// bit 31 -- 0,main stream;1,sub stream
		        										// bit 0~30 -- link type,0-TCP;1-UDP;2-multicast;3-RTP 
		        ClientInfo.sMultiCastIP = null;
		        
				// net sdk start preview
		        m_iPlayID = m_oHCNetSDK.NET_DVR_RealPlay_V30(m_iLogID, ClientInfo, fRealDataCallBack, true);
		       // m_oHCNetSDK.NET_DVR_RealPlay_V30(arg0, arg1, arg2, arg3)
				if (m_iPlayID < 0) {
					//previewAgain();
					Log.e(TAG, "NET_DVR_RealPlay is failed!Err:" + m_oHCNetSDK.NET_DVR_GetLastError());
				 	return;
				}
				int errorCode=m_oHCNetSDK.NET_DVR_GetLastError();
				Log.i(TAG, "NetSdk Play sucess ***********************3***************************");									
			}
			else {
				//stopPlay();
			}				
		} 
		catch (Exception err) {
			Log.e(TAG, "error: " + err.toString());
		}
	}
	
	
	/** 
     * @fn stopPlay
     * @author huyf
     * @brief stop preview
     * @param NULL [in]
     * @param NULL [out]
     * @return NULL
     */
	private void stopPlay()
	{
		if ( m_iPlayID < 0)
		{
			Log.e(TAG, "m_iPlayID < 0");
			return;
		}
		
		//  net sdk stop preview
		if (!m_oHCNetSDK.NET_DVR_StopRealPlay(m_iPlayID))
		{
			Log.e(TAG, "StopRealPlay is failed!Err:" + m_oHCNetSDK.NET_DVR_GetLastError());
			return;
		}
		
		// player stop play
		if (!m_oPlayerSDK.stop(m_iPort)) 
        {
            Log.e(TAG, "stop is failed!");
            return;
        }	
		
		if(!m_oPlayerSDK.closeStream(m_iPort))
		{
            Log.e(TAG, "closeStream is failed!");
            return;
        }
		if(!m_oPlayerSDK.freePort(m_iPort))
		{
            Log.e(TAG, "freePort is failed!");
            return;
        }
		m_iPort = -1;
		// set id invalid
		m_iPlayID = -1;		
	}
	
	/** 
     * @fn loginDevice
     * @author huyf
     * @brief login on device
     * @param NULL [in]
     * @param NULL [out]
     * @return login ID
     */
	private int loginDevice()
	{
		// get instance
		m_oNetDvrDeviceInfoV30 = new NET_DVR_DEVICEINFO_V30();
		if (null == m_oNetDvrDeviceInfoV30)
		{
			Log.e(TAG, "HKNetDvrDeviceInfoV30 new is failed!");
			return -1;
		}
		// call NET_DVR_Login_v30 to login on, port 8000 as default
		int iLogID = m_oHCNetSDK.NET_DVR_Login_V30(videoIP, videoPort, videoUser, videoPass, m_oNetDvrDeviceInfoV30);
		if (iLogID < 0)
		{
			Log.e(TAG, "NET_DVR_Login is failed!Err:" + m_oHCNetSDK.NET_DVR_GetLastError());
			return -1;
		}
		
		Log.i(TAG, "NET_DVR_Login is Successful!");
		
		return iLogID;
	}
	

	
	/**
     * @fn getExceptiongCbf
     * @author huyf
     * @brief process exception
     * @param NULL [in]
     * @param NULL [out]
     * @return exception instance
     */
	private ExceptionCallBack getExceptiongCbf() {
	    ExceptionCallBack oExceptionCbf = new ExceptionCallBack()
        {
            public void fExceptionCallBack(int iType, int iUserID, int iHandle)
            {
            	;// you can add process here
            }
        };
        return oExceptionCbf;
	}
	
	/** 
     * @fn getRealPlayerCbf
     * @author huyf
     * @brief get realplay callback instance
     * @param NULL [in]
     * @param NULL [out]
     * @return callback instance
     */
	private RealPlayCallBack getRealPlayerCbf() {
	    RealPlayCallBack cbf = new RealPlayCallBack() {
             public void fRealDataCallBack(int iRealHandle, int iDataType, byte[] pDataBuffer, int iDataSize) {
            	// player channel 1
            	VideoOnlineActivity.this.processRealData(1, iDataType, pDataBuffer, iDataSize, Player.STREAM_REALTIME); 
             }
        };
        return cbf;
	}
	
	
	/** 
     * @fn processRealData
     * @author huyf
     * @brief process real data
     * @param iPlayViewNo - player channel [in]
     * @param iDataType	  - data type [in]
     * @param pDataBuffer - data buffer [in]
     * @param iDataSize   - data size [in]
     * @param iStreamMode - stream mode [in]
     * @param NULL [out]
     * @return NULL
     */
	private void processRealData(int iPlayViewNo, int iDataType, byte[] pDataBuffer, int iDataSize, int iStreamMode) {
		int i = 0;
		Log.i(TAG, "iPlayViewNo:" + iPlayViewNo + "iDataType:" + iDataType + "iDataSize:" + iDataSize);
		try {
			switch (iDataType) {
			case HCNetSDK.NET_DVR_SYSHEAD:
				if (m_iPort >= 0) {
					break;
				}
				m_iPort = m_oPlayerSDK.getPort();
				if (m_iPort == -1) {
					Log.e(TAG, "getPort is failed!");
					break;
				}
				if (iDataSize > 0) {
					if (!m_oPlayerSDK.setStreamOpenMode(m_iPort, iStreamMode)) {
						Log.e(TAG, "setStreamOpenMode failed");
						break;
					}
					if (!m_oPlayerSDK.setSecretKey(m_iPort, 1,
							"ge_security_3477".getBytes(), 128)) {
						Log.e(TAG, "setSecretKey failed");
						break;
					}
					if (!m_oPlayerSDK.openStream(m_iPort, pDataBuffer,
							iDataSize, 2 * 1024 * 1024)) // open stream
					{
						Log.e(TAG, "openStream failed");
						break;
					}

					if (!m_oPlayerSDK.play(m_iPort, m_osurfaceView.getHolder())) {
						Log.e(TAG, "play failed");
						break;
					}
				}
				break;
			case HCNetSDK.NET_DVR_STREAMDATA:
			case HCNetSDK.NET_DVR_STD_AUDIODATA:
			case HCNetSDK.NET_DVR_STD_VIDEODATA:
				if (iDataSize > 0 && m_iPort != -1) {
					for (i = 0; i < 400; i++) {
						if (m_oPlayerSDK.inputData(m_iPort, pDataBuffer,
								iDataSize)) {
							break;
						}
						Thread.sleep(10);
					}
					if (i == 400) {
						Log.e(TAG, "inputData failed");
					}
				}
				break;
			default:
				break;
			}
		} catch (Exception e) {
			Log.e(TAG, "processRealData Exception!err:" + e.toString());
		}
	}
		
	/** 
     * @fn Cleanup
     * @author huyf
     * @brief cleanup
     * @param NULL [in]
     * @param NULL [out]
     * @return NULL
     */
    public void Cleanup() {
        // release player resource
    	
        m_oPlayerSDK.freePort(m_iPort);
        
        // release net SDK resource
	    m_oHCNetSDK.NET_DVR_Cleanup();
    }
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK) {
			Log.i(TAG, "返回键");
			if(m_iPlayID > 0) {
				stopPlay();//停止播放
				logout();//登出操作
			} else if (m_iLogID < 0 && m_iLogID == 0 ){//登陆成功，播放失败
				logout();//登出
			} 
			Cleanup();//释放资源
			VideoOnlineActivity.this.finish();
		}
		return super.onKeyDown(keyCode, event);
	}
	
	@Override
	protected void onPause() {
		Log.i(TAG, "onPause");
		if(m_iPlayID > 0) {
			stopPlay();
			logout();
		} else if (m_iLogID < 0 && m_iLogID == 0 ){//登陆成功，播放失败
			logout();
		} 
		Cleanup();//释放资源
		super.onPause();
	}
	
	/**
	 * 解析视频配置文件，获取视频信息
	 * fileName 表示文件路径
	 * @param fileName
	 * @return List(条件，序号，表)
	 */
	public List<HashMap<String, Object>> getConfigInfoFromXml(InputStream in) {
		List<HashMap<String, Object>> dataList = new ArrayList<HashMap<String, Object>>();
		HashMap<String, Object> item = null;
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			//InputStream in = this.getResources().getAssets().open(fileName);
			org.w3c.dom.Document doc = builder.parse(in);
			//获得节点列表
			NodeList nl = doc.getElementsByTagName("item");
			for(int i = 0; i < nl.getLength(); i++){
				item = new HashMap<String, Object>();
				String qymc = doc.getElementsByTagName("qymc").item(i).getFirstChild().getNodeValue();
				String ip = doc.getElementsByTagName("ip").item(i).getFirstChild().getNodeValue();
				String user = doc.getElementsByTagName("user").item(i).getFirstChild().getNodeValue();
				String pass = doc.getElementsByTagName("pass").item(i).getFirstChild().getNodeValue();
				String port = doc.getElementsByTagName("port").item(i).getFirstChild().getNodeValue();
				String status = doc.getElementsByTagName("status").item(i).getFirstChild().getNodeValue();
				String channel = doc.getElementsByTagName("channel").item(i).getFirstChild().getNodeValue();
				
				item.put("qymc", qymc);
				item.put("ip", ip);
				item.put("user", user);
				item.put("pass", pass);
				item.put("port", port);
				item.put("status", status);
				item.put("channel", channel);

				dataList.add(item);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return dataList;
	}
}
