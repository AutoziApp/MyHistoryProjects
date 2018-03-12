package com.yutu.car.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yutu.car.R;
import com.yutu.car.bean.CityNameBean;
import com.yutu.car.fragment.HomePageFragment;
import com.yutu.car.presenter.NetControl;
import com.yutu.car.utils.JsonUtil;
import com.yutu.car.utils.SPUtils;
import com.yutu.car.views.YutuLoading;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.Call;

/**
 * @name CarEmission_ENS
 * @class name：com.mapuni.caremission_ens.activity
 * @class describe
 * @anthor tianfy
 * @time 2017/10/25 14:36
 * @change
 * @chang time
 * @class describe
 */

public class CityManagerActivity extends BaseActivity implements AdapterView.OnItemClickListener {


    @Bind(R.id.c_list)
    GridView mCList;
    @Bind(R.id.showFailed)
    RelativeLayout mShowFailed;

    private Context mContext;
    private List<CityNameBean.InfoBean> info;
    private GridViewAdapter mGridViewAdapter;
    private int mCurrentPosition;
    YutuLoading yutuLoading ;

    private String[] cityNames=new String[]{"黄石市","十堰市","宜昌市","襄阳市",
            "鄂州市","荆门市","孝感市","荆州市","黄冈市","咸宁市","随州市","恩施州","仙桃市",
            "潜江市","天门市","神农架","武汉市"}; 
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city_manager);
        ButterKnife.bind(this);
        yutuLoading = new YutuLoading(this);
        this.mContext=this;
        setTitle("城市管理",true,false);
        initData();
    }

    private void initData() {
        mCurrentPosition = (int) SPUtils.getSp(mContext, "currentPosition", 0);
        NetControl mControl = new NetControl();
        info=new ArrayList<CityNameBean.InfoBean>();
        mGridViewAdapter = new GridViewAdapter(mContext,info);
        mCList.setAdapter(mGridViewAdapter);
        mCList.setOnItemClickListener(this);
        yutuLoading.showDialog();
        mControl.requestCityData(call);
    }

    private StringCallback call = new StringCallback() {
        @Override
        public void onError(Call call, Exception e, int id) {
            
            loadLocalData();
        }

        @Override
        public void onResponse(String response, int id) {
            yutuLoading.dismissDialog();
            CityNameBean cityNameBean= (CityNameBean) JsonUtil.jsonToBean(response, CityNameBean.class);
            String result = cityNameBean.getResult();
            List<CityNameBean.InfoBean> tempInfo = cityNameBean.getInfo();
            if ("1".equals(result)&&tempInfo.size()>0){
                info.addAll(tempInfo);
                mGridViewAdapter.notifyDataSetChanged();
            }else{
                loadLocalData();
            }
        }
    };

    private void loadLocalData() {
        yutuLoading.dismissDialog();
        for(String cityName:cityNames){
            CityNameBean.InfoBean infoBean=new CityNameBean.InfoBean();
            infoBean.setJC(cityName);
            info.add(infoBean);
        }
        mGridViewAdapter.notifyDataSetChanged();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        GridViewAdapter gridViewAdapter= (GridViewAdapter) parent.getAdapter();
        CityNameBean.InfoBean infoBean= (CityNameBean.InfoBean) gridViewAdapter.getItem(position);
        GridViewAdapter.ViewHolder viewHolder= (GridViewAdapter.ViewHolder) view.getTag();
        viewHolder.tvLocate.setText("默认");
        viewHolder.tvLocate.setVisibility(View.VISIBLE);
        SPUtils.setSP(mContext,"currentPosition",position);
        Intent intent=new Intent();
        intent.putExtra("cityName",infoBean.getJC());
        setResult(HomePageFragment.mResultCode,intent);
        finish();
    }
    

    private class GridViewAdapter extends BaseAdapter{
        private Context mContext;
        private List<CityNameBean.InfoBean> mInfo;

        public GridViewAdapter(Context context, List<CityNameBean.InfoBean> info) {
            this.mContext=context;
            this.mInfo=info;
        }

        @Override
        public int getCount() {
            return mInfo.size();
        }

        @Override
        public Object getItem(int position) {
            return mInfo.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder=null;
            if (convertView==null){
                convertView=View.inflate(mContext,R.layout.city_name_item,null);
                viewHolder=new ViewHolder();
                viewHolder.tvCityName = (TextView) convertView.findViewById(R.id.tv_cityName);
                 viewHolder.tvLocate = (TextView) convertView.findViewById(R.id.tv_locate);
                convertView.setTag(viewHolder);
            }else{
                viewHolder = (ViewHolder) convertView.getTag();
            }
            viewHolder.tvCityName.setText(mInfo.get(position).getJC());
            if (mCurrentPosition==position){
                viewHolder.tvLocate.setText("默认");
                viewHolder.tvLocate.setVisibility(View.VISIBLE);
            }
            return convertView;
        }
        class ViewHolder{
            TextView tvCityName;
            TextView tvLocate;
        }
    }
}
