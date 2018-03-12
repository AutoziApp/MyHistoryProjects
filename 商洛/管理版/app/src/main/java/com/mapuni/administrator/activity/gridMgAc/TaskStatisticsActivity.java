package com.mapuni.administrator.activity.gridMgAc;

import android.graphics.Color;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mapuni.administrator.R;
import com.mapuni.administrator.activity.BaseActivity;
import com.mapuni.administrator.bean.TaskSumBean;
import com.mapuni.administrator.fragment.TaskStatisticsFragment;
import com.mapuni.administrator.manager.NetManager;
import com.mapuni.administrator.utils.Logger;
import com.mapuni.administrator.utils.MyChartUtil;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import lecho.lib.hellocharts.gesture.ZoomType;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Column;
import lecho.lib.hellocharts.model.ColumnChartData;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PieChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.SliceValue;
import lecho.lib.hellocharts.model.SubcolumnValue;
import lecho.lib.hellocharts.model.ValueShape;
import lecho.lib.hellocharts.view.ColumnChartView;
import lecho.lib.hellocharts.view.LineChartView;
import lecho.lib.hellocharts.view.PieChartView;
import okhttp3.Call;

/**
 * @author Tianfy
 * @time 2017/9/7  11:32
 * @describe 任务统计Activity
 */
public class TaskStatisticsActivity extends BaseActivity implements Toolbar.OnMenuItemClickListener {
    //饼状图
    private PieChartData data;

    private boolean hasLabels = true;//是否显示标签
    private boolean hasLabelsOutside = false;//设置是否将标签显示在饼状图外侧。False显示在内测，true显示在外侧。
    private boolean hasCenterCircle = false;//设置饼状图中心是否为空心圆
    private boolean isExploded = false;
    private boolean hasLabelForSelected = false;//是否只有在点击“点”时才显示标签
    //柱状图

    private ColumnChartData data2;


    //折线图
    private LineChartData data3;
    private int numberOfLines = 1;
    private int maxNumberOfLines = 4;
    private int numberOfPoints = 12;

    float[][] randomNumbersTab = new float[maxNumberOfLines][numberOfPoints];

    private boolean hasAxes = true;
    private boolean hasAxesNames = true;
    private boolean hasLines = true;
    private boolean hasPoints = true;
    private ValueShape shape = ValueShape.CIRCLE;
    private boolean isFilled = false;
    private boolean isCubic = false;
    private boolean pointsHaveDifferentColor;
    private boolean hasGradientToTransparent = false;

    @BindView(R.id.app_bar)
    AppBarLayout mAppBar;
    @BindView(R.id.content_frame)
    RelativeLayout mContentFrame;
    @BindView(R.id.nav_view)
    RelativeLayout mNavView;
    @BindView(R.id.tv_noData)
    TextView mTvNoData;
    private DrawerLayout mDrawerLayout;
    private TaskStatisticsFragment mTabFragment;

    private PieChartView mPieChartView;
    private ColumnChartView mColumnChartView;
    private LineChartView mLineChartView;
    private int[] taskColors = {R.color.task_color1, R.color.task_color2,
            R.color.task_color3, R.color.task_color4, R.color.task_color5,
            R.color.task_color6, R.color.task_color7, R.color.task_color8};
    private int mQueryType;

    @Override
    public int setLayoutResID() {
        return R.layout.activity_task_statistics;
    }


    @Override
    public void initView() {
        setToolbarTitle("任务统计");
        //初始化DrawerLayout
        initDreawerLayout();

        mToolbar.setOnMenuItemClickListener(this);
        mPieChartView = (PieChartView) findViewById(R.id.chart_pie);
        mColumnChartView = (ColumnChartView) findViewById(R.id.chart_column);
        mLineChartView = (LineChartView) findViewById(R.id.chart_line);

        mPieChartView.setChartRotationEnabled(true);//设置饼状图是否可以通过滑动来旋转
        mColumnChartView.setZoomType(ZoomType.HORIZONTAL);//设置缩放类型
//        mPieChartView.setOnValueTouchListener(new PieChartOnValueSelectListener() {
//            @Override
//            public void onValueSelected(int arcIndex, SliceValue value) {
//                char[] labelAsChars = value.getLabelAsChars();
//                Toast.makeText(mContext, labelAsChars[arcIndex], Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onValueDeselected() {
//
//            }
//        });
    }

