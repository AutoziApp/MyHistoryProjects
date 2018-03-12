package cn.com.mapuni.meshing.base;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.CommonDataKinds.StructuredName;
import android.provider.ContactsContract.Data;
import android.provider.ContactsContract.RawContacts;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.Toast;

import cn.com.mapuni.meshing.base.R;
import cn.com.mapuni.meshing.base.adapter.ListActivityAdapter;
import cn.com.mapuni.meshing.base.adapter.ListActivityAdapter.ViewHolder;
import cn.com.mapuni.meshing.base.business.BaseClass;
import cn.com.mapuni.meshing.base.controls.loading.YutuLoading;
import cn.com.mapuni.meshing.base.interfaces.IList;
import cn.com.mapuni.meshing.base.util.ExceptionManager;
import cn.com.mapuni.meshing.base.util.LogUtil;
import cn.com.mapuni.meshing.base.widget.PagingListView;
import cn.com.mapuni.meshing.base.widget.PagingListView.PageCountChangListener;


/**
 * FileName: ListActivity.java Description: 列表基类
 * 
 * @author 王红娟
 * @Version 1.3.4
 * @Copyright 中科宇图天下科技有限公司 Create at: 2012-12-6 上午10:10:06
 */
public class ListActivity extends BaseActivity 
{
	/** 定义该页的Tag内容 */
	private final String TAG = "ListActivity";
	/** 是否显示左边的图片 */
	public final static String IS_LEFTICON_VISIBLE = "isShowLefticon";
	/** 是否显示第一行数据 */
	public final String IS_TITLE_VISIBLE = "isShowTitle";
	/** 是否显示第二行数据 */
	public final static String IS_CONTENT_VISIBLE = "isShowContent";
	/** 是否显示第二行靠后的数据 */
	public final static String IS_DATE_VISIBLE = "isShowDate";
	/** 是否显示右边的图片 */
	public final static String IS_RIGHTICON_VISIBLE = "isShowRighticon";
	/** 是否显示复选框 */
	public final String IS_CHECKBOX_VISIABLE = "isShowCheckBox";
	/** 定义列表滑动次数 */
	int i = 1;
	/** 判断是否是企业信息列表 */
	public String WRYCX = "企业信息列表";
	/** 判断是否是检查记录信息列表 */
	public String JCJLCX = "检查记录信息列表";
	/** 判断是否是通知公示列表 */
	public String TZGGCX = "通知公示列表";
	/** 判断是否是邮件管理列表 */
	public String EmailCX = "邮件管理列表";
	/** 判断是否是企业列表 */
	public String PaiWuLb = "企业列表";
	/** 所有复选框是否勾选了 */
	public static boolean IS_CHECKED = false;
	/** 具有刷新功能的列表视图 */
	protected PagingListView listView = null;
	/** ListView适配器 */
	protected ListActivityAdapter adapter = null;
	/** 当前业务类向上转型 */
	protected IList businessObj = null;
	/** 保存勾选的复选框 */
	protected LinkedList<String> chkChoice = null;
	/** 列表数据源 */
	protected ArrayList<HashMap<String, Object>> dataList = null;
	/** 用于列表下拉的时候临时存放加载的数据源 */
	protected ArrayList<HashMap<String, Object>> Datalist = null;
	/** 存放列表数据的Bundle */
	protected Bundle bundle = null;
	/** 判断当前是否有同步任务 */
	protected boolean isDataSync = true;
	/** 列表样式集合 */
	protected HashMap<String, Object> style = null;
	/** ListView底部加载数据的进度框 */
	protected LinearLayout mProgressLoadLayout;
	/** 最后加载的一条数据位置，目前没有使用 */
	protected int mLastItem = 0;
	/** 列表最多加载的数量 */
	protected int mCount = 1000;
	/** 在Handler中加载数据 */
	protected final Handler mHandler = new Handler();
	/** 定义一个布局参数 */
	protected final LayoutParams mLayoutParams = new LinearLayout.LayoutParams( LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT );
	/** ListView标题上的查询view，初始为隐藏 */
	public RelativeLayout searchview;
	/** 显示自定义的进度条 */
	protected YutuLoading yutuLoading;
	/** ListView滚动状态 ，0为滚动 */
	protected int scrollState;
	/** ListView可见的条数 */
	protected int visibleItemCount;
	/** 列表的父布局 */
	protected LinearLayout listLayout;
	/** 加载数据等待框的父布局 */
	protected LinearLayout mLoadLayout;
	/** 该参数已过期 */
	protected int listviewIndex;
	/** 获取该业务类列表的过滤条件集合 */
	public HashMap<String, Object> fliterHashMap;
	
