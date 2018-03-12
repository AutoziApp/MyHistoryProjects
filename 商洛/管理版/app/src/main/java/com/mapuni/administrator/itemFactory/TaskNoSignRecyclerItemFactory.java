package com.mapuni.administrator.itemFactory;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.mapuni.administrator.R;
import com.mapuni.administrator.activity.wdAc.NotSignTaskActivity;
import com.mapuni.administrator.bean.NoTaskSignBean;

import me.xiaopan.assemblyadapter.AssemblyRecyclerItem;
import me.xiaopan.assemblyadapter.AssemblyRecyclerItemFactory;

/**
 * Created by 15225 on 2017/8/17.
 */

public class TaskNoSignRecyclerItemFactory extends AssemblyRecyclerItemFactory<TaskNoSignRecyclerItemFactory.TaskNoSignRecyclerItem> {

    private Context baseContext;

    public TaskNoSignRecyclerItemFactory(Context context) {
        baseContext=context;
    }



    @Override
    public boolean isTarget(Object o) {
        return o instanceof NoTaskSignBean.RowsBean;
    }

    @Override
    public TaskNoSignRecyclerItem createAssemblyItem(ViewGroup viewGroup) {
        return new TaskNoSignRecyclerItem(R.layout.list_item_no_sign_task,viewGroup);
    }

    public interface EventListener{
        void onClickDetail(int position, NoTaskSignBean.RowsBean rowsBean);
    }

    public class TaskNoSignRecyclerItem extends AssemblyRecyclerItem<NoTaskSignBean.RowsBean> {


        private TextView tvTaskName;
        private TextView tvEndDate;
        private TextView tvTaskSource;
        private TextView tvTaskDetail;

        public TaskNoSignRecyclerItem(int itemLayoutId, ViewGroup parent) {
            super(itemLayoutId, parent);
        }

        @Override
        protected void onFindViews() {

            tvTaskName= (TextView) findViewById(R.id.tvTaskName);
            tvEndDate= (TextView) findViewById(R.id.tvEndDate);
            tvTaskSource= (TextView) findViewById(R.id.tvTaskSource);
            tvTaskDetail= (TextView) findViewById(R.id.tvTaskDetail);
        }

        @Override
        protected void onConfigViews(final Context context) {
            tvTaskDetail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((NotSignTaskActivity)baseContext).onClickDetail(getLayoutPosition(),getData());
                }
            });
        }

        @Override
        protected void onSetData(int i, NoTaskSignBean.RowsBean rowsBean) {

            tvTaskName.setText(rowsBean.getTask().getName());
            tvEndDate.setText(rowsBean.getCreateTime());
            tvTaskSource.setText(rowsBean.getTask().getTaskType());

            tvTaskDetail.setText("签收");
        }
    }
}
