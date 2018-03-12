package com.mapuni.android.onlinemonitor;

import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.achartengine.chart.PointStyle;
import org.achartengine.model.CategorySeries;
import org.achartengine.model.MultipleCategorySeries;
import org.achartengine.model.TimeSeries;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.DefaultRenderer;
import org.achartengine.renderer.SimpleSeriesRenderer;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

public abstract class AbstractDemoChart {
	public void addXYSeries(XYMultipleSeriesDataset paramXYMultipleSeriesDataset, String[] paramArrayOfString, List<double[]> paramList1, List<double[]> paramList2, int paramInt) {
		int i = paramArrayOfString.length;
		int j = 0;
		if (j >= i)
			return;
		XYSeries localXYSeries = new XYSeries(paramArrayOfString[j], paramInt);
		double[] arrayOfDouble1 = (double[]) paramList1.get(j);
		double[] arrayOfDouble2 = (double[]) paramList2.get(j);
		int k = arrayOfDouble1.length;
		for (int m = 0;; m++) {
			if (m >= k) {
				paramXYMultipleSeriesDataset.addSeries(localXYSeries);
				j++;
				break;
			}
			localXYSeries.add(arrayOfDouble1[m], arrayOfDouble2[m]);
		}
	}

	protected XYMultipleSeriesDataset buildBarDataset(String[] paramArrayOfString, List<double[]> paramList) {
		XYMultipleSeriesDataset localXYMultipleSeriesDataset = new XYMultipleSeriesDataset();
		int i = paramArrayOfString.length;
		int j = 0;
		if (j >= i)
			return localXYMultipleSeriesDataset;
		CategorySeries localCategorySeries = new CategorySeries(paramArrayOfString[j]);
		double[] arrayOfDouble = (double[]) paramList.get(j);
		int k = arrayOfDouble.length;
		for (int m = 0;; m++) {
			if (m >= k) {
				localXYMultipleSeriesDataset.addSeries(localCategorySeries.toXYSeries());
				j++;
				break;
			}
			localCategorySeries.add(arrayOfDouble[m]);
		}
		return localXYMultipleSeriesDataset;
	}

	protected XYMultipleSeriesRenderer buildBarRenderer(int[] paramArrayOfInt) {
		XYMultipleSeriesRenderer localXYMultipleSeriesRenderer = new XYMultipleSeriesRenderer();
		localXYMultipleSeriesRenderer.setAxisTitleTextSize(16.0F);
		localXYMultipleSeriesRenderer.setChartTitleTextSize(20.0F);
		localXYMultipleSeriesRenderer.setLabelsTextSize(15.0F);
		localXYMultipleSeriesRenderer.setLegendTextSize(15.0F);
		int i = paramArrayOfInt.length;
		for (int j = 0;; j++) {
			if (j >= i)
				return localXYMultipleSeriesRenderer;
			SimpleSeriesRenderer localSimpleSeriesRenderer = new SimpleSeriesRenderer();
			localSimpleSeriesRenderer.setColor(paramArrayOfInt[j]);
			localXYMultipleSeriesRenderer.addSeriesRenderer(localSimpleSeriesRenderer);
		}
	}

	protected CategorySeries buildCategoryDataset(String paramString, double[] paramArrayOfDouble) {
		CategorySeries localCategorySeries = new CategorySeries(paramString);
		int i = 0;
		int j = paramArrayOfDouble.length;
		for (int k = 0;; k++) {
			if (k >= j)
				return localCategorySeries;
			double d = paramArrayOfDouble[k];
			StringBuilder localStringBuilder = new StringBuilder("Project ");
			i++;
			localCategorySeries.add(i + "", d);
		}
	}

	protected DefaultRenderer buildCategoryRenderer(int[] paramArrayOfInt) {
		DefaultRenderer localDefaultRenderer = new DefaultRenderer();
		localDefaultRenderer.setLabelsTextSize(15.0F);
		localDefaultRenderer.setLegendTextSize(15.0F);
		localDefaultRenderer.setMargins(new int[] { 20, 30, 15, 20 });
		int i = paramArrayOfInt.length;
		for (int j = 0;; j++) {
			if (j >= i)
				return localDefaultRenderer;
			int k = paramArrayOfInt[j];
			SimpleSeriesRenderer localSimpleSeriesRenderer = new SimpleSeriesRenderer();
			localSimpleSeriesRenderer.setColor(k);
			localDefaultRenderer.addSeriesRenderer(localSimpleSeriesRenderer);
		}
	}

	protected XYMultipleSeriesDataset buildDataset(String[] paramArrayOfString, List<double[]> paramList1, List<double[]> paramList2) {
		XYMultipleSeriesDataset localXYMultipleSeriesDataset = new XYMultipleSeriesDataset();
		addXYSeries(localXYMultipleSeriesDataset, paramArrayOfString, paramList1, paramList2, 0);
		return localXYMultipleSeriesDataset;
	}

