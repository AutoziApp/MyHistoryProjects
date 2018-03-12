package com.mapuni.caremission_ens.itemfactory;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import  com.mapuni.caremission_ens.adapter.assemblyadapter.AssemblyRecyclerItem;
import com.mapuni.caremission_ens.R;
import com.mapuni.caremission_ens.adapter.assemblyadapter.AssemblyRecyclerItemFactory;
import com.mapuni.caremission_ens.bean.DetailBean;

public class DetailItemFactory extends AssemblyRecyclerItemFactory<DetailItemFactory.DetailRecyclerItem> {

    public DetailItemFactory(Context context) {
//        this.eventListener = new EventProcessor(context);
    }

    @Override
    public boolean isTarget(Object data) {
        return data instanceof DetailBean;
    }

    @Override
    public DetailRecyclerItem createAssemblyItem(ViewGroup parent) {
        return new DetailRecyclerItem(R.layout.list_item_detail, parent);
    }

    public interface EventListener{
        void onClick(int position, DetailBean user);
    }



    public class DetailRecyclerItem extends AssemblyRecyclerItem<DetailBean> {
        private TextView nameTextView;
        private TextView valueTextView;

        public DetailRecyclerItem(int itemLayoutId, ViewGroup parent) {
            super(itemLayoutId, parent);
        }

        @Override
        protected void onFindViews() {
            nameTextView = (TextView) findViewById(R.id.name);
            valueTextView = (TextView) findViewById(R.id.value);
        }

        @Override
        protected void onConfigViews(Context context) {

            nameTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    eventListener.onClick(getLayoutPosition(), getData());
                }
            });
        }

        @Override
        protected void onSetData(int position, DetailBean bean) {
            nameTextView.setText(bean.getTitle());
            valueTextView.setText(bean.getValue());
        }
    }
}
