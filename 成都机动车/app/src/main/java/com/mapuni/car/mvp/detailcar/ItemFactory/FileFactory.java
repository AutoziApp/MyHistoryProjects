package com.mapuni.car.mvp.detailcar.ItemFactory;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.mapuni.car.R;
import com.mapuni.car.mvp.detailcar.model.TimeValueBean;
import com.mapuni.core.assemblyadapter.AssemblyRecyclerItem;
import com.mapuni.core.assemblyadapter.AssemblyRecyclerItemFactory;

/**
 * Created by yawei on 2017/8/25.
 */

public class FileFactory extends AssemblyRecyclerItemFactory<FileFactory.CarItem> {
    Activity act;
    private static final int FILE_SELECT_CODE = 0X111;
    @Override
    public boolean isTarget(Object data) {
        return data instanceof TimeValueBean;
    }

    public FileFactory(Activity act){
        this.act = act;
    }

    @Override
    public CarItem createAssemblyItem(ViewGroup parent) {
        return new CarItem(R.layout.item_file_layout,parent);
    }

    public class CarItem extends AssemblyRecyclerItem<TimeValueBean>{
        TextView name,value,timeSelect,divider;
        Context context;
        public CarItem(int itemLayoutId, ViewGroup parent){
            super(itemLayoutId,parent);
        }
        @Override
        protected void onFindViews() {
            name = (TextView) findViewById(R.id.name);
            value = (TextView) findViewById(R.id.value);
            timeSelect = (TextView) findViewById(R.id.timeSelect);
            divider = (TextView) findViewById(R.id.divider);
        }

        @Override
        protected void onConfigViews(Context context) {
            this.context = context;
            value.setVisibility(View.GONE);
            timeSelect.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onSetData(int position, TimeValueBean bean) {

            name.setText(bean.getName());
            timeSelect.setOnClickListener(l->OpenSystemFile());
        }


    }

    public void OpenSystemFile() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
//        intent.setType("file/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        try {
            act.startActivityForResult(Intent.createChooser(intent, "请选择文件!"),
                    FILE_SELECT_CODE);
        } catch (android.content.ActivityNotFoundException ex) {
            // Potentially direct the user to the Market with a Dialog
           Toast.makeText(act, "请安装文件管理器", Toast.LENGTH_SHORT).show();
        }
    }

}