	protected XYMultipleSeriesDataset buildDateDataset(String[] paramArrayOfString, List<Date[]> paramList, List<double[]> paramList1) {
		XYMultipleSeriesDataset localXYMultipleSeriesDataset = new XYMultipleSeriesDataset();
		int i = paramArrayOfString.length;
		int j = 0;
		if (j >= i)
			return localXYMultipleSeriesDataset;
		TimeSeries localTimeSeries = new TimeSeries(paramArrayOfString[j]);
		Date[] arrayOfDate = (Date[]) paramList.get(j);
		double[] arrayOfDouble = (double[]) paramList1.get(j);
		int k = arrayOfDate.length;
		for (int m = 0;; m++) {
			if (m >= k) {
				localXYMultipleSeriesDataset.addSeries(localTimeSeries);
				j++;
				break;
			}
			localTimeSeries.add(arrayOfDate[m], arrayOfDouble[m]);
		}
		return localXYMultipleSeriesDataset;
	}

	protected MultipleCategorySeries buildMultipleCategoryDataset(String paramString, List<String[]> paramList, List<double[]> paramList1) {
		MultipleCategorySeries localMultipleCategorySeries = new MultipleCategorySeries(paramString);
		int i = 0;
		Iterator localIterator = paramList1.iterator();
		while (true) {
			if (!localIterator.hasNext())
				return localMultipleCategorySeries;
			double[] arrayOfDouble = (double[]) localIterator.next();
			localMultipleCategorySeries.add((String[]) paramList.get(i), arrayOfDouble);
			i++;
		}
	}

	protected XYMultipleSeriesDataset buildMyDataset(String[] paramArrayOfString, List<double[]> paramList1, List<double[]> paramList2) {
		XYMultipleSeriesDataset localXYMultipleSeriesDataset = new XYMultipleSeriesDataset();
		addXYSeries(localXYMultipleSeriesDataset, paramArrayOfString, paramList1, paramList2, 0);
		return localXYMultipleSeriesDataset;
	}

	protected XYMultipleSeriesRenderer buildRenderer(int[] paramArrayOfInt, PointStyle[] paramArrayOfPointStyle) {
		XYMultipleSeriesRenderer localXYMultipleSeriesRenderer = new XYMultipleSeriesRenderer();
		setRenderer(localXYMultipleSeriesRenderer, paramArrayOfInt, paramArrayOfPointStyle);
		return localXYMultipleSeriesRenderer;
	}

	protected void setChartSettings(XYMultipleSeriesRenderer paramXYMultipleSeriesRenderer, String paramString1, String paramString2, String paramString3, double paramDouble1,
			double paramDouble2, double paramDouble3, double paramDouble4, int paramInt1, int paramInt2) {
		paramXYMultipleSeriesRenderer.setChartTitle(paramString1);
		paramXYMultipleSeriesRenderer.setXTitle(paramString2);
		paramXYMultipleSeriesRenderer.setYTitle(paramString3);
		paramXYMultipleSeriesRenderer.setXAxisMin(paramDouble1);
		paramXYMultipleSeriesRenderer.setXAxisMax(paramDouble2);
		paramXYMultipleSeriesRenderer.setYAxisMin(paramDouble3);
		paramXYMultipleSeriesRenderer.setYAxisMax(paramDouble4);
		paramXYMultipleSeriesRenderer.setAxesColor(paramInt1);
		paramXYMultipleSeriesRenderer.setLabelsColor(paramInt2);
	}

	protected void setRenderer(XYMultipleSeriesRenderer paramXYMultipleSeriesRenderer, int[] paramArrayOfInt, PointStyle[] paramArrayOfPointStyle) {
		paramXYMultipleSeriesRenderer.setAxisTitleTextSize(16.0F);
		paramXYMultipleSeriesRenderer.setChartTitleTextSize(20.0F);
		paramXYMultipleSeriesRenderer.setLabelsTextSize(15.0F);
		paramXYMultipleSeriesRenderer.setLegendTextSize(15.0F);
		paramXYMultipleSeriesRenderer.setPointSize(5.0F);
		paramXYMultipleSeriesRenderer.setMargins(new int[] { 20, 30, 15, 20 });
		int i = paramArrayOfInt.length;
		for (int j = 0;; j++) {
			if (j >= i)
				return;
			XYSeriesRenderer localXYSeriesRenderer = new XYSeriesRenderer();
			localXYSeriesRenderer.setColor(paramArrayOfInt[j]);
			localXYSeriesRenderer.setPointStyle(paramArrayOfPointStyle[j]);
			paramXYMultipleSeriesRenderer.addSeriesRenderer(localXYSeriesRenderer);
		}
	}
}