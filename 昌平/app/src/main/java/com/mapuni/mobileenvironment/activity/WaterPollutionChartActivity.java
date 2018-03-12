package com.mapuni.mobileenvironment.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.mapuni.mobileenvironment.R;

import java.util.ArrayList;
import java.util.List;

import lecho.lib.hellocharts.gesture.ContainerScrollType;
import lecho.lib.hellocharts.gesture.ZoomType;
import lecho.lib.hellocharts.listener.ColumnChartOnValueSelectListener;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Column;
import lecho.lib.hellocharts.model.ColumnChartData;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.SubcolumnValue;
import lecho.lib.hellocharts.model.ValueShape;
import lecho.lib.hellocharts.model.Viewport;
import lecho.lib.hellocharts.util.ChartUtils;
import lecho.lib.hellocharts.view.ColumnChartView;
import lecho.lib.hellocharts.view.LineChartView;

public class WaterPollutionChartActivity extends AppCompatActivity implements View.OnClickListener {

    private LineChartView lineChart;
    private ColumnChartView columnChart;
    private ColumnChartData columnData;
    private ImageView mBack;
    private TextView change;//图标切换按钮
//    String[] laberX = {"2016-12-08 ", "2016-12-08 01:00:00", "02:00", "03:00", "04:00", "05:00", "06:00", "07:00", "08:00", "09:00", "10:00", "11:00", "12:00", "13:00", "14:00", "15:00", "16:00", "17:00", "18:00", "19:00", "20:00", "21:00", "22:00", "23:00"};//Y轴的标注
//    int[] values = {50, 42, 90, 33, 10, 74, 22, 18, 79, 20, 34, 89, 124, 154, 23, 45, 77, 23, 78, 36, 65, 33, 67, 03};//图表的数据点
    private  ArrayList<String> values;
    private  ArrayList<String> laberX;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_water_pollution_chart);
        lineChart = (LineChartView) findViewById(R.id.linechart);
        columnChart = (ColumnChartView) findViewById(R.id.columnchart);
        change = (TextView) findViewById(R.id.change);
        mBack = (ImageView) findViewById(R.id.back);
        Intent _Intent = getIntent();
        values = (ArrayList<String>) ((ArrayList) _Intent.getExtras().get("value")).get(0);
        laberX = (ArrayList<String>) ((ArrayList) _Intent.getExtras().get("date")).get(0);
        initLineCharView();//初始化折线图
        dataInit();//初始化柱状图
        mBack.setOnClickListener(this);
        change.setOnClickListener(this);
    }


    private void initLineCharView() {
        List<PointValue> mPointValues = new ArrayList<>();//数据点坐标信息
        List<AxisValue> mAxisXValuesX = new ArrayList<>();//X轴刻度值集合
        for (int i = 0; i < values.size(); i++) {
            mPointValues.add(new PointValue(i, Float.valueOf(values.get(i))));
        }
        for (int i = 0; i < laberX.size(); i++) {
            mAxisXValuesX.add(new AxisValue(i).setLabel(laberX.get(i)).setLabel(laberX.get(i)));
        }

        Line line = new Line(mPointValues).setColor(Color.parseColor("#3967a6"));  //折线的颜色（橙色）
        List<Line> lines = new ArrayList<Line>();
        line.setStrokeWidth(2);
        line.setPointRadius(3);// 设置节点半径
        line.setShape(ValueShape.CIRCLE);//折线图上每个数据点的形状  这里是圆形 （有三种 ：ValueShape.SQUARE  ValueShape.CIRCLE  ValueShape.DIAMOND）
        line.setCubic(false);//曲线是否平滑，即是曲线还是折线
        line.setFilled(false);//是否填充曲线的面积
        line.setHasLabels(true);//曲线的数据坐标是否加上备注
//        line.setHasLabelsOnlyForSelected(true);//点击数据坐标提示数据（设置了这个line.setHasLabels(true);就无效）
        line.setHasLines(true);//是否用线显示。如果为false 则没有曲线只有点显示
        line.setHasPoints(true);//是否显示圆点 如果为false 则没有原点只有点显示（每个数据点都是个大的圆点）
        lines.add(line);
        LineChartData data = new LineChartData();
        data.setLines(lines);
        data.setValueLabelsTextColor(Color.parseColor("#3967a6"));//设置数据文字颜色
        data.setValueLabelBackgroundEnabled(false);//设置是否有数据背景
        //坐标轴
        Axis axisX = new Axis(); //X轴
        axisX.setHasTiltedLabels(true);  //X坐标轴字体是斜的显示还是直的，true是斜的显示
        axisX.setLineColor(Color.GRAY);
        axisX.setTextColor(Color.BLACK);  //设置字体颜色
        axisX.setName(" ");  //表格名称
        axisX.setTextSize(10);//设置字体大小
        axisX.setMaxLabelChars(5); //最多几个X轴坐标，意思就是你的缩放让X轴上数据的个数7<=x<=mAxisXValues.length
        axisX.setValues(mAxisXValuesX);  //填充X轴的坐标名称
        data.setAxisXBottom(axisX); //x 轴在底部
        axisX.setHasLines(true); //x 轴分割线
//        axisX.setInside(true);// 设置X轴文字是否在X轴内部

        // Y轴是根据数据的大小自动设置Y轴上限(在下面我会给出固定Y轴数据个数的解决方案)
        Axis axisY = new Axis();  //Y轴
        axisY.setName("");//y轴标注
        axisY.setLineColor(Color.GRAY);
        axisY.setTextColor(Color.BLACK);  //设置字体颜色
        axisY.setTextSize(10);//设置字体大小
        axisY.setHasLines(true); //x 轴分割线
        data.setAxisYLeft(axisY);  //Y轴设置在左边
        //data.setAxisYRight(axisY);  //y轴设置在右边

        //设置行为属性，支持缩放、滑动以及平移
        lineChart.setInteractive(true);
        lineChart.setZoomType(ZoomType.HORIZONTAL);
        lineChart.setMaxZoom((float) 3);//最大方法比例
        lineChart.setContainerScrollEnabled(true, ContainerScrollType.HORIZONTAL);
        lineChart.setLineChartData(data);

        /**注：下面的7，10只是代表一个数字去类比而已
         * 当时是为了解决X轴固定数据个数。见（http://forum.xda-developers.com/tools/programming/library-hellocharts-charting-library-t2904456/page2）;
         */
        Viewport v = new Viewport(lineChart.getMaximumViewport());
        v.left = 0;
        v.right = 23;
        lineChart.setCurrentViewport(v);
    }


    //初始化数据并显示在图表上
    private void dataInit() {

        int numSubcolumns = 1;
        int numColumns = laberX.size();

        List<AxisValue> axisValues = new ArrayList<AxisValue>();
        List<Column> columns = new ArrayList<Column>();
        List<SubcolumnValue> values;
        for (int i = 0; i < numColumns; ++i) {

            values = new ArrayList<SubcolumnValue>();
            for (int j = 0; j < numSubcolumns; ++j) {
                values.add(new SubcolumnValue((float) Math.random() * 50f + 5,
                        ChartUtils.pickColor()));
            }
            // 点击柱状图就展示数据量
            axisValues.add(new AxisValue(i).setLabel((String)laberX.get(i)));

            columns.add(new Column(values).setHasLabelsOnlyForSelected(true));
        }

        columnData = new ColumnChartData(columns);

        columnData.setAxisXBottom(new Axis(axisValues).setHasLines(true)
                .setTextColor(Color.BLACK));
        columnData.setAxisYLeft(new Axis().setHasLines(true)
                .setTextColor(Color.BLACK).setMaxLabelChars(2));

        columnChart.setColumnChartData(columnData);

// Set value touch listener that will trigger changes for chartTop.
        columnChart.setOnValueTouchListener(new ValueTouchListener());

// Set selection mode to keep selected month column highlighted.
        columnChart.setValueSelectionEnabled(true);

        columnChart.setZoomType(ZoomType.HORIZONTAL);

    }

    @Override
    public void onClick(View view) {
        if(view.getId()==R.id.change){
            if (change.getText().toString().equals("曲线图")) {
                lineChart.setVisibility(View.VISIBLE);
                columnChart.setVisibility(View.GONE);
                change.setText("柱状图");
            } else {
                lineChart.setVisibility(View.GONE);
                columnChart.setVisibility(View.VISIBLE);
                change.setText("曲线图");
            }
        }
        if(view.getId()==R.id.back){
            finish();
        }
    }

    /**
     * 柱状图监听器
     *
     * @author 1017
     */
    private class ValueTouchListener implements
            ColumnChartOnValueSelectListener {

        @Override
        public void onValueSelected(int columnIndex, int subcolumnIndex,
                                    SubcolumnValue value) {
// generateLineData(value.getColor(), 100);
        }

        @Override
        public void onValueDeselected() {

// generateLineData(ChartUtils.COLOR_GREEN, 0);

        }
    }
}
