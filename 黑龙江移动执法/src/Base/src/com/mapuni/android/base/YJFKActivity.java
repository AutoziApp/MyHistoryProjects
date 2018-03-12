package com.mapuni.android.base;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * FileName: YJFKActivity.java
 * Description: �������
 * @author ������
 * @Version 1.3.4
 * @Copyright �п���ͼ���¿Ƽ����޹�˾
 * Create at: 2012-12-3 ����02:55:57
 */
public class YJFKActivity extends BaseActivity {

	private EditText contentET;
	private Button cancelBtn, submitBtn;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
		setContentView(R.layout.base_yjfk);
		
		contentET = (EditText) findViewById(R.id.contentET);
		cancelBtn = (Button) findViewById(R.id.cancelBtn);
		submitBtn = (Button) findViewById(R.id.submitBtn);
		cancelBtn.setOnClickListener(listener);
		submitBtn.setOnClickListener(listener);
		
	}
	
	OnClickListener listener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			if( v.getId()==R.id.cancelBtn)
				finish();
			else if(v.getId()==R.id.submitBtn)	
			{
				String content = contentET.getText().toString();
				if("".equals(content.trim())){
					Toast.makeText(YJFKActivity.this, "���������ݺ����ύ", 0).show();
					return ;
				}
			}
		}
	};
}
