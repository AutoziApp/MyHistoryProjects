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
public class YqydPwkxqActivity extends BaseActivity {
	private View parentView;
	LinearLayout middleLayout;
	String type;
	HashMap<String, Object> arr;
	EditText wz_pkmc_et, wz_bh_et, wz_wz_et, wz_pfkgd_et, wz_pfknj_et,
			wz_jd_et, wz_wd_et, wz_scydm_et, wz_cjgdmc_et, wz_sbmc_et,
			wz_kzymc_et, wz_zt_et, wz_gxsj_et, wz_wrysj_et, wz_wrybm_et;

	@SuppressWarnings("unchecked")
	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.ui_mapuni);

		parentView = LayoutInflater.from(this).inflate(
				R.layout.yqyd_pwk_layout, null);

		SetBaseStyle((RelativeLayout) findViewById(R.id.parentLayout), "排污口详情");

		// 从Intent当中根据key取得value
		arr = (HashMap<String, Object>) getIntent().getSerializableExtra(
				"emergency");
		middleLayout = (LinearLayout) findViewById(R.id.middleLayout);
		middleLayout.setPadding(0, 0, 0, 0);
		middleLayout.addView(parentView);
		init();
	}

	public void init() {

		wz_pkmc_et = (EditText) findViewById(R.id.wz_pkmc_et);
		wz_bh_et = (EditText) findViewById(R.id.wz_bh_et);
		wz_wz_et = (EditText) findViewById(R.id.wz_wz_et);
		wz_pfkgd_et = (EditText) findViewById(R.id.wz_pfkgd_et);
		wz_pfknj_et = (EditText) findViewById(R.id.wz_pfknj_et);
		wz_jd_et = (EditText) findViewById(R.id.wz_jd_et);
		wz_wd_et = (EditText) findViewById(R.id.wz_wd_et);
		wz_scydm_et = (EditText) findViewById(R.id.wz_scydm_et);
		wz_cjgdmc_et = (EditText) findViewById(R.id.wz_cjgdmc_et);
		wz_sbmc_et = (EditText) findViewById(R.id.wz_sbmc_et);
		wz_kzymc_et = (EditText) findViewById(R.id.wz_kzymc_et);
		wz_zt_et = (EditText) findViewById(R.id.wz_zt_et);
		wz_gxsj_et = (EditText) findViewById(R.id.wz_gxsj_et);
		wz_wrysj_et = (EditText) findViewById(R.id.wz_wrysj_et);
		wz_wrybm_et = (EditText) findViewById(R.id.wz_wrybm_et);
		
		wz_pkmc_et.setText(arr.get("outputname") + "");
		wz_bh_et.setText(arr.get("outputnumber") + "");
		wz_wz_et.setText(arr.get("outputposition") + "");
		wz_pfkgd_et.setText(arr.get("outputhigh") + "");
		wz_pfknj_et.setText(arr.get("outputdiameter") + "");
		wz_jd_et.setText(arr.get("longitude") + "");
		wz_wd_et.setText(arr.get("latitude") + "");
		wz_scydm_et.setText(arr.get("dgimn") + "");
		wz_cjgdmc_et.setText(arr.get("workshopname") + "");
		wz_sbmc_et.setText(arr.get("equipmentname") + "");
		wz_kzymc_et.setText(arr.get("automonitorinstrument") + "");
		wz_zt_et.setText(arr.get("status") + "");
		wz_gxsj_et.setText(arr.get("updatedate") + "");
		wz_wrysj_et.setText(arr.get("factordata") + "");
		wz_wrybm_et.setText(arr.get("factorcode") + "");

	}
}