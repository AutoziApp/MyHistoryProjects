package com.mapuni.caremission_ens.itemfactory;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mapuni.caremission_ens.R;
import com.mapuni.caremission_ens.adapter.assemblyadapter.AssemblyRecyclerItem;
import com.mapuni.caremission_ens.adapter.assemblyadapter.AssemblyRecyclerItemFactory;
import com.mapuni.caremission_ens.bean.FileBean;
import com.mapuni.caremission_ens.config.PathManager;
import com.mapuni.caremission_ens.presenter.DownLoadFile;
import com.mapuni.caremission_ens.utils.FileUtils;
import com.mapuni.caremission_ens.views.ButtonProgressBar;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class FileItemFactory extends AssemblyRecyclerItemFactory<FileItemFactory.FileRecyclerItem> {
    Context mContext;
    DownLoadFile downFile;
    List<FileBean> fileBeanList;
    List fileNameList;
    List fileSizeList;
    public FileItemFactory(Context context,DownLoadFile downFile) {
        getAdapter();
        mContext = context;
        this.downFile = downFile;
        initFileNameList();
//        fileBeanList = this.downFile.getListFiles();
//        fileNameList = new ArrayList();
//        if(fileBeanList!=null){
//            for(int i=0;i<fileBeanList.size();i++){
//                fileNameList.add(fileBeanList.get(i).getFILENAME());
//            }
//        }

//        this.eventListener = new EventProcessor(context);
    }

    @Override
    public boolean isTarget(Object data) {
        return data instanceof FileBean;
    }

    @Override
    public FileRecyclerItem createAssemblyItem(ViewGroup parent) {
        return new FileRecyclerItem(R.layout.list_item_file, parent);
    }

    public interface EventListener {
        void onClick(int position, FileBean file);
    }
    public class EventOnClick implements EventListener{

        @Override
        public void onClick(int position, FileBean file) {

        }
    }
    public void initFileNameList(){
        fileBeanList = downFile.getListFiles();
        fileNameList = new ArrayList();
        fileSizeList=new ArrayList();
        if(fileBeanList!=null){
            for(int i=0;i<fileBeanList.size();i++){
                fileNameList.add(fileBeanList.get(i).getFILENAME());
                fileSizeList.add(fileBeanList.get(i).getLocalSize());
            }
        }
    }

    public class FileRecyclerItem extends AssemblyRecyclerItem<FileBean> {
        ImageView fileImg;
        TextView fileName;
        TextView nameBelow;
        TextView localSize;
        LinearLayout fileLayout;
        LinearLayout progressLayout;
        ButtonProgressBar progressBar;

        public FileRecyclerItem(int itemLayoutId, ViewGroup parent) {
            super(itemLayoutId, parent);
        }

        @Override
        protected void onFindViews() {
            fileImg = (ImageView) findViewById(R.id.fileImg);
            fileName = (TextView) findViewById(R.id.fileName);
            nameBelow = (TextView) findViewById(R.id.nameBelow);
            localSize = (TextView) findViewById(R.id.localSize);
            fileLayout = (LinearLayout) findViewById(R.id.fileLayout);
            progressBar = (ButtonProgressBar) findViewById(R.id.progressBar);
            progressLayout = (LinearLayout) findViewById(R.id.progressLayout);
        }

        @Override
        protected void onConfigViews(Context context) {
            OnClick onClick = new OnClick();
            fileLayout.setOnClickListener(onClick);
            progressBar.setOnClickListener(onClick);
        }
        private class OnClick implements View.OnClickListener{

            @Override
            public void onClick(View v) {
                switch (v.getId()){
                    case R.id.fileLayout:
                        String name =(String)v.getTag();
                        if(name==null||name.equals("")||!fileNameList.contains(name))
                            break;
                        File file = new File(PathManager.Dir, name);
                        if(!file.exists()){
                            return;
                        }else {
                            String path = file.getPath();
                            Intent pdfFileIntent = FileUtils.getPdfFileIntent(path);
                            mContext.startActivity(pdfFileIntent);
                        }
//                        Intent pdfFileIntent = FileUtils.getPdfFileIntent(PathManager.Dir+name);
//                        mContext.startActivity(pdfFileIntent);
//                        Intent intent = new Intent(mContext, PdfActivity.class);
//                        intent.putExtra("name",name);
//                        mContext.startActivity(intent);
                        break;
                    case R.id.progressBar:
                        if(progressBar.getStatus()==ButtonProgressBar.Status.Start){
                            progressBar.setStatus(ButtonProgressBar.Status.End);
                        }else{
                            progressBar.setStatus(ButtonProgressBar.Status.Start);
                            FileBean bean = getData();
                            downFile.down((ButtonProgressBar) v,bean.getFILEDOWNLOADPATH(),(int)progressBar.getTag(),bean.getFILENAME());
                        }
                        break;
                }
            }
        }

        @Override
        protected void onSetData(int position, FileBean bean) {
            initFileNameList();
            if(fileNameList.contains(bean.getFILENAME())){
//                FileBean _Bean = fileBeanList.get(fileNameList.indexOf(bean.getFILENAME()));
                progressLayout.setVisibility(View.GONE);
//                localSize.setVisibility(View.VISIBLE);
                fileLayout.setTag(bean.getFILENAME());
                String length = bean.getLENGTH();

                fileImg.setImageResource(R.mipmap.file_icon);
//                localSize.setText(_Bean.getLocalSize());
            }else{
                progressLayout.setVisibility(View.VISIBLE);
//                localSize.setVisibility(View.GONE);
                fileImg.setImageResource(R.mipmap.file_icon_gray);
            }
            fileName.setText(bean.getFILENAME());
            nameBelow.setText(bean.getUPLOADTIME());
            if(progressBar.getVisibility()==View.VISIBLE)
                progressBar.setTag(position);
        }
    }
}
