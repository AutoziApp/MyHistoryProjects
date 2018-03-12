package com.mapuni.administrator.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatSpinner;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mapuni.administrator.R;
import com.mapuni.administrator.activity.BaseActivity;
import com.mapuni.administrator.bean.AreaBean;
import com.mapuni.administrator.bean.TaskType;
import com.mapuni.administrator.manager.NetManager;
import com.mapuni.administrator.utils.Logger;
import com.mapuni.administrator.utils.SPUtils;
import com.mapuni.administrator.utils.TimePickerUtil;
import com.mapuni.administrator.utils.TimeUtil;
import com.mapuni.administrator.view.treeViewHolder.CustomViewHolder;
import com.unnamed.b.atv.model.TreeNode;
import com.unnamed.b.atv.view.AndroidTreeView;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;

/**
 * @name shangluoAdminstor
 * @class name：com.mapuni.administrator.fragment
 * @class describe
 * @anthor Tianfy
 * @time 2017/9/7 18:06
 * @change
 * @chang time
 * @class describe
 */

public abstract class TababstractFragment extends Fragment implements View.OnClickListener, AdapterView.OnItemSelectedListener, AdapterView.OnItemClickListener {

    public int QUERY_TYPE = 1;
    private int HELLO_CHARTS_TYPE = -1;
    private String taskTypeId = "";
    private TextView mBtnShowArea;
    public AppCompatSpinner mSpinner;
    public AppCompatSpinner mSpinnerStatistic;


