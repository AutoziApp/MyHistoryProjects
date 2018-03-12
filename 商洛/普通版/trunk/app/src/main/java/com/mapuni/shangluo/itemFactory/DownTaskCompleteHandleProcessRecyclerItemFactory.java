package com.mapuni.shangluo.itemFactory;

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

import com.amap.api.maps2d.model.Text;
import com.bigkoo.pickerview.TimePickerView;
import com.mapuni.shangluo.R;
import com.mapuni.shangluo.activity.dbAc.AttachmentActivity;
import com.mapuni.shangluo.bean.TreatmentprocessBean;
import com.mapuni.shangluo.manager.NetManager;
import com.mapuni.shangluo.utils.SPUtils;
import com.mapuni.shangluo.utils.TimeUtil;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.Calendar;
import java.util.Date;

import me.xiaopan.assemblyadapter.AssemblyRecyclerItem;
import me.xiaopan.assemblyadapter.AssemblyRecyclerItemFactory;
import okhttp3.Call;

/**
 * Created by 15225 on 2017/8/17.
 */

public class DownTaskCompleteHandleProcessRecyclerItemFactory extends AssemblyRecyclerItemFactory<DownTaskCompleteHandleProcessRecyclerItemFactory.HandleProcessRecyclerItem>{

    private EventListener mEventListener;
    private Context baseContext;

    public DownTaskCompleteHandleProcessRecyclerItemFactory(Context context) {
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
        void onClickDetail(View v ,int position, TreatmentprocessBean.RowsBean data);
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
                            showTimePicker(tvAdviseTime);
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

        public void showTimePicker(final TextView tv) {
            Calendar selectedDate = Calendar.getInstance();
            Calendar startDate = Calendar.getInstance();
            Calendar endDate = Calendar.getInstance();
            //正确设置方式 原因：注意事项有说明
            startDate.set(2017,0,1);
            endDate.set(2050,11,31);

            TimePickerView pvTime = new TimePickerView.Builder(context, new TimePickerView.OnTimeSelectListener() {
                @Override
                public void onTimeSelect(Date date, View v) {//选中事件回调
                    tv.setText(TimeUtil.formatDate(date,"yyyy-MM-dd HH:mm:ss"));
                }
            })
                    .setType(new boolean[]{true, true, true, true, true, true})// 默认全部显示
                    .setCancelText("取消")//取消按钮文字
                    .setSubmitText("确定")//确认按钮文字
                    .setContentSize(16)//滚轮文字大小
                    .setTitleSize(20)//标题文字大小
                    .setTitleText("")//标题文字
                    .setOutSideCancelable(true)//点击屏幕，点在控件外部范围时，是否取消显示
                    .isCyclic(true)//是否循环滚动
                    .setTitleColor(Color.BLACK)//标题文字颜色
                    .setSubmitColor(Color.BLACK)//确定按钮文字颜色
                    .setCancelColor(Color.BLACK)//取消按钮文字颜色
                    .setTitleBgColor(Color.WHITE)//标题背景颜色 Night mode
                    .setBgColor(Color.WHITE)//滚轮背景颜色 Night mode
                    .setDate(selectedDate)// 如果不设置的话，默认是系统时间*/
                    .setRangDate(startDate,endDate)//起始终止年月日设定
                    .setLabel("年","月","日","时","分","秒")//默认设置为年月日时分秒
                    .isCenterLabel(true) //是否只显示中间选中项的label文字，false则每项item全部都带有label。
                    .isDialog(true)//是否显示为对话框样式
                    .build();
            pvTime.show();
        }
    }


    

    public class HandleProcessRecyclerItem extends AssemblyRecyclerItem<TreatmentprocessBean.RowsBean>{


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
            
            //点击附件
            tvAttach.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mEventListener.onClickDetail(v,getLayoutPosition(),getData());
                }
            });
            //点击催办
            tvReminders.setOnClickListener(new View.OnClickListener(){

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
