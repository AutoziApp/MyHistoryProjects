package com.mapuni.mobileenvironment.record.itemfactory;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mapuni.mobileenvironment.R;

import java.util.HashMap;


public class LineItemFactory extends AssemblyRecyclerItemFactory<LineItemFactory.WhiteRecyclerItem> {


    public LineItemFactory(Context context) {
        
    }

    @Override
    public boolean isTarget(Object data) {
        return data instanceof HashMap;
    }

    @Override
    public WhiteRecyclerItem createAssemblyItem(ViewGroup parent) {
        return new WhiteRecyclerItem(R.layout.line_item_factory, parent);
    }
    public class WhiteRecyclerItem extends AssemblyRecyclerItem<HashMap> {
        private TextView nameTv;
        private TextView valueTv;
        public WhiteRecyclerItem(int itemLayoutId, ViewGroup parent) {

            super(itemLayoutId, parent);
        }

        @Override
        protected void onFindViews() {
            nameTv= (TextView) findViewById(R.id.name);
            valueTv = (TextView) findViewById(R.id.value);
        }

        @Override
        protected void onConfigViews(Context context) {

        }

        @Override
        public void onChangeListener(DataChangeListener listener) {
        }

        @Override
        protected void onSetData(int position, HashMap map) {

            nameTv.setText((String) map.get("name"));
            String value = (String) map.get("value");
            if(value==null||value.equals(""))
                value = "0.0";
            valueTv.setText(value);
        }
    }
}
