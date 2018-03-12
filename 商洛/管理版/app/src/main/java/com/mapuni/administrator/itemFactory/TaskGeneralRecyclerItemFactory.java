package com.mapuni.administrator.itemFactory;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mapuni.administrator.R;
import com.mapuni.administrator.activity.myNewsAc.DelayApplicationActivity;
import com.mapuni.administrator.activity.myNewsAc.DownTaskDetailActivity;
import com.mapuni.administrator.activity.myNewsAc.UpTaskDetailActivity;
import com.mapuni.administrator.bean.TaskGeneral;

import me.xiaopan.assemblyadapter.AssemblyRecyclerItem;
import me.xiaopan.assemblyadapter.AssemblyRecyclerItemFactory;

/**
 * Created by 15225 on 2017/8/17.
 */

public class TaskGeneralRecyclerItemFactory extends AssemblyRecyclerItemFactory<TaskGeneralRecyclerItemFactory.TaskGeneralRecyclerItem> {

    private EventListener mEventListener;
    private Context baseContext;

    public TaskGeneralRecyclerItemFactory(Context context) {
        mEventListener=new EventProcess(context);
        baseContext=context;
    }



    @Override
    public boolean isTarget(Object o) {
        return o instanceof TaskGeneral;
    }

    @Override
    public TaskGeneralRecyclerItem createAssemblyItem(ViewGroup viewGroup) {
        return new TaskGeneralRecyclerItem(R.layout.list_item_task,viewGroup);
    }

    public interface EventListener{
        void onClickDetail(int position, TaskGeneral taskGeneral);
    }

    private static class EventProcess implements EventListener{
        private Context context;
        public EventProcess(Context context) {
            this.context=context;
        }

        @Override
        public void onClickDetail(int position, TaskGeneral taskGeneral) {

            switch (taskGeneral.getTaskType()){
                case 0:
                    Intent intent=new Intent(context, UpTaskDetailActivity.class);
                    intent.putExtra("handlingRecordUuid",taskGeneral.getUuid());
                    intent.putExtra("taskType",taskGeneral.getTaskType()+"");
                    context.startActivity(intent);
                    ((Activity)context).overridePendingTransition(R.anim.activity_enter_anim, R.anim.activity_exit_anim);
                    break;
                case 1:
                    Intent intent1=new Intent(context, DownTaskDetailActivity.class);
                    intent1.putExtra("handlingRecordUuid",taskGeneral.getUuid());
                    intent1.putExtra("taskType",taskGeneral.getTaskType()+"");
                    context.startActivity(intent1);
                    ((Activity)context).overridePendingTransition(R.anim.activity_enter_anim, R.anim.activity_exit_anim);
                    break;
                case 2:
                    Intent  intent2=new Intent(context,DelayApplicationActivity.class);
                    intent2.putExtra("handlingRecordUuid",taskGeneral.getUuid());
                    intent2.putExtra("taskType",taskGeneral.getTaskType()+"");
                    intent2.putExtra("delayApplyTaskType",taskGeneral.getDelayApplyTaskType());
                    context.startActivity(intent2);
                    ((Activity)context).overridePendingTransition(R.anim.activity_enter_anim, R.anim.activity_exit_anim);
                    break;
                default:
                    break;

            }

        }
    }

    public class TaskGeneralRecyclerItem extends AssemblyRecyclerItem<TaskGeneral> {


        private TextView tvTaskName;
        private TextView tvEndDate;
        private TextView tvTaskSource;
        private TextView tvTaskDetail;

        public TaskGeneralRecyclerItem(int itemLayoutId, ViewGroup parent) {
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
        protected void onSetData(int i, TaskGeneral taskGeneral) {

            tvTaskName.setText(taskGeneral.getTaskName());
            tvEndDate.setText(taskGeneral.getCreateTime());
            tvTaskSource.setText(taskGeneral.getTaskTypeName());
            switch (taskGeneral.getTaskType()){
                case 0://上报
                    Drawable report=baseContext.getResources().getDrawable(R.drawable.report);
                    report.setBounds(0, 0, report.getMinimumWidth(), report.getMinimumHeight());
                    tvTaskSource.setCompoundDrawables(report,null,null,null);
                    break;
                case 1://下发
                    Drawable issued=baseContext.getResources().getDrawable(R.drawable.issued);
                    issued.setBounds(0, 0, issued.getMinimumWidth(), issued.getMinimumHeight());
                    tvTaskSource.setCompoundDrawables(issued,null,null,null);
                    break;
                case 2://延时申请
                    Drawable delay=baseContext.getResources().getDrawable(R.drawable.delay);
                    delay.setBounds(0, 0, delay.getMinimumWidth(), delay.getMinimumHeight());
                    tvTaskSource.setCompoundDrawables(delay,null,null,null);
                    break;
            }
            tvTaskDetail.setText("详情");
        }
    }
}
