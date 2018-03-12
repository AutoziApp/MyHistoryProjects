package cn.com.mapuni.chart.meshingtotal.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.pickerview.TimePickerView;
import com.google.gson.Gson;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.vhtableview.VHTableView;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import cn.com.mapuni.chart.meshingtotal.R;
import cn.com.mapuni.chart.meshingtotal.adapter.VHTableAdapter;
import cn.com.mapuni.chart.meshingtotal.model.DepCheckInfoBean;
import cn.com.mapuni.chart.meshingtotal.util.WeekUtils;
import cn.com.mapuni.meshing.base.BaseActivity;
import cn.com.mapuni.meshing.base.controls.loading.YutuLoading;
import cn.com.mapuni.meshing.base.interfaces.PathManager;

public class DepCheckInfoActivity extends BaseActivity implements View.OnClickListener {

    private View mainView;
    private Button btnSearch;
    private TextView tvStartTime, tvEndTime,tvNull;
    private Spinner spCheckType, spYear, spDate;
    private LinearLayout ll1, ll2;
    private HashMap<String, String> conditions = new HashMap<>();
    private YutuLoading yutuLoading;
    private DepCheckInfoBean mDepCheckInfoBean;
    private VHTableView vht_table;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ui_mapuni);
        SetBaseStyle((RelativeLayout) findViewById(R.id.parentLayout), "部门考核");
        initView();
        getTableData();
    }

    private void getTableData() {
        initConditions();
        yutuLoading = new YutuLoading(this);
        yutuLoading.setCancelable(true);
        yutuLoading.setLoadMsg("正在获取信息，请稍候", "");
        yutuLoading.showDialog();
        //接口调用方法一
        String url = PathManager.JINAN_URL + PathManager.getDepCheckInfo;
        RequestParams params = new RequestParams();// 添加提交参数
        params.addBodyParameter("StartTime", conditions.get("StartTime"));
        params.addBodyParameter("EndTime", conditions.get("EndTime"));
        params.addBodyParameter("CheckType","" );
        HttpUtils utils = new HttpUtils();
        utils.configCurrentHttpCacheExpiry(1000 * 5);
        utils.configTimeout(60 * 1000);//
        utils.configSoTimeout(60 * 1000);//
        utils.send(HttpRequest.HttpMethod.POST, url, params, new RequestCallBack<String>() {
            @Override
            public void onFailure(HttpException arg0, String arg1) {
                Toast.makeText(DepCheckInfoActivity.this, "数据请求失败", Toast.LENGTH_SHORT).show();
                if (yutuLoading != null) {
                    yutuLoading.dismissDialog();
                }
            }

            @Override
            public void onSuccess(ResponseInfo<String> arg0) {
                String result = String.valueOf(arg0.result);
                try {
                    ByteArrayInputStream inputStream = new ByteArrayInputStream(result.getBytes());
                    SAXReader reader = new SAXReader();
                    Document document = reader.read(inputStream);
                    Element root = document.getRootElement();
                    mDepCheckInfoBean=new Gson().fromJson(root.getText(),DepCheckInfoBean.class);
                    initVHTtable(mDepCheckInfoBean);
                } catch (Exception e) {

                }
                if (yutuLoading != null) {
                    yutuLoading.dismissDialog();
                }

            }

        });
    }


    private void initVHTtable(DepCheckInfoBean depCheckInfoBean) {

        //设置数据源

        ArrayList<String> titleData=new ArrayList<>();
        titleData.add("部门名称");
        titleData.add("考核类型");
        titleData.add("考核时间");
        titleData.add("小时得分");
        titleData.add("小时超标得分");
        titleData.add("日得分");
        titleData.add("日超标得分");
        titleData.add("总得分");


        ArrayList<ArrayList<String>> contentData=new ArrayList<>();

        List<DepCheckInfoBean.DataBean> dataBeens=  depCheckInfoBean.getData();

        if(dataBeens!=null&&dataBeens.size()>0){
            tvNull.setVisibility(View.GONE);
            vht_table.setVisibility(View.VISIBLE);
        for(DepCheckInfoBean.DataBean dataBean:dataBeens){
            ArrayList<String> contentRowData=new ArrayList<>();
            contentRowData.add(dataBean.getDeptName());//部门名称
            contentRowData.add(depCheckInfoBean.getCheckType());//考核类型
            contentRowData.add(depCheckInfoBean.getCheckTimespan());//考核时间
            contentRowData.add(dataBean.getHourScore());//小时得分
            contentRowData.add(dataBean.getHourAlarmScore() );//小时超标的份
            contentRowData.add(dataBean.getDayScore());//日得分
            contentRowData.add(dataBean.getDayAlarmScore());//日超标的份
            contentRowData.add(dataBean.getScore());//总得分

            contentData.add(contentRowData);
        }


        VHTableAdapter tableAdapter=new VHTableAdapter(this,titleData,contentData,depCheckInfoBean);
        //vht_table.setFirstColumnIsMove(true);//设置第一列是否可移动,默认不可移动
        //vht_table.setShowTitle(false);//设置是否显示标题行,默认显示
        //一般表格都只是展示用的，所以这里没做刷新，真要刷新数据的话，重新setadaper一次吧
        vht_table.setAdapter(tableAdapter);
        }else{
            vht_table.setVisibility(View.GONE);
            tvNull.setVisibility(View.VISIBLE);
        }
    }

    private void initView() {

        middleLayout = ((LinearLayout) findViewById(R.id.middleLayout));
        mainView = View.inflate(this, R.layout.activity_dep_check_info, null);
        middleLayout.addView(mainView);
        vht_table=(VHTableView)mainView.findViewById(R.id.vht_table);
        tvStartTime = (TextView) mainView.findViewById(R.id.tv_start_time);
        tvEndTime = (TextView) mainView.findViewById(R.id.tv_end_time);
        tvNull= (TextView) mainView.findViewById(R.id.tv_null);
        spCheckType = (Spinner) mainView.findViewById(R.id.sp_checktype);
        ll1 = (LinearLayout) mainView.findViewById(R.id.ll1);
        ll2 = (LinearLayout) mainView.findViewById(R.id.ll2);
        spYear = (Spinner) mainView.findViewById(R.id.sp_year);
        spDate = (Spinner) mainView.findViewById(R.id.sp_date);
        btnSearch = (Button) mainView.findViewById(R.id.btn_search);
        spCheckType.setOnItemSelectedListener(checkTypesListener);
        spYear.setOnItemSelectedListener(spYearListener);
        btnSearch.setOnClickListener(this);
        tvStartTime.setOnClickListener(this);
        tvEndTime.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.tv_start_time) {
            popTimeCheckWindow(tvStartTime);
        } else if (i == R.id.tv_end_time) {
            popTimeCheckWindow(tvEndTime);
        } else if (i == R.id.btn_search) {
            getTableData();
        }
    }

    private void popTimeCheckWindow(final TextView tv) {
        Calendar selectedDate = Calendar.getInstance();
        Calendar startDate = Calendar.getInstance();
        //正确设置方式 原因：注意事项有说明
        startDate.set(2013, 0, 1);
        //时间选择器
        TimePickerView pvTime = new TimePickerView.Builder(this, new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {//选中事件回调tvTime.setText(getTime(date));
                Calendar cal = Calendar.getInstance();
                cal.setTime(date);
                String day = cal.get(Calendar.DAY_OF_MONTH) < 10 ? "0" + cal.get(Calendar.DAY_OF_MONTH) : "" + cal.get(Calendar.DAY_OF_MONTH);
                String month = (cal.get(Calendar.MONTH) + 1) < 10 ? "0" + (cal.get(Calendar.MONTH) + 1) : "" + (cal.get(Calendar.MONTH) + 1);
                tv.setText(cal.get(Calendar.YEAR) + "-" + month + "-" + day);

            }
        })
                .setType(new boolean[]{true, true, true, false, false, false})// 默认全部显示
                .setOutSideCancelable(true)//点击屏幕，点在控件外部范围时，是否取消显示
                .isCyclic(true)//是否循环滚动
                .setDate(selectedDate)// 如果不设置的话，默认是系统时间*/
                .setRangDate(startDate, selectedDate)//起始终止年月日设定
                .setLabel("年", "月", "日", "时", "分", "秒")//默认设置为年月日时分秒
                .isDialog(true)//是否显示为对话框样式
                .build();
        pvTime.show();
    }

    private Spinner.OnItemSelectedListener checkTypesListener = new Spinner.OnItemSelectedListener() {

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            switch (position) {
                case 0://时间段考核
                    ll1.setVisibility(View.VISIBLE);
                    ll2.setVisibility(View.GONE);
                    initStaEndTime();
                    break;
                case 1://周考核
                    ll1.setVisibility(View.GONE);
                    ll2.setVisibility(View.VISIBLE);
                    initSpinner(spYear, WeekUtils.getYearList());
                    break;
                case 2://月考核
                    ll1.setVisibility(View.GONE);
                    ll2.setVisibility(View.VISIBLE);
                    initSpinner(spYear, WeekUtils.getYearList());
                    initSpinner(spDate, WeekUtils.getMonthList());
                    break;
                case 3://季度考核
                    ll1.setVisibility(View.GONE);
                    ll2.setVisibility(View.VISIBLE);
                    initSpinner(spYear, WeekUtils.getYearList());
                    initSpinner(spDate, WeekUtils.getJiduList());
                    break;
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };

    private Spinner.OnItemSelectedListener spYearListener = new Spinner.OnItemSelectedListener() {

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            int i = spCheckType.getSelectedItemPosition();
            switch (i) {
                case 1://周考核
                    initSpinner(spDate, WeekUtils.getWeeksByYear(Integer.parseInt(spYear.getSelectedItem().toString().replace("年", ""))));
                    break;
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
        }
    };


    // spinner初始化简单封装
    private void initSpinner(Spinner spinner, List<String> data) {
        // 建立Adapter并且绑定数据源
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, data);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }

    private void initStaEndTime() {
        Calendar cal = Calendar.getInstance();
        String month = (cal.get(Calendar.MONTH) + 1) < 10 ? "0" + (cal.get(Calendar.MONTH) + 1) : cal.get(Calendar.MONTH) + 1 + "";
        String day = cal.get(Calendar.DAY_OF_MONTH) < 10 ? "0" + cal.get(Calendar.DAY_OF_MONTH) : cal.get(Calendar.DAY_OF_MONTH) + "";
        tvStartTime.setText(cal.get(Calendar.YEAR) + "-" + month + "-01");
        tvEndTime.setText(cal.get(Calendar.YEAR) + "-" + month + "-" + day);

    }

    private void initConditions() {
        int i = spCheckType.getSelectedItemPosition();
        switch (i) {
            case 0://时间段考核
                conditions.put("CheckType", "");
                conditions.put("StartTime", tvStartTime.getText().toString() + " 00:00:00");
                conditions.put("EndTime", tvEndTime.getText().toString() + " 00:00:00");
                break;
            case 1://周考核
                conditions.put("CheckType", "1");
                String weekStr = spDate.getSelectedItem().toString();
                String startTime = weekStr.substring(4, 14) + " 00:00:00";
                String endTime = weekStr.substring(16) + " 00:00:00";
                conditions.put("StartTime", startTime);
                conditions.put("EndTime", endTime);
                break;
            case 2://月考核
                conditions.put("CheckType", "2");
                int selectYear = Integer.parseInt(spYear.getSelectedItem().toString().replace("年", ""));
                int selectMonth = Integer.parseInt(spDate.getSelectedItem().toString().replace("月", ""));
                conditions.put("StartTime", WeekUtils.getFirstDayOfMonth(selectYear, selectMonth) + " 00:00:00");
                conditions.put("EndTime", WeekUtils.getLastDayOfMonth(selectYear, selectMonth) + " 00:00:00");
                break;
            case 3://季度考核
                conditions.put("CheckType", "3");
                int selectYear1 = Integer.parseInt(spYear.getSelectedItem().toString().replace("年", ""));
                int indexOfJidu = spDate.getSelectedItemPosition() + 1;
                conditions.put("StartTime", WeekUtils.getFirstDayOfJidu(selectYear1, indexOfJidu) + " 00:00:00");
                conditions.put("EndTime", WeekUtils.getLastDayOfJidu(selectYear1, indexOfJidu) + " 00:00:00");
                break;
        }
    }

}
