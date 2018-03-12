package com.mapuni.car.mvp.ywrequest.ItemFactory;

import android.content.Context;
import android.graphics.Color;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.mapuni.car.R;
import com.mapuni.car.mvp.ywrequest.model.BigEditValueBean;
import com.mapuni.car.mvp.ywrequest.model.EditValueBean;
import com.mapuni.car.mvp.ywrequest.presenter.DetailPresenter;
import com.mapuni.core.assemblyadapter.AssemblyRecyclerItem;
import com.mapuni.core.assemblyadapter.AssemblyRecyclerItemFactory;

/**
 * Created by lybin on 2017/8/25.
 */

public class BigEditItemFactory extends AssemblyRecyclerItemFactory<BigEditItemFactory.EditBigItem> {

    @Override
    public boolean isTarget(Object data) {
        return data instanceof BigEditValueBean;
    }


    @Override
    public EditBigItem createAssemblyItem(ViewGroup parent) {
        return new EditBigItem(R.layout.item_edit_big_layout,parent);
    }

    public class EditBigItem extends AssemblyRecyclerItem<BigEditValueBean> {
        TextView name, timeSelect, divider,tvFlag;
        EditText value;
        Context context;

        public EditBigItem(int itemLayoutId, ViewGroup parent) {
            super(itemLayoutId, parent);
        }

        @Override
        protected void onFindViews() {
            name = (TextView) findViewById(R.id.name);
           tvFlag = (TextView) findViewById(R.id.flag);
            value = (EditText) findViewById(R.id.value_item);
            divider = (TextView) findViewById(R.id.divider);
        }

        @Override
        protected void onConfigViews(Context context) {
            this.context = context;
            value.addTextChangedListener(new MyEditTextChangeListener());
        }

        @Override
        protected void onSetData(int position, BigEditValueBean bean) {
                if (bean.getColor() != null) {
                    value.setTextColor(Color.parseColor(bean.getColor()));
//                value.setTextColor(context.getResources().getColor(R.color.Blue));
                }
//                value.setFilters(new InputFilter[]{new InputFilter.LengthFilter((Integer.parseInt(bean.getMaxLength())))});
                name.setText(bean.getName());
               value.setText(bean.getValue());
            if("true".equals(bean.getFlag())){
                tvFlag.setVisibility(View.VISIBLE);
            }else {
                tvFlag.setVisibility(View.GONE);
            }
//            value.setOnTouchListener(new View.OnTouchListener() {
//                @Override
//                public boolean onTouch(View v, MotionEvent event) {
//                    value.setFocusable(true);
//                    value.setFocusableInTouchMode(true);
//                    value.requestFocus();
//                    value.findFocus();
//                    InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
//                    imm.showSoftInput(value, InputMethodManager.SHOW_FORCED);// 显示输入法
//                    return true;
//                }
//            });

        }

        public class MyEditTextChangeListener implements TextWatcher {
            /**
             * 编辑框的内容发生改变之前的回调方法
             */
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            /**
             * 编辑框的内容正在发生改变时的回调方法 >>用户正在输入
             * 我们可以在这里实时地 通过搜索匹配用户的输入
             */
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            /**
             * 编辑框的内容改变以后,用户没有继续输入时 的回调方法
             */
            @Override
            public void afterTextChanged(Editable editable) {
                getData().setValue(value.getText().toString());
                DetailPresenter.setParams(getData().getKey(),value.getText().toString());
            }
        }
    }


}
