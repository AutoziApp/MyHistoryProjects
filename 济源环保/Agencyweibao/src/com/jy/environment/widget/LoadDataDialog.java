package com.jy.environment.widget;



import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Window;

import com.jy.environment.R;

public class LoadDataDialog extends Dialog {
    private Context mcontext;
	public LoadDataDialog(Context context) {
		super(context);
		mcontext=context;

	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.setContentView(R.layout.loaddata_dialog);
		setCanceledOnTouchOutside(false);

	}
	

}
