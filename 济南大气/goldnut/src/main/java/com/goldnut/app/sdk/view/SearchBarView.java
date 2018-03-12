package com.goldnut.app.sdk.view;

import com.goldnut.app.sdk.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

public abstract class SearchBarView extends LinearLayout implements View.OnClickListener{
	
	private EditText et;
	private ImageView btn;

	public SearchBarView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		View view =  LayoutInflater.from(context).inflate(R.layout.uc_searchbar, null);
		et = (EditText)view.findViewById(R.id.et_txt);
		btn = (ImageView)view.findViewById(R.id.btn_search);
		btn.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		OnSearch(et.getText().toString());
	}
	
	
	protected abstract void OnSearch(String text);

}
