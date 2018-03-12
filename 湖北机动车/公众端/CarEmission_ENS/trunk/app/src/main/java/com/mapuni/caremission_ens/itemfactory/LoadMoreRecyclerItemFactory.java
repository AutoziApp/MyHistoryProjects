package com.mapuni.caremission_ens.itemfactory;

import android.view.View;
import android.view.ViewGroup;

import com.mapuni.caremission_ens.R;
import com.mapuni.caremission_ens.adapter.assemblyadapter.AssemblyLoadMoreRecyclerItemFactory;
import com.mapuni.caremission_ens.adapter.assemblyadapter.OnRecyclerLoadMoreListener;


public class LoadMoreRecyclerItemFactory extends AssemblyLoadMoreRecyclerItemFactory {

    public LoadMoreRecyclerItemFactory(OnRecyclerLoadMoreListener eventListener) {
        super(eventListener);
    }

    @Override
    public AssemblyLoadMoreRecyclerItemFactory.AssemblyLoadMoreRecyclerItem createAssemblyItem(ViewGroup parent) {
        return new LoadMoreRecyclerItem(R.layout.list_item_load_more, parent);
    }

    public class LoadMoreRecyclerItem extends AssemblyLoadMoreRecyclerItem {
        private View loadingView;
        private View errorView;
        private View endView;

        public LoadMoreRecyclerItem(int itemLayoutId, ViewGroup parent) {
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
