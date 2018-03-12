package com.yutu.car.itemfactory;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yutu.car.R;
import com.yutu.car.adapter.assemblyadapter.AssemblyRecyclerItem;
import com.yutu.car.adapter.assemblyadapter.AssemblyRecyclerItemFactory;
import com.yutu.car.bean.CoAccountDetailBean;
import com.yutu.car.bean.DetailBean;

public class CoDetailItemFactory extends AssemblyRecyclerItemFactory<CoDetailItemFactory.DetailRecyclerItem> {

    public CoDetailItemFactory(Context context) {
//        this.eventListener = new EventProcessor(context);
    }

    @Override
    public boolean isTarget(Object data) {
        return data instanceof CoAccountDetailBean.DataBean;
    }

    @Override
    public DetailRecyclerItem createAssemblyItem(ViewGroup parent) {
        return new DetailRecyclerItem(R.layout.list_item_co, parent);
    }

    public interface EventListener{
        void onClick(int position, DetailBean user);
    }



    public class DetailRecyclerItem extends AssemblyRecyclerItem<CoAccountDetailBean.DataBean> {
        private TextView tvKzjb;
        private TextView tvCarNumber;
        private TextView tvPfl;

        public DetailRecyclerItem(int itemLayoutId, ViewGroup parent) {
            super(itemLayoutId, parent);
        }

        @Override
        protected void onFindViews() {
            tvKzjb = (TextView) findViewById(R.id.tv_kzjb);//控制级别
            tvCarNumber = (TextView) findViewById(R.id.tv_carNumber);//车辆数
            tvPfl = (TextView) findViewById(R.id.tv_pfl);//排放量
        }

        @Override
        protected void onConfigViews(Context context) {

            tvKzjb.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    eventListener.onClick(getLayoutPosition(), getData());
                }
            });
        }

        @Override
        protected void onSetData(int position, CoAccountDetailBean.DataBean bean) {
            int cls = bean.getCLS();
            String pfbz = bean.getPFBZ();
            int pfl = bean.getPFL();
            tvKzjb.setText(bean.getPFBZ()==null?"暂无":bean.getPFBZ());
            tvPfl.setText(pfl+"");
            tvCarNumber.setText(cls+"");
        }
    }
}
