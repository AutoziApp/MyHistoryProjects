package com.mapuni.administrator.itemFactory;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mapuni.administrator.R;
import com.mapuni.administrator.bean.TaskNuberOfGridNumberBean;

import me.xiaopan.assemblyadapter.AssemblyRecyclerItem;
import me.xiaopan.assemblyadapter.AssemblyRecyclerItemFactory;

/**
 * Created by 15225 on 2017/9/23.
 */

public class TaskCountOfGridNumberItemFactory  extends AssemblyRecyclerItemFactory<TaskCountOfGridNumberItemFactory.TaskCountOfGridNumberItem> {

    private TaskCountOfGridNumberItemFactory.EventListener mEventListener;
    private Context baseContext;

    public TaskCountOfGridNumberItemFactory(Context context) {
        mEventListener=new TaskCountOfGridNumberItemFactory.EventProcess(context);
        baseContext=context;
    }


    public  int checkType;

    @Override
    public boolean isTarget(Object o) {
        return o instanceof TaskNuberOfGridNumberBean.RowsBean;
    }

    @Override
    public TaskCountOfGridNumberItem createAssemblyItem(ViewGroup viewGroup) {
        return new TaskCountOfGridNumberItem(R.layout.list_item_grid_number,viewGroup);
    }

    public interface EventListener{
        void onClickDetail(int position,TaskNuberOfGridNumberBean.RowsBean rowsBean);
    }

    private static class EventProcess implements EventListener {
        private Context context;
        public EventProcess(Context context) {
            this.context=context;
        }

        @Override
        public void onClickDetail(int position, TaskNuberOfGridNumberBean.RowsBean rowsBean) {

        }
    }

    public class TaskCountOfGridNumberItem extends AssemblyRecyclerItem<TaskNuberOfGridNumberBean.RowsBean> {


        private TextView tvRanking;
        private TextView tvName;
        private TextView tvValue;


        public TaskCountOfGridNumberItem(int itemLayoutId, ViewGroup parent) {
            super(itemLayoutId, parent);
        }

        @Override
        protected void onFindViews() {

            tvRanking= (TextView) findViewById(R.id.tv_ranking);
            tvName= (TextView) findViewById(R.id.tv_areaName);
            tvValue= (TextView) findViewById(R.id.tv_number);
        }

        @Override
        protected void onConfigViews(Context context) {

        }

        @Override
        protected void onSetData(int i,  TaskNuberOfGridNumberBean.RowsBean rowsBean) {

            tvRanking.setText((i+1)+"");
            tvName.setText(rowsBean.getUserRealname());
            tvValue.setText(rowsBean.getCount()+"");

        }
    }
}
