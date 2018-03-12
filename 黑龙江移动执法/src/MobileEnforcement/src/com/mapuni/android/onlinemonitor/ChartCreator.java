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
 * ͼ�������
 * 
 * @author �Ծ���
 * 
 */
public class ChartCreator extends AbstractDemoChart {
	Context c;
	private List<HashMap<String, Object>> data;

	/* ����ͼ�����Ҫ���� */
	private XYMultipleSeriesRenderer render;// ����ͼ��������Ⱦ��
	private XYMultipleSeriesDataset dataset;// ���ڻ�������ͼ�����ݼ�
	/*******/
	private String chem;
	private String chemName;
	private double min;
	private double max;

	/**
	 * ���췽�� ��ʼ��һЩ��Ա����
	 * 
	 * @param context
	 *            ������
	 * @param data
	 *            ����Ϊhashmap��list���ϴ�ŷ���˷�������json����
	 * @param chemName
	 *            ��Ҫ��������ͼ�ļ�����ӵ�������
	 * @param chem
	 *            ������ӵĻ�ѧʽ Ϊ���ݼ���hashmap��һ������ֵ
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
	 * ͼ��Ļ��ƶ���
	 * 
	 * @return ���ƺõ�ͼ���view����
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
			s.add(i, value);// ���һ������� ָ��xy��ֵ
			String xDate = getTime(date.toString(), "");
			render.addXTextLabel(i, xDate);// ������ֺ�����
											// i��ԭ����������ĺ�����ֵ
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
	 * ������Ⱦ������������
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
		localXYMultipleSeriesRenderer.setMargins(new int[] { 20, 80, 50, 20 });// ��������
		localXYMultipleSeriesRenderer.setBackgroundColor(Color.WHITE);
		localXYMultipleSeriesRenderer.setApplyBackgroundColor(true);
		localXYMultipleSeriesRenderer.setFitLegend(true);
		localXYMultipleSeriesRenderer.setMarginsColor(Color.argb(0, 243, 243, 243));
		localXYMultipleSeriesRenderer.setChartTitleTextSize(30.0F);
		localXYMultipleSeriesRenderer.setAxisTitleTextSize(30.0F);
		localXYMultipleSeriesRenderer.setOrientation(XYMultipleSeriesRenderer.Orientation.HORIZONTAL);
		setChartSettings(localXYMultipleSeriesRenderer, chemName + "��������ͼ", "    ʱ��", chem + "Ũ��", 0.0D, 12.0D, min - 10, max + 10, -16777216, -16777216);
		localXYMultipleSeriesRenderer.setLabelsTextSize(28f);

		localXYMultipleSeriesRenderer.setXLabels(0);
		localXYMultipleSeriesRenderer.setXLabelsAngle(45);// ����X���ǩ��б��
		localXYMultipleSeriesRenderer.setYLabels(9);
		localXYMultipleSeriesRenderer.setShowGrid(true);
		localXYMultipleSeriesRenderer.setXLabelsAlign(Paint.Align.CENTER);
		localXYMultipleSeriesRenderer.setYLabelsAlign(Paint.Align.RIGHT);
		localXYMultipleSeriesRenderer.setPanEnabled(true);
		localXYMultipleSeriesRenderer.setPointSize(7f);
		localXYMultipleSeriesRenderer.setLegendHeight(70);
		localXYMultipleSeriesRenderer.setZoomButtonsVisible(false);
		localXYMultipleSeriesRenderer.setYTitle(chem + (ZxjcDetailActivity.isGas ? "Ũ�ȣ�mg/m3��" : "Ũ�ȣ�mg/L��"));
		localXYMultipleSeriesRenderer.setLegendTextSize(30.0f);
	}

	/* �õ�ʱ��Сʱ���� */
	private String getTime(String date, String formats) {
		SimpleDateFormat formater = new SimpleDateFormat("yyyyMMddHHmmss");
		Date res = null;
		try {
			res = formater.parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return new SimpleDateFormat("dd��HHʱ").format(res);
	}

}
