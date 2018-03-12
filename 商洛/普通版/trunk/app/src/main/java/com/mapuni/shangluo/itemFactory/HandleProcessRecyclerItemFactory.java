package com.mapuni.shangluo.itemFactory;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.mapuni.shangluo.R;
import com.mapuni.shangluo.activity.BaseActivity;
import com.mapuni.shangluo.activity.dbAc.AttachmentActivity;
import com.mapuni.shangluo.activity.dbAc.UpTaskDetailActivity;
import com.mapuni.shangluo.bean.HandleProcess;
import com.mapuni.shangluo.update.BaseAutoUpdate;
import com.mapuni.shangluo.utils.FileDownUtil;

import org.codehaus.jackson.map.deser.ValueInstantiators;

import me.xiaopan.assemblyadapter.AssemblyRecyclerItem;
import me.xiaopan.assemblyadapter.AssemblyRecyclerItemFactory;

/**
 * Created by 15225 on 2017/8/17.
 */

public class HandleProcessRecyclerItemFactory extends AssemblyRecyclerItemFactory<HandleProcessRecyclerItemFactory.HandleProcessRecyclerItem>{

    private EventListener mEventListener;
    private Context baseContext;

    public HandleProcessRecyclerItemFactory(Context context) {
        mEventListener=new EventProcess(context);
        baseContext=context;
    }



    @Override
    public boolean isTarget(Object o) {
        return o instanceof HandleProcess;
    }

    @Override
    public HandleProcessRecyclerItem createAssemblyItem(ViewGroup viewGroup) {
        return new HandleProcessRecyclerItem(R.layout.list_item_handle_process,viewGroup);
    }

    public interface EventListener{
        void onClickDetail(int position, HandleProcess data);
    }

    private static class EventProcess implements EventListener{
        private Context context;
        public EventProcess(Context context) {
            this.context=context;
        }

        @Override
        public void onClickDetail(int position, HandleProcess data) {
            //进入附件详情页
            Intent intent=new Intent(context,AttachmentActivity.class);
            intent.putExtra("handledRecordUuid",data.getUuid());
            ((Activity)context).startActivity(intent);
            ((Activity)context).overridePendingTransition(R.anim.activity_enter_anim,R.anim.activity_exit_anim);
            
        }
    }

    public class HandleProcessRecyclerItem extends AssemblyRecyclerItem<HandleProcess>{


        private TextView tvGrid;
        private TextView tvOperator;
        private TextView tvOperateType;
        private TextView tvOperateTime;
        private TextView tvOpinion;
        private TextView tvAttach;

        public HandleProcessRecyclerItem(int itemLayoutId, ViewGroup parent) {
            super(itemLayoutId, parent);
        }

        @Override
        protected void onFindViews() {

            tvGrid= (TextView) findViewById(R.id.tv_grid);
            tvOperator= (TextView) findViewById(R.id.tv_operator);
            tvOperateType= (TextView) findViewById(R.id.tv_operateType);
            tvOperateTime= (TextView) findViewById(R.id.tv_operateTime);
            tvOpinion= (TextView) findViewById(R.id.tv_opinion);
            tvAttach= (TextView) findViewById(R.id.tv_attach);
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
        protected void onSetData(int i, HandleProcess data) {
            tvGrid.setText(data.getGridName());
            tvOperator.setText(data.getHandlerUser());
            tvOperateType.setText(data.getOperationTypeName());
            tvOperateTime.setText(data.getCreateTime());
            tvOpinion.setText(data.getOpinion());
        }
    }
}
