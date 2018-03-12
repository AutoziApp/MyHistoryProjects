package com.mapuni.administrator.activity.gridMgAc;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mapuni.administrator.R;
import com.mapuni.administrator.activity.BaseActivity;
import com.mapuni.administrator.bean.AreaBean;
import com.mapuni.administrator.bean.Trend;
import com.mapuni.administrator.fragment.TaskStatisticsFragment;
import com.mapuni.administrator.manager.NetManager;
import com.mapuni.administrator.utils.Logger;
import com.mapuni.administrator.utils.MyChartUtil;
import com.mapuni.administrator.utils.SPUtils;
import com.mapuni.administrator.utils.TimePickerUtil;
import com.mapuni.administrator.utils.TimeUtil;
import com.mapuni.administrator.view.treeViewHolder.CustomViewHolder;
import com.unnamed.b.atv.model.TreeNode;
import com.unnamed.b.atv.view.AndroidTreeView;
import com.zhy.http.okhttp.callback.StringCallback;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import lecho.lib.hellocharts.gesture.ZoomType;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.ValueShape;
import lecho.lib.hellocharts.model.Viewport;
import lecho.lib.hellocharts.view.LineChartView;
import okhttp3.Call;

public class ChangeTrendActivity extends BaseActivity implements View.OnClickListener{

    private GridView mGvPic;
    //时间选择
    private TextView mTvStartTime;
    private TextView mTvEndTime;
    private ImageView mBtnQuery;
    private TimePickerUtil mTimePickerUtil;
    //区域选择
    private TextView mBtnShowArea;
    private String mSessionId;
    private Gson mGson;
    private TreeNode mRoot;
    private AlertDialog mDialog;
    private AndroidTreeView mAndroidTreeView;
    //折线图
    private LineChartView chart;
    private LineChartData data;

    List<Float> values = new ArrayList<>();//折线图y值
    List<String> kedu = new ArrayList<>();//x轴刻度值

    //接口参数
    String startTime="";
    String endTime="";
    String patrolProblemDictUuids="";
    String gridUuid="";
    private WindowManager.LayoutParams mP;

    @Override
    public int setLayoutResID() {
        return R.layout.activity_change_trend;
    }

    @Override
    public void initView() {
        setToolbarTitle("变化趋势");
        mTvStartTime = (TextView) findViewById(R.id.tv_startTime);
        mTvEndTime = (TextView) findViewById(R.id.tv_endTime);
        mBtnQuery = (ImageView) findViewById(R.id.btn_query);
        mBtnShowArea = (TextView) findViewById(R.id.btn_showArea);
        chart = (LineChartView) findViewById(R.id.linechart);
        chart.setBackgroundColor(Color.parseColor("#ffffff"));
        chart.setZoomType(ZoomType.HORIZONTAL);

//        for(int i=1;i<=20;i++){
//            values.add((float) i);
//            kedu.add(i+"");
//        }
//        generateData(values,kedu);

        initListener();
        //获取前7天的时间-当天当前时间 默认请求网络
        mTvStartTime.setText(TimeUtil.getDateBefore(7));
        mTvEndTime.setText(TimeUtil.getStrToday());
        initGVPicData();
    }

    private int[] taskColors = {R.color.task_color1, R.color.task_color2,
            R.color.task_color3, R.color.task_color4, R.color.task_color5,
            R.color.task_color6, R.color.task_color7, R.color.task_color8};