	/** type */
	public String type = "SiteRecord";
	/** 在列表中当前选中的Item在列表中的位置 */
	private int detailPosition;

	@Override
	public void onCreate( Bundle savedInstanceState )
	{
		super.onCreate( savedInstanceState );
		setContentView( R.layout.ui_mapuni );
		Intent it = this.getIntent();
		/** 若intent中Bundle为空则从全局变量中取出Bundle */
		bundle = it.getExtras();
		if ( bundle != null )
		{
			/** 业务类向上转型，如果从Base跳转过来，则反射出类型 */
			businessObj = ( IList ) bundle.getSerializable( "BusinessObj" );
		}
		else
		{
			businessObj = ( IList ) bundle.getSerializable( "BusinessObj" );
		}
		/** 初始化的列表设置当前业务类列表滚到次数为1 */
		businessObj.setListScrolltimes( 1 );
		/** 初始化控件 */
		RelativeLayout linearLayout = ( RelativeLayout ) this.findViewById( R.id.parentLayout );
		listView = new PagingListView( this );
		listView.setCacheColorHint( Color.TRANSPARENT );

		yutuLoading = new YutuLoading( this );
		yutuLoading.setLoadMsg( "加载中，请稍等...", "" );

		listLayout = ( LinearLayout ) this.findViewById( R.id.middleLayout );
		LayoutInflater inflater = LayoutInflater.from( this );
		searchview = ( RelativeLayout ) inflater.inflate( R.layout.listview_search, null );
		listLayout.addView( searchview );
		listView.setOnPageCountChangListener(new PageCountChangListener() {
			
			@Override
			public void onAddPage(AbsListView view) {
				LoadListDate();
			}
		});
		listLayout.addView( listView );

		/** 获取列表标题显示 */
		String titleText = businessObj.getListTitleText();
		super.SetBaseStyle( linearLayout, titleText );
		/** 是否具有添加任务的权限 */
		super.isAddTask = bundle.containsKey( "IsAddTask" ) ? true : false;
		/** 设置列表布局，控制BaseActivity中按钮的显示 */
		boolean isShowTitle = bundle.getBoolean( "IsShowTitle" );
		boolean isShowSyncBtn = bundle.containsKey( "IsShowSyncBtn" ) ? bundle.getBoolean( "IsShowSyncBtn" ) : true;
		boolean IsShowQueryBtn = bundle.containsKey( "IsShowQueryBtn" ) ? true : false;
		boolean IsShowQUERYBtn = bundle.containsKey( "IsShowQUERYBtn" ) ? true : false;
		/** 设置快捷菜单是否可见 */
		super.setSynchronizeButtonVisiable( isShowSyncBtn );
		/** 设置标题栏查询按钮是否可见 */
		super.setSearchButtonVisiable( IsShowQueryBtn );
		/** 设置快捷菜单查询 */
		super.setQueryButtonVisiable( IsShowQUERYBtn );
		/** 设置标题布局是否可见 */
		super.setTitleLayoutVisiable( isShowTitle );

		/** 获取当前业务类查询数据的过滤条件 */
		fliterHashMap = businessObj.getFilter();

		/** 根据过滤器判断当前列表显示数据 */
		if ( fliterHashMap != null && fliterHashMap.size() > 0 )
		{
			/** 通过查询条件获取列表数据 */
			dataList = businessObj.getDataList( fliterHashMap );
		}
		else
		{
			/** 获取列表数据 */
			dataList = businessObj.getDataList();
		}
		if ( "同步数据".equals( bundle.getString( "TitleText" ) ) )
		{
			isDataSync = false;
		}
		if ( dataList != null && dataList.size() >= 1 )
		{
			listView.setDivider( getResources().getDrawable( R.drawable.list_divider ) );
		}
		try
		{
			style = businessObj.getStyleList( ListActivity.this );
		}
		catch ( IOException e )
		{
			ExceptionManager.WriteCaughtEXP( e, "ListActivity" );
			LogUtil.e( TAG, "//" + e.getMessage() );
		}

		LoadList( bundle, dataList, style );
	}

