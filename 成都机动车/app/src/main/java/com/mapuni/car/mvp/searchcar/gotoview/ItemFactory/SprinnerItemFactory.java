package com.mapuni.car.mvp.searchcar.gotoview.ItemFactory;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TextView;

import com.mapuni.car.R;
import com.mapuni.car.mvp.detailcar.model.SprinnerValueBean;
import com.mapuni.car.mvp.searchcar.gotoview.presenter.CarListPresenter;
import com.mapuni.car.mvp.searchcar.gotoview.presenter.CarListReciverPresenter;
import com.mapuni.core.assemblyadapter.AssemblyRecyclerItem;
import com.mapuni.core.assemblyadapter.AssemblyRecyclerItemFactory;

/**
 * Created by yawei on 2017/8/25.
 */

public class SprinnerItemFactory extends AssemblyRecyclerItemFactory<SprinnerItemFactory.CarItem> {
    @Override
    public boolean isTarget(Object data) {
        return data instanceof SprinnerValueBean;
    }

    @Override
    public CarItem createAssemblyItem(ViewGroup parent) {
        return new CarItem(R.layout.item_list_layout,parent);
    }

    public class CarItem extends AssemblyRecyclerItem<SprinnerValueBean>{
        TextView name;
        TextView Tvflag;
        Spinner spValue;
//        XRadioGroup group;
        Context context;



        public CarItem(int itemLayoutId, ViewGroup parent){
            super(itemLayoutId,parent);
        }
        @Override
        protected void onFindViews() {
            name = (TextView) findViewById(R.id.name);
            Tvflag = (TextView) findViewById(R.id.flag);
            spValue = (Spinner) findViewById(R.id.sp_value);
//            group = (XRadioGroup) findViewById(R.id.group);
        }

        @Override
        protected void onConfigViews(Context context) {
           this.context = context;
            if(getData()!=null){
                inflaterSprinner(getData());
            }
        }

