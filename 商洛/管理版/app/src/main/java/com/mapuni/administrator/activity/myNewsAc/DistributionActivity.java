package com.mapuni.administrator.activity.myNewsAc;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.mapuni.administrator.R;
import com.mapuni.administrator.activity.BaseActivity;
import com.mapuni.administrator.bean.GridNameBean;
import com.mapuni.administrator.divider.DividerItemDecoration;
import com.mapuni.administrator.fragment.PictureFragment;
import com.mapuni.administrator.itemFactory.DistributionProcessRecyclerItemFactory;
import com.mapuni.administrator.manager.NetManager;
import com.mapuni.administrator.utils.Logger;
import com.mapuni.administrator.utils.SPUtils;
import com.mapuni.administrator.utils.TxtUtil;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import me.xiaopan.assemblyadapter.AssemblyRecyclerAdapter;
import okhttp3.Call;

/**
 * @name shangluoAdminstor
 * @class name：com.mapuni.administrator.activity.myNewsAc
 * @class 任务分配界面
 * @anthor tianfy
 * @time 2017/10/18 11:12
 * @change
 * @chang time
 * @class describe
 */

public class DistributionActivity extends BaseActivity implements AdapterView.OnItemClickListener {


    @BindView(R.id.tv_taskName)
    TextView mTvTaskName;
    @BindView(R.id.tv_taskType)
    TextView mTvTaskType;
    @BindView(R.id.tv_startTime)
    TextView mTvStartTime;
    @BindView(R.id.tv_endTime)
    TextView mTvEndTime;
    @BindView(R.id.tv_description)
    TextView mTvDescription;
    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.btn_addTask)
    Button mBtnAddTask;
    @BindView(R.id.ll_fragment_container)
    LinearLayout llFragmentContainer;
    @BindView(R.id.tv_noAttachInfo)
    TextView tvNoAttachInfo;
    List<Object> dataList = new ArrayList<Object>();//数据源

    private String taskUuid;
    private List<GridNameBean> gridnameList;
    private CityNameAdapter mCityNameAdapter;
    private AssemblyRecyclerAdapter adapter;//万能适配器
    private String sessionId;
    
    private Handler mHandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            tvNoAttachInfo.setVisibility(View.VISIBLE);
            llFragmentContainer.setVisibility(View.GONE);
        }
    };

    @Override
    public int setLayoutResID() {
        return R.layout.activity_distribution;
    }

    @Override
    public void initView() {
        setToolbarTitle("任务分配");
        sessionId = (String) SPUtils.getSp(mContext, "sessionId", "");
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));
        adapter = new AssemblyRecyclerAdapter(dataList);
        adapter.addItemFactory(new DistributionProcessRecyclerItemFactory(this));
        mRecyclerView.setAdapter(adapter);
    }




    @Override
    public void initData() {
        taskUuid = getIntent().getStringExtra("taskUuid");
        requestBaseData();//请求基本数据
        addFragment();//添加fargment
    }

    private void addFragment() {
        tvNoAttachInfo.setVisibility(View.GONE);
        llFragmentContainer.setVisibility(View.VISIBLE);
        PictureFragment fragment=new PictureFragment();
        Bundle bundle=new Bundle();
        bundle.putString("taskUuid",taskUuid);
        bundle.putString("flag",DistributionActivity.class.getSimpleName());
        fragment.setHandler(mHandler);
        fragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().replace(R.id.ll_fragment_container,fragment).commit();
    }


    /**
     * 请求基本数据
     */
    private void requestBaseData() {
        mSvProgressHUD.showWithStatus("正在加载...");
        NetManager.requestDistributionTask(taskUuid,
                new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Toast.makeText(DistributionActivity.this, "请求数据失败", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        Logger.i(DistributionActivity.class.getSimpleName(), response.toString());
                        try {
                            JSONObject jsonObject = new JSONObject(response.toString());
                            if (jsonObject.getString("msg").equals("success") && jsonObject.getInt("status") == 0) {
                                JSONObject data = jsonObject.getJSONObject("data");
                                String name = data.getString("name");
                                String taskType = data.getString("taskType");
                                String description = data.getString("description");
                                String startTime = data.getString("startTime");
                                String endTime = data.getString("endTime");
                                initBaseData(name, taskType, startTime, endTime, description);
                                requestListData();//请求列表数据接口
                            } else {
                                Toast.makeText(mContext, "请求数据失败", Toast.LENGTH_SHORT).show();
                                DistributionActivity.this.finish();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(mContext, "请求数据失败", Toast.LENGTH_SHORT).show();
                            DistributionActivity.this.finish();
                        }
                    }
                });
    }



    /**
     * 初始化基础数据
     *
     * @param name
     * @param taskType
     * @param startTime
     * @param endTime
     * @param description
     */
    private void initBaseData(String name, String taskType, String startTime, String endTime, String description) {
        mTvTaskName.setText(TxtUtil.isEmpty(name));
        mTvTaskType.setText(TxtUtil.isEmpty(taskType));
        mTvStartTime.setText(TxtUtil.isEmpty(startTime));
        mTvEndTime.setText(TxtUtil.isEmpty(endTime));
        mTvDescription.setText(TxtUtil.isEmpty(description));
    }

    /**
     * 请求列表数据接口
     */
    public void requestListData() {
        NetManager.requestDistributionListData(sessionId, taskUuid, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                Toast.makeText(mContext, "请求数据失败", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(String response, int id) {
                Logger.i(DistributionActivity.class.getSimpleName(), response.toString());
                if (mSvProgressHUD.isShowing()) {
                    mSvProgressHUD.dismiss();
                }
                try {
                    JSONObject jsonObject = new JSONObject(response.toString());
                    JSONArray rows = jsonObject.getJSONArray("rows");
                    gridnameList = new ArrayList<GridNameBean>();
                    if (rows != null && rows.length() > 0) {
                        GridNameBean gridNameBean;
                        for (int i = 0; i < rows.length(); i++) {
                            gridNameBean = new GridNameBean();
                            JSONObject object = rows.getJSONObject(i);
                            String gridName = object.getString("gridName");
                            String createTime = object.getString("createTime");
                            String uuid = object.getString("uuid");
                            String taskType = object.getString("issuedTaskStatus");
                            int judgeDetailsStatus = object.getInt("judgeDetailsStatus");
                            gridNameBean.setName(gridName);
                            gridNameBean.setCreateTime(createTime);
                            gridNameBean.setUuid(uuid);
                            gridNameBean.setIssuedTaskStatus(taskType);
                            gridNameBean.setJudgeDetailsStatus(judgeDetailsStatus);
                            gridnameList.add(gridNameBean);
                        }
                        adapter.setDataList(gridnameList);
                    } else {
//                        Toast.makeText(mContext, "暂无数据", Toast.LENGTH_SHORT).show();
                        adapter.setDataList(gridnameList);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(mContext, "请求数据失败", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    @OnClick(R.id.btn_addTask)
    public void onViewClicked() {

        requestGridsByCurrentUserData();//获取下级网格
    }

    /**
     * 获取下级网格
     *
     * @author tianfy
     */
    private void requestGridsByCurrentUserData() {

        NetManager.requestGridsByCurrentUser(taskUuid, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                Toast.makeText(DistributionActivity.this, "请求数据失败", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(String response, int id) {
                Logger.i(DistributionActivity.class.getSimpleName(), response.toString());
                try {
                    JSONObject jsonObject = new JSONObject(response.toString());
                    JSONArray data = jsonObject.getJSONArray("data");
                    if (data != null && data.length() > 0) {
                        gridnameList = new ArrayList<GridNameBean>();
                        GridNameBean gridNameBean;
                        for (int i = 0; i < data.length(); i++) {
                            gridNameBean = new GridNameBean();
                            JSONObject object = data.getJSONObject(i);
                            String cityName = object.getString("name");
                            String uuid = object.getString("uuid");
                            gridNameBean.setName(cityName);
                            gridNameBean.setUuid(uuid);
                            gridnameList.add(gridNameBean);
                        }
                        //展示自定义对话框
                        showCustomDialog();
                    } else {
                        Toast.makeText(mContext, "没有更多数据了", Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(mContext, "请求数据失败", Toast.LENGTH_SHORT).show();

                }
            }
        });


    }

    /**
     * 展示自定义对话框
     */
    private void showCustomDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("选择网格");
        View view = View.inflate(this, R.layout.distribution_dialog_layout, null);
        ListView lvCityName = (ListView) view.findViewById(R.id.lv_cityName);
        lvCityName.setOnItemClickListener(this);
        mCityNameAdapter = new CityNameAdapter(this, gridnameList);
        lvCityName.setAdapter(mCityNameAdapter);
        builder.setView(view);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (mCityNameAdapter.isSelected != null && mCityNameAdapter.isSelected.size() > 0) {
                    StringBuffer gridUuids = new StringBuffer();
                    for (int i = 0; i < mCityNameAdapter.getCount(); i++) {
                        if (mCityNameAdapter.isSelected.get(i)) {
                            GridNameBean item = (GridNameBean) mCityNameAdapter.getItem(i);
                            gridUuids.append(item.getUuid() + ",");
                        }
                    }
                    //下级网格分配确认接口
                    requestDistributionTaskToGrids(gridUuids.toString());

                } else {
                    Toast.makeText(mContext, "请选择要分配的网格", Toast.LENGTH_SHORT).show();
                }
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.create().show();
    }

    /**
     * 下级网格分配确认接口
     *
     * @param gridUuids
     * @author tianfy
     */
    private void requestDistributionTaskToGrids(String gridUuids) {
        NetManager.requestDistributionTaskToGrids(taskUuid, gridUuids, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                Toast.makeText(mContext, "请求失败", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(String response, int id) {
                //请求列表数据接口
                requestListData();
            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        CityNameAdapter.ViewHolder vHollder = (CityNameAdapter.ViewHolder) view.getTag();
//在每次获取点击的item时将对于的checkbox状态改变，同时修改map的值。  
        vHollder.cbCheck.toggle();
        mCityNameAdapter.isSelected.put(position, vHollder.cbCheck.isChecked());

    }

    private class CityNameAdapter extends BaseAdapter {
        public List<GridNameBean> gridnameList;
        private Context mContext;
        public Map<Integer, Boolean> isSelected;

        public CityNameAdapter(Context context, List<GridNameBean> gridnameList) {
            this.gridnameList = gridnameList;
            this.mContext = context;
            init();
        }

        private void init() {
            //这儿定义isSelected这个map是记录每个listitem的状态，初始状态全部为false。  
            isSelected = new HashMap<Integer, Boolean>();
            for (int i = 0; i < gridnameList.size(); i++) {
                isSelected.put(i, false);
            }
        }

        @Override
        public int getCount() {
            return gridnameList.size();
        }

        @Override
        public Object getItem(int position) {
            return gridnameList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = View.inflate(mContext, R.layout.distribution_dialog_item, null);
                holder.tvCityName = (TextView) convertView.findViewById(R.id.tv_cityName);
                holder.cbCheck = (CheckBox) convertView.findViewById(R.id.cb_check);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            holder.tvCityName.setText(gridnameList.get(position).getName());
            holder.cbCheck = (CheckBox) convertView.findViewById(R.id.cb_check);
            holder.cbCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    mCityNameAdapter.isSelected.put(position, isChecked);
                }
            });
            return convertView;
        }

        class ViewHolder {
            TextView tvCityName;
            CheckBox cbCheck;
        }
    }
}
