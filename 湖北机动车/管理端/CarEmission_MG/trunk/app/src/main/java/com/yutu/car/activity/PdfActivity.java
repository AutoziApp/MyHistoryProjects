package com.yutu.car.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.joanzapata.pdfview.PDFView;
import com.joanzapata.pdfview.listener.OnPageChangeListener;
import com.yutu.car.R;
import com.yutu.car.config.PathManager;
import com.yutu.car.utils.FileUtils;

import java.io.File;

import butterknife.Bind;
import butterknife.ButterKnife;

public class PdfActivity extends AppCompatActivity implements OnPageChangeListener {
    @Bind(R.id.pdfView)
    PDFView pdfView;
    @Bind(R.id.text)
    TextView text;

    String pdfDir;
    String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf);
        ButterKnife.bind(this);
        pdfDir = PathManager.Dir;
        name = getIntent().getExtras().getString("name");
        display(pdfDir, false);

    }


    private void display(String assetFileName, boolean jumpToFirstPage) {
        if (jumpToFirstPage)
            setTitle(pdfDir = assetFileName);
        File file = new File(assetFileName, name);
//        pdfView.fromFile(file)
//                //                .pages(0, 0, 0, 0, 0, 0) // 默认全部显示，pages属性可以过滤性显示
//                .defaultPage(1)//默认展示第一页
//                .onPageChange(this)//监听页面切换
//                .load();
        String path = file.getPath();
        Intent pdfFileIntent = FileUtils.getPdfFileIntent(path);
        startActivity(pdfFileIntent);
    }

    @Override
    public void onPageChanged(int page, int pageCount) {
        text.setText(page + "/" + pageCount);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
