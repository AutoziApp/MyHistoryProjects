package com.mapuni.mobileenvironment.record.itemfactory;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mapuni.mobileenvironment.R;


public class ThreeListItemFactory extends AssemblyRecyclerItemFactory<ThreeListItemFactory.TitleHeadItem> {


    public ThreeListItemFactory(Context context) {
        
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
        private TextView nameTv;
        private TextView valueTv;
        public TitleHeadItem(int itemLayoutId, ViewGroup parent) {

            super(itemLayoutId, parent);
        }

        @Override
        protected void onFindViews() {
//            nameTv= (TextView) findViewById(R.id.name);
//            valueTv = (TextView) findViewById(R.id.value);
        }

        @Override
        protected void onConfigViews(Context context) {

        }

        @Override
        public void onChangeListener(DataChangeListener listener) {
        }

        @Override
        protected void onSetData(int position, String name) {
//            nameTv.setText(name);
        }
    }
}
