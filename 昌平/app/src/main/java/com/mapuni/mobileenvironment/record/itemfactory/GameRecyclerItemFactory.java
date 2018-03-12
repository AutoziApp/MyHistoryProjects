package com.mapuni.mobileenvironment.record.itemfactory;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mapuni.mobileenvironment.R;


public class GameRecyclerItemFactory extends AssemblyRecyclerItemFactory<GameRecyclerItemFactory.GameRecyclerItem> {


    public GameRecyclerItemFactory(Context context) {
    }

    @Override
    public boolean isTarget(Object data) {
        return data instanceof String;
    }

    @Override
    public GameRecyclerItem createAssemblyItem(ViewGroup parent) {
        return new GameRecyclerItem(0, parent);
    }
    public class GameRecyclerItem extends AssemblyRecyclerItem<String> {
        private ImageView iconImageView;
        private TextView nameTextView;
        private TextView likeTextView;
        public GameRecyclerItem(int itemLayoutId, ViewGroup parent) {
            super(itemLayoutId, parent);
        }

        @Override
        protected void onFindViews() {
//            iconImageView = (ImageView) findViewById(R.id.image_gameListItem_icon);
//            nameTextView = (TextView) findViewById(R.id.text_gameListItem_name);
//            likeTextView = (TextView) findViewById(R.id.text_gameListItem_like);
        }

        @Override
        protected void onConfigViews(Context context) {
            iconImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                }
            });
            nameTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                }
            });
            likeTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                }
            });
        }

        @Override
        public void onChangeListener(DataChangeListener listener) {

        }

        @Override
        protected void onSetData(int position, String game) {
//            iconImageView.setImageResource(game.iconResId);
//            nameTextView.setText(game.name);
//            likeTextView.setText(game.like);
        }
    }
}