	/**
	 * Description: 给ImageView绑定监听器
	 * 
	 * @param lefticon
	 *            图片视图
	 * @param holder
	 *            ViewHolder对象
	 * @param position
	 *            位置 void
	 * @author 王红娟 Create at: 2012-12-6 上午11:07:11
	 */
	public void bindingImgLefticon( ImageView lefticon, ViewHolder holder, int position )
	{
	}

	public class ImageOnClick implements LeftImageOnCliclkListener
	{
		@Override
		public void bindLeftImage( ImageView imgLefticon, ViewHolder holder, int position )
		{
			bindingImgLefticon( imgLefticon, holder, position );
		}
	}

	/**
	 * Description: 填充并展示数据
	 * 
	 * @param bundle
	 *            业务类封装的bundle，传递给当前ListActivity
	 * @param data
	 *            列表数据源
	 * @param style
	 *            列表样式信息 void
	 * @author 王红娟 Create at: 2012-12-6 上午11:09:02
	 */
	public void LoadList( final Bundle bundle, ArrayList<HashMap<String, Object>> data, final HashMap<String, Object> style )
	{

		adapter = new ListActivityAdapter( this, bundle, data, style );
		if ( bundle.containsKey( IS_CHECKBOX_VISIABLE ) )
		{
			chkChoice = adapter.getchkchoice();
		}
		final ArrayList<HashMap<String, Object>> datalist = data;
		if ( data.size() > 17 )
		{
			listView.setAdapter( adapter );
			//listView.setOnScrollListener( this );
		}
		else
		{
			listView.setAdapter( adapter );
			listView.removeFooterView( mLoadLayout );
		}

		/** 并且在这里实现列表项目的单击响应事件 */
		listView.setOnItemClickListener( new OnItemClickListener()
		{

			@Override
			public void onItemClick( AdapterView<?> parent, View view, final int position, long id )
			{
				ViewHolder v = ( ViewHolder ) view.getTag();
				if ( v == null || v.id == null || v.title == null )
				{
					return;
				}
				final String idValue = v.id == null ? "" : v.id.getTag().toString();
				final String content = v.content == null ? "" : v.content.getText().toString();

				/** 显示加载的进度条 */
				yutuLoading.showDialog();

				new Thread( new Runnable()
				{

					@Override
					public void run()
					{
						businessObj.setCurrentID( idValue );
						detailPosition = position;
						listItemClick( idValue, content );

					}
				} ).start();
			}
		} );

		/** 这里实现列表项目的长按响应事件 */
		listView.setOnItemLongClickListener( new OnItemLongClickListener()
		{

			@Override
			public boolean onItemLongClick( AdapterView<?> parent, View view, int position, long id )
			{
				ViewHolder viewHolder = ( ViewHolder ) view.getTag();
				if ( viewHolder == null || viewHolder.id == null || viewHolder.title == null )
				{
					return false;
				}
				String idValue = viewHolder.id == null ? "" : viewHolder.id.getTag().toString();
				businessObj.setCurrentID( idValue );
				/** 区分对专家库的操作 */
				if ( businessObj.getListTitleText().equals( "专家库信息列表" ) )
				{
					LayoutInflater lin = LayoutInflater.from( ListActivity.this );
					View v = lin.inflate( R.layout.popdwindow, null );
					PopupWindow pop = new PopupWindow( v, LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT );
					pop.setBackgroundDrawable( new BitmapDrawable() );
					pop.setOutsideTouchable( true );
					pop.showAsDropDown( view );
					LinearLayout layout1 = ( LinearLayout ) v.findViewById( R.id.layout1 );
					LinearLayout layout2 = ( LinearLayout ) v.findViewById( R.id.layout2 );
					LinearLayout layout3 = ( LinearLayout ) v.findViewById( R.id.layout3 );

					/** 获取电话号 */
					final String num = datalist.get( position ).get( "move_phone" ).toString();
					String[] phone = null;
					String uriString = null;
					final int pos = position;

					if ( !num.equals( "" ) || num.equals( null ) )
					{
						if ( num.contains( "\\" ) )
						{
							String number = num.replace( "\\", "," );
							phone = number.split( "," );
							uriString = phone[0];
						}
						else if ( num.contains( " " ) )
						{
							String number = num.replace( " ", "," );
							phone = number.split( "," );
							uriString = phone[0];
						}
						else
						{
							uriString = num;
						}
					}
					else
					{
						Toast.makeText( ListActivity.this, "号码为空！", 0 ).show();
					}
					/** 拨打电话URI */
					final Uri uri = Uri.parse( "tel:" + uriString );
					/** 发送信息URI */
					final Uri uri2 = Uri.parse( "smsto:" + uriString );
					final String addPhoneNum = uriString;

					layout1.setOnClickListener( new OnClickListener()
					{
						@Override
						public void onClick( View v )
						{
							Intent it = new Intent( Intent.ACTION_CALL, uri );
							startActivity( it );
						}
					} );
					layout2.setOnClickListener( new OnClickListener()
					{
						@Override
						public void onClick( View v )
						{
							Intent it = new Intent( Intent.ACTION_SENDTO, uri2 );
							startActivity( it );
						}
					} );
					layout3.setOnClickListener( new OnClickListener()
					{
						@Override
						public void onClick( View v )
						{
							String name = datalist.get( pos ).get( "name" ).toString();
							String a = addPhoneNum;
							if ( name.equals( "" ) || name.equals( null ) || a.equals( "" ) || a.equals( null ) )
							{
								Toast.makeText( ListActivity.this, "添加信息不全！", 0 ).show();
							}
							else
							{

								ContentValues values = new ContentValues();
								Uri rawContactUri = getContentResolver().insert( RawContacts.CONTENT_URI, values );
								long rawContactId = ContentUris.parseId( rawContactUri );
								values.clear();
								values.put( Data.RAW_CONTACT_ID, rawContactId );
								values.put( Data.MIMETYPE, StructuredName.CONTENT_ITEM_TYPE );
								values.put( StructuredName.GIVEN_NAME, name );
								getContentResolver().insert( android.provider.ContactsContract.Data.CONTENT_URI, values );
								values.clear();
								values.put( Data.RAW_CONTACT_ID, rawContactId );
								values.put( Data.MIMETYPE, Phone.CONTENT_ITEM_TYPE );
								values.put( Phone.NUMBER, a );
								values.put( Phone.TYPE, Phone.TYPE_MOBILE );
								getContentResolver().insert( android.provider.ContactsContract.Data.CONTENT_URI, values );
								Toast.makeText( ListActivity.this, "联系人添加成功", 0 ).show();
							}
						}
					} );
				}

				return true;
			}
		} );
	}

	
	/**
	 * Description: 列表的点击事件业务处理
	 * 
	 * @param idValue
	 *            点击项的Id值
	 * @param content
	 *            点击项的标题名称 void
	 * @author 王红娟 Create at: 2012-12-6 上午11:24:19
	 */
	public void listItemClick( String idValue, String content )
	{
		/** 构造 Intent 传送数据, 展开显示详细信息的 Activity */
		Bundle nextBundle = new Bundle();
		nextBundle = bundle;
		Intent intent = new Intent();
		if ( JCJLCX.equals( businessObj.getListTitleText() ) || EmailCX.equals( businessObj.getListTitleText() ) )
		{
			nextBundle.putString( "rwbh", content );
			nextBundle.putString( "read", "read" );
		}
		else if ( WRYCX.equals( businessObj.getListTitleText() ) )
		{
			intent.putExtra( "qyid", idValue );
			intent.setClassName( ListActivity.this, "com.mapuni.android.enterpriseArchives.EnterpriseArchivesActivitySlide" );
			// intent.setClassName( ListActivity.this,
			// "com.mapuni.android.enterpriseArchives.EnterpriseArchivesActivity"
			// );
		}
		else if ( PaiWuLb.equals( businessObj.getListTitleText() ) )
		{
			intent.putExtra( "qydm", idValue );
			intent.putExtra( "qyid", getQYBM( idValue ) );
			intent.setClassName( "com.mapuni.android.MobileEnforcement", "com.mapuni.android.ui.PWSBActivity" );
		}
		else
		{
			intent.setClassName( "com.mapuni.android.MobileEnforcement", "com.mapuni.android.common.DetailedActivity" );
			nextBundle.putInt( "selectedindex", detailPosition );
		}
		intent.putExtras( nextBundle );
		startActivity( intent );
	}

