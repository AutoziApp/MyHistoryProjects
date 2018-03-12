package com.mapuni.core.assemblyadapter.loadmoreitemfactory;

import android.view.View;
import android.view.ViewGroup;

import com.mapuni.core.R;
import com.mapuni.core.assemblyadapter.AssemblyLoadMoreItemFactory;
import com.mapuni.core.assemblyadapter.OnLoadMoreListener;


public class LoadMoreItemFactory extends AssemblyLoadMoreItemFactory {

    public LoadMoreItemFactory(OnLoadMoreListener eventListener) {
        super(eventListener);
    }

    @Override
    public AssemblyLoadMoreItemFactory.AssemblyLoadMoreItem createAssemblyItem(ViewGroup parent) {
        return new LoadMoreItem(R.layout.list_item_load_more, parent);
    }

    public class LoadMoreItem extends AssemblyLoadMoreItem {
        private View loadingView;
        private View errorView;
        private View endView;

        public LoadMoreItem(int itemLayoutId, ViewGroup parent) {
            super(itemLayoutId, parent);
        }

        @Override
        protected void onFindViews() {
            loadingView = findViewById(R.id.text_loadMoreListItem_loading);
            errorView = findViewById(R.id.text_loadMoreListItem_error);
            endView = findViewById(R.id.text_loadMoreListItem_end);
        }

        @Override
        public View getErrorRetryView() {
            return errorView;
        }

        @Override
        public void showLoading() {
            loadingView.setVisibility(View.VISIBLE);
            errorView.setVisibility(View.INVISIBLE);
            endView.setVisibility(View.INVISIBLE);
        }

        @Override
        public void showErrorRetry() {
            loadingView.setVisibility(View.INVISIBLE);
            errorView.setVisibility(View.VISIBLE);
            endView.setVisibility(View.INVISIBLE);
        }

        @Override
        public void showEnd() {
            loadingView.setVisibility(View.INVISIBLE);
            errorView.setVisibility(View.INVISIBLE);
            endView.setVisibility(View.VISIBLE);
        }
    }
}
