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
 * FileName: ListActivity.java Description: �б����
 * 
 * @author �����
 * @Version 1.3.4
 * @Copyright �п���ͼ���¿Ƽ����޹�˾ Create at: 2012-12-6 ����10:10:06
 */
public class ListActivity extends BaseActivity 
{
	/** �����ҳ��Tag���� */
	private final String TAG = "ListActivity";
	/** �Ƿ���ʾ��ߵ�ͼƬ */
	public final static String IS_LEFTICON_VISIBLE = "isShowLefticon";
	/** �Ƿ���ʾ��һ������ */
	public final String IS_TITLE_VISIBLE = "isShowTitle";
	/** �Ƿ���ʾ�ڶ������� */
	public final static String IS_CONTENT_VISIBLE = "isShowContent";
	/** �Ƿ���ʾ�ڶ��п�������� */
	public final static String IS_DATE_VISIBLE = "isShowDate";
	/** �Ƿ���ʾ�ұߵ�ͼƬ */
	public final static String IS_RIGHTICON_VISIBLE = "isShowRighticon";
	/** �Ƿ���ʾ��ѡ�� */
	public final String IS_CHECKBOX_VISIABLE = "isShowCheckBox";
	/** �����б������� */
	int i = 1;
	/** �ж��Ƿ�����ҵ��Ϣ�б� */
	public String WRYCX = "��ҵ��Ϣ�б�";
	/** �ж��Ƿ��Ǽ���¼��Ϣ�б� */
	public String JCJLCX = "����¼��Ϣ�б�";
	/** �ж��Ƿ���֪ͨ��ʾ�б� */
	public String TZGGCX = "֪ͨ��ʾ�б�";
	/** �ж��Ƿ����ʼ������б� */
	public String EmailCX = "�ʼ������б�";
	/** �ж��Ƿ�����ҵ�б� */
	public String PaiWuLb = "��ҵ�б�";
	/** ���и�ѡ���Ƿ�ѡ�� */
	public static boolean IS_CHECKED = false;
	/** ����ˢ�¹��ܵ��б���ͼ */
	protected PagingListView listView = null;
	/** ListView������ */
	protected ListActivityAdapter adapter = null;
	/** ��ǰҵ��������ת�� */
	protected IList businessObj = null;
	/** ���湴ѡ�ĸ�ѡ�� */
	protected LinkedList<String> chkChoice = null;
	/** �б�����Դ */
	protected ArrayList<HashMap<String, Object>> dataList = null;
	/** �����б�������ʱ����ʱ��ż��ص�����Դ */
	protected ArrayList<HashMap<String, Object>> Datalist = null;
	/** ����б����ݵ�Bundle */
	protected Bundle bundle = null;
	/** �жϵ�ǰ�Ƿ���ͬ������ */
	protected boolean isDataSync = true;
	/** �б���ʽ���� */
	protected HashMap<String, Object> style = null;
	/** ListView�ײ��������ݵĽ��ȿ� */
	protected LinearLayout mProgressLoadLayout;
	/** �����ص�һ������λ�ã�Ŀǰû��ʹ�� */
	protected int mLastItem = 0;
	/** �б������ص����� */
	protected int mCount = 1000;
	/** ��Handler�м������� */
	protected final Handler mHandler = new Handler();
	/** ����һ�����ֲ��� */
	protected final LayoutParams mLayoutParams = new LinearLayout.LayoutParams( LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT );
	/** ListView�����ϵĲ�ѯview����ʼΪ���� */
	public RelativeLayout searchview;
	/** ��ʾ�Զ���Ľ����� */
	protected YutuLoading yutuLoading;
	/** ListView����״̬ ��0Ϊ���� */
	protected int scrollState;
	/** ListView�ɼ������� */
	protected int visibleItemCount;
	/** �б�ĸ����� */
	protected LinearLayout listLayout;
	/** �������ݵȴ���ĸ����� */
	protected LinearLayout mLoadLayout;
	/** �ò����ѹ��� */
	protected int listviewIndex;
	/** ��ȡ��ҵ�����б�Ĺ����������� */
	public HashMap<String, Object> fliterHashMap;
	
	/** type */
	public String type = "SiteRecord";
	/** ���б��е�ǰѡ�е�Item���б��е�λ�� */
	private int detailPosition;

