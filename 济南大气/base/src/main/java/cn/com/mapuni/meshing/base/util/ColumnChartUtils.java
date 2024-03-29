package cn.com.mapuni.meshing.base.util;

import android.graphics.Color;

import java.util.ArrayList;
import java.util.List;

import lecho.lib.hellocharts.gesture.ZoomType;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Column;
import lecho.lib.hellocharts.model.ColumnChartData;
import lecho.lib.hellocharts.model.SubcolumnValue;
import lecho.lib.hellocharts.model.Viewport;
import lecho.lib.hellocharts.view.ColumnChartView;

public class ColumnChartUtils {
	private ColumnChartView chartBottom;
	private ColumnChartData columnData;

	public final static String[] months = new String[] { "1月", "2月", "3月", "4月", "5月", "6月" };
	public final static String[] months_qy = new String[] { "开工", "停运" };
	private String[] times;
	private List<Integer> list_hg = new ArrayList<Integer>();
	private List<Integer> list_bhg = new ArrayList<Integer>();

	public ColumnChartUtils(List<Integer> list_hg, List<Integer> list_bhg, String[] times) {
		this.list_hg = list_hg;
		this.list_bhg = list_bhg;
		this.times = times;
	}

	public void setChartView_qy(ColumnChartView view) {
		this.chartBottom = view;
		generateColumnData_qy();
	}

	private void generateColumnData_qy() {
		int numColumns = times.length;

		List<AxisValue> axisValues = new ArrayList<AxisValue>();
		List<Column> columns = new ArrayList<Column>();
		List<SubcolumnValue> values;
		for (int i = 0; i < numColumns; ++i) {

			values = new ArrayList<SubcolumnValue>();
			if (i == 0) {
				values.add(new SubcolumnValue(list_hg.get(i), Color.parseColor("#19c6f3")));
			} else if (i == 1) {
				values.add(new SubcolumnValue(list_hg.get(i), Color.parseColor("#19c6f3")));
			} else {
				values.add(new SubcolumnValue(list_hg.get(i), Color.parseColor("#19c6f3")));
			}

			axisValues.add(new AxisValue(i).setLabel(times[i]));
//			columns.add(new Column(values).setHasLabels(true));
			columns.add(new Column(values).setHasLabels(false).setHasLabelsOnlyForSelected(true));
		}

		columnData = new ColumnChartData(columns);
		columnData.setAxisXBottom(
				new Axis(axisValues).setTextColor(Color.parseColor("#424242")).setHasSeparationLine(false).setHasLines(false).setMaxLabelChars(2));
		columnData.setAxisYLeft(new Axis().setTextColor(Color.parseColor("#424242")).setHasSeparationLine(false).setHasTiltedLabels(false).setHasLines(true)
				.setMaxLabelChars(2));
		columnData.setFillRatio(0.30f);
		// columnData.setBaseValue(5);
		chartBottom.setColumnChartData(columnData);
		// Set value touch listener that will trigger changes for chartTop.
		// chartBottom.setOnValueTouchListener(new ValueTouchListener());
		// Set selection mode to keep selected month column highlighted.
		chartBottom.setValueSelectionEnabled(true);
		chartBottom.setZoomEnabled(true);
		chartBottom.setZoomType(ZoomType.HORIZONTAL);
		Viewport tempViewport = new Viewport(chartBottom.getMaximumViewport());
		tempViewport.set(tempViewport.left, tempViewport.top * 1.3f + 1, tempViewport.right, tempViewport.bottom);
		chartBottom.setMaximumViewport(tempViewport);
		chartBottom.setZoomLevel(0, tempViewport.top, 1);
	}

}
