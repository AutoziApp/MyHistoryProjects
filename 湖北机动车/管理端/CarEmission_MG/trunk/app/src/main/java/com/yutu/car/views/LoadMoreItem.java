package com.yutu.car.views;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yutu.car.R;


/**
 * Created by lenovo on 2017/4/17.
 */

public class LoadMoreItem extends LinearLayout {
    private final Context mContext;
    private LinearLayout loadingView;
    private TextView errorView;
    private TextView endView;
    public LoadMoreItem(Context context){
        super(context);
        mContext = context;
        init();
    }
    private void init (){
        loadingView=(LinearLayout)findViewById(R.id.text_loadMoreListItem_loading);
        errorView=(TextView)findViewById(R.id.text_loadMoreListItem_error);
        endView=(TextView)findViewById(R.id.text_loadMoreListItem_end);
    }
    public void showLoading() {
        loadingView.setVisibility(View.VISIBLE);
        errorView.setVisibility(View.INVISIBLE);
        endView.setVisibility(View.INVISIBLE);
    }
    public void showErrorRetry() {
        loadingView.setVisibility(View.INVISIBLE);
        errorView.setVisibility(View.VISIBLE);
        endView.setVisibility(View.INVISIBLE);
    }
    public void showEnd() {
        loadingView.setVisibility(View.INVISIBLE);
        errorView.setVisibility(View.INVISIBLE);
        endView.setVisibility(View.VISIBLE);
    }

}
