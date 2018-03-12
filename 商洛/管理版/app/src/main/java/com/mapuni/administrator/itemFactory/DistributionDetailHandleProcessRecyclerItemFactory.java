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
import com.mapuni.administrator.bean.DistributionDetailBean;
import com.mapuni.administrator.bean.TreatmentprocessBean;
import com.mapuni.administrator.utils.TxtUtil;

import me.xiaopan.assemblyadapter.AssemblyRecyclerItem;
import me.xiaopan.assemblyadapter.AssemblyRecyclerItemFactory;

/**
 * Created by 15225 on 2017/8/17.
 */

public class DistributionDetailHandleProcessRecyclerItemFactory extends AssemblyRecyclerItemFactory<DistributionDetailHandleProcessRecyclerItemFactory.HandleProcessRecyclerItem> {

    private Context baseContext;

    public DistributionDetailHandleProcessRecyclerItemFactory(Context context) {
        baseContext=context;
    }



    @Override
    public boolean isTarget(Object o) {
        return o instanceof DistributionDetailBean.RowsBean;
    }

    @Override
    public HandleProcessRecyclerItem createAssemblyItem(ViewGroup viewGroup) {
        return new HandleProcessRecyclerItem(R.layout.list_item_distribution_detail_handle_process,viewGroup);
    }
    

    public class HandleProcessRecyclerItem extends AssemblyRecyclerItem<DistributionDetailBean.RowsBean> {


        private TextView tvGrid;
        private TextView tvOperator;
        private TextView tvOperateType;
        private TextView tvOperateTime;
        private TextView tvOpinion;
        private TextView tvNextHandlerName;


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
            tvNextHandlerName = (TextView) findViewById(R.id.tv_nextHandlerName);
        }

        @Override
        protected void onConfigViews(Context context) {

        }

        @Override
        protected void onSetData(int i, DistributionDetailBean.RowsBean data) {
            tvGrid.setText(TxtUtil.isEmpty(data.getGridName()));//网格名称
            tvOperator.setText(TxtUtil.isEmpty(data.getHandlerUser()));//处理人
            tvOperateType.setText(TxtUtil.isEmpty(data.getOperationTypeName()));//操作类型
            tvOperateTime.setText(TxtUtil.isEmpty(data.getCreateTime()));//操作时间
            tvOpinion.setText(TxtUtil.isEmpty(data.getOpinion()));
            String nextHandlerName = (String) data.getNextHandlerName();
            tvNextHandlerName.setText(TxtUtil.isEmpty(nextHandlerName));
        }
    }
}
