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
 * 显示
 * 
 * @author
 * 
 */
@SuppressLint("ResourceAsColor")
public class YqydScssyxqkxqActivity extends BaseActivity {
	private View parentView;
	LinearLayout middleLayout;
	String type;
	HashMap<String, Object> arr;
	EditText wz_name_et, wz_bh_et, wz_xh_et, wz_sysj_et, wz_scgy_et,
			wz_yxqk_et, wz_gjsj_et, wz_ysl_et, wz_hml_et, wz_bz_et;

	@SuppressWarnings("unchecked")
	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.ui_mapuni);

		parentView = LayoutInflater.from(this).inflate(
				R.layout.yqyd_scss_layout, null);

		SetBaseStyle((RelativeLayout) findViewById(R.id.parentLayout),
				"生产设施运行情况详情");

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
		wz_sysj_et = (EditText) findViewById(R.id.wz_sysj_et);
		wz_scgy_et = (EditText) findViewById(R.id.wz_scgy_et);
		wz_yxqk_et = (EditText) findViewById(R.id.wz_yxqk_et);
		wz_gjsj_et = (EditText) findViewById(R.id.wz_gjsj_et);
		wz_ysl_et = (EditText) findViewById(R.id.wz_ysl_et);
		wz_hml_et = (EditText) findViewById(R.id.wz_hml_et);
		wz_bz_et = (EditText) findViewById(R.id.wz_bz_et);

		wz_name_et.setText(arr.get("productionname") + "");
		wz_bh_et.setText(arr.get("productionnumber") + "");
		wz_xh_et.setText(arr.get("models") + "");
		wz_sysj_et.setText(arr.get("servicetime") + "");
		wz_scgy_et.setText(arr.get("technique") + "");
		wz_yxqk_et.setText(arr.get("condition") + "");
		wz_gjsj_et.setText(arr.get("productiontime") + "");
		wz_ysl_et.setText(arr.get("wateryield") + "");
		wz_hml_et.setText(arr.get("coalquantity") + "");
		wz_bz_et.setText(arr.get("remark") + "");
		
	}
}