	/**
	 * Description: 获取企业代码
	 * 
	 * @param id
	 *            企业ID
	 * @return 返回企业代码 String
	 * @author 王红娟 Create at: 2012-12-6 上午11:25:38
	 */
	public String getQYBM( String id )
	{
		BaseClass object = ( BaseClass ) businessObj;
		HashMap<String, String> primaryKey = new HashMap<String, String>();
		primaryKey.put( "key", "guid" );
		primaryKey.put( "keyValue", id );
		HashMap<String, Object> qybmmap = BaseClass.DBHelper.getDetailed( object.GetTableName(), primaryKey );
		String qybm = ( String ) qybmmap.get( "qydm" );
		return qybm;

	}

	/**
	 * Description: 根据项目类型设置左右图标的名称
	 * 
	 * @param itemType
	 *            图标名称
	 * @param itemIconOptions
	 *            图片名称集合
	 * @return 图片名称 String
	 * @author 王红娟 Create at: 2012-12-6 上午11:27:26
	 */
	public String getListItemIcon( String itemType, String itemIconOptions )
	{
		String returnValue = "icon_list_header";
		String[] TypeToLeftIconArray = itemIconOptions.split( "," );
		if ( TypeToLeftIconArray != null )
		{
			for ( int i = 0; i < TypeToLeftIconArray.length; i++ )
			{
				String[] TypeToIcon = TypeToLeftIconArray[i].split( "=" );
				if ( itemType.equals( TypeToIcon[0] ) )
				{
					returnValue = TypeToIcon[1];
					break;
				}
			}
		}
		return returnValue;
	}