    private TextView mTvStartTime;
    private TextView mTvEndTime;
    private Button mBtnQuery;
    private TimePickerUtil mTimePickerUtil;
    private Context mContext;
    private String mSessionId;
    private Gson mGson;
    private TreeNode mRoot;
    private AlertDialog mDialog;
    private AndroidTreeView mAndroidTreeView;
    private LinearLayout mContainerTaskType;
    private GridView mGvTaskType;
    private String mStartTimeStr;
    private String mEndTimeStr;
    public BaseActivity mActivity;
    private TextView mTvTaskType;
    private GridView mGvPic;
    public LinearLayout mContainerPic;
    public LinearLayout mContainerHellorcharts;
    public LinearLayout mContainerTaskStatistic;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mContext = getActivity();
        View view = View.inflate(mContext, R.layout.fragment_tab, null);
        initView(view);
        initData();
        return view;
    }

    protected void initView(View view) {
        mTvStartTime = (TextView) view.findViewById(R.id.tv_startTime);
        mTvEndTime = (TextView) view.findViewById(R.id.tv_endTime);
        mBtnQuery = (Button) view.findViewById(R.id.btn_query);

        mBtnShowArea = (TextView) view.findViewById(R.id.btn_showArea);
        mTvTaskType = (TextView) view.findViewById(R.id.tv_taskType);

        mSpinnerStatistic = (AppCompatSpinner) view.findViewById(R.id.spinner_statistic);
        mSpinner = (AppCompatSpinner) view.findViewById(R.id.spinner);
        mContainerTaskType = (LinearLayout) view.findViewById(R.id.container_task_type);
        mContainerHellorcharts = (LinearLayout) view.findViewById(R.id.container_hellocharts);
        mContainerTaskStatistic = (LinearLayout) view.findViewById(R.id.container_task_statistic);
        mGvTaskType = (GridView) view.findViewById(R.id.gv_task_type);
        mGvPic = (GridView) view.findViewById(R.id.gv_pic);
        mContainerPic = (LinearLayout) view.findViewById(R.id.container_pic);

        initListener();
        initSpinner();
    }

    private void initListener() {
        mBtnShowArea.setOnClickListener(this);
        mTvStartTime.setOnClickListener(this);
        mTvEndTime.setOnClickListener(this);
        mBtnQuery.setOnClickListener(this);
        mSpinner.setOnItemSelectedListener(this);
        mSpinnerStatistic.setOnItemSelectedListener(this);
        mGvTaskType.setSelected(true);
//        mGvTaskType.setOnItemSelectedListener(this);
        mGvTaskType.setOnItemClickListener(this);

    }

    private void initSpinner() {
        String[] stringArray = getActivity().getResources().getStringArray(R.array.chattypes);
        String[] stringStatisticArray = getActivity().getResources().getStringArray(R.array.staticstic_types);
        List<String> itemNames = new ArrayList<>();
        List<String> itemStatisticNames = new ArrayList<>();
        for (int i = 0; i < stringArray.length; i++) {
            itemNames.add(stringArray[i]);
        }
        for (int i = 0; i < stringStatisticArray.length; i++) {
            itemStatisticNames.add(stringStatisticArray[i]);
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), R.layout.my_simple_spinner_item, itemNames);
        ArrayAdapter<String> adapterStatistic = new ArrayAdapter<String>(getActivity(), R.layout.my_simple_spinner_item, itemStatisticNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapterStatistic.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinner.setAdapter(adapter);
        mSpinnerStatistic.setAdapter(adapterStatistic);
    }

    public void initData() {
        mGson = new Gson();
        mActivity = (BaseActivity) getActivity();
        mSessionId = (String) SPUtils.getSp(mContext, "sessionId", "");
        //初始化城市筛选条件
        mRoot = TreeNode.root();
        initCityData(id);
        initGVPicData();
        mTimePickerUtil = new TimePickerUtil(mContext);
        //获取前7天的时间-当天当前时间 默认请求网络
        mTvStartTime.setText(TimeUtil.getDateBefore(7));
        mTvEndTime.setText(TimeUtil.getStrToday());
    }

    private int[] taskColors = {R.color.task_color1, R.color.task_color2,
            R.color.task_color3, R.color.task_color4, R.color.task_color5, 
            R.color.task_color6, R.color.task_color7, R.color.task_color8};

    private void initGVPicData() {
        String[] stringArray = getResources().getStringArray(R.array.task_types);
        List<String> taskTypes = new ArrayList<>();
        for (String taskType : stringArray) {
            taskTypes.add(taskType);
        }
        TaskPicAdapter taskPicAdapter = new TaskPicAdapter(taskTypes, taskColors);
        mGvPic.setAdapter(taskPicAdapter);
    }


    class TaskPicAdapter extends BaseAdapter {
        private List<String> taskTypes;
        private int[] taskColors;

        public TaskPicAdapter(List<String> taskTypes, int[] taskColors) {
            this.taskTypes = taskTypes;
            this.taskColors = taskColors;
        }

        @Override
        public int getCount() {
            return taskTypes.size();
        }

        @Override
        public Object getItem(int position) {
            return taskTypes.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder = null;
            if (convertView == null) {
                viewHolder = new ViewHolder();
                convertView = View.inflate(mContext, R.layout.layout_pic_item, null);
                viewHolder.picView = convertView.findViewById(R.id.vPic_color);
                viewHolder.tvTaskTypePic = (TextView) convertView.findViewById(R.id.tv_taskTypePic);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            viewHolder.tvTaskTypePic.setText(taskTypes.get(position));
            viewHolder.picView.setBackgroundResource(taskColors[position]);
            return convertView;
        }

        class ViewHolder {
            View picView;
            TextView tvTaskTypePic;
        }
    }

    /**
     * @author Tianfy
     * @time 2017/9/20  10:32
     * @describe 初始化任务类型查询条件
     */
    public void initTaskTypeData() {
        aShowDialog();
        NetManager.requestTaskTypeData(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                Toast.makeText(mContext, "请求数据失败", Toast.LENGTH_SHORT).show();
                dissDialog();
                mActivity.finish();
            }

            @Override
            public void onResponse(String response, int id) {
                dissDialog();
                TaskType taskType = mGson.fromJson(response.toString(), TaskType.class);
                List<TaskType.RowsBean> taskTypeRows = taskType.getRows();
                if (taskTypeRows != null && taskTypeRows.size() > 0) {
                    taskTypeId = taskTypeRows.get(0).getUuid();
                    mTvTaskType.setText(taskTypeRows.get(0).getName());
                    TaskTypeAdpter taskTypeAdpter = new TaskTypeAdpter(mContext, taskTypeRows);
                    mGvTaskType.setAdapter(taskTypeAdpter);
                }

            }
        });


