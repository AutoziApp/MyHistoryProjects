package cn.com.mapuni.meshingtotal.activity.common;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.goldnut.app.sdk.view.pullrefresh.PullToRefreshBase;
import com.goldnut.app.sdk.view.pullrefresh.PullToRefreshListView;
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
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.com.mapuni.meshing.base.BaseActivity;
import cn.com.mapuni.meshing.base.controls.loading.YutuLoading;
import cn.com.mapuni.meshing.base.interfaces.PathManager;
import cn.com.mapuni.meshingtotal.R;
import cn.com.mapuni.meshingtotal.adapter.CommonItemRecordsAdapter;
import cn.com.mapuni.meshingtotal.model.CommonItemRecords;

@SuppressWarnings("LossyEncoding")
public class CommonListActivity extends BaseActivity implements View.OnClickListener{
    private YutuLoading yutuLoading;
    private EditText etSearch;
    private PullToRefreshListView mPullRefreshListView;
    private ListView listView;
    private List<CommonItemRecords> data = new ArrayList<>();
    private List<Map<String, Object>> totalData = new ArrayList<>();
    private CommonItemRecordsAdapter adapter;
    private int pageIndex=1;
    private int pageSize=10;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ui_mapuni);
        setBACK_ISSHOW(true);
        SetBaseStyle((RelativeLayout) findViewById(R.id.parentLayout),"��������");
        initView();
        initData();
    }

    protected void initData() {
        readRiskSources("");
    }

    private void initView() {
        middleLayout = ((LinearLayout) findViewById(R.id.middleLayout));
        LayoutInflater inflater = LayoutInflater.from(this);
        View mainView = inflater.inflate(R.layout.activity_common_list, null);
        mainView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT));
        middleLayout.addView(mainView);

        etSearch = (EditText) findViewById(R.id.etSearch);
        findViewById(R.id.ivSearch).setOnClickListener(this);
        mPullRefreshListView=(PullToRefreshListView)findViewById(R.id.listView);
        mPullRefreshListView.setHasMoreData(true);
        listView=mPullRefreshListView.getRefreshableView();
        mPullRefreshListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener() {
            @Override
            public void onPullDownToRefresh() {
                pageIndex++;
                readRiskSources(etSearch.getText().toString().trim());
            }

            @Override
            public void onPullUpToRefresh() {
                pageIndex++;
                readRiskSources(etSearch.getText().toString().trim());
            }
        });

        adapter=new CommonItemRecordsAdapter(this, data);
        listView.setAdapter(adapter);
        listView.setDividerHeight(0);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(CommonListActivity.this, CommonDetailActivity.class);
                intent.putExtra("bean", (HashMap<String, Object>)totalData.get(position));
                intent.putExtra("title", "������Ϣ");
                intent.putExtra("imgurl",data.get(position).getImgPath());
                startActivity(intent);
            }
        });
    }

    @Override
    public void onClick(View arg0) {
        // TODO Auto-generated method stub
        Intent intent = null;
        switch (arg0.getId()) {
            case R.id.ivSearch:
                data.clear();
                pageIndex=1;
                readRiskSources(etSearch.getText().toString().trim());
                break;
            default:
                break;
        }
    }

    /**
     * �������ƶ�ȡ����Դ
     * @param
     */
    private void readRiskSources(String key) {
        yutuLoading = new YutuLoading(this);
        yutuLoading.setCancelable(true);
        yutuLoading.setLoadMsg("���ݻ�ȡ�У����Ե�...", "");
        yutuLoading.showDialog();
        //�ӿڵ��÷���һ
        String url = PathManager.JINAN_URL + PathManager.getGridRecodeInfo;

        RequestParams params = new RequestParams();// ����ύ����?
        params.addBodyParameter("startTime","");
        params.addBodyParameter("endTime","");
        params.addBodyParameter("gridCode","");
        params.addBodyParameter("pageSize",pageSize+"");
        params.addBodyParameter("pageIndex",pageIndex+"" );
        params.addBodyParameter("key",key);

        HttpUtils utils = new HttpUtils();
        utils.configCurrentHttpCacheExpiry(1000 * 60);
        utils.configTimeout(60 * 1000);//
        utils.configSoTimeout(60 * 1000);//
        utils.send(HttpRequest.HttpMethod.POST, url, params, new RequestCallBack<String>() {
            @Override
            public void onFailure(HttpException arg0, String arg1) {
                Toast.makeText(CommonListActivity.this, "���ݻ�ȡʧ��", Toast.LENGTH_SHORT).show();
                if (yutuLoading != null) {
                    yutuLoading.dismissDialog();
                }
                updateList();
            }

            @Override
            public void onSuccess(ResponseInfo<String> arg0) {
                String result = String.valueOf(arg0.result);

                try {
                    ByteArrayInputStream inputStream = new ByteArrayInputStream(result.getBytes());
                    SAXReader reader = new SAXReader();
                    Document document = reader.read(inputStream);
                    Element root = document.getRootElement();
                    JSONObject jsonObject = new JSONObject(root.getText());
                    JSONArray jsonArray=jsonObject.getJSONArray("Data");
                    if (jsonArray != null && jsonArray.length() > 0) {
                        for (int i = 0; i < jsonArray.length(); i++) {
                            String task_name = jsonArray.getJSONObject(i).optString("task_name");
                            String GridArea = jsonArray.getJSONObject(i).optString("GridArea");
                            String SupervisePerson = jsonArray.getJSONObject(i).optString("SupervisePerson");
                            String patrol_object_name = jsonArray.getJSONObject(i).optString("patrol_object_name");
                            String create_time = jsonArray.getJSONObject(i).optString("create_time");
                            String address = jsonArray.getJSONObject(i).optString("address");
                            String problem_desc = jsonArray.getJSONObject(i).optString("problem_desc");
                            String imgUrl=jsonArray.getJSONObject(i).optString("img_path");
                            //ȫ������
                            Map<String, Object> map = new HashMap<String, Object>();
                            map.put("task_name", task_name);
                            map.put("GridArea", GridArea);
                            map.put("SupervisePerson", SupervisePerson);
                            map.put("patrol_object_name", patrol_object_name);
                            map.put("create_time", create_time);
                            map.put("address", address);
                            map.put("problem_desc", problem_desc);
                            totalData.add(map);
                            //�б���ʾ����
                            CommonItemRecords bean = new CommonItemRecords();
                            bean.setName(task_name);
                            bean.setDesc(SupervisePerson);
                            bean.setImgPath(PathManager.IMG_URL_JINAN+imgUrl);
                            data.add(bean);
                        }
                    }

                    adapter.notifyDataSetChanged();
                } catch (Exception e) {

                }
                if (yutuLoading != null) {
                    yutuLoading.dismissDialog();
                }
                updateList();
            }
        });
    }

    private android.os.Handler handler = new android.os.Handler(){

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (yutuLoading != null) {
                yutuLoading.dismissDialog();
            }
            updateList();
        }
    };

    private void updateList(){
        adapter.notifyDataSetChanged();
        mPullRefreshListView.onPullUpRefreshComplete();
        mPullRefreshListView.onPullDownRefreshComplete();
    }
}