	/**
	 * Description: 根据项目类型设置左右图标的资源ID
	 * 
	 * @param itemType
	 *            图片名称
	 * @param itemIconOptions
	 *            图片名称集合
	 * @return 图片文件的资源ID
	 * @author 王红娟 Create at: 2012-12-6 上午11:29:27
	 */
	public int getListItemIconId( String itemType, String itemIconOptions )
	{
		int returnValue = R.drawable.icon_list_header;
		String[] TypeToLeftIconArray = itemIconOptions.split( "," );
		if ( TypeToLeftIconArray != null )
		{
			for ( int i = 0; i < TypeToLeftIconArray.length; i++ )
			{
				String[] TypeToIcon = TypeToLeftIconArray[i].split( "=" );
				if ( itemType.equals( TypeToIcon[0] ) )
				{
					returnValue = this.getResources().getIdentifier( TypeToIcon[1], "drawable", this.getPackageName() );
					break;
				}
			}
		}
		return returnValue;
	}

	/**
	 * Description: 根据图片的名字（不要后缀）来获取BitpMap
	 * 
	 * @param name
	 *            图片名称
	 * @return Bitmap格式的图片 Bitmap
	 * @author 王红娟 Create at: 2012-12-6 上午11:35:12
	 */
	public Bitmap getRes( String name )
	{
		ApplicationInfo appInfo = getApplicationInfo();
		int resID = getResources().getIdentifier( name, "drawable", appInfo.packageName );
		return BitmapFactory.decodeResource( getResources(), resID );
	}

	


	/** 加载列表数据 */
	public void LoadListDate()
	{
		mHandler.postDelayed( new Runnable()
		{
			@Override
			public void run()
			{   businessObj.setListScrolltimes(++i);
				ArrayList<HashMap<String, Object>> newData=null;
				if(fliterHashMap!=null){
					newData=businessObj.getDataList(fliterHashMap);
				}else{
					newData=businessObj.getDataList();
				}
				if(newData.size()<Global.getGlobalInstance().getListNumber()){
					listView.setIsCompleted(true);
					Toast.makeText(ListActivity.this, "全部数据加载完成!", Toast.LENGTH_LONG).show();
					
				}
				dataList.addAll(newData);
			
				adapter.notifyDataSetChanged();
				listView.setFootViewVisibility(false);
			}
		}, 0 );
	}

	@Override
	protected void onStop()
	{
		if ( yutuLoading != null )
			yutuLoading.dismissDialog();
		super.onStop();
	}

	@Override
	protected void onRestart()
	{
		if ( yutuLoading != null )
			yutuLoading.dismissDialog();
		super.onRestart();
	}

	public interface LeftImageOnCliclkListener
	{
		void bindLeftImage( ImageView imgLefticon, ViewHolder holder, int position );
	}
}
