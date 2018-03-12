package com.mapuni.mobileenvironment.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.mapuni.mobileenvironment.R;
import com.mapuni.mobileenvironment.widget.NiceSpinner;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class CompanySearchActivity extends AppCompatActivity {
    private Button btnSearch;
    private NiceSpinner spinnerA;
    private NiceSpinner spinnerB;
    private NiceSpinner spinnerC;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_search);
        initView();
    }

    private void initView() {
        btnSearch = (Button) findViewById(R.id.btn_search);
        spinnerA = (NiceSpinner) findViewById(R.id.qyjbxx_query_city_name);
        spinnerB = (NiceSpinner) findViewById(R.id.qyjbxx_query_county_name);
        spinnerC = (NiceSpinner) findViewById(R.id.qyjbxx_query_village_name);
        List<String> aSet = new LinkedList<>(Arrays.asList("举报触发",
                "日常创建", "工作上报", "一般任务", "领导交办", "重点企业监管", "信访推送"));
        List<String> bSet = new LinkedList<>(Arrays.asList("气",
                "水", "声", "固废", "手续"));
        List<String> cSet = new LinkedList<>(Arrays.asList("   是", "   否"));
        NiceSpinner.IconChange ichange = new NiceSpinner.IconChange() {
            @Override
            public void spinner() {

            }

            @Override
            public void spinnerPress() {

            }
        };
        spinnerA.attachDataSource(aSet);
        spinnerB.attachDataSource(bSet);
        spinnerC.attachDataSource(cSet);
        spinnerB.setIconChange(ichange);
        spinnerA.setIconChange(ichange);
        spinnerC.setIconChange(ichange);
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(CompanySearchActivity.this, CompanySearchResultActivity.class));
            }
        });
    }
}
