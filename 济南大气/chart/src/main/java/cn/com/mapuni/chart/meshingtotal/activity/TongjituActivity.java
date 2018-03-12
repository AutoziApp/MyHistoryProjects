package cn.com.mapuni.chart.meshingtotal.activity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.json.JSONArray;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.com.mapuni.chart.meshingtotal.R;
import cn.com.mapuni.meshing.base.BaseActivity;
import cn.com.mapuni.meshing.base.controls.loading.YutuLoading;
import cn.com.mapuni.meshing.base.interfaces.PathManager;
import cn.com.mapuni.meshing.base.util.ColumnChartUtils;
import cn.com.mapuni.meshing.base.util.LineChartUtils;
import lecho.lib.hellocharts.view.ColumnChartView;
import lecho.lib.hellocharts.view.LineChartView;

public class TongjituActivity extends BaseActivity implements OnClickListener {
    private YutuLoading yutuLoading;
    private boolean isFirst = true;

    private TextView textView_desc;
    private Spinner spinner_area;
    private ColumnChartView chart_column;
    private LineChartView chart_line;
    private RadioGroup rg_time;
    private RadioButton radio_ri, radio_zhou, radio_yue;

    private Map<String, Object> cityCode = new HashMap<>();
    List<Integer> list_hg = new ArrayList<Integer>();
    List<String> list_lable = new ArrayList<String>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ui_mapuni);
        setBACK_ISSHOW(true);
        SetBaseStyle((RelativeLayout) findViewById(R.id.parentLayout), "Ѳ��ͳ��");
        initView();
        initData();
    }

    private void initData() {
        cityCode.put("������", "370199");
        cityCode.put("������", "370102");
        cityCode.put("������", "370103");
        cityCode.put("������", "370104");
        cityCode.put("������", "370105");
        cityCode.put("������", "370112");
        cityCode.put("������", "370113");
        cityCode.put("ƽ����", "370124");
        cityCode.put("������", "370125");
        cityCode.put("�̺���", "370126");
        cityCode.put("������", "370181");
        radio_zhou.setChecked(true);
        callWeb();
    }

    private void initView() {
        middleLayout = ((LinearLayout) findViewById(R.id.middleLayout));
        LayoutInflater inflater = LayoutInflater.from(this);
        View mainView = inflater.inflate(R.layout.tongjituactivity_layout, null);
        middleLayout.addView(mainView);

        textView_desc = (TextView) findViewById(R.id.textView_desc);
        spinner_area = (Spinner) findViewById(R.id.spinner_area);
        spinner_area.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(!isFirst) {
                    callWeb();
                }
                isFirst = false;
//                try {
//                    Field field = AdapterView.class.getDeclaredField("mOldSelectedPosition");
//                    field.setAccessible(true);  //����mOldSelectedPosition�ɷ���
//                    field.setInt(spinner_area, AdapterView.INVALID_POSITION); //����mOldSelectedPosition��ֵ
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        chart_column = (ColumnChartView) findViewById(R.id.chart_column);
        chart_line = (LineChartView) findViewById(R.id.chart_line);
        rg_time = (RadioGroup) mainView.findViewById(R.id.rg_time);
        rg_time.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.radio_ri) {
                    Toast.makeText(TongjituActivity.this, "��ͼ�ɽ���ˮƽ���Ų鿴", Toast.LENGTH_SHORT);
                    textView_desc.setText("��ȥ24Сʱ��Ѳ�����");
                } else if (checkedId == R.id.radio_zhou) {
                    textView_desc.setText("��ȥ7���Ѳ�����");
                } else if (checkedId == R.id.radio_yue) {
                    Toast.makeText(TongjituActivity.this, "��ͼ�ɽ���ˮƽ���Ų鿴", Toast.LENGTH_SHORT);
                    textView_desc.setText("��ȥ30���Ѳ�����");
                }
            }
        });

        radio_ri = (RadioButton) mainView.findViewById(R.id.radio_ri);
        radio_zhou = (RadioButton) mainView.findViewById(R.id.radio_zhou);
        radio_yue = (RadioButton) mainView.findViewById(R.id.radio_yue);
        radio_ri.setOnClickListener(this);
        radio_zhou.setOnClickListener(this);
        radio_yue.setOnClickListener(this);

    }

    void callWeb() {
        list_hg = new ArrayList<Integer>();
        list_lable = new ArrayList<String>();

        yutuLoading = new YutuLoading(TongjituActivity.this);
        yutuLoading.setCancelable(true);
        yutuLoading.setLoadMsg("���ڻ�ȡ���ݣ����Ժ�", "");
        yutuLoading.showDialog();
        //�ӿڵ��÷���һ
        String url = PathManager.JINAN_URL + "/patrolStatisticByDate";

        String regionCode = "";
        if (spinner_area.getSelectedItemPosition() != 0) {
            regionCode = cityCode.get(spinner_area.getSelectedItem().toString()).toString();
        }
        String dateType = "";
        if (radio_ri.isChecked()) {
            dateType = "day";
        } else if(radio_zhou.isChecked()) {
            dateType = "week";
        } else if(radio_yue.isChecked()) {
            dateType = "month";
        }

        RequestParams params = new RequestParams();// ����ύ����
        params.addBodyParameter("regionCode", regionCode);
        params.addBodyParameter("dateType", dateType);

        HttpUtils utils = new HttpUtils();
        utils.configCurrentHttpCacheExpiry(1000 * 5);
        utils.configTimeout(5 * 1000);//
        utils.configSoTimeout(5 * 1000);//
        utils.send(HttpRequest.HttpMethod.POST, url, params, new RequestCallBack<String>() {
            @Override
            public void onFailure(HttpException arg0, String arg1) {
                if (yutuLoading != null) {
                    yutuLoading.dismissDialog();
                }
                Toast.makeText(TongjituActivity.this, "��������ʧ��", Toast.LENGTH_SHORT).show();
                updateCharVIew();
            }

            @Override
            public void onSuccess(ResponseInfo<String> arg0) {
                if (yutuLoading != null) {
                    yutuLoading.dismissDialog();
                }

                String result = String.valueOf(arg0.result);
                try {
                    ByteArrayInputStream inputStream = new ByteArrayInputStream(result.getBytes());
                    SAXReader reader = new SAXReader();
                    Document document = reader.read(inputStream);
                    Element root = document.getRootElement();
                    JSONArray jsonArray = new JSONArray(root.getText());
                    if (jsonArray != null && jsonArray.length() > 0) {
                        for (int i = 0; i < jsonArray.length(); i++) {
                            String datetime = jsonArray.getJSONObject(i).optString("datetime");
                            int countprolem = jsonArray.getJSONObject(i).optInt("countprolem");
                            list_hg.add(countprolem);
                            if (radio_ri.isChecked()) {
                                list_lable.add(datetime.substring(datetime.length() - 2) + "ʱ");
                            } else if(radio_zhou.isChecked()) {
                                list_lable.add(datetime.substring(datetime.length() - 2) + "��");
                            } else if(radio_yue.isChecked()) {
                                list_lable.add(datetime.substring(datetime.length() - 2) + "��");
                            }

                        }
                    } else {
                        Toast.makeText(TongjituActivity.this, "��������", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    Toast.makeText(TongjituActivity.this, "���������쳣", Toast.LENGTH_SHORT).show();
                }

                updateCharVIew();
            }
        });
    }

    @Override
    public void onClick(View arg0) {
        // TODO Auto-generated method stub
        if (arg0.getId() == R.id.radio_ri || arg0.getId() == R.id.radio_zhou || arg0.getId() == R.id.radio_yue) {
            callWeb();
        }
    }

    private void updateCharVIew() {
        ColumnChartUtils columnChartUtils = new ColumnChartUtils(list_hg, null,
                list_lable.toArray(new String[list_lable.size()]));
        columnChartUtils.setChartView_qy(chart_column);

        LineChartUtils lineChartUtils = new LineChartUtils(list_hg, null,
                list_lable.toArray(new String[list_lable.size()]));
        lineChartUtils.setChartView_qy(chart_line);
    }
}
