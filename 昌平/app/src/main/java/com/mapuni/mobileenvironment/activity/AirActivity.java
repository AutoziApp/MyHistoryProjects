package com.mapuni.mobileenvironment.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;
import android.widget.TextView;

import com.mapuni.mobileenvironment.R;
import com.mapuni.mobileenvironment.adapter.AirListAdapter;
import com.mapuni.mobileenvironment.utils.TestData;

import java.util.List;

public class AirActivity extends AppCompatActivity {
    private ListView listView;
    private TextView noData;
    private List<String> list;
    private AirListAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_air);
        initDate();
        initView();
    }

    private void initDate() {
        list= TestData.getAirSource();
    }

    private void initView() {
        listView= (ListView) findViewById(R.id.list);
        noData= (TextView) findViewById(R.id.nodata);
        adapter=new AirListAdapter(this,list);
        listView.setAdapter(adapter);
    }
}
