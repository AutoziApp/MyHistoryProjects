package com.mapuni.mobileenvironment.record.itemfactory;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import com.mapuni.mobileenvironment.R;


public class GameRecyclerHeadFactory extends AssemblyRecyclerItemFactory<GameRecyclerHeadFactory.GameRecyclerItem> {


    public GameRecyclerHeadFactory(Context context) {
    }

    @Override
    public boolean isTarget(Object data) {
        return data instanceof String;
    }

    @Override
    public GameRecyclerItem createAssemblyItem(ViewGroup parent) {
        return new GameRecyclerItem(R.layout.air_data_item, parent);
    }

    public class GameRecyclerItem extends AssemblyRecyclerItem<String> {
        private android.widget.TextView TextView;
        private android.widget.TextView TextView2;
        private DataChangeListener listener;
        public GameRecyclerItem(int itemLayoutId, ViewGroup parent) {
            super(itemLayoutId, parent);
        }

        @Override
        protected void onFindViews() {
            TextView = (android.widget.TextView) findViewById(R.id.textView);
            TextView2 = (android.widget.TextView) findViewById(R.id.textView2);
        }

        @Override
        protected void onConfigViews(Context context) {
            TextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    int index = 0;
//                    List<Object> dataList = new ArrayList<Object>(20);
//                    while (index < 20) {
//                        Game game = new Game();
//                        game.iconResId = R.drawable.ic_launcher;
//                        game.name = "英雄联盟" + (index + 1);
//                        game.like = "不喜欢";
//                        dataList.add(game);
//                        index++;
//                    }
//                    listener.onDataChange(dataList);
                }
            });
            TextView2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    eventListener.onClickName(getLayoutPosition(), getData());
//                    int index = 0;
//                    List<Object> dataList = new ArrayList<Object>(20);
//                    while (index < 20) {
//                        Game game = new Game();
//                        game.iconResId = R.drawable.ic_launcher;
//                        game.name = "英雄不联盟" + (index + 1);
//                        game.like = "喜欢";
//                        dataList.add(game);
//                        index++;
//                    }
//                    listener.onDataChange(dataList);

                }
            });
        }

        @Override
        public void onChangeListener(DataChangeListener listener) {
            this.listener = listener;
        }

        @Override
        protected void onSetData(int position, String s) {
//            nameTextView.setText(s);
        }
    }
}
