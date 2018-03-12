package com.mapuni.shangluo.itemFactory;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.mapuni.shangluo.R;
import com.mapuni.shangluo.bean.Task;

import me.xiaopan.assemblyadapter.AssemblyRecyclerItem;
import me.xiaopan.assemblyadapter.AssemblyRecyclerItemFactory;

/**
 * Created by 15225 on 2017/8/17.
 */

public class TaskRecyclerItemFactory extends AssemblyRecyclerItemFactory<TaskRecyclerItemFactory.TaskRecyclerItem>{

    private EventListener mEventListener;
    private Context baseContext;
    
    public TaskRecyclerItemFactory(Context context) {
        mEventListener=new EventProcess(context);
        baseContext=context;
    }



    @Override
    public boolean isTarget(Object o) {
        return o instanceof Task;
    }

    @Override
    public TaskRecyclerItem createAssemblyItem(ViewGroup viewGroup) {
        return new TaskRecyclerItem(R.layout.list_item_task,viewGroup);
    }

    public interface EventListener{
        void onClickDetail(int position,Task task);
    }

    private static class EventProcess implements EventListener{
        private Context context;
        public EventProcess(Context context) {
            this.context=context;
        }

        @Override
        public void onClickDetail(int position, Task task) {
            Toast.makeText(context,"你点击了第"+position+"条的详情",Toast.LENGTH_SHORT).show();
        }
    }

    public class TaskRecyclerItem extends AssemblyRecyclerItem<Task>{


        private TextView tvTaskName;
        private TextView tvEndDate;
        private TextView tvTaskSource;
        private TextView tvTaskDetail;

        public TaskRecyclerItem(int itemLayoutId, ViewGroup parent) {
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
        protected void onConfigViews(Context context) {
            tvTaskDetail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mEventListener.onClickDetail(getLayoutPosition(),getData());
                }
            });
        }

        @Override
        protected void onSetData(int i, Task task) {

            tvTaskName.setText(task.getTaskName());
            tvEndDate.setText(task.getEndDate());
            tvTaskSource.setText(task.getTaskSource());
            switch (task.getTaskSource()){
                case "上报":
                    Drawable report=baseContext.getResources().getDrawable(R.drawable.report);
                    report.setBounds(0, 0, report.getMinimumWidth(), report.getMinimumHeight());
                    tvTaskSource.setCompoundDrawables(report,null,null,null);
                    break;
                case "下发":
                    Drawable issued=baseContext.getResources().getDrawable(R.drawable.issued);
                    issued.setBounds(0, 0, issued.getMinimumWidth(), issued.getMinimumHeight());
                    tvTaskSource.setCompoundDrawables(issued,null,null,null);
                    break;
            }
            tvTaskDetail.setText(task.getTaskDetail());
        }
    }
}
