package com.mapuni.caremission_ens.itemfactory;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mapuni.caremission_ens.R;
import com.mapuni.caremission_ens.activity.NewsContentActivity;
import com.mapuni.caremission_ens.adapter.assemblyadapter.AssemblyRecyclerItem;
import com.mapuni.caremission_ens.adapter.assemblyadapter.AssemblyRecyclerItemFactory;
import com.mapuni.caremission_ens.bean.DetailBean;
import com.mapuni.caremission_ens.bean.NewsBean;
import com.mapuni.caremission_ens.views.NewsBar;

public class NewsItemFactory extends AssemblyRecyclerItemFactory<NewsItemFactory.NewsRecyclerItem> {

    public NewsItemFactory(Context context) {
//        this.eventListener = new EventProcessor(context);
    }

    @Override
    public boolean isTarget(Object data) {
        return data instanceof NewsBean;
    }

    @Override
    public NewsRecyclerItem createAssemblyItem(ViewGroup parent) {
        return new NewsRecyclerItem(R.layout.news_item, parent);
    }

    public interface EventListener{
        void onClick(int position, DetailBean user);
    }



    public class NewsRecyclerItem extends AssemblyRecyclerItem<NewsBean> {
        private TextView titleTv;
        private TextView senderTv;
        private TextView timeTv;
        private LinearLayout layout;
        public NewsRecyclerItem(int itemLayoutId, ViewGroup parent) {
            super(itemLayoutId, parent);
        }

        @Override
        protected void onFindViews() {
            titleTv = (TextView) findViewById(R.id.title);
            senderTv = (TextView) findViewById(R.id.sender);
            timeTv = (TextView) findViewById(R.id.time);
            layout = (LinearLayout) findViewById(R.id.layout);
        }

        @Override
        protected void onConfigViews(final Context context) {

            layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, NewsContentActivity.class);
                    intent.putExtra("bean",getData());
                    context.startActivity(intent);
                }
            });
        }

        @Override
        protected void onSetData(int position, NewsBean bean) {
            titleTv.setText(bean.getTITLE());
            senderTv.setText("发送人:"+bean.getSENDPEOPLE());
            timeTv.setText(bean.getSENDTIME());
        }
    }
}
