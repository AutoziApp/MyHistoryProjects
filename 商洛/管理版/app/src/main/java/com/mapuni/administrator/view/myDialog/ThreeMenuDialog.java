package com.mapuni.administrator.view.myDialog;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.mapuni.administrator.R;
import com.mapuni.administrator.manager.NetManager;

import java.util.ArrayList;
import java.util.List;

/**
 * 三级菜单列表
 * Created by LaiYingtang on 2016/5/25.
 */
public class ThreeMenuDialog extends SecondMenuDialog{
    private String sessionId;

    private  int mWidth;    //宽度
    private MyViewPager mViewPager; //滑动viewPager
    private LinearLayout mRootView;    //需要显示的layout
    private View view1,view2,view3,view4;    //三个菜单级view
    private ListView mListView1,mListView2,mListView3,mListView4;  //每个菜单列表都是一个listView
    private MenuDialogAdapter mListView1Adapter, mListView2Adapter, mListView3Adapter,mListView4Adapter; //列表显示数据必须要的adapter
    private List<View> views = new ArrayList<View>(); //数据集合
//    public MenuDataManager mDictDataManager = MenuDataManager.getInstance(); //全部数据
    private MenuItemClickListener menuItemClickListener;   //接口，点击监听

    Handler mHandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.arg1){
                case 0://初始化
                    List<MenuData> list=TestJson.getList((String) msg.obj);

                    if(list==null||list.size()==0){
                        Toast.makeText(mContext,"该网格下暂无监管对象",Toast.LENGTH_SHORT).show();
                        dismiss();
                    }
                    //关联adapter
                    mListView1Adapter = new MenuDialogAdapter(mContext, list);
                    mListView1Adapter.setSelectedBackgroundResource(R.drawable.select_white);//选中时的背景
                    mListView1Adapter.setHasDivider(false);
                    mListView1Adapter.setNormalBackgroundResource(R.color.menudialog_bg_gray);//未选中
                    mListView1.setAdapter(mListView1Adapter);

                    views.add(view1);
                    views.add(view2);//当前是第三级菜单，所以前面已经存在第一，第二菜单了

                    //关联
                    mViewPager.setAdapter(new MyPagerAdapter(views));
                    break;
                case 1:
                    List<MenuData> list2 = TestJson.getList((String) msg.obj);
                    if (mListView2Adapter == null) {
                        mListView2Adapter = new MenuDialogAdapter(mContext, list2);
                        mListView2Adapter.setNormalBackgroundResource(R.color.white);
                        mListView2.setAdapter(mListView2Adapter);
                    } else {
                        mListView2Adapter.setData(list2);
                        mListView2Adapter.notifyDataSetChanged();
                    }
                    break;
                case 2:
                    List<MenuData> list3 = TestJson.getList((String) msg.obj);
                    if (mListView3Adapter == null) {
                        mListView3Adapter = new MenuDialogAdapter(mContext, list3);
                        mListView3Adapter.setHasDivider(false);
                        mListView3Adapter.setNormalBackgroundResource(R.color.menudialog_bg_gray);
                        mListView3.setAdapter(mListView3Adapter);
                    } else {
                        mListView3Adapter.setData(list3);
                        mListView3Adapter.notifyDataSetChanged();
                    }

                    //放入第三级菜单列表
                    views.add(view3);
                    mViewPager.getAdapter().notifyDataSetChanged();

                    if (mViewPager.getCurrentItem() != 1) {
                        mViewPager.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                mViewPager.setCurrentItem(1);//选一个
                            }
                        }, 300);
                    }
                    break;
                case 3:
                    List<MenuData> list4 = TestJson.getList((String) msg.obj);
                    if (mListView4Adapter == null) {
                        mListView4Adapter = new MenuDialogAdapter(mContext, list4);
                        mListView4Adapter.setHasDivider(false);
                        mListView4Adapter.setNormalBackgroundResource(R.color.menudialog_bg_gray);
                        mListView4.setAdapter(mListView4Adapter);
                    } else {
                        mListView4Adapter.setData(list4);
                        mListView4Adapter.notifyDataSetChanged();
                    }

                    //放入第三级菜单列表
                    views.add(view4);
                    mViewPager.getAdapter().notifyDataSetChanged();

                    if (mViewPager.getCurrentItem() != 2) {
                        mViewPager.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                mViewPager.setCurrentItem(2);//选一个
                            }
                        }, 300);
                    }
                    break;
                case 4:

                    break;
                case 5://请求数据失败
                    Toast.makeText(mContext,"获取监管对象失败",Toast.LENGTH_SHORT).show();
                    dismiss();
                    break;

            }

        }
    };


    public ThreeMenuDialog(Context context,String sessionId) {
        super(context);
        this.sessionId=sessionId;
        mWidth = mContext.getResources().getDisplayMetrics().widthPixels;//获取屏幕参数
        mContentView = LayoutInflater.from(context).inflate(R.layout.three_menu_dialog,null);
        //初始化控件及对控件操作
        initViews();
        setTitle("请选择监管对象");//设置title

    }

    private void initViews() {
        mRootView = (LinearLayout) findViewById(R.id.rootview);
        mViewPager = (MyViewPager) findViewById(R.id.viewpager);
        mViewPager.setOffscreenPageLimit(2);//显示2页

        //为view加载layout,由于三个级的菜单都是只有一个listView，这里就只xie一个了
        LayoutInflater inflater = LayoutInflater.from(mContext);
        view1 = inflater.inflate(R.layout.pager_number, null);
        view2 = inflater.inflate(R.layout.pager_number, null);
        view3 = inflater.inflate(R.layout.pager_number, null);
        view4 = inflater.inflate(R.layout.pager_number, null);

        //获取id
        mListView1 = (ListView) view1.findViewById(R.id.listview);
        mListView2 = (ListView) view2.findViewById(R.id.listview);
        mListView3 = (ListView) view3.findViewById(R.id.listview);
        mListView4 = (ListView) view4.findViewById(R.id.listview);

        //获取列表数据了
//        List<MenuData> list=mDictDataManager.getTripleColumnData(mContext, 0);

        NetManager.getJsonByPost(sessionId,"",mHandler,0);
//        List<MenuData> list=TestJson.getList(sessionId,"");
//        //关联adapter
//        mListView1Adapter = new MenuDialogAdapter(mContext, list);
//        mListView1Adapter.setSelectedBackgroundResource(R.drawable.select_white);//选中时的背景
//        mListView1Adapter.setHasDivider(false);
//        mListView1Adapter.setNormalBackgroundResource(R.color.menudialog_bg_gray);//未选中
//        mListView1.setAdapter(mListView1Adapter);
//
//
//        views.add(view1);
//        views.add(view2);//当前是第三级菜单，所以前面已经存在第一，第二菜单了
//
//        //关联
//        mViewPager.setAdapter(new MyPagerAdapter(views));
        //触屏监听
        mRootView.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return mViewPager.dispatchTouchEvent(event);
            }
        });

        //view1的listView的点击事件
        //点击事件
        mListView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (mListView1Adapter != null)
                    mListView1Adapter.setSelectedPos(position);
                if (mListView2Adapter != null)
                    mListView2Adapter.setSelectedPos(-1);

                if (views.contains(view3)) {
                    views.remove(view3);
                    mViewPager.getAdapter().notifyDataSetChanged();//立即更新adapter数据
                }
                if (views.contains(view4)) {
                    views.remove(view4);
                    mViewPager.getAdapter().notifyDataSetChanged();//立即更新adapter数据
                }
                MenuData menuData = (MenuData) parent.getItemAtPosition(position);//得到第position个menu子菜单
                if(menuData.isQY==1){//是企业的话 直接终结
                    setDictItemClickListener(menuData);
                    return;
                }

