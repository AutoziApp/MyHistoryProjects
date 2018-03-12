package com.mapuni.administrator.activity.dbAc;


import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.mapuni.administrator.R;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import uk.co.senab.photoview.PhotoView;
/**
 *  @author Tianfy
 *  @time 2017/9/12  15:20
 *  @describe 图片展示Activity
 */
public class ImagePlayerActivity extends AppCompatActivity {


    @BindView(R.id.tv_phone)
    PhotoView mTvPhone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_player);
        ButterKnife.bind(this);
        String url = getIntent().getStringExtra("imageUrl");
//        Toast.makeText(this, url, Toast.LENGTH_SHORT).show();
        Picasso.with(this)
                .load(url)
                .placeholder(R.drawable.loading_pic)
                .error(R.drawable.jzsb)
                .into(mTvPhone);
    }


}
