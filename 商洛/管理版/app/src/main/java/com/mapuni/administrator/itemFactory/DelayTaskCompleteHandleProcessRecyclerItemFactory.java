package com.mapuni.administrator.itemFactory;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;


import com.mapuni.administrator.R;
import com.mapuni.administrator.activity.dbAc.AttachmentActivity;
import com.mapuni.administrator.bean.TreatmentprocessBean;
import com.mapuni.administrator.utils.TxtUtil;

import me.xiaopan.assemblyadapter.AssemblyRecyclerItem;
import me.xiaopan.assemblyadapter.AssemblyRecyclerItemFactory;

/**
 * Created by 15225 on 2017/8/17.
 */

public class DelayTaskCompleteHandleProcessRecyclerItemFactory extends AssemblyRecyclerItemFactory<DelayTaskCompleteHandleProcessRecyclerItemFactory.HandleProcessRecyclerItem> {

    private EventListener mEventListener;
    private Context baseContext;

    public DelayTaskCompleteHandleProcessRecyclerItemFactory(Context context) {
        mEventListener=new EventProcess(context);
        baseContext=context;
    }



    @Override
    public boolean isTarget(Object o) {
        return o instanceof TreatmentprocessBean.RowsBean;
    }

    @Override
    public HandleProcessRecyclerItem createAssemblyItem(ViewGroup viewGroup) {
        return new HandleProcessRecyclerItem(R.layout.list_item_delaytask_handle_process,viewGroup);
    }

    public interface EventListener{
        void onClickDetail(int position, TreatmentprocessBean.RowsBean data);
    }

    private static class EventProcess implements EventListener{
        private Context context;
        public EventProcess(Context context) {
            this.context=context;
        }

        @Override
        public void onClickDetail(int position, TreatmentprocessBean.RowsBean data) {
            //进入附件详情页
            Toast.makeText(context,"你点击了第"+position+"条的附件信息",Toast.LENGTH_SHORT).show();
            Intent intent=new Intent(context,AttachmentActivity.class);
            intent.putExtra("handledRecordUuid",data.getUuid());
            ((Activity)context).startActivity(intent);
            ((Activity)context).overridePendingTransition(R.anim.activity_enter_anim,R.anim.activity_exit_anim);
        }
    }

    public class HandleProcessRecyclerItem extends AssemblyRecyclerItem<TreatmentprocessBean.RowsBean> {


        private TextView tvGrid;
        private TextView tvOperator;
        private TextView tvOperateType;
        private TextView tvOperateTime;
        private TextView tvOpinion;
        private TextView tvAttach;
        private TextView tvNextHandlerName;
        private TextView mTvApplayStartTime;
        private TextView mTvApplayEndTime;

        public HandleProcessRecyclerItem(int itemLayoutId, ViewGroup parent) {
            super(itemLayoutId, parent);
        }

        @Override
        protected void onFindViews() {

            tvGrid= (TextView) findViewById(R.id.tv_grid);
            tvOperator= (TextView) findViewById(R.id.tv_operator);
            tvOperateType= (TextView) findViewById(R.id.tv_operateType);
            tvOperateTime= (TextView) findViewById(R.id.tv_operateTime);
            mTvApplayStartTime = (TextView) findViewById(R.id.tv_applayStartTime);
            mTvApplayEndTime = (TextView) findViewById(R.id.tv_applayEndTime);
            tvOpinion= (TextView) findViewById(R.id.tv_opinion);
            tvAttach= (TextView) findViewById(R.id.tv_attach);
            tvNextHandlerName = (TextView) findViewById(R.id.tv_nextHandlerName);
        }

        @Override
        protected void onConfigViews(Context context) {
            tvAttach.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mEventListener.onClickDetail(getLayoutPosition(),getData());
                }
            });
        }

        @Override
        protected void onSetData(int i, TreatmentprocessBean.RowsBean data) {
            tvGrid.setText(data.getGridName());
            tvOperator.setText(data.getHandlerUser());
            tvOperateType.setText(data.getOperationTypeName());
            tvOperateTime.setText(data.getCreateTime());
            tvOpinion.setText(data.getOpinion());
            mTvApplayStartTime.setText(data.getStartTime());
            mTvApplayEndTime.setText(data.getEndTime());
            String nextHandlerName = (String) data.getNextHandlerName();
            tvNextHandlerName.setText(TxtUtil.isEmpty(nextHandlerName));
        }
    }
}
