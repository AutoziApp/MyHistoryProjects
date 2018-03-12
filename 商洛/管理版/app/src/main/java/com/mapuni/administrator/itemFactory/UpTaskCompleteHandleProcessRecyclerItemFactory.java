package com.mapuni.administrator.itemFactory;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.pickerview.TimePickerView;
import com.mapuni.administrator.R;
import com.mapuni.administrator.activity.dbAc.AttachmentActivity;
import com.mapuni.administrator.bean.TreatmentprocessBean;
import com.mapuni.administrator.manager.NetManager;
import com.mapuni.administrator.utils.SPUtils;
import com.mapuni.administrator.utils.TimePickerUtil;
import com.mapuni.administrator.utils.TimeUtil;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.Calendar;
import java.util.Date;

import me.xiaopan.assemblyadapter.AssemblyRecyclerItem;
import me.xiaopan.assemblyadapter.AssemblyRecyclerItemFactory;
import okhttp3.Call;

/**
 * Created by 15225 on 2017/8/17.
 */

public class UpTaskCompleteHandleProcessRecyclerItemFactory extends AssemblyRecyclerItemFactory<UpTaskCompleteHandleProcessRecyclerItemFactory.HandleProcessRecyclerItem> {

    private EventListener mEventListener;
    private Context baseContext;

    public UpTaskCompleteHandleProcessRecyclerItemFactory(Context context) {
        mEventListener=new EventProcess(context);
        baseContext=context;
    }



    @Override
    public boolean isTarget(Object o) {
        return o instanceof TreatmentprocessBean.RowsBean;
    }

    @Override
    public HandleProcessRecyclerItem createAssemblyItem(ViewGroup viewGroup) {
        return new HandleProcessRecyclerItem(R.layout.list_item_uptask_handle_process,viewGroup);
    }

    public interface EventListener{
        void onClickDetail(View v, int position, TreatmentprocessBean.RowsBean data);
    }

    private static class EventProcess implements EventListener{
        private Context context;
        public EventProcess(Context context) {
            this.context=context;
        }

        @Override
        public void onClickDetail(final View v, int position, final TreatmentprocessBean.RowsBean data) {
            
            switch (v.getId()){
                case R.id.tv_attach:
                    //进入附件详情页
                    Intent intent=new Intent(context,AttachmentActivity.class);
                    intent.putExtra("handledRecordUuid",data.getUuid());
                    ((Activity)context).startActivity(intent);
                    ((Activity)context).overridePendingTransition(R.anim.activity_enter_anim,R.anim.activity_exit_anim);
                    break;
                case R.id.tv_reminders:
                    AlertDialog.Builder builder=new AlertDialog.Builder(context);
                    View view = View.inflate(context, R.layout.dialog_remind_layout, null);
                    final TextView tvAdviseTime= (TextView) view.findViewById(R.id.tv_adbise_time);
                    final TextView tvRemindersTime= (TextView) view.findViewById(R.id.tv_reminders_time);
                    final EditText etAdvise= (EditText) view.findViewById(R.id.et_advise);
                    tvRemindersTime.setText(TimeUtil.convert2String(System.currentTimeMillis()));
                    builder.setView(view);

                    tvAdviseTime.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            new TimePickerUtil(context).showDateTimePicker(tvAdviseTime);
                        }
                    });
                    builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(final DialogInterface dialog, int which) {
                            String remindersTime = tvRemindersTime.getText().toString();
                            String adviseTime = tvAdviseTime.getText().toString();
                            String sessionId = (String) SPUtils.getSp(context, "sessionId", "");
                            if (TextUtils.isEmpty(adviseTime)){
                                Toast.makeText(context, "请选择建议完成时间", Toast.LENGTH_SHORT).show();
                                return;
                            }
                            String advise = etAdvise.getText().toString().trim();
                            NetManager.requestRemind(remindersTime, adviseTime, advise, data.getUuid(), sessionId, new StringCallback() {
                                @Override
                                public void onError(Call call, Exception e, int id) {
                                    Toast.makeText(context, "催办失败", Toast.LENGTH_SHORT).show();
                                }

                                @Override
                                public void onResponse(String response, int id) {
                                    Toast.makeText(context, "催办成功", Toast.LENGTH_SHORT).show();
                                    dialog.dismiss();
                                    ((TextView)v).setText("已催办");
                                    ((TextView)v).setTextColor(context.getResources().getColor(R.color.red));
                                    ((TextView)v).setClickable(false);
                                }
                            });
                        }
                    });
                    builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    AlertDialog dialog = builder.create();
                    dialog.setCancelable(false);
                    dialog.show();
                    break;
            }
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
        private TextView tvReminders;

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
            tvNextHandlerName = (TextView) findViewById(R.id.tv_nextHandlerName);
            tvReminders= (TextView) findViewById(R.id.tv_reminders);
        }

        @Override
        protected void onConfigViews(Context context) {
            tvAttach.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mEventListener.onClickDetail(v,getLayoutPosition(),getData());
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
            tvNextHandlerName.setText(data.getNextHandlerName());

            if (data.getCurrentUserUuid().equals(data.getHandlerUserUuid())){
                if (data.getJudgeRemind()==0){
                    tvReminders.setText("催办");
                }else if (data.getJudgeRemind()==1){
                    tvReminders.setText("已催办");
                    tvReminders.setTextColor(baseContext.getResources().getColor(R.color.red));
                    tvReminders.setClickable(false);
                }else{
                    tvReminders.setText("");
                    tvReminders.setClickable(false);
                }
            }else{
                if (data.getRemindFlag()==0){
                    tvReminders.setText("未催办");
                    tvReminders.setTextColor(baseContext.getResources().getColor(R.color.text_color));
                    tvReminders.setClickable(false);
                }else if (data.getRemindFlag()==1){
                    tvReminders.setText("已催办");
                    tvReminders.setTextColor(baseContext.getResources().getColor(R.color.red));
                    tvReminders.setClickable(false);
                }
            }
        }
    }
}
