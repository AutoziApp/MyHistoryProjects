package cn.com.mapuni.meshingtotal.activity.common;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.HashMap;

import cn.com.mapuni.meshing.base.BaseActivity;
import cn.com.mapuni.meshingtotal.R;

public class CommonDetailActivity extends BaseActivity {
    private TextView task_name, GridArea, SupervisePerson, patrol_object_name, create_time, address, problem_desc, problem_result;
    private ImageView iv_img;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ui_mapuni);
        setBACK_ISSHOW(true);
        SetBaseStyle((RelativeLayout) findViewById(R.id.parentLayout), "案件线索");
        initView();
        initData();
    }

    protected void initData() {
        HashMap<String, Object> hashMap = (HashMap<String, Object>) getIntent().getSerializableExtra("bean");
        task_name.setText(hashMap.get("task_name").toString());
        GridArea.setText(hashMap.get("GridArea").toString());
        SupervisePerson.setText(hashMap.get("SupervisePerson").toString());
        patrol_object_name.setText(hashMap.get("patrol_object_name").toString());
        create_time.setText(hashMap.get("create_time").toString());
        address.setText(hashMap.get("address").toString());
        problem_desc.setText(hashMap.get("problem_desc").toString());
        problem_result.setText("暂无");
        Glide.with(this).load(getIntent().getStringExtra("imgurl")).placeholder(R.mipmap.pic_loading).error(R.mipmap.pic_loading_fail).into(iv_img);
    }

    private void initView() {
        middleLayout = ((LinearLayout) findViewById(R.id.middleLayout));
        LayoutInflater inflater = LayoutInflater.from(this);
        View mainView = inflater.inflate(R.layout.activity_common_detail, null);
        mainView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT));
        middleLayout.addView(mainView);

        task_name = (TextView) findViewById(R.id.task_name);
        GridArea = (TextView) findViewById(R.id.GridArea);
        SupervisePerson = (TextView) findViewById(R.id.SupervisePerson);
        patrol_object_name = (TextView) findViewById(R.id.patrol_object_name);
        create_time = (TextView) findViewById(R.id.create_time);
        address = (TextView) findViewById(R.id.address);
        problem_desc = (TextView) findViewById(R.id.problem_desc);
        problem_result = (TextView) findViewById(R.id.problem_result);
        iv_img = (ImageView) findViewById(R.id.iv_img);
    }
}
