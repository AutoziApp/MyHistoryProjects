package com.mapuni.mobileenvironment.record.itemfactory;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TextView;

import com.mapuni.mobileenvironment.R;
import com.mapuni.mobileenvironment.app.DataApplication;
import com.mapuni.mobileenvironment.app.DataFactory;
import com.mapuni.mobileenvironment.model.PagerModel;
import com.mapuni.mobileenvironment.utils.SqliteUtil;
import com.mapuni.mobileenvironment.widget.NiceSpinner;

import java.util.ArrayList;
import java.util.HashMap;

public class RectHeadFactory extends AssemblyRecyclerItemFactory<RectHeadFactory.RectRecyclerItem> {
    private ArrayList data;
    private String title;
    private Handler handler;
    private PagerModel model;
    public RectHeadFactory(Context context, String title,PagerModel model) {
        this.title = title;
        this.model = model;
        data = model.getOneSelectHeadData(title);
        this.handler = model.mHandler;
    }

    @Override
    public boolean isTarget(Object data) {
        return data instanceof String;
    }

    @Override
    public RectRecyclerItem createAssemblyItem(ViewGroup parent) {
        return new RectRecyclerItem(R.layout.rect_head_factory, parent);
    }

    public class RectRecyclerItem extends AssemblyRecyclerItem<String> {
        private android.widget.TextView TextView;
        private NiceSpinner spinner;
        private DataChangeListener listener;
        public RectRecyclerItem(int itemLayoutId, ViewGroup parent) {
            super(itemLayoutId, parent);
        }

        @Override
        protected void onFindViews() {
            TextView = (android.widget.TextView) findViewById(R.id.name);
            spinner = (NiceSpinner) findViewById(R.id.spinner);
            setSpinner();
        }
        String sql;
        private void setSpinner(){
            ArrayList keys = DataFactory.getSelectKeysByName(title);
            if(keys==null||keys.size()==0||data==null)
                return;
            ArrayList IDs = new ArrayList();
            ArrayList Names = new ArrayList();
            for(int i=0;i<data.size();i++){
                HashMap map = (HashMap) data.get(i);
                for(int ii=0;ii<keys.size();ii++){
                    String key = (String) keys.get(ii);
                    String s = (String) map.get(key);
                    if(key.contains("id")){
                        IDs.add(s);
                    }else {
//                        change
                        if(s.contains("黑龙江省铁力市")){
                            s = s.substring(6,s.length());
                        }
                        Names.add(s);
                    }

                }
            }
            spinner.attachDataSource(Names);

            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                    if(title.equals("建设项目环评")){
                        sql = DataFactory.BUILD_ASSESS_INFO_SQL+"'"+"B9B5B027-EEA8-441E-8C80-1A96C335D629"+"'";
                    }else if(title.equals("建设项目验收")){
                        sql = DataFactory.BUILD_ACCEPT_INFO_SQL+"'"+"F673A56E-BFF6-4B24-8171-0F76B9E2F745"+"'";
                    }
                    Log.i("Lybin",""+position);
                    new MyTask().execute(sql);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {}
            });
        }
        @Override
        protected void onConfigViews(Context context) {
            TextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }

        @Override
        public void onChangeListener(DataChangeListener listener) {
            this.listener = listener;
        }

        @Override
        protected void onSetData(int position, String s) {
            TextView.setText(s);
//            nameTextView.setText(s);
        }

        private class MyTask extends AsyncTask<String,Object,ArrayList>{
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
