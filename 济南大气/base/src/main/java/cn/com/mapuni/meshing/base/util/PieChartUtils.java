package cn.com.mapuni.meshing.base.util;

import java.util.ArrayList;
import java.util.List;
import lecho.lib.hellocharts.model.PieChartData;
import lecho.lib.hellocharts.model.SliceValue;
import lecho.lib.hellocharts.util.ChartUtils;
import lecho.lib.hellocharts.view.PieChartView;

public class PieChartUtils {
	private PieChartView chartBottom;
	private PieChartData pieData;

	private List<Integer> values = new ArrayList<Integer>();
	private List<String> lables = new ArrayList<String>();

	public PieChartUtils(List<Integer> values, List<String> lables) {
		this.values = values;
		this.lables = lables;
	}
	public void setChartView_qy(PieChartView view) {
		this.chartBottom = view;
		generatePieData_qy();
	}

	private void generatePieData_qy() {
		int numberOfPies = lables.size();

		List<SliceValue> temp_values = new ArrayList<SliceValue>();
		for (int i = 0; i < numberOfPies; ++i) {
			temp_values.add(new SliceValue(values.get(i), ChartUtils.pickColor()).setLabel(lables.get(i) + "(" + values.get(i) + ")"));
		}

		pieData = new PieChartData(temp_values);
		pieData.setHasLabels(true);
		pieData.setHasLabelsOutside(true);
		pieData.setHasLabelsOnlyForSelected(true);
		pieData.setHasCenterCircle(false);

		chartBottom.setPieChartData(pieData);
		chartBottom.setCircleFillRatio(0.85f);
		chartBottom.setValueSelectionEnabled(true);
		chartBottom.setZoomEnabled(false);
	}

}