//                    if (mListView2Adapter != null) {
//                        mListView2Adapter.setData(new ArrayList<MenuData>());
//                        mListView2Adapter.notifyDataSetChanged();//刷新
//                    }
//                    setDictItemClickListener(menuData);

                    NetManager.getJsonByPost(sessionId,menuData.uuid,mHandler,1);
//                    List<MenuData> list2 = mDictDataManager.getTripleColumnData(mContext, menuData.id);
//                    List<MenuData> list2 = TestJson.getList(sessionId,menuData.uuid);
//                    if (mListView2Adapter == null) {
//                        mListView2Adapter = new MenuDialogAdapter(mContext, list2);
//                        mListView2Adapter.setNormalBackgroundResource(R.color.white);
//                        mListView2.setAdapter(mListView2Adapter);
//                    } else {
//                        mListView2Adapter.setData(list2);
//                        mListView2Adapter.notifyDataSetChanged();
//                    }


            }
        });


        //view2的listView点击
        mListView2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (mListView2Adapter != null) {
                    mListView2Adapter.setSelectedPos(position);
                    mListView2Adapter.setSelectedBackgroundResource(R.drawable.select_gray);
                }
                if (mListView3Adapter != null) {
                    mListView3Adapter.setSelectedPos(-1);
                }

                if (views.contains(view3)) {
                    views.remove(view3);
                    mViewPager.getAdapter().notifyDataSetChanged();//立即更新adapter数据
                }
                if (views.contains(view4)) {
                    views.remove(view4);
                    mViewPager.getAdapter().notifyDataSetChanged();//立即更新adapter数据
                }
                MenuData menuData = (MenuData) parent.getItemAtPosition(position);//得到第position个menu子菜单
                if(menuData.isQY==1){//是企业的话 直接终结
                    setDictItemClickListener(menuData);
                    return;
                }
                //从第二级菜单的基础上加载第三级菜单
