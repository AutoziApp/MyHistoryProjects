package com.mapuni.mobileenvironment.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.OnScrollListener;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.mapuni.mobileenvironment.R;
import com.mapuni.mobileenvironment.adapter.WatchDataAdapter;
import com.mapuni.mobileenvironment.config.PathManager;
import com.mapuni.mobileenvironment.model.EntModel;
import com.mapuni.mobileenvironment.model.Pollutant;
import com.mapuni.mobileenvironment.model.PollutantChild;
import com.mapuni.mobileenvironment.model.PollutantModel;
import com.mapuni.mobileenvironment.utils.DateUtils;
import com.mapuni.mobileenvironment.utils.JsonUtil;
import com.mapuni.mobileenvironment.view.MyDecoration;
import com.mapuni.mobileenvironment.view.YutuLoading;
import com.mapuni.mobileenvironment.widget.NiceSpinner;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;
import com.zhy.http.okhttp.request.RequestCall;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import okhttp3.Call;
import okhttp3.Request;

public class CompanyPollutionDetailActivity extends ActivityBase {

    private LinearLayout startTimeLayout, endTimeLayout;
    private RadioButton waterBtn;
    private RadioButton airBtn;
    TextView startTime, endTime;
    private NiceSpinner portSpinner;
    private NiceSpinner grainSpinner;
    private TextView mSearch;
    private EntModel.EnterpriseBean model;
    private RecyclerView mRecycle;
    int currentTimePicker = 0;
    final int StartTimePicker = 0;
    final int EndTimePicker = 1;
    private PollutantModel mPollutant;
    private int CHECKTYPE;
    WatchDataAdapter adapter;
    LinearLayoutManager layoutManager;
    private Map<String,String[]> waterSelect;
    private Map<String,String[]> airSelect;
    private int CurrentPager = 1;
    int i=1;
    private OnScrollListener scrollListener = new OnScrollListener() {
        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            final int lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition();
                if(lastVisibleItemPosition + 1 == adapter.getItemCount()&&date.size()<mPollutant.getTotalRows()){
                    CurrentPager++;
                    readySearch(LOADINGBTN);
                }else{
                        adapter.footRemoved();
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_pollution_detail);
        model = (EntModel.EnterpriseBean) getIntent().getExtras().get("data");
        initView();
        initCalendar();
        readySearch(FIRSTBTN);
    }

