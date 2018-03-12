package com.mapuni.mobileenvironment.record.itemfactory;

import android.content.Context;
import android.view.ViewGroup;

import com.mapuni.mobileenvironment.R;
import com.mapuni.mobileenvironment.utils.DisplayUitl;
import com.mapuni.mobileenvironment.widget.NiceSpinner;

public class SearchHeadFactory extends AssemblyRecyclerItemFactory<SearchHeadFactory.SearchHead> {
    Context context;
    public SearchHeadFactory(Context context) {
        this.context = context;
    }

    @Override
    public boolean isTarget(Object data) {
        return data instanceof String;
    }

    @Override
    public SearchHead createAssemblyItem(ViewGroup parent) {
        return new SearchHead(R.layout.search_head_factory, parent);
    }

    public class SearchHead extends AssemblyRecyclerItem<String> {
        private android.widget.TextView TextView;
        private NiceSpinner spinnerR;
        public SearchHead(int itemLayoutId, ViewGroup parent) {
            super(itemLayoutId, parent);
        }

        @Override
        protected void onFindViews() {
        }

        @Override
        protected void onConfigViews(Context context) {

        }

        @Override
        public void onChangeListener(DataChangeListener listener) {
//            this.listener = listener;
        }

        @Override
        protected void onSetData(int position, String s) {
//            nameTextView.setText(s);
        }
    }
}
