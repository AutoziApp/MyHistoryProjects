package com.mapuni.mobileenvironment.record.itemfactory;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mapuni.mobileenvironment.R;

import java.util.ArrayList;


public class TitleHeadFactory extends AssemblyRecyclerItemFactory<TitleHeadFactory.TitleHeadItem> {


    public TitleHeadFactory(Context context) {
        
    }

    @Override
    public boolean isTarget(Object data) {
        return data instanceof String;
    }

    @Override
    public TitleHeadItem createAssemblyItem(ViewGroup parent) {
        return new TitleHeadItem(R.layout.title_head_factory, parent);
    }
    public class TitleHeadItem extends AssemblyRecyclerItem<String> {
        private TextView tvL;
        private TextView tvC;
        private TextView tvR;
        public TitleHeadItem(int itemLayoutId, ViewGroup parent) {

            super(itemLayoutId, parent);
        }

        @Override
        protected void onFindViews() {
            tvL= (TextView) findViewById(R.id.textL);
            tvC = (TextView) findViewById(R.id.textC);
            tvR= (TextView) findViewById(R.id.textR);
            tvR.getPaint().setFakeBoldText(true);
            tvL.getPaint().setFakeBoldText(true);
            tvC.getPaint().setFakeBoldText(true);
        }

        @Override
        protected void onConfigViews(Context context) {

        }

        @Override
        public void onChangeListener(DataChangeListener listener) {
        }

        @Override
        protected void onSetData(int position, String name) {
        }
    }
}