    private void initGVPicData() {
        mGvPic= (GridView) findViewById(R.id.gv_pic);
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
            TaskPicAdapter.ViewHolder viewHolder = null;
            if (convertView == null) {
                viewHolder = new TaskPicAdapter.ViewHolder();
                convertView = View.inflate(mContext, R.layout.layout_pic_item, null);
                viewHolder.picView = convertView.findViewById(R.id.vPic_color);
                viewHolder.tvTaskTypePic = (TextView) convertView.findViewById(R.id.tv_taskTypePic);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (TaskPicAdapter.ViewHolder) convertView.getTag();
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

    //更新折线图
    private void generateData(List<Trend> dataList ) {

        if(dataList==null||dataList.size()==0){
            return;
        }
        List<Line> lines = new ArrayList<Line>();
        List<String> typeList=new ArrayList<>();
        typeList.add("企事业单位和其他生产经营者");
        typeList.add("施工工地");
        typeList.add("各类道路");
        typeList.add("散乱污企业");
        typeList.add("公共水体");
        typeList.add("畜牧养殖单位");
        typeList.add("秸秆垃圾焚烧");
        typeList.add("其他问题");

        List<String> keduList=new ArrayList<>();//横轴
        for (Trend trend:dataList){
            keduList.add(trend.getCorrespondingTime());
        }

        for (int i=0;i<typeList.size();i++){
            List<PointValue> values = new ArrayList<PointValue>();
            for (int j = 0; j < dataList.size(); ++j){
                for (Trend.PatrolProblemDictCountBean bean:dataList.get(j).getPatrolProblemDictCount()){
                    if(bean.getName().equals(typeList.get(i))){
                        values.add(new PointValue(j,0 ).setTarget(j,bean.getPatrolProblemDictCount()));
                    }
                }
            }
            Line line = new Line(values);
            line.setColor(MyChartUtil.mypickColor(i));
            line.setShape(ValueShape.CIRCLE);
            line.setPointRadius(3);
            line.setStrokeWidth(1);
            line.setCubic(true);//设置折线是否平滑
            line.setFilled(false);
            line.setHasLabels(false);
            line.setHasLabelsOnlyForSelected(false);
            line.setHasLines(true);
            line.setHasPoints(true);
            line.setPointColor(MyChartUtil.mypickColor(i));
            line.setHasLabels(true);
//            line.setHasLabelsOnlyForSelected(true);//点击数据坐标提示数据（设置了这个line.setHasLabels(true);就无效）
            lines.add(line);
        }

//        List<PointValue> values = new ArrayList<PointValue>();
//        for (int j = 0; j < randomNumbersTab.size(); ++j) {
//            values.add(new PointValue(j, randomNumbersTab.get(j)));
//        }
//        Line line = new Line(values);
//        line.setColor(Color.parseColor("#3967a6"));
//        line.setShape(ValueShape.CIRCLE);
//        line.setPointRadius(3);
//        line.setStrokeWidth(1);
//        line.setCubic(false);//设置折线是否平滑
//        line.setFilled(false);
//        line.setHasLabels(false);
//        line.setHasLabelsOnlyForSelected(false);
//        line.setHasLines(true);
//        line.setHasPoints(true);
//        line.setPointColor(Color.parseColor("#3967a6"));
//        line.setHasLabels(true);
////        line.setHasLabelsOnlyForSelected(true);//点击数据坐标提示数据（设置了这个line.setHasLabels(true);就无效）
//        lines.add(line);
//


        data = new LineChartData(lines);

        List<AxisValue> mAxisValues = new ArrayList<AxisValue>();
        for (int i = 0; i < keduList.size(); i++) {
            mAxisValues.add(new AxisValue(i).setLabel(keduList.get(i)));
        }
        Axis axisX = new Axis().setTextSize(10).setHasLines(false).setTextColor(Color.parseColor("#666666"));
        axisX.setValues(mAxisValues);  //填充X轴的坐标名称
        axisX.setHasTiltedLabels(true);  //X坐标轴字体是斜的显示还是直的，true是斜的显示
        axisX.setMaxLabelChars(2);
        Axis axisY = new Axis().setHasLines(false).setTextSize(10).setTextColor(Color.parseColor("#666666"));
//        data.setValueLabelBackgroundColor(Color.parseColor("#00000000"));
        data.setValueLabelBackgroundAuto(true);
        data.setAxisXBottom(axisX);
        data.setAxisYLeft(axisY);
        data.setBaseValue(Float.NEGATIVE_INFINITY);
        chart.setLineChartData(data);
//        chart.startDataAnimation();
        /**注：下面的7，10只是代表一个数字去类比而已
         * 当时是为了解决X轴固定数据个数。见（http://forum.xda-developers.com/tools/programming/library-hellocharts-charting-library-t2904456/page2）;
         */
        Viewport v = new Viewport(chart.getMaximumViewport());
        v.left = 0;
        v.right = 6;
        chart.setCurrentViewport(v);
        chart.startDataAnimation();
    }

    @Override
    public void initData() {
        mGson = new Gson();
        mSessionId = (String) SPUtils.getSp(mContext, "sessionId", "");
        mTimePickerUtil = new TimePickerUtil(mContext);
        //初始化城市筛选条件
        mRoot = TreeNode.root();
        initCityData(id);
    }



    private void initListener() {
        mBtnShowArea.setOnClickListener(this);
        mTvStartTime.setOnClickListener(this);
        mTvEndTime.setOnClickListener(this);
        mBtnQuery.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_startTime:
                mTimePickerUtil.showTimePicker(mTvStartTime);
                break;
            case R.id.tv_endTime:
                mTimePickerUtil.showTimePicker(mTvEndTime);
                break;
            case R.id.btn_showArea:
                //弹出Dialog
                showDialog();
                break;
            case R.id.btn_query:

                requestData();
                break;
        }
    }

    private void requestData() {
        startTime=mTvStartTime.getText().toString();
        endTime=mTvEndTime.getText().toString();
        mSvProgressHUD.showWithStatus("正在加载...");
        NetManager.variationTrend(mSessionId, startTime, endTime, "", gridUuid, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
//                Log.i("aaa",e.getMessage());
                mSvProgressHUD.dismiss();
            }

            @Override
            public void onResponse(String response, int id) {
                Log.i("aaa",response);
                mSvProgressHUD.dismiss();
                Type type = new TypeToken<ArrayList<Trend>>() {}.getType();
                List<Trend> list=new Gson().fromJson(response,type);

                generateData(list);
            }
        });

    }

    /**
     * @param id
     * @author Tianfy
     * @time 2017/9/14  11:25
     * @describe 初始化城市筛选条件
     */
    //默认id为空
    private String id = "";
    TreeNode mParentNode;
    private void initCityData(String id) {

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
                    //请求父节点填充父级目录
                    mParentNode = new TreeNode(areaBeanList.get(0)).setViewHolder(new CustomViewHolder(mContext));

                    mRoot.addChild(mParentNode);
                    mAndroidTreeView = new AndroidTreeView(mContext, mRoot);
                    //打开复选框开关
                    mAndroidTreeView.setSelectionModeEnabled(true);
                    initAreaType(areaBeanList);
                    initDialog();
                    mAndroidTreeView.setDefaultNodeClickListener(new TreeNode.TreeNodeClickListener() {
                        @Override
                        public void onClick(TreeNode node, Object value) {
                            AreaBean areaBean= (AreaBean) value;
                            //bean中的isLeaf和node中的isLeaf相反
                            if (!areaBean.isLeaf()){//有子
                                if(node.isLeaf()){//有子
                                    requestChildNode(((AreaBean) value).getId(),node);
                                }

                            }
                        }
                    });
                    requestData();
                }
            }
        });
    }

    /**
     * @param
     * @param areaBeanList
     * @author Tianfy
     * @time 2017/9/18  8:52
     * @describe 初始化区域类型
     */
    private void initAreaType(List<AreaBean> areaBeanList) {
        String areaStr = areaBeanList.get(0).getText();
        gridUuid=areaBeanList.get(0).getId();
        mBtnShowArea.setText(areaStr);
    }
    /**
     *  @author Tianfy
     *  @time 2017/9/15  14:41
     *  @describe 请求子节点数据并添加到父节点的TreeView中
     *  @param id 父节点id
     *  @param parentNode 父节点
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
                    for (int i = 0; i <areaBeanList.size() ; i++) {
                        TreeNode childNode = new TreeNode(areaBeanList.get(i)).setViewHolder(new CustomViewHolder(mContext));
                        parentNode.addChild(childNode);
                    }
                    //再次打开所有树，解决点击一次子叶子不出现的bug
                    mAndroidTreeView.expandNode(parentNode);
                }else{

                }
            }
        });
    }
    /**
     *  @author Tianfy
     *  @time 2017/9/15  14:42
     *  @describe 初始化Dialog
     */
    private void initDialog() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setMessage("选择城市");
        View treeContainer = View.inflate(mContext, R.layout.layout_tree_container, null);
        LinearLayout llContianer= (LinearLayout) treeContainer.findViewById(R.id.ll_container);
//        LinearLayout contanier=new LinearLayout(mContext);
//        contanier.setVerticalGravity(LinearLayout.VERTICAL);
//        contanier.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        View treeView = mAndroidTreeView.getView();
        treeView.setPadding(30,0,0,0);
        if (llContianer.getParent() != null) {
            ((ViewGroup) llContianer.getParent()).removeAllViews();
        }
        llContianer.addView(treeView);
        
        builder.setView(llContianer);
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
//                Toast.makeText(mContext, mAndroidTreeView.getSelected().size() + " selected", Toast.LENGTH_SHORT).show();
                if(mAndroidTreeView.getSelected().size()>0){
                    AreaBean bean= (AreaBean) mAndroidTreeView.getSelected().get(0).getValue();
                    gridUuid=bean.getId();
                    mBtnShowArea.setText(bean.getText());
                }
            }
        });
        mDialog = builder.create();
        setDialog(mDialog,treeView);
    }

    private void showDialog() {
        mDialog.show();
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