    private void initView(){
        setTitle(model.getEntName(),1,0);
        yutuLoading = new YutuLoading(this);
        startTimeLayout = (LinearLayout) findViewById(R.id.starttime_layout);
        endTimeLayout = (LinearLayout) findViewById(R.id.endtime_layout);
        startTime = (TextView) findViewById(R.id.start_time);
        endTime = (TextView) findViewById(R.id.end_time);
        airBtn = (RadioButton) findViewById(R.id.airBtn);
        waterBtn = (RadioButton) findViewById(R.id.waterBtn);
        mSearch = (TextView) findViewById(R.id.searchBtn);
        portSpinner = (NiceSpinner) findViewById(R.id.portSpinner);
        grainSpinner = (NiceSpinner) findViewById(R.id.grainSpinner);
        mRecycle = (RecyclerView) findViewById(R.id.recycle);
        airBtn.setOnClickListener(this);
        waterBtn.setOnClickListener(this);
        mSearch.setOnClickListener(this);
        startTimeLayout.setOnClickListener(this);
        endTimeLayout.setOnClickListener(this);
    }
    private void changeCheckType(){
        CHECKTYPE = mPollutant.getType();
        if(CHECKTYPE==PollutantModel.WATERTYPE){
            waterBtn.setChecked(true);
            airBtn.setTextColor(getResources().getColor(R.color.hide_text_color));
            airBtn.setBackground(getResources().getDrawable(R.drawable.btn_nor));
            waterBtn.setBackground(getResources().getDrawable(R.drawable.btn_press));
        }else if(CHECKTYPE==PollutantModel.AIRTYPE){
            airBtn.setChecked(true);
            waterBtn.setTextColor(getResources().getColor(R.color.hide_text_color));
            waterBtn.setBackground(getResources().getDrawable(R.drawable.btn_nor));
            airBtn.setBackground(getResources().getDrawable(R.drawable.btn_press));
        }else if(CHECKTYPE==PollutantModel.ALLTYPE){
            waterBtn.setChecked(true);
        }
    }
    List<String> date;
    List<String> value;
    private void initData(){
        String[] dates;
        String[] values;
        value = new ArrayList<>();
        date = new ArrayList<>();
        waterSelect = mPollutant.getSelect().get("water");
        airSelect = mPollutant.getSelect().get("air");
        setSpinner(waterSelect);
        startTime.setText(DateUtils.getDate().substring(0,10)+" 00:00");
        endTime.setText(DateUtils.getDate());
        values = mPollutant.getValue();
        dates = mPollutant.getMonitorTime();
       if(values!=null&&dates!=null){
           value.addAll(new LinkedList<>(Arrays.asList(values)));
           date.addAll(new LinkedList<>(Arrays.asList(dates)));
       }
        layoutManager = new LinearLayoutManager(this);
        mRecycle.setLayoutManager(layoutManager);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        adapter = new WatchDataAdapter(this,date,value);
        mRecycle.addItemDecoration(new MyDecoration(this, OrientationHelper.VERTICAL),0);
        mRecycle.setAdapter(adapter);
        mRecycle.addOnScrollListener(scrollListener);
    }
    private void updateData(int type){
        List<String> _Data=null;
        List<String> _Value=null;
        if(mPollutant.getMonitorTime()!=null&&mPollutant.getValue()!=null){
            _Data = Arrays.asList(mPollutant.getMonitorTime());
            _Value = Arrays.asList(mPollutant.getValue());
        }
        if(type!=LOADINGBTN&&value!=null){
            this.date.clear();
            this.value.clear();
        }
        if(_Data!=null&&_Data.size()>0){
            this.date.addAll(_Data);
        }
        if(_Value!=null&&_Value.size()>0){
            this.value.addAll(_Value);
        }
        adapter.footShow();
    }