	@Override
	public void onCreate( Bundle savedInstanceState )
	{
		super.onCreate( savedInstanceState );
		setContentView( R.layout.ui_mapuni );
		Intent it = this.getIntent();
		/** ��intent��BundleΪ�����ȫ�ֱ�����ȡ��Bundle */
		bundle = it.getExtras();
		if ( bundle != null )
		{
			/** ҵ��������ת�ͣ������Base��ת��������������� */
			businessObj = ( IList ) bundle.getSerializable( "BusinessObj" );
		}
		else
		{
			businessObj = ( IList ) bundle.getSerializable( "BusinessObj" );
		}
		/** ��ʼ�����б����õ�ǰҵ�����б��������Ϊ1 */
		businessObj.setListScrolltimes( 1 );
		/** ��ʼ���ؼ� */
		RelativeLayout linearLayout = ( RelativeLayout ) this.findViewById( R.id.parentLayout );
		listView = new PagingListView( this );
		listView.setCacheColorHint( Color.TRANSPARENT );

		yutuLoading = new YutuLoading( this );
		yutuLoading.setLoadMsg( "�����У����Ե�...", "" );

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

		/** ��ȡ�б������ʾ */
		String titleText = businessObj.getListTitleText();
		super.SetBaseStyle( linearLayout, titleText );
		/** �Ƿ������������Ȩ�� */
		super.isAddTask = bundle.containsKey( "IsAddTask" ) ? true : false;
		/** �����б��֣�����BaseActivity�а�ť����ʾ */
		boolean isShowTitle = bundle.getBoolean( "IsShowTitle" );
		boolean isShowSyncBtn = bundle.containsKey( "IsShowSyncBtn" ) ? bundle.getBoolean( "IsShowSyncBtn" ) : true;
		boolean IsShowQueryBtn = bundle.containsKey( "IsShowQueryBtn" ) ? true : false;
		boolean IsShowQUERYBtn = bundle.containsKey( "IsShowQUERYBtn" ) ? true : false;
		/** ���ÿ�ݲ˵��Ƿ�ɼ� */
		super.setSynchronizeButtonVisiable( isShowSyncBtn );
		/** ���ñ�������ѯ��ť�Ƿ�ɼ� */
		super.setSearchButtonVisiable( IsShowQueryBtn );
		/** ���ÿ�ݲ˵���ѯ */
		super.setQueryButtonVisiable( IsShowQUERYBtn );
		/** ���ñ��Ⲽ���Ƿ�ɼ� */
		super.setTitleLayoutVisiable( isShowTitle );

		/** ��ȡ��ǰҵ�����ѯ���ݵĹ������� */
		fliterHashMap = businessObj.getFilter();

		/** ���ݹ������жϵ�ǰ�б���ʾ���� */
		if ( fliterHashMap != null && fliterHashMap.size() > 0 )
		{
			/** ͨ����ѯ������ȡ�б����� */
			dataList = businessObj.getDataList( fliterHashMap );
		}
		else
		{
			/** ��ȡ�б����� */
			dataList = businessObj.getDataList();
		}
		if ( "ͬ������".equals( bundle.getString( "TitleText" ) ) )
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
	 * Description: ��ImageView�󶨼�����
	 * 
	 * @param lefticon
	 *            ͼƬ��ͼ
	 * @param holder
	 *            ViewHolder����
	 * @param position
	 *            λ�� void
	 * @author ����� Create at: 2012-12-6 ����11:07:11
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
	 * Description: ��䲢չʾ����
	 * 
	 * @param bundle
	 *            ҵ�����װ��bundle�����ݸ���ǰListActivity
	 * @param data
	 *            �б�����Դ
	 * @param style
	 *            �б���ʽ��Ϣ void
	 * @author ����� Create at: 2012-12-6 ����11:09:02
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

		/** ����������ʵ���б���Ŀ�ĵ�����Ӧ�¼� */
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

				/** ��ʾ���صĽ����� */
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

		/** ����ʵ���б���Ŀ�ĳ�����Ӧ�¼� */
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
				/** ���ֶ�ר�ҿ�Ĳ��� */
				if ( businessObj.getListTitleText().equals( "ר�ҿ���Ϣ�б�" ) )
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

					/** ��ȡ�绰�� */
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
						Toast.makeText( ListActivity.this, "����Ϊ�գ�", 0 ).show();
					}
					/** ����绰URI */
					final Uri uri = Uri.parse( "tel:" + uriString );
					/** ������ϢURI */
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
								Toast.makeText( ListActivity.this, "�����Ϣ��ȫ��", 0 ).show();
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
								Toast.makeText( ListActivity.this, "��ϵ����ӳɹ�", 0 ).show();
							}
						}
					} );
				}

				return true;
			}
		} );
	}

	
	/**
	 * Description: �б�ĵ���¼�ҵ����
	 * 
	 * @param idValue
	 *            ������Idֵ
	 * @param content
	 *            �����ı������� void
	 * @author ����� Create at: 2012-12-6 ����11:24:19
	 */
	public void listItemClick( String idValue, String content )
	{
		/** ���� Intent ��������, չ����ʾ��ϸ��Ϣ�� Activity */
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
	 * Description: ��ȡ��ҵ����
	 * 
	 * @param id
	 *            ��ҵID
	 * @return ������ҵ���� String
	 * @author ����� Create at: 2012-12-6 ����11:25:38
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
	 * Description: ������Ŀ������������ͼ�������
	 * 
	 * @param itemType
	 *            ͼ������
	 * @param itemIconOptions
	 *            ͼƬ���Ƽ���
	 * @return ͼƬ���� String
	 * @author ����� Create at: 2012-12-6 ����11:27:26
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
	 * Description: ������Ŀ������������ͼ�����ԴID
	 * 
	 * @param itemType
	 *            ͼƬ����
	 * @param itemIconOptions
	 *            ͼƬ���Ƽ���
	 * @return ͼƬ�ļ�����ԴID
	 * @author ����� Create at: 2012-12-6 ����11:29:27
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
	 * Description: ����ͼƬ�����֣���Ҫ��׺������ȡBitpMap
	 * 
	 * @param name
	 *            ͼƬ����
	 * @return Bitmap��ʽ��ͼƬ Bitmap
	 * @author ����� Create at: 2012-12-6 ����11:35:12
	 */
	public Bitmap getRes( String name )
	{
		ApplicationInfo appInfo = getApplicationInfo();
		int resID = getResources().getIdentifier( name, "drawable", appInfo.packageName );
		return BitmapFactory.decodeResource( getResources(), resID );
	}

	


	/** �����б����� */
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
					Toast.makeText(ListActivity.this, "ȫ�����ݼ������!", Toast.LENGTH_LONG).show();
					
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
