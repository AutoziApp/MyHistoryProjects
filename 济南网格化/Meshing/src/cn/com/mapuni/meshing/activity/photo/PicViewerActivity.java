package cn.com.mapuni.meshing.activity.photo;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;


import android.view.Window;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;

import com.example.meshing.R;

/**
 * Simple TouchGallery demo based on ViewPager and Photoview.
 * Created by Trojx on 2016/1/3.
 */
public class PicViewerActivity extends FragmentActivity {

    private PhotoViewPager viewPager;
    private TextView tv_indicator;
    private ArrayList<String> urlList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_pic_viewer);

//        String[] urls = {"http://tupian.enterdesk.com/2012/0606/gha/10/11285966_1334673509285.jpg",
//                "http://pic41.nipic.com/20140518/18521768_133448822000_2.jpg",
//                "http://pic41.nipic.com/20140527/2131749_195511402164_2.jpg",
//                "http://scimg.jb51.net/allimg/160810/103-160Q019204J26.jpg",
//                "http://pic47.nipic.com/20140901/6608733_145238341000_2.jpg",
//                "http://pic45.nipic.com/20140807/2531170_221641791877_2.jpg",
//                "http://pic49.nipic.com/file/20140923/2531170_120747532000_2.jpg",};//闇�瑕佸姞杞界殑缃戠粶鍥剧墖
//
        urlList = new ArrayList<String>();
        urlList=getIntent().getStringArrayListExtra("imgPaths");
//        Collections.addAll(urlList, urls);
        
        viewPager = (PhotoViewPager) findViewById(R.id.viewpager);
        tv_indicator = (TextView) findViewById(R.id.tv_indicator);

        viewPager.setAdapter(new PictureSlidePagerAdapter(getSupportFragmentManager()));
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                tv_indicator.setText(String.valueOf(position + 1) + "/" + urlList.size());//<span style="white-space: pre;">鍦ㄥ綋鍓嶉〉闈㈡粦鍔ㄨ嚦鍏朵粬椤甸潰鍚庯紝鑾峰彇position鍊�</span>
            }

            @Override
            public void onPageSelected(int position) {
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    private class PictureSlidePagerAdapter extends FragmentStatePagerAdapter {

        public PictureSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return PictureSlideFragment.newInstance(urlList.get(position));
        }

        @Override
        public int getCount() {
            return urlList.size();//<span style="white-space: pre;">鎸囧畾ViewPager鐨勬�婚〉鏁�</span>
        }
    }
}