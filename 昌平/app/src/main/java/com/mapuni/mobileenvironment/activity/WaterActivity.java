package com.mapuni.mobileenvironment.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.mapuni.mobileenvironment.R;
import com.mapuni.mobileenvironment.adapter.WaterListAdapter;
import com.mapuni.mobileenvironment.utils.TestData;

import java.util.List;

public class WaterActivity extends AppCompatActivity {
    private ListView listView;
    private TextView noData;
    private List<String> list;
    private WaterListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_water);
        initDate();
        initView();
        initLister();
    }

    private void initLister() {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Bundle bundle = new Bundle();
                bundle.putBoolean("isWater", true);
                bundle.putString("name", list.get(i));
                startActivity(new Intent(WaterActivity.this, WaterPollutionDetailActivity.class).putExtras(bundle));
            }
        });
    }

    private void initDate() {
//        list = TestData.getWaterSource();
    }

    private void initView() {
        listView = (ListView) findViewById(R.id.list);
        noData = (TextView) findViewById(R.id.nodata);
        adapter = new WaterListAdapter(this, list);
        listView.setAdapter(adapter);
    }
}