//        String[] stringArray = getResources().getStringArray(R.array.task_types);
//        List<String> taskTypeNames=new ArrayList<String>();
//        for (String taskType:stringArray){
//            taskTypeNames.add(taskType);
//        }

    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        TaskTypeAdpter taskTypeAdapter = (TaskTypeAdpter) parent.getAdapter();
//        taskTypeAdapter.getItem(position);
        TaskTypeAdpter.ViewHolder holder = (TaskTypeAdpter.ViewHolder) view.getTag();
        //将当前点击的position传递过去做相应的状态改变
//        taskTypeAdapter.choiceState(position);
        holder.tvTaskName.setSelected(true);
        TaskType.RowsBean bean = (TaskType.RowsBean) taskTypeAdapter.getItem(position);
        taskTypeId = bean.getUuid();
        Log.i("qqq",taskTypeId.toString());
        mTvTaskType.setText(bean.getName());
    }

    class TaskTypeAdpter extends BaseAdapter {
        private Context mContext;
        private List<TaskType.RowsBean> taskTypeRows;

        //        private boolean[] isCheck;
        public TaskTypeAdpter(Context context, List<TaskType.RowsBean> taskTypeRows) {
            this.mContext = context;
            this.taskTypeRows = taskTypeRows;
            //给数组设置大小,并且全部赋值为false
//            if (taskTypeRows != null) {
//                isCheck = new boolean[taskTypeRows.size()];
//                for (int i = 0; i < taskTypeRows.size(); i++) {
//                    isCheck[i] = false;
//                }
//            }
        }

        @Override
        public int getCount() {
            return taskTypeRows.size();
        }

        @Override
        public Object getItem(int position) {
            return taskTypeRows.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder = null;
            if (convertView == null) {
                viewHolder = new ViewHolder();
                convertView = View.inflate(mContext, R.layout.layout_item_task_type_name, null);
                viewHolder.tvTaskName = (TextView) convertView.findViewById(R.id.tv_taskName);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            viewHolder.tvTaskName.setText(taskTypeRows.get(position).getName());
            if (position == 0) {
                viewHolder.tvTaskName.setSelected(true);
            }
//            if (isCheck[position]) {
//                viewHolder.tvTaskName.setBackgroundResource(R.drawable.text_bg_select);
//            } else {
//                viewHolder.tvTaskName.setBackgroundResource(R.drawable.text_bg);
//            }
            return convertView;
        }


//        /**
//         * 改变某一个选项的状态
//         * @param post
//         */
//        public void choiceState(int post) {
//            /**
//             *  传递过来所点击的position,如果是本身已经是选中状态,就让他变成不是选中状态,
//             *  如果本身不是选中状态,就让他变成选中状态
//             */
//            isCheck[post] = isCheck[post] == true ? false : true;
//
//            this.notifyDataSetChanged();
//        }

        class ViewHolder {
            TextView tvTaskName;
        }
    }

    //默认id为空
    private String id = "";

    /**
     * @param id
     * @author Tianfy
     * @time 2017/9/14  11:25
     * @describe 初始化城市筛选条件
     */
    TreeNode mParentNode;

    private void initCityData(String id) {
        aShowDialog();
        NetManager.requestCityData(id, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                Logger.e(TaskStatisticsFragment.class.getSimpleName(), e.toString());
                Toast.makeText(mContext, "请求数据失败", Toast.LENGTH_SHORT).show();
                dissDialog();
                mActivity.finish();
            }

            @Override
            public void onResponse(String response, int id) {
                dissDialog();
                Logger.i(TaskStatisticsFragment.class.getSimpleName(), response.toString());
                List<AreaBean> areaBeanList = mGson.fromJson(response, new TypeToken<List<AreaBean>>() {
                }.getType());
                if (areaBeanList != null && areaBeanList.size() > 0) {
                    //请求父节点填充父级目录
                    mParentNode = new TreeNode(areaBeanList.get(0)).setViewHolder(new CustomViewHolder(mContext));

                    mRoot.addChild(mParentNode);
                    mAndroidTreeView = new AndroidTreeView(mContext, mRoot);
                    //打开复选框开关
                    mAndroidTreeView.setSelectionModeEnabled(true);
                    //初始化区域类型
                    initAreaType(areaBeanList);
                    //对比开始结束时间      
                    compareTime();
                    //初始化Dialog
                    initDialog();
                    mAndroidTreeView.setDefaultNodeClickListener(new TreeNode.TreeNodeClickListener() {
                        @Override
                        public void onClick(TreeNode node, Object value) {
                            AreaBean areaBean = (AreaBean) value;
                            //bean中的isLeaf和node中的isLeaf相反
                            if (!areaBean.isLeaf()) {//有子
                                if (node.isLeaf()) {//有子
                                    requestChildNode(((AreaBean) value).getId(), node);
                                }

                            }
                        }
                    });
                }
            }
        });
    }

    protected abstract void dissDialog();

    protected abstract void aShowDialog();

    private String areaId = "";


    /**
     * @param
     * @param areaBeanList
     * @author Tianfy
     * @time 2017/9/18  8:52
     * @describe 初始化区域类型
     */
    private void initAreaType(List<AreaBean> areaBeanList) {
        String areaStr = areaBeanList.get(0).getText();
        areaId = areaBeanList.get(0).getId();
        mBtnShowArea.setText(areaStr);
    }

    /**
     * @param id         父节点id
     * @param parentNode 父节点
     * @author Tianfy
     * @time 2017/9/15  14:41
     * @describe 请求子节点数据并添加到父节点的TreeView中
     */
    private void requestChildNode(String id, final TreeNode parentNode) {

        NetManager.requestCityData(id, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                Logger.e(TaskStatisticsFragment.class.getSimpleName(), e.toString());
                Toast.makeText(mContext, "请求数据失败", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(String response, int id) {
                Logger.i(TaskStatisticsFragment.class.getSimpleName(), response.toString());
                List<AreaBean> areaBeanList = mGson.fromJson(response, new TypeToken<List<AreaBean>>() {
                }.getType());
                if (areaBeanList != null && areaBeanList.size() > 0) {
                    //请求子节点填充父节点
                    for (int i = 0; i < areaBeanList.size(); i++) {
                        TreeNode childNode = new TreeNode(areaBeanList.get(i)).setViewHolder(new CustomViewHolder(mContext));
                        parentNode.addChild(childNode);
                    }
                    //再次打开所有树，解决点击一次子叶子不出现的bug
                    mAndroidTreeView.expandNode(parentNode);
                } else {

                }
            }
        });
    }

    /**
     * @author Tianfy
     * @time 2017/9/15  14:42
     * @describe 初始化Dialog
     */
    private void initDialog() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setMessage("选择城市");
        LinearLayout contanier = new LinearLayout(mContext);
        contanier.setVerticalGravity(LinearLayout.VERTICAL);
        contanier.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        View view = mAndroidTreeView.getView();
        view.setPadding(30,0,0,0);
        contanier.addView(view);
        builder.setView(contanier);

        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                List<TreeNode> selectedNode = mAndroidTreeView.getSelected();
                if (selectedNode != null && selectedNode.size() > 0) {

                    AreaBean areaBean = (AreaBean) selectedNode.get(0).getValue();
                    mBtnShowArea.setText(areaBean.getText());
                    areaId = areaBean.getId();
                } else {
                    Toast.makeText(mContext, "请选择区域类型", Toast.LENGTH_SHORT).show();

                }
            }
        });
        mDialog = builder.create();
        setDialog(mDialog, view);
    }


    private long startTimeL;
    private long endTimeL;

    /**
     * @author Tianfy
     * @time 2017/9/4  17:05
     * @describe 对比结束时间是否大于开始时间，
     * 如果是第一次进入 默认取当日0点-当前时间 不是第一次进入或点击了查询按钮 取用户输入的时间 。
     */
    private void compareTime() {
        startTimeL = TimeUtil.convert2long(mTvStartTime.getText().toString(), TimeUtil.DATE_FORMAT);
        endTimeL = TimeUtil.convert2long(mTvEndTime.getText().toString(), TimeUtil.DATE_FORMAT);

        if (startTimeL >= endTimeL) {
            Toast.makeText(mContext, "结束时间应大于开始时间", Toast.LENGTH_SHORT).show();
        } else {
            query();
        }
    }

    /**
     * @author Tianfy
     * @time 2017/9/4  14:27
     * @describe 请求网络查询数据
     */
    private void query() {
        mStartTimeStr = TimeUtil.convert2String(startTimeL);
        mEndTimeStr = TimeUtil.convert2String(endTimeL);
//        mActivity.query(mStartTimeStr, mEndTimeStr, areaId, taskTypeId, QUERY_TYPE);
        dataQuery(mStartTimeStr, mEndTimeStr, areaId, taskTypeId, QUERY_TYPE);
    }

    protected abstract void dataQuery(String startTimeStr, String endTimeStr, String areaId, String taskTypeId, int QUERY_TYPE);


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_showArea:
                //弹出Dialog
                showDialog();
                break;
            case R.id.tv_startTime:
                mTimePickerUtil.showTimePicker(mTvStartTime);
                break;
            case R.id.tv_endTime:
                mTimePickerUtil.showTimePicker(mTvEndTime);
                break;
            case R.id.btn_query:
                compareTime();
                break;

        }
    }

    private void showDialog() {
        mDialog.show();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        int viewId = parent.getId();
        if (R.id.spinner == viewId) {//选择图标类型
            switch (position) {
                case 0:
                    HELLO_CHARTS_TYPE = 0;
                    break;
                case 1:
                    HELLO_CHARTS_TYPE = 1;
                    break;
                case 2:
                    HELLO_CHARTS_TYPE = 2;
                    break;
            }
            goneOrVisibility(HELLO_CHARTS_TYPE);

//            mActivity.goneOrVisibility(HELLO_CHARTS_TYPE);
        } else if (R.id.spinner_statistic == viewId) {//统计类型
            switch (position) {//区域查询
                case 0:
                    QUERY_TYPE = 1;
                    mContainerTaskType.setVisibility(View.GONE);
                    mContainerPic.setVisibility(View.VISIBLE);
                    break;
                case 1://类别查询
                    QUERY_TYPE = 2;
                    initTaskTypeData();
                    mContainerTaskType.setVisibility(View.VISIBLE);
                    mContainerPic.setVisibility(View.GONE);
                    break;
            }
            getQueryType(QUERY_TYPE);
        }
    }

    protected abstract void getQueryType(int QUERY_TYPE);

    //隐藏显示
    public abstract void goneOrVisibility(int HELLO_CHARTS_TYPE);

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    /**
     * 设置Dialog的大小和Dialog中TreeView的自适应 
     * @param dialog 对话框 
     * @param view 对话框中的TreeView 
     */
    public void setDialog(Dialog dialog, View view) {
        if (dialog != null) {
            //得到当前dialog对应的窗体  
            Window dialogWindow = dialog.getWindow();
            //管理器  
            WindowManager m = ((Activity) mContext).getWindowManager();
            //屏幕分辨率，获取屏幕宽、高用  
            Display d = m.getDefaultDisplay();
            //获取对话框当前的参数值  
            WindowManager.LayoutParams p = dialogWindow.getAttributes();
            //宽度设置为屏幕的0.8  
            p.width = (int) (d.getWidth() * 0.8);
            //获取TreeView的高度和当前屏幕的0.6进行比较，如果高，就自适应改变  
//            if(getTotalHeightofTerrViewView(view) > d.getHeight()*0.6){
            //得到TreeView的参数值  
            ViewGroup.LayoutParams params = view.getLayoutParams();
            //设置TreeView的高度是屏幕的一半  
            params.height = (int) (d.getHeight()*0.5);
            //设置  
            view.setLayoutParams(params);
//            }
            //设置Dialog的高度  
            dialogWindow.setAttributes(p);
        }
    }
}
