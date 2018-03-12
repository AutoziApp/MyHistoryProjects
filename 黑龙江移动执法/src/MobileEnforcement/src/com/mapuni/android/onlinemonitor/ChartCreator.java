package com.mapuni.android.onlinemonitor;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.achartengine.ChartFactory;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;

/**
 * 图表绘制器
 * 
 * @author 赵军辉
 * 
 */
public class ChartCreator extends AbstractDemoChart {
	Context c;
	private List<HashMap<String, Object>> data;

	/* 绘制图表的重要参数 */
	private XYMultipleSeriesRenderer render;// 折线图的整体渲染器
	private XYMultipleSeriesDataset dataset;// 用于绘制折线图的数据集
	/*******/
	private String chem;
	private String chemName;
	private double min;
	private double max;

	/**
	 * 构造方法 初始化一些成员变量
	 * 
	 * @param context
	 *            上下文
	 * @param data
	 *            数据为hashmap的list集合存放服务端返回来的json数据
	 * @param chemName
	 *            需要绘制折线图的检测因子的中文名
	 * @param chem
	 *            检测因子的化学式 为数据集中hashmap的一个键的值
	 */
	public ChartCreator(Context context, List<HashMap<String, Object>> data, String chemName, String chem) {
		this.c = context;
		this.data = data;
		this.chem = chem;
		this.chemName = chemName;
		dataset = new XYMultipleSeriesDataset();
		render = new XYMultipleSeriesRenderer();
	}

	/**
	 * 图表的绘制动作
	 * 
	 * @return 绘制好的图表的view对象
	 */
	public View create() {

		XYSeries s = new XYSeries(chem);

		int dateSize = data.size();
		for (int i = 0; i < dateSize; i++) {
			HashMap<String, Object> map = data.get(i);
			Object date = map.get("MonitorTime") == null ? map.get("MonitorTime") : map.get("MonitorTime");
			double value = Double.parseDouble(map.get(chem).toString());
			DecimalFormat df = new DecimalFormat("####0.0");
			value = Double.parseDouble(df.format(value));
			s.add(i, value);// 添加一个坐标点 指定xy的值
			String xDate = getTime(date.toString(), "");
			render.addXTextLabel(i, xDate);// 添加文字横坐标
											// i是原先添加坐标点的横坐标值
			if (min > value) {
				min = value;
			}
			if (max < value) {
				max = value;
			}
		}
		dataset.addSeries(s);
		setUpRender(render);
		return ChartFactory.getLineChartView(c, dataset, render);
	}

	/**
	 * 两个渲染器的属性设置
	 * 
	 * @param localXYMultipleSeriesRenderer
	 */
	private void setUpRender(XYMultipleSeriesRenderer localXYMultipleSeriesRenderer) {
		XYSeriesRenderer rend = new XYSeriesRenderer();
		rend.setColor(Color.BLACK);
		rend.setPointStyle(PointStyle.CIRCLE);
		rend.setFillPoints(true);
		rend.setDisplayChartValues(true);
		rend.setLineWidth(3.0f);
		rend.setChartValuesTextAlign(Paint.Align.CENTER);
		rend.setChartValuesTextSize(30.0f);

		localXYMultipleSeriesRenderer.addSeriesRenderer(rend);
		localXYMultipleSeriesRenderer.setMargins(new int[] { 20, 80, 50, 20 });// 右左下上
		localXYMultipleSeriesRenderer.setBackgroundColor(Color.WHITE);
		localXYMultipleSeriesRenderer.setApplyBackgroundColor(true);
		localXYMultipleSeriesRenderer.setFitLegend(true);
		localXYMultipleSeriesRenderer.setMarginsColor(Color.argb(0, 243, 243, 243));
		localXYMultipleSeriesRenderer.setChartTitleTextSize(30.0F);
		localXYMultipleSeriesRenderer.setAxisTitleTextSize(30.0F);
		localXYMultipleSeriesRenderer.setOrientation(XYMultipleSeriesRenderer.Orientation.HORIZONTAL);
		setChartSettings(localXYMultipleSeriesRenderer, chemName + "数据折线图", "    时间", chem + "浓度", 0.0D, 12.0D, min - 10, max + 10, -16777216, -16777216);
		localXYMultipleSeriesRenderer.setLabelsTextSize(28f);

		localXYMultipleSeriesRenderer.setXLabels(0);
		localXYMultipleSeriesRenderer.setXLabelsAngle(45);// 设置X轴标签倾斜度
		localXYMultipleSeriesRenderer.setYLabels(9);
		localXYMultipleSeriesRenderer.setShowGrid(true);
		localXYMultipleSeriesRenderer.setXLabelsAlign(Paint.Align.CENTER);
		localXYMultipleSeriesRenderer.setYLabelsAlign(Paint.Align.RIGHT);
		localXYMultipleSeriesRenderer.setPanEnabled(true);
		localXYMultipleSeriesRenderer.setPointSize(7f);
		localXYMultipleSeriesRenderer.setLegendHeight(70);
		localXYMultipleSeriesRenderer.setZoomButtonsVisible(false);
		localXYMultipleSeriesRenderer.setYTitle(chem + (ZxjcDetailActivity.isGas ? "浓度（mg/m3）" : "浓度（mg/L）"));
		localXYMultipleSeriesRenderer.setLegendTextSize(30.0f);
	}

	/* 得到时间小时部分 */
	private String getTime(String date, String formats) {
		SimpleDateFormat formater = new SimpleDateFormat("yyyyMMddHHmmss");
		Date res = null;
		try {
			res = formater.parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return new SimpleDateFormat("dd日HH时").format(res);
	}

}
