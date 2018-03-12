package com.mapuni.mobileenvironment.activity;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.mapuni.mobileenvironment.R;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TaskExecuteActivity extends ActivityBase {
    private EditText mEdit;
    private ImageView mCamera;
    private ImageView mFile;
    private TextView tEnd;
    private TextView tTurn;
    private TextView tReturn;
    private LinearLayout bottomLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_execute);
        setTitle("案卷处置");
        initView();
    }
    private void initView(){
        mEdit = (EditText) findViewById(R.id.editText);
        mCamera = (ImageView) findViewById(R.id.cameraView);
        mFile = (ImageView) findViewById(R.id.fileView);
        tEnd = (TextView) findViewById(R.id.endView);
        tTurn = (TextView) findViewById(R.id.turnView);
        tReturn = (TextView) findViewById(R.id.returnView);
        bottomLayout = (LinearLayout) findViewById(R.id.bottomLayout);
        mCamera.setOnClickListener(this);
        mFile.setOnClickListener(this);
        tEnd.setOnClickListener(this);
        tTurn.setOnClickListener(this);
        tReturn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view){
        switch (view.getId()){
            case R.id.cameraView:
                takePhoto();
                break;
            case R.id.fileView:
                takefigure(view);
                break;
            case R.id.endView:
                finish();
                break;
            case R.id.turnView:
               startActivity(new Intent(TaskExecuteActivity.this,TaskTurnActivity.class));
                break;
            case R.id.returnView:
                finish();
                break;
        }
    }

    private void addView(ViewGroup view, String s){
        TextView textView = new TextView(this);
        textView.setText(s);
        textView.setTextColor(getResources().getColor(R.color.PagerBg));
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        view.addView(textView,params);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == TAKE_PHOTOS) {
            if (resultCode == -1) {
//                PATH:PathManager.SDCARD_RASK_DATA_PATH + "Attach/CJCZ/"+imageGuid + "." + "jpg""
                Date now = new Date();
                SimpleDateFormat dataFormat = new SimpleDateFormat("yyyyMMddhhmmss");
                String fileName = dataFormat.format(now);
                addView(bottomLayout,imageGuid.toString());
//                String sql = "insert into T_Attachment (Guid,FileName,FilePath,Extension,FK_Unit,FK_Id,Remark) " + "values ('" + imageGuid
//                        + "','" + fileName + "','" + imageGuid + ".jpg','.jpg','" + T_Attachment.RWXF + "','" + task_id + "','1')";
//                SqliteUtil.getInstance().execute(sql);

//                attachAdapterData = getAttachAdapterData(T_Attachment.RWXF + "", task_id);
//                attachAdapter.updateData(attachAdapterData);
            }
        }

        if (data != null && requestCode == SELECT_SDKARD_FILE) {
            Uri uri = data.getData();
            String path = "";

            if ("content".equalsIgnoreCase(uri.getScheme())) {
                String[] projection = { "_data" };
                Cursor cursor = null;

                try {
                    cursor = this.getContentResolver().query(uri, projection, null, null, null);
                    int column_index = cursor.getColumnIndexOrThrow("_data");
                    if (cursor.moveToFirst()) {
                        path = cursor.getString(column_index);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if ("file".equalsIgnoreCase(uri.getScheme())) {
                path = uri.getPath();
            }

            File souceFile = new File(path);
            String fileName = souceFile.getName();
            addView(bottomLayout,fileName);
//            String extension = "";
//            if (path.contains(".")) {
//                extension = path.substring(path.lastIndexOf("."));
//                fileName = fileName.substring(0, fileName.lastIndexOf("."));
//            }
//            File targetFile = new File(TASK_PATH);
//            if (!targetFile.exists()) {
//                targetFile.mkdirs();
//            }
//            targetFile = new File(TASK_PATH + imageGuid + extension);

//            try {
//                FileHelper.copyFile(souceFile, targetFile);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            String sql = "insert into T_Attachment (Guid,FileName,FilePath,Extension,FK_Unit,FK_Id,Remark) " + "values ('" + imageGuid
//                    + "','" + fileName + "','" + imageGuid + extension + "','" + extension + "','" + T_Attachment.RWXF + "','" + task_id
//                    + "','1')";
//            SqliteUtil.getInstance().execute(sql);

//            attachAdapterData = getAttachAdapterData(T_Attachment.RWXF + "", task_id);
//            attachAdapter.updateData(attachAdapterData);

        }
    }
}
