package cn.com.mapuni.meshing.activity.wd_activity;

import java.util.HashMap;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.example.meshing.R;
import com.mapuni.android.base.BaseActivity;

/**
 * 显示排污口
 * 
 * @author
 * 
 */
@SuppressLint("ResourceAsColor")
public class YqydWrwzlssxqActivity extends BaseActivity {
	private View parentView;
	LinearLayout middleLayout;
	String type;
	HashMap<String, Object> arr;
	EditText wz_name_et, wz_bh_et, wz_xh_et, wz_clnl_et, wz_cll_et, wz_clff_et,
			wz_gdzc_et, wz_yxfy_et;

	@SuppressWarnings("unchecked")
	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.ui_mapuni);

		parentView = LayoutInflater.from(this).inflate(
				R.layout.yqyd_wrwzlss_layout, null);

		SetBaseStyle((RelativeLayout) findViewById(R.id.parentLayout),
				"污染物治理设施详情");

		// 从Intent当中根据key取得value
		 arr = (HashMap<String, Object>) getIntent().getSerializableExtra(
		 "emergency");
		middleLayout = (LinearLayout) findViewById(R.id.middleLayout);
		middleLayout.setPadding(0, 0, 0, 0);
		middleLayout.addView(parentView);
		init();
	}

	public void init() {

		wz_name_et = (EditText) findViewById(R.id.wz_name_et);
		wz_bh_et = (EditText) findViewById(R.id.wz_bh_et);
		wz_xh_et = (EditText) findViewById(R.id.wz_xh_et);
		wz_clnl_et = (EditText) findViewById(R.id.wz_clnl_et);
		wz_cll_et = (EditText) findViewById(R.id.wz_cll_et);
		wz_clff_et = (EditText) findViewById(R.id.wz_clff_et);
		wz_gdzc_et = (EditText) findViewById(R.id.wz_gdzc_et);
		wz_yxfy_et = (EditText) findViewById(R.id.wz_yxfy_et);

		wz_name_et.setText(arr.get("processingname") + "");
		wz_bh_et.setText(arr.get("processingnumber") + "");
		wz_xh_et.setText(arr.get("specificationsmodels") + "");
		wz_clnl_et.setText(arr.get("capacity") + "");
		wz_cll_et.setText(arr.get("theactualrate") + "");
		wz_clff_et.setText(arr.get("method") + "");
		wz_gdzc_et.setText(arr.get("assets") + "");
		wz_yxfy_et.setText(arr.get("annual") + "");

	}
}