package com.mapuni.administrator.itemFactory;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.mapuni.administrator.R;
import com.mapuni.administrator.activity.myNewsAc.XfshListActivity;
import com.mapuni.administrator.bean.CreatedTaskListBean;

import me.xiaopan.assemblyadapter.AssemblyRecyclerItem;
import me.xiaopan.assemblyadapter.AssemblyRecyclerItemFactory;

/**
 * Created by 15225 on 2017/8/17.
 */

public class CreatedTaskRecyclerItemFactory1 extends AssemblyRecyclerItemFactory<CreatedTaskRecyclerItemFactory1.CreatedTaskRecyclerItem> {

    private Context baseContext;

    public CreatedTaskRecyclerItemFactory1(Context context) {
        baseContext=context;
    }



    @Override
    public boolean isTarget(Object o) {
        return o instanceof CreatedTaskListBean.RowsBean;
    }

    @Override
    public CreatedTaskRecyclerItem createAssemblyItem(ViewGroup viewGroup) {
        return new CreatedTaskRecyclerItem(R.layout.list_item_created_task1,viewGroup);
    }

    public interface EventListener{
        void onClickDistribute(int position, CreatedTaskListBean.RowsBean rowsBean);
        void onClickEdit(int position, CreatedTaskListBean.RowsBean rowsBean);

    }

    public class CreatedTaskRecyclerItem extends AssemblyRecyclerItem<CreatedTaskListBean.RowsBean> {


        private TextView tvTaskName;
        private TextView tvCreateTime;
        private TextView tvTaskType;
        private Button btnDistribute;
        private Button btnEdit;
        private Button btnDel;


        public CreatedTaskRecyclerItem(int itemLayoutId, ViewGroup parent) {
            super(itemLayoutId, parent);
        }

        @Override
        protected void onFindViews() {

            tvTaskName= (TextView) findViewById(R.id.tvTaskName);
            tvCreateTime= (TextView) findViewById(R.id.tvCreateTime);
            tvTaskType= (TextView) findViewById(R.id.tvTaskType);
            btnDistribute= (Button) findViewById(R.id.btn_distribute);
            btnEdit= (Button) findViewById(R.id.btn_edit);
            btnDel= (Button) findViewById(R.id.btn_del);

        }

        @Override
        protected void onConfigViews(final Context context) {
            btnDistribute.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((XfshListActivity)baseContext).onClickDistribute(getLayoutPosition(),getData());
                }
            });

            btnEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((XfshListActivity)baseContext).onClickEdit(getLayoutPosition(),getData());
                }
            });


        }

        @Override
        protected void onSetData(int i, CreatedTaskListBean.RowsBean rowsBean) {

            tvTaskName.setText(rowsBean.getName());
            tvCreateTime.setText(rowsBean.getCreateTime());
            tvTaskType.setText(rowsBean.getTaskType());

        }
    }
}
