package com.mapuni.administrator.itemFactory;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.mapuni.administrator.R;
import com.mapuni.administrator.activity.myNewsAc.DistributionActivity;
import com.mapuni.administrator.activity.myNewsAc.DistributionDetailActivity;
import com.mapuni.administrator.bean.GridNameBean;
import com.mapuni.administrator.manager.NetManager;
import com.mapuni.administrator.utils.Logger;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import me.xiaopan.assemblyadapter.AssemblyRecyclerItem;
import me.xiaopan.assemblyadapter.AssemblyRecyclerItemFactory;
import okhttp3.Call;

/**
 * Created by 15225 on 2017/8/17.
 */

public class DistributionProcessRecyclerItemFactory extends AssemblyRecyclerItemFactory<DistributionProcessRecyclerItemFactory.HandleProcessRecyclerItem> {

    private EventListener mEventListener;
    private Context baseContext;

    public DistributionProcessRecyclerItemFactory(Context context) {
        mEventListener=new DistributionEventProcess(context);
        baseContext=context;
    }
    
    @Override
    public boolean isTarget(Object o) {
        return o instanceof GridNameBean;
    }

    @Override
    public HandleProcessRecyclerItem createAssemblyItem(ViewGroup viewGroup) {
        return new HandleProcessRecyclerItem(R.layout.list_item_distribution_handle_process,viewGroup);
    }

    public interface EventListener{
        void onClickDetail(View v, int position, GridNameBean bean);
    }

    private static class DistributionEventProcess implements EventListener{
        private Context context;
        public DistributionEventProcess(Context context) {
            this.context=context;
        }

        @Override
        public void onClickDetail(final View v, int position, final GridNameBean bean) {

            int judgeDetailsStatus = bean.getJudgeDetailsStatus();
            if (judgeDetailsStatus==0){
                //删除接口
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("提示：");
                builder.setMessage("确定删除该条任务吗？");
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        
                        requestDelete(bean);                
                    
                    }
                });
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(context, "取消删除", Toast.LENGTH_SHORT).show();
                    }
                });
                builder.create().show();
            }else if (judgeDetailsStatus==1){
                //查看详情
                requestDetail(bean);
            }
        }
        private void requestDetail(GridNameBean bean) {
            
            Intent intent=new Intent(context,DistributionDetailActivity.class);
            String taskType=bean.getIssuedTaskStatus();
            intent.putExtra("taskUuid",bean.getUuid());
            intent.putExtra("taskType",taskType);
            context.startActivity(intent);
            ((DistributionActivity)context).overridePendingTransition(R.anim.activity_enter_anim,R.anim.activity_exit_anim);
        }

        private void requestDelete(GridNameBean bean) {
            NetManager.requestDeleteDistribution(bean.getUuid(), new StringCallback() {
                @Override
                public void onError(Call call, Exception e, int id) {
                    Toast.makeText(context, "网络错误！删除失败", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onResponse(String response, int id) {
                    Logger.i(DistributionProcessRecyclerItemFactory.class.getSimpleName(),response.toString());
                    try {
                        JSONObject jsonObject=new JSONObject(response.toString());
                        int status = jsonObject.getInt("status");
                        String msg = jsonObject.getString("msg");
                        if (status==0){
                            Toast.makeText(context, "删除成功", Toast.LENGTH_SHORT).show();

                        }else if(status==-1){
                            Toast.makeText(context, "任务已分配，不能删除", Toast.LENGTH_SHORT).show();
                        }
                        ((DistributionActivity)context).requestListData();
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(context, "网络错误，删除失败", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
    

    public class HandleProcessRecyclerItem extends AssemblyRecyclerItem<GridNameBean> {

        private TextView tvTaskName;
        private TextView tvEndDate;
        private TextView tvTaskSource;
        private TextView tvTaskDetail;
        public HandleProcessRecyclerItem(int itemLayoutId, ViewGroup parent) {
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
                    mEventListener.onClickDetail(v,getLayoutPosition(),getData());
                }
            });
            
        }

        @Override
        protected void onSetData(int i, GridNameBean bean) {
            tvTaskName.setText(bean.getName());
            tvEndDate.setText(bean.getCreateTime());
            int judgeDetailsStatus = bean.getJudgeDetailsStatus();
            if (judgeDetailsStatus==0){
                tvTaskDetail.setText("删除");
                tvTaskSource.setTextColor(baseContext.getResources().getColor(R.color.red));
                tvTaskSource.setText("未签收");
            }else if(judgeDetailsStatus==1){
                tvTaskSource.setTextColor(baseContext.getResources().getColor(R.color.sign_task_color));
                tvTaskSource.setText("已签收");
                tvTaskDetail.setText("查看详情");
            }
        }
    }
}