    private void initDreawerLayout() {
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager
                .beginTransaction();
        mTabFragment = new TaskStatisticsFragment();
        fragmentTransaction.replace(R.id.nav_view, mTabFragment).commit();

        mDrawerLayout.setDrawerListener(new DrawerLayout.SimpleDrawerListener() {
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }
        });
        //默认打开侧边栏
//        mDrawerLayout.openDrawer(Gravity.RIGHT);
    }

    @Override
    public void initData() {

    }

    public void showProgressDialog() {
        mSvProgressHUD.showWithStatus("正在加载...");
    }

    public void dissProgressDialog() {
        mSvProgressHUD.dismiss();
    }

    public void inviliProgressDialog() {
        mSvProgressHUD.dismissImmediately();
    }

    /**
     * @param startTime  开始时间
     * @param endTime    结束时间
     * @param areaId     区域uuid
     * @param taskType   任务类型uuid
     * @param searchType 查询条件
     * @author Tianfy
     * @time 2017/9/20  10:05
     * @describe 请求任务统计数据
     */
    public void query(String startTime, String endTime, String areaId, String taskType, int searchType) {
        if (mDrawerLayout.isDrawerOpen(Gravity.RIGHT)) {
            mDrawerLayout.closeDrawer(Gravity.RIGHT);
        }
        mSvProgressHUD.showWithStatus("正在加载...");
        NetManager.requestAreaData(startTime, endTime, areaId, taskType, searchType, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                Toast.makeText(mContext, "请求数据失败", Toast.LENGTH_SHORT).show();
                Logger.e(TaskStatisticsFragment.class.getSimpleName(), e.toString());
                mSvProgressHUD.dismissImmediately();
                ((TaskStatisticsActivity) mContext).finish();
            }

            @Override
            public void onResponse(String response, int id) {
//                Toast.makeText(mContext, response.toString(), Toast.LENGTH_SHORT).show();
                mSvProgressHUD.dismissImmediately();
                //关闭侧边栏
//                mDrawerLayout.closeDrawer(Gravity.RIGHT);
                TaskSumBean taskSumBean = gson.fromJson(response.toString(), TaskSumBean.class);
                List<TaskSumBean.RowsBean> rows = taskSumBean.getRows();
                if (rows != null && rows.size() > 0) {
                    mTvNoData.setVisibility(View.GONE);
                    generateData(rows);//饼状图
                    generateDefaultData(rows);//柱状图
                    generateData2(rows);//折线图
                } else {
                    mTvNoData.setVisibility(View.VISIBLE);
                }
            }
        });
    }
    /**
     *  @author Tianfy
     *  @time 2017/9/23  16:17
     *  @describe 饼状图
     *  @param rows
     */
    private void generateData(List<TaskSumBean.RowsBean> rows) {
        int numValues = rows.size();
        List<SliceValue> values = new ArrayList<SliceValue>();
        for (int i = 0; i < numValues; i++) {
            SliceValue sliceValue = new SliceValue(rows.get(i).getProblemTypeCount(), MyChartUtil.mypickColor(i));
            if (mQueryType == 1) {//区域
                sliceValue.setLabel(rows.get(i).getProblemTypeName()+rows.get(i).getProblemTypeCount());
            }else{
                sliceValue.setLabel((String) rows.get(i).getGridName()+rows.get(i).getProblemTypeCount());
            }
            values.add(sliceValue);
        }
        data = new PieChartData(values);
        data.setHasLabels(hasLabels);
        data.setHasLabelsOnlyForSelected(hasLabelForSelected);
        data.setHasLabelsOutside(hasLabelsOutside);
        data.setHasCenterCircle(hasCenterCircle);
        data.setHasLabelsOnlyForSelected(false);//不用点击显示占的百分比  
        data.setHasLabelsOutside(false);//占的百分比是否显示在饼图外面  
        
        if (isExploded) {
            data.setSlicesSpacing(24);//设置扇形之间的间距大小
        }

        mPieChartView.setPieChartData(data);
    }
    /**
     *  @author Tianfy
     *  @time 2017/9/23  16:17
     *  @describe 柱状图 
     *  @param rows
     */
    private void generateDefaultData(List<TaskSumBean.RowsBean> rows) {
        int numSubcolumns = 1;
        int numColumns = rows.size();
        // Column can have many subcolumns, here by default I use 1 subcolumn in each of 8 columns.
        List<Column> columns = new ArrayList<Column>();
        List<AxisValue> axisValues = new ArrayList<AxisValue>();
        List<SubcolumnValue> values;
        for (int i = 0; i < numColumns; i++) {
            values = new ArrayList<SubcolumnValue>();
            SubcolumnValue subcolumnValue = new SubcolumnValue(rows.get(i).getProblemTypeCount(), MyChartUtil.mypickColor(i));
            values.add(subcolumnValue);
            if (mQueryType == 1) {//区域
                axisValues.add(new AxisValue(i).setLabel(rows.get(i).getProblemTypeName()));
            } else {//类型
                axisValues.add(new AxisValue(i).setLabel((String) rows.get(i).getGridName()));
            }

            Column column = new Column(values);
            column.setHasLabels(hasLabels);//设置是否显示坐标网格
            column.setHasLabelsOnlyForSelected(hasLabelForSelected);//设置选中时是否显示坐标网格
            columns.add(column);
        }
        
        data2 = new ColumnChartData(columns);

        if (hasAxes) {
            Axis axisX = new Axis().setTextSize(10).setHasLines(true).setTextColor(Color.parseColor("#666666"));
            Axis axisY = new Axis().setTextSize(10).setHasLines(true).setTextColor(Color.parseColor("#666666"));
            axisX.setValues(axisValues);
            axisX.setHasTiltedLabels(true);  //X坐标轴字体是斜的显示还是直的，true是斜的显示
            if (hasAxesNames) {
                axisX.setName("");//设置X轴的名称，默认为空不显示。
                axisY.setName("");//设置Y轴的名称，默认为空不显示。
            }
            data2.setAxisXBottom(axisX);
            data2.setAxisYLeft(axisY);
        } else {
            data2.setAxisXBottom(null);
            data2.setAxisYLeft(null);
        }

        mColumnChartView.setColumnChartData(data2);

    }

    /**
     *  @author Tianfy
     *  @time 2017/9/23  16:17
     *  @describe 折线图 
     *  @param rows
     */
    private void generateData2(List<TaskSumBean.RowsBean> rows) {

        List<Line> lines = new ArrayList<Line>();
        List<PointValue> values = new ArrayList<PointValue>();
        List<AxisValue> mAxisValues = new ArrayList<AxisValue>();

        for (int i = 0; i < rows.size(); i++) {
            values.add(new PointValue(i, (float) rows.get(i).getProblemTypeCount()));
            if (mQueryType == 1) {//区域
                mAxisValues.add(new AxisValue(i).setLabel(rows.get(i).getProblemTypeName()));
            } else {//类型
                mAxisValues.add(new AxisValue(i).setLabel((String) rows.get(i).getGridName()));
            }
        }
        Line line = new Line(values);
        line.setColor(Color.parseColor("#3967a6"));
        line.setShape(shape);
        line.setCubic(isCubic);
        line.setFilled(isFilled);
        line.setHasLabels(hasLabels);
        line.setHasLabelsOnlyForSelected(hasLabelForSelected);
        line.setHasLines(hasLines);
        line.setHasPoints(hasPoints);

        lines.add(line);
        data3 = new LineChartData(lines);
        Axis axisX = new Axis().setTextSize(10).setHasLines(false).setTextColor(Color.parseColor("#666666"));
        axisX.setValues(mAxisValues);  //填充X轴的坐标名称
        axisX.setHasTiltedLabels(true);  //X坐标轴字体是斜的显示还是直的，true是斜的显示
        axisX.setMaxLabelChars(2);
        Axis axisY = new Axis().setHasLines(false).setTextSize(10).setTextColor(Color.parseColor("#666666"));

        data3.setValueLabelBackgroundAuto(true);
        data3.setAxisXBottom(axisX);
        data3.setAxisYLeft(axisY);
        data3.setBaseValue(Float.NEGATIVE_INFINITY);

        mLineChartView.setLineChartData(data3);

    }


    public void goneOrVisibility(int helloChartsType) {
        switch (helloChartsType) {
            case 0:
                mPieChartView.setVisibility(View.GONE);
                mColumnChartView.setVisibility(View.VISIBLE);
                mLineChartView.setVisibility(View.GONE);
                break;
            case 1:
                mPieChartView.setVisibility(View.GONE);
                mColumnChartView.setVisibility(View.GONE);
                mLineChartView.setVisibility(View.VISIBLE);
                break;
            case 2:
                mPieChartView.setVisibility(View.VISIBLE);
                mColumnChartView.setVisibility(View.GONE);
                mLineChartView.setVisibility(View.GONE);
                break;
        }
        //每次切换关闭侧边栏
        if (mDrawerLayout.isDrawerOpen(Gravity.RIGHT)) {
            mDrawerLayout.closeDrawer(Gravity.RIGHT);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }


    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_query:
                mDrawerLayout.openDrawer(Gravity.RIGHT);
                break;
        }
        return true;

    }

    public void setQueryType(int queryType) {
        mQueryType = queryType;
    }



}
