package com.mapuni.shangluo.itemFactory;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.mapuni.shangluo.R;
import com.mapuni.shangluo.activity.wdAc.KnowledgeBaseActivity;
import com.mapuni.shangluo.bean.KnowledgeListBean;
import com.mapuni.shangluo.utils.SPUtils;

import me.xiaopan.assemblyadapter.AssemblyRecyclerItem;
import me.xiaopan.assemblyadapter.AssemblyRecyclerItemFactory;

/**
 * Created by 15225 on 2017/8/17.
 */

public class KnowledgeListFactory extends AssemblyRecyclerItemFactory<KnowledgeListFactory.CreatedKnowledgeRecyclerItem> {

    private Context baseContext;

    public KnowledgeListFactory(Context context) {
        baseContext=context;
    }



    @Override
    public boolean isTarget(Object o) {
        return o instanceof KnowledgeListBean.RowsBean;
    }

    @Override
    public CreatedKnowledgeRecyclerItem createAssemblyItem(ViewGroup viewGroup) {
        return new CreatedKnowledgeRecyclerItem(R.layout.list_item_knowledge,viewGroup);
    }

    public interface EventListener{
        void onClickEdit(int position, KnowledgeListBean.RowsBean rowsBean);
        void onClickDel(int position, KnowledgeListBean.RowsBean rowsBean);
    }

    public class CreatedKnowledgeRecyclerItem extends AssemblyRecyclerItem<KnowledgeListBean.RowsBean> {


        private TextView tvTitle;
        private TextView tvCreateTime;
        private TextView tvContent;
        private Button btnEdit;
        private Button btnDel;


        public CreatedKnowledgeRecyclerItem(int itemLayoutId, ViewGroup parent) {
            super(itemLayoutId, parent);
        }

        @Override
        protected void onFindViews() {

            tvTitle= (TextView) findViewById(R.id.tv_title);
            tvContent= (TextView) findViewById(R.id.tv_content);
            tvCreateTime= (TextView) findViewById(R.id.tv_createTime);
            btnEdit= (Button) findViewById(R.id.btn_edit);
            btnDel= (Button) findViewById(R.id.btn_del);

        }

        @Override
        protected void onConfigViews(final Context context) {

            btnEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((KnowledgeBaseActivity)baseContext).onClickEdit(getLayoutPosition(),getData());
                }
            });

            btnDel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((KnowledgeBaseActivity)baseContext).onClickDel(getLayoutPosition(),getData());
                }
            });
        }

        @Override
        protected void onSetData(int i, KnowledgeListBean.RowsBean rowsBean) {

            tvTitle.setText(rowsBean.getTitle());
            tvCreateTime.setText(rowsBean.getCreateTime());
            tvContent.setText(rowsBean.getContent());

            String roleId=(String) SPUtils.getSp(baseContext,"roleId","");
            if(roleId.contains("2")){//管理员
               btnDel.setVisibility(View.VISIBLE);
                btnEdit.setText("查看/修改");
            }else {
                btnDel.setVisibility(View.GONE);
                btnEdit.setText("查看");
            }

        }
    }
}