        @Override
        protected void onSetData(int position, SprinnerValueBean bean) {
            name.setText(bean.getName());
//                inflaterRadioButton(bean);
            if("true".equals(bean.getFlag())){
               Tvflag.setVisibility(View.VISIBLE);
            }else {
                Tvflag.setVisibility(View.GONE);
            }
                inflaterSprinner(bean);

//            divider = (TextView) findViewById(R.id.divider);
//            group = (XRadioGroup) findViewById(R.id.group);
//            name.setText(bean.getName());

//            if(group.getTag()!=null){
//                LinearLayout layout = (LinearLayout) group.getTag();
//                group.removeView(layout);
//                group.addView(layout);
//            }else{
//                inflaterRadioButton(bean);
//            }
        }
        private void inflaterSprinner(SprinnerValueBean bean) {
//                        group.setOnCheckedChangeListener(l);
            if(bean!=null) {
                String[] selectes = bean.getSelects();
                String[] codes = bean.getCode();
                String value = bean.getValue();
                if (selectes != null && selectes.length > 0) {
                  SprinnerAdapter adapter=new SprinnerAdapter(context,selectes);
//                    ArrayAdapter adapter = new ArrayAdapter(context, android.R.layout.simple_spinner_item, selectes);
                    spValue.setAdapter(adapter);
                    for (int i = 0; i < selectes.length; i++) {
                        if (value!=null&&value.equals(selectes[i])) {
                            spValue.setSelection(i, true);
                        }
                       //添加联网获取数据
                        if("车辆信息录入".equals(bean.getTitle())) {
                            CarListPresenter.setParams(getData().getKey(), bean.getNameCode());
                        }else {
                            CarListReciverPresenter.setParams(getData().getKey(), bean.getNameCode());
                        }
                    }
                    spValue.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                            spValue.setSelection(i, true);
                            String value = selectes[i];
                            String code = codes[i];
                            if("车辆信息录入".equals(getData().getTitle())) {
                                CarListPresenter.setParams(getData().getKey(), code);
                            }else {
                                CarListReciverPresenter.setParams(getData().getKey(), code);
                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {

                        }
                    });
                }
            }
//            spValue.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                @Override
//                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                    spValue.setSelection(i,true);
//                    String value = selectes[i];
//                    Log.e("kkk",getData().getKey()+"===="+value);
//                    DetailPresenter.setParams(getData().getKey(),value);
//                }
//            });
//            LinearLayout parentLayout = new LinearLayout(context);
//            parentLayout.setOrientation(LinearLayout.VERTICAL);
//            parentLayout.setGravity(Gravity.CENTER_VERTICAL);
//            XRadioGroup.LayoutParams params = new XRadioGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
//                    ViewGroup.LayoutParams.WRAP_CONTENT);
//            parentLayout.setLayoutParams(params);
//            LinearLayout lineLayout = new LinearLayout(context);
//            lineLayout.setOrientation(LinearLayout.HORIZONTAL);
//            XRadioGroup.LayoutParams lineParams = new XRadioGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
//                    ViewGroup.LayoutParams.WRAP_CONTENT);
//            for(int i=0;i<selectes.length;i++){
//                RadioButton rb = new RadioButton(context);
//                rb.setButtonDrawable(R.drawable.radio_bg);
//                rb.setText(selectes[i]);
//                if(getData().getValue()!=null&&selectes[i].equals(getData().getValue())){
////                    group.setTag(rb);
//                }
//                LinearLayout.LayoutParams _Params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
//                        LinearLayout.LayoutParams.WRAP_CONTENT);
//                _Params.weight = 1;
////                LinearLayout.LayoutParams _Params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
////                        DisplayUtil.dip2px(context,30));
//                if(i%3==0&&i>0){ //控制RadioButton换行
//                    parentLayout.addView(lineLayout,lineParams);
//                    lineLayout = new LinearLayout(context);
//                    lineLayout.setOrientation(LinearLayout.HORIZONTAL);
//                    lineLayout.setGravity(Gravity.CENTER_VERTICAL);
//                }
//                lineLayout.addView(rb,_Params);
//            }
//            parentLayout.addView(lineLayout,lineParams);
//            if(group.getChildCount()>=1){
//                group.removeAllViews();
//            }
//            group.addView(parentLayout,params);
//            if(group.getTag()!=null){
//                ((RadioButton)group.getTag()).setChecked(true);
//            }
//        }
        }
//        private void inflaterRadioButton(SelectValueBean bean){
//            group.setOnCheckedChangeListener(l);
//            String[] selectes = bean.getSelects();
//            LinearLayout parentLayout = new LinearLayout(context);
//            parentLayout.setOrientation(LinearLayout.VERTICAL);
//            parentLayout.setGravity(Gravity.CENTER_VERTICAL);
//            XRadioGroup.LayoutParams params = new XRadioGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
//                    ViewGroup.LayoutParams.WRAP_CONTENT);
//            parentLayout.setLayoutParams(params);
//            LinearLayout lineLayout = new LinearLayout(context);
//            lineLayout.setOrientation(LinearLayout.HORIZONTAL);
//            XRadioGroup.LayoutParams lineParams = new XRadioGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
//                    ViewGroup.LayoutParams.WRAP_CONTENT);
//            for(int i=0;i<selectes.length;i++){
//                RadioButton rb = new RadioButton(context);
//                rb.setButtonDrawable(R.drawable.radio_bg);
//                rb.setText(selectes[i]);
//                if(getData().getValue()!=null&&selectes[i].equals(getData().getValue())){
//                    group.setTag(rb);
//                }
//                LinearLayout.LayoutParams _Params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
//                        LinearLayout.LayoutParams.WRAP_CONTENT);
//                _Params.weight = 1;
////                LinearLayout.LayoutParams _Params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
////                        DisplayUtil.dip2px(context,30));
//                if(i%3==0&&i>0){ //控制RadioButton换行
//                    parentLayout.addView(lineLayout,lineParams);
//                    lineLayout = new LinearLayout(context);
//                    lineLayout.setOrientation(LinearLayout.HORIZONTAL);
//                    lineLayout.setGravity(Gravity.CENTER_VERTICAL);
//                }
//                lineLayout.addView(rb,_Params);
//            }
//            parentLayout.addView(lineLayout,lineParams);
//            if(group.getChildCount()>=1){
//                group.removeAllViews();
//            }
//            group.addView(parentLayout,params);
//            if(group.getTag()!=null){
//                ((RadioButton)group.getTag()).setChecked(true);
//            }
//        }
    }


}
