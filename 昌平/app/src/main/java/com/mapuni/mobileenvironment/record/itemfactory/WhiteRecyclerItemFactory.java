package com.mapuni.mobileenvironment.record.itemfactory;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mapuni.mobileenvironment.R;

import java.util.HashMap;


public class WhiteRecyclerItemFactory extends AssemblyRecyclerItemFactory<WhiteRecyclerItemFactory.WhiteRecyclerItem> {


    public WhiteRecyclerItemFactory(Context context) {
        
    }

    @Override
    public boolean isTarget(Object data) {
        return data instanceof HashMap;
    }

    @Override
    public WhiteRecyclerItem createAssemblyItem(ViewGroup parent) {
        return new WhiteRecyclerItem(R.layout.white_item_factory, parent);
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
//            nameTv.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                }
//            });
//            nameTextView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                }
//            });
//            likeTextView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                }
//            });
        }

        @Override
        public void onChangeListener(DataChangeListener listener) {

        }

        @Override
        protected void onSetData(int position, HashMap map) {
            nameTv.setText((String) map.get("name"));
            valueTv.setText((String) map.get("value"));
//            iconImageView.setImageResource(game.iconResId);
//            nameTextView.setText(game.name);
//            likeTextView.setText(game.like);
        }
    }
}
