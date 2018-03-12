package com.yutu.car.itemfactory;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yutu.car.R;
import com.yutu.car.adapter.assemblyadapter.AssemblyRecyclerItem;
import com.yutu.car.adapter.assemblyadapter.AssemblyRecyclerItemFactory;
import com.yutu.car.bean.IllegalOperationBean;


/**
 * Created by 15225 on 2017/8/17.
 */

public class HomePageRecyclerItemFactory extends AssemblyRecyclerItemFactory<HomePageRecyclerItemFactory.HandleProcessRecyclerItem> {

    private Context baseContext;

    public HomePageRecyclerItemFactory(Context context) {
        baseContext=context;
    }



    @Override
    public boolean isTarget(Object o) {
        return o instanceof IllegalOperationBean.InfoBean;
    }

    @Override
    public HandleProcessRecyclerItem createAssemblyItem(ViewGroup viewGroup) {
        return new HandleProcessRecyclerItem(R.layout.home_page_item,viewGroup);
    }
    

    public class HandleProcessRecyclerItem extends AssemblyRecyclerItem<IllegalOperationBean.InfoBean> {


        private TextView mTvCarNumber;
        private TextView mTvCarColor;
        private TextView mTvCarUser;
        private TextView mTvReason;
        private TextView mTvTime;

        public HandleProcessRecyclerItem(int itemLayoutId, ViewGroup parent) {
            super(itemLayoutId, parent);
        }

        @Override
        protected void onFindViews() {
            mTvCarNumber = (TextView) findViewById(R.id.tv_carNumber);
            mTvCarColor = (TextView) findViewById(R.id.tv_carColor);
            mTvCarUser = (TextView) findViewById(R.id.tv_carUser);
            mTvReason = (TextView) findViewById(R.id.tv_reason);
            mTvTime = (TextView) findViewById(R.id.tv_time);
        }

        @Override
        protected void onConfigViews(Context context) {

        }

        @Override
        protected void onSetData(int i, IllegalOperationBean.InfoBean data) {
            mTvCarNumber.setText(data.getHPHM());
            mTvCarColor.setText(data.getCPYS());
            mTvCarUser.setText(data.getJDCSYR());
            mTvReason.setText(data.getYY());
            mTvTime .setText(data.getVIN());
        }
    }
}
