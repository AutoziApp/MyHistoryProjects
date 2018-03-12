package com.mapuni.mobileenvironment.record.itemfactory;

import android.content.Context;
import android.view.ViewGroup;

import com.mapuni.mobileenvironment.R;
import com.mapuni.mobileenvironment.utils.DisplayUitl;
import com.mapuni.mobileenvironment.widget.NiceSpinner;

public class BigIconHeadFactory extends AssemblyRecyclerItemFactory<BigIconHeadFactory.BigIconHead> {
    Context context;
    public BigIconHeadFactory(Context context) {
        this.context = context;
    }

    @Override
    public boolean isTarget(Object data) {
        return data instanceof String;
    }

    @Override
    public BigIconHead createAssemblyItem(ViewGroup parent) {
        return new BigIconHead(R.layout.big_icon_head_factory, parent);
    }

    public class BigIconHead extends AssemblyRecyclerItem<String> {
        private android.widget.TextView TextView;
        private NiceSpinner spinnerR;
        public BigIconHead(int itemLayoutId, ViewGroup parent) {
            super(itemLayoutId, parent);
        }

        @Override
        protected void onFindViews() {
            spinnerR = (NiceSpinner) findViewById(R.id.spinnerR);
            spinnerR.setBackgroundResource(R.drawable.color_spinner_bg);
            spinnerR.setTextColor(context.getResources().getColor(R.color.white));
            spinnerR.setPadding(DisplayUitl.dip2px(context,10),0,0,0);
        }

        @Override
        protected void onConfigViews(Context context) {

        }

        @Override
        public void onChangeListener(DataChangeListener listener) {
//            this.listener = listener;
        }

        @Override
        protected void onSetData(int position, String s) {
//            nameTextView.setText(s);
        }
    }
}