    private void setSpinner(final Map<String,String[]> map){
        List<String> portData = dataInto(map);
        NiceSpinner.IconChange iChange = new NiceSpinner.IconChange() {
            @Override
            public void spinner() {
                String[] s = map.get(portSpinner.getText().toString());
                if(s.length==0||s==null){
                    s = new String[]{" "};
                }
                List<String> list = Arrays.asList(s);
                grainSpinner.attachDataSource(list);
            }
            @Override
            public void spinnerPress() {
            }
        };
        portSpinner.setIconChange(iChange);
        portSpinner.attachDataSource(portData);
        grainSpinner.attachDataSource(Arrays.asList(map.get(portData.get(0))));
    }
    private List<String> dataInto(Map<String,String[]> selectInfo){
        List<String> portData = new ArrayList<>();
        Set<String> set = selectInfo.keySet();
        Iterator it = set.iterator();
        while(it.hasNext()){
            String s = (String) it.next();
            portData.add(s);
        }
        return  portData;
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.starttime_layout:
                currentTimePicker = StartTimePicker;
                showPopwindow(getDataPick(R.id.starttime_layout));
                break;
            case R.id.endtime_layout:
                currentTimePicker = EndTimePicker;
                showPopwindow(getDataPick(R.id.endtime_layout));
                break;
            case R.id.set:
                if(currentTimePicker==StartTimePicker){
                    if(timeView.findViewById(R.id.date).getVisibility()==View.VISIBLE){
                        startTime.setText(getDate(view));
                    }else{
                        startTime.append(" "+getTime());
                        dismissPopwindow();
                    }
                }else if(currentTimePicker==EndTimePicker){
                    if(timeView.findViewById(R.id.date).getVisibility()==View.VISIBLE){
                        endTime.setText(getDate(view));
                    }else{
                        endTime.append(" "+getTime());
                        dismissPopwindow();
                    }
                }
                break;
            case R.id.airBtn:
                if(CHECKTYPE==Pollutant.AIRTYPE)
                    return;
                CurrentPager = 1;
                readySearch(AIRBTN);
                break;
            case R.id.waterBtn:
                if(CHECKTYPE==Pollutant.WATERTYPE)
                    return;
                CurrentPager = 1;
                readySearch(WATERBTN);
                break;
            case R.id.searchBtn:
                CurrentPager = 1;
                readySearch(SEARCHBTN);
                break;
            case R.id.rightIconB:
                if(value!=null&&date!=null&&value.size()>0&&date.size()>0){
                Intent intent = new Intent(CompanyPollutionDetailActivity.this, ChartActivity.class);
                intent.putStringArrayListExtra("value",new ArrayList(Arrays.asList(value)));
                intent.putStringArrayListExtra("date",new ArrayList(Arrays.asList(date)));
                intent.putExtra("title",model.getEntName());
                intent.putExtra("grain",grainSpinner.getText().toString());
                startActivity(intent);
                }else{
                    Toast.makeText(this,"无数据",Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
    private void readySearch(int btn){
        RequestCall call;
        switch(btn){
            case FIRSTBTN:
                search( OkHttpUtils.post().url(PathManager.FirDataApp)
                        .addParams("EntCode",model.getEntCode()+"").build(),FIRSTBTN);
                break;
            case AIRBTN:
                if(CHECKTYPE==Pollutant.AIRTYPE||CHECKTYPE==Pollutant.ALLTYPE){
                    setSpinner(airSelect);
                    call=intoGetInput();
                    if(call!=null)
                        search(call,SEARCHBTN);
                }else{
                    Toast.makeText(this,getResources().getString(R.string.toast_no_air),Toast.LENGTH_SHORT).show();
                }
                break;
            case WATERBTN:
                if(CHECKTYPE==Pollutant.WATERTYPE||CHECKTYPE==Pollutant.ALLTYPE){
                    setSpinner(waterSelect);
                    call=intoGetInput();
                    if(call!=null){
                        search( call,SEARCHBTN);
                    }

                }else{
                    Toast.makeText(this,getResources().getString(R.string.toast_no_water),Toast.LENGTH_SHORT).show();
                }
                break;
            case SEARCHBTN:
                call = intoGetInput();
                if(call!=null)
                    search( call,SEARCHBTN);
                break;
            case LOADINGBTN:
              call = intoGetInput();
                if(call!=null)
                    search(call,LOADINGBTN);
                break;
        }
    }
    private RequestCall intoGetInput(){
        String port=" ";
        String grain=" ";
        String start=" ";
        String end=" ";
        port = mPollutant.getOutportCode(portSpinner.getText().toString());
        grain = mPollutant.getpollutantCode(grainSpinner.getText().toString());
        start = startTime.getText().toString();
        end = endTime.getText().toString();
        if(port==null||grain==null||start==null||end==null){
            Toast.makeText(this,getResources().getString(R.string.please_all_select),Toast.LENGTH_SHORT).show();
            return null;
        }
        RequestCall call = OkHttpUtils.post().url(PathManager.DataApp)
                .addParams("OutportCode",port)
                .addParams("PollutantCode",grain)
                .addParams("StartTime",start)
                .addParams("EndTime",end)
                .addParams("PageSize","15")
                .addParams("PageIndex",CurrentPager+"")
                .build();
     return call;
    }
//  type==0,初始化
    public void search(RequestCall request ,final int type){
                request
                .execute(new StringCallback() {
                    @Override
                    public void onBefore(Request request, int id) {
                        yutuLoading.showDialog();
                        super.onBefore(request, id);
                    }

                    @Override
                    public void onAfter(int id) {
                        super.onAfter(id);
                    }

                    @Override
                    public void onError(Call call, Exception e, int id) {
                        yutuLoading.showFailed();
                        handle.sendEmptyMessageDelayed(type,2000);
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        yutuLoading.dismissDialog();
                        Log.i("Lybin",response);
                        if(type==FIRSTBTN){
                            mPollutant = (PollutantModel) JsonUtil.jsonToBean(response,PollutantModel.class);
                            changeCheckType();
                            initData();
                        }else{
                            PollutantChild _Pollutant = (PollutantChild) JsonUtil.jsonToBean(response,PollutantChild.class);
                            mPollutant.setValue(_Pollutant.getValue());
                            mPollutant.setMonitorTime(_Pollutant.getMonitorTime());
                            mPollutant.setTotalRows(_Pollutant.getTotalRows());
                            updateData(type);
                        }

                    }
                });
    }

}
