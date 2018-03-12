package com.mapuni.administrator.itemFactory;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mapuni.administrator.R;
import com.mapuni.administrator.activity.wdAc.MessageDetailActivity;
import com.mapuni.administrator.bean.MessageListBean;

import me.xiaopan.assemblyadapter.AssemblyRecyclerItem;
import me.xiaopan.assemblyadapter.AssemblyRecyclerItemFactory;

/**
 * Created by 15225 on 2017/8/17.
 */

public class MessageListRecyclerItemFactory extends AssemblyRecyclerItemFactory<MessageListRecyclerItemFactory.MessageListRecyclerItem> {

    private Context baseContext;

    public MessageListRecyclerItemFactory(Context context) {
        baseContext=context;
    }



    @Override
    public boolean isTarget(Object o) {
        return o instanceof MessageListBean.RowsBean;
    }

    @Override
    public MessageListRecyclerItem createAssemblyItem(ViewGroup viewGroup) {
        return new MessageListRecyclerItem(R.layout.item_message_list,viewGroup);
    }

    public interface EventListener{
        void onClickDetail(int position, MessageListBean.RowsBean rowsBean);
    }

    public class MessageListRecyclerItem extends AssemblyRecyclerItem<MessageListBean.RowsBean> {


        private TextView tvTime;
        private TextView tvName;
        private TextView tvTitle;
        private RelativeLayout rl_message;

        public MessageListRecyclerItem(int itemLayoutId, ViewGroup parent) {
            super(itemLayoutId, parent);
        }

        @Override
        protected void onFindViews() {

            tvTime= (TextView) findViewById(R.id.tv_time);
            tvName= (TextView) findViewById(R.id.tv_name);
            tvTitle= (TextView) findViewById(R.id.tv_title);
            rl_message = (RelativeLayout) findViewById(R.id.rl_message);
        }

        @Override
        protected void onConfigViews(final Context context) {

        }

        @Override
        protected void onSetData(int i, final MessageListBean.RowsBean rowsBean) {

            tvName.setText(rowsBean.getSenderName());
            tvTime.setText(rowsBean.getCreateTime());
            tvTitle.setText(rowsBean.getTitle());
            rl_message.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(baseContext, MessageDetailActivity.class);
                    intent.putExtra("id",rowsBean.getUuid());
//                    intent.putExtra("title",rowsBean.getTitle());
//                    intent.putExtra("time",rowsBean.getCreateTime());
//                    intent.putExtra("name",rowsBean.getSenderName());
//                    intent.putExtra("content",rowsBean.getContent());
                    baseContext.startActivity(intent);
                    ((Activity)baseContext).overridePendingTransition(R.anim.activity_enter_anim,R.anim.activity_exit_anim);
                }
            });
        }
    }
}
