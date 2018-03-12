package com.mapuni.mobileenvironment.record.itemfactory;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.mapuni.mobileenvironment.R;
import com.mapuni.mobileenvironment.app.DataFactory;
import com.mapuni.mobileenvironment.model.PagerModel;
import com.mapuni.mobileenvironment.utils.DisplayUitl;
import com.mapuni.mobileenvironment.utils.SqliteUtil;
import com.mapuni.mobileenvironment.widget.NiceSpinner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TwoSpinnerHeadFactory extends AssemblyRecyclerItemFactory<TwoSpinnerHeadFactory.TwoSpinnerRecyclerHead> {
    Context context;
    HashMap map;
    PagerModel model;
    String title;
    public TwoSpinnerHeadFactory(Context context,String title, PagerModel model) {
        this.context = context;
        this.model = model;
        this.title = title;
        map = model.getTwoSelectHeadData(title);
    }

    @Override
    public boolean isTarget(Object data) {
        return data instanceof String;
    }

    @Override
    public TwoSpinnerRecyclerHead createAssemblyItem(ViewGroup parent) {
        return new TwoSpinnerRecyclerHead(R.layout.two_spinner_head_factory, parent);
    }

    public class TwoSpinnerRecyclerHead extends AssemblyRecyclerItem<String> {
        private android.widget.TextView TextView;
        private NiceSpinner spinnerL;
        private NiceSpinner spinnerR;
        private DataChangeListener listener;
        List year;
        public TwoSpinnerRecyclerHead(int itemLayoutId, ViewGroup parent) {
            super(itemLayoutId, parent);
            setSpinner();
        }
        String sql;
        private void setSpinner(){
            year = (List<Object>) map.get("nf");
            for(int i=0;i<year.size();i++){
                String s = (String) year.get(i);
                if(!s.contains("年份/"))
                    year.set(i,"年份/"+s);
            }
            spinnerL.attachDataSource(year);
            spinnerL.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    String _Year = (String) year.get(position);
//                    _Year.replace("年","");
//                    _Year.replace("份","");
//                    _Year.replace("/","");
                    _Year = _Year.substring(3,_Year.length());
//                    String Sql="SELECT SYW_YCPFL, FQ_PFL_EYHL,SYW_SCGYGZZPFL,FS_PFL_HXXYL,FS_PFL_AD FROM  HJTJ_BAS_GYQY WHERE glid ='"+model.getComPanyCode()+"' and NF = '"+_Year+"'";
                    String Sql="SELECT SYW_YCPFL, FQ_PFL_EYHL,SYW_SCGYGZZPFL,FS_PFL_HXXYL,FS_PFL_AD FROM  HJTJ_BAS_GYQY WHERE glid ='0000077' and NF = '"+_Year+"'";

                    new MyTask().execute(Sql);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {}
            });
        }
        @Override
        protected void onFindViews() {
            spinnerL = (NiceSpinner) findViewById(R.id.spinnerL);
            spinnerR = (NiceSpinner) findViewById(R.id.spinnerR);
            spinnerL.setBackgroundResource(R.drawable.color_spinner_bg);
            spinnerR.setBackgroundResource(R.drawable.color_spinner_bg);
            spinnerL.setTextColor(context.getResources().getColor(R.color.white));
            spinnerR.setTextColor(context.getResources().getColor(R.color.white));
            spinnerR.setPadding(DisplayUitl.dip2px(context,10),0,0,0);
            spinnerL.setPadding(DisplayUitl.dip2px(context,10),0,0,0);
        }

        @Override
        protected void onConfigViews(Context context) {

        }

        @Override
        public void onChangeListener(DataChangeListener listener) {
            this.listener = listener;
        }

        @Override
        protected void onSetData(int position, String s) {
//            nameTextView.setText(s);
        }
        private class MyTask extends AsyncTask<String,Object,ArrayList> {
            @Override
            protected void onPostExecute(ArrayList list) {
                super.onPostExecute(list);
                listener.onDataChange(list);
            }

            @Override
            protected ArrayList doInBackground(String... params) {
                Object obj= SqliteUtil.getInstance().queryBySqlReturnHashMap(params[0]);
                model.setTwoColumnData(obj,title);
                return model.getTwoColumnData(title);
            }
        }
    }
}