//                List<MenuData> list3 = mDictDataManager.getTripleColumnData(mContext, menuData.id);
                NetManager.getJsonByPost(sessionId,menuData.uuid,mHandler,2);
//                List<MenuData> list3 = TestJson.getList(sessionId,menuData.uuid);
//                if (mListView3Adapter == null) {
//                    mListView3Adapter = new MenuDialogAdapter(mContext, list3);
//                    mListView3Adapter.setHasDivider(false);
//                    mListView3Adapter.setNormalBackgroundResource(R.color.menudialog_bg_gray);
//                    mListView3.setAdapter(mListView3Adapter);
//                } else {
//                    mListView3Adapter.setData(list3);
//                    mListView3Adapter.notifyDataSetChanged();
//                }
//
//                //放入第三级菜单列表
//                views.add(view3);
//                mViewPager.getAdapter().notifyDataSetChanged();
//
//                if (mViewPager.getCurrentItem() != 1) {
//                    mViewPager.postDelayed(new Runnable() {
//                        @Override
//                        public void run() {
//                            mViewPager.setCurrentItem(1);//选一个
//                        }
//                    }, 300);
//                }
            }
        });
  //view3的listView点击
        mListView3.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (mListView3Adapter != null) {
                    mListView3Adapter.setSelectedPos(position);
                    mListView3Adapter.setSelectedBackgroundResource(R.drawable.select_gray);
                }

                if (views.contains(view4)) {
                    views.remove(view4);
                    mViewPager.getAdapter().notifyDataSetChanged();//立即更新adapter数据
                }
                MenuData menuData = (MenuData) parent.getItemAtPosition(position);//得到第position个menu子菜单
                //从第二级菜单的基础上加载第三级菜单
//                List<MenuData> list3 = mDictDataManager.getTripleColumnData(mContext, menuData.id);

                if(menuData.isQY==1){//是企业的话 直接终结
                    setDictItemClickListener(menuData);
                    return;
                }
                NetManager.getJsonByPost(sessionId,menuData.uuid,mHandler,3);
//                List<MenuData> list4 = TestJson.getList(sessionId,menuData.uuid);
//                if (mListView4Adapter == null) {
//                    mListView4Adapter = new MenuDialogAdapter(mContext, list4);
//                    mListView4Adapter.setHasDivider(false);
//                    mListView4Adapter.setNormalBackgroundResource(R.color.menudialog_bg_gray);
//                    mListView4.setAdapter(mListView4Adapter);
//                } else {
//                    mListView4Adapter.setData(list4);
//                    mListView4Adapter.notifyDataSetChanged();
//                }
//
//                //放入第三级菜单列表
//                views.add(view4);
//                mViewPager.getAdapter().notifyDataSetChanged();
//
//                if (mViewPager.getCurrentItem() != 2) {
//                    mViewPager.postDelayed(new Runnable() {
//                        @Override
//                        public void run() {
//                            mViewPager.setCurrentItem(2);//选一个
//                        }
//                    }, 300);
//                }
            }
        });


        //最后就是第四级菜单的点击了
        mListView4.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MenuData menuData = (MenuData) parent.getItemAtPosition(position);
                setDictItemClickListener(menuData);//选中点击的子菜单，去设置titleName
            }
        });

    }

    private void setDictItemClickListener(MenuData menuData) {
        if (menuItemClickListener != null) {
            menuItemClickListener.onMenuItemClick(menuData);
        }
        dismiss();
    }

    public final void setonItemClickListener(MenuItemClickListener listener) {
        menuItemClickListener = listener;
    }

    public interface MenuItemClickListener {
        public void onMenuItemClick(MenuData menuData);
    }
}
