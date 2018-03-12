package cn.com.mapuni.meshing.base.util;

import android.graphics.Color;

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

public class LineChartUtils {
	private LineChartView chartBottom;
	private LineChartData lineData;

	public final static String[] months = new String[] { "1月", "2月", "3月", "4月", "5月", "6月" };
	public final static String[] months_qy = new String[] { "开工", "停运" };
	private String[] times;
	private List<Integer> list_hg = new ArrayList<Integer>();
	private List<Integer> list_bhg = new ArrayList<Integer>();

	public LineChartUtils(List<Integer> list_hg, List<Integer> list_bhg, String[] times) {
		this.list_hg = list_hg;
		this.list_bhg = list_bhg;
		this.times = times;
	}
	public void setChartView_qy(LineChartView view) {
		this.chartBottom = view;
		generateLineData_qy();
	}

	private void generateLineData_qy() {
		int numberOfLines = times.length;

		List<PointValue> values = new ArrayList<PointValue>();
		List<AxisValue> axisValues = new ArrayList<AxisValue>();
		List<Line> lines = new ArrayList<Line>();
		for (int i = 0; i < 1; ++i) {

			values = new ArrayList<PointValue>();
			axisValues = new ArrayList<AxisValue>();
			for (int j = 0; j < numberOfLines; ++j) {
				values.add(new PointValue(j, list_hg.get(j)));
				axisValues.add(new AxisValue(j).setLabel(times[j]));
			}

			Line line = new Line(values);
			line.setColor(Color.parseColor("#19c6f3"));
			line.setShape(ValueShape.CIRCLE);
			line.setCubic(false);
			line.setFilled(false);
			line.setHasLabels(false);
			line.setHasLabelsOnlyForSelected(true);
			line.setHasLines(true);
			line.setHasPoints(true);
			line.setPointColor(Color.parseColor("#19c6f3"));
			lines.add(line);
		}

		lineData = new LineChartData(lines);
		lineData.setAxisXBottom(
				new Axis(axisValues).setTextColor(Color.parseColor("#424242")).setHasSeparationLine(false).setHasLines(false).setMaxLabelChars(2));
		lineData.setAxisYLeft(new Axis().setTextColor(Color.parseColor("#424242")).setHasSeparationLine(false).setHasTiltedLabels(false).setHasLines(true)
				.setMaxLabelChars(2));
		lineData.setBaseValue(Float.NEGATIVE_INFINITY);
		chartBottom.setLineChartData(lineData);

		chartBottom.setValueSelectionEnabled(true);
		chartBottom.setZoomEnabled(true);
		chartBottom.setZoomType(ZoomType.HORIZONTAL);
		Viewport tempViewport = new Viewport(chartBottom.getMaximumViewport());
		tempViewport.set(tempViewport.left, tempViewport.top * 1.3f + 1, tempViewport.right, tempViewport.bottom);
		chartBottom.setMaximumViewport(tempViewport);
		chartBottom.setZoomLevel(0, tempViewport.top, 1);
	}

}
