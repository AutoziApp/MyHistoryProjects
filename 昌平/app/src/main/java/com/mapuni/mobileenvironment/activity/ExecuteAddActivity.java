package com.mapuni.mobileenvironment.activity;

import android.content.Intent;
import android.database.Cursor;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mapuni.mobileenvironment.R;
import com.mapuni.mobileenvironment.location.GPSLocation;
import com.mapuni.mobileenvironment.model.Node;
import com.mapuni.mobileenvironment.view.AllGridCityDialog;
import com.mapuni.mobileenvironment.view.YutuLoading;
import com.mapuni.mobileenvironment.widget.NiceSpinner;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class ExecuteAddActivity extends ActivityBase  {
    private Button btnCancel,btnCommit;
    private NiceSpinner spinnerA;
    private NiceSpinner spinnerB;
    private NiceSpinner spinnerC;
    private TextView vGrid;
    private TextView mLat;
    private TextView mLong;
    private TextView startTime;
    private TextView endTime;
    private ImageView cameraView;
    private ImageView fileView;
    private YutuLoading loading;
    private LinearLayout bottomLayout;
    private Node node;
    int currentTimePicker = 0;
    final int StartTimePicker = 0;
    final int EndTimePicker = 1;
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.commit:
                finish();
                break;
            case R.id.cancel:
                finish();
                break;
            case R.id.gridView:
                if(node==null){
                    new SearchData().execute();
                }else{
                    loading.showDialog();
                    startGrid(node);
                }
                break;
            case R.id.lat:
                Location location = GPSLocation.getInstance(this).getLocation(LocationManager.PASSIVE_PROVIDER);
                if (location != null) {
                    locationChangeListener.onChange(location);
                }
                break;
            case R.id.start_time:
                currentTimePicker = StartTimePicker;
                showPopwindow(getDataPick(R.id.start_time));
                break;
            case R.id.end_time:
                currentTimePicker = EndTimePicker;
                showPopwindow(getDataPick(R.id.end_time));
                break;
            case R.id.set:
                if(currentTimePicker==StartTimePicker){
                    if(timeView.findViewById(R.id.date).getVisibility()==View.VISIBLE){
                        startTime.setText(getDate(v));
                    }else{
                        startTime.append(" "+getTime());
                    }
                }else if(currentTimePicker==EndTimePicker){
                    if(timeView.findViewById(R.id.date).getVisibility()==View.VISIBLE){
                        endTime.setText(getDate(v));
                    }else{
                        endTime.append(" "+getTime());
                    }
                }
                break;
            case R.id.cameraView:
                takePhoto();
                break;
            case R.id.fileView:
                takefigure(v);
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_execute_add);
        initView();
        setTitle("创建处置");
        initData();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==0){
            loading.dismissDialog();
            if(data==null){
                return;
            }
            String _name = (String)data.getExtras().get("hylbname");
            String _Code = (String)data.getExtras().get("hylbcode");
            vGrid.setText(_name);
        }
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
    private void addView(ViewGroup view,String s){
        TextView textView = new TextView(this);
        textView.setText(s);
        textView.setTextColor(getResources().getColor(R.color.PagerBg));
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        view.addView(textView,params);
    }
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 0:
                    Intent intent = new Intent(ExecuteAddActivity.this, AllGridCityDialog.class);
                    startActivityForResult(intent,0);
                    break;
            }
        }
    };
    private void initView(){
        btnCancel = (Button) findViewById(R.id.cancel);
        btnCommit = (Button) findViewById(R.id.commit);
        spinnerA = (NiceSpinner) findViewById(R.id.spinnerA);
        spinnerB = (NiceSpinner) findViewById(R.id.spinnerB);
        spinnerC = (NiceSpinner) findViewById(R.id.spinnerC);
        vGrid = (TextView) findViewById(R.id.gridView);
        mLat = (TextView) findViewById(R.id.lat);
        mLong = (TextView) findViewById(R.id.longitude);
        startTime = (TextView) findViewById(R.id.start_time);
        endTime = (TextView) findViewById(R.id.end_time);
        cameraView = (ImageView) findViewById(R.id.cameraView);
        fileView = (ImageView) findViewById(R.id.fileView);
        bottomLayout = (LinearLayout) findViewById(R.id.bottomLayout);
        cameraView.setOnClickListener(this);
        fileView.setOnClickListener(this);
        startTime.setOnClickListener(this);
        endTime.setOnClickListener(this);
        mLat.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
        btnCommit.setOnClickListener(this);
        vGrid.setOnClickListener(this);
    }
    private void initData(){
        if(loading==null){
            loading = new YutuLoading(this);
        }
        NiceSpinner.IconChange  ichange = new NiceSpinner.IconChange() {
            @Override
            public void spinner() {

            }

            @Override
            public void spinnerPress() {

            }
        };
        List<String> typeSet = new LinkedList<>(Arrays.asList("气",
                "水","声","固废","手续"));
        List<String> srcSet = new LinkedList<>(Arrays.asList("举报触发",
                "日常创建","工作上报","一般任务","领导交办","重点企业监管","信访推送"));
        List<String> ifSet = new LinkedList<>(Arrays.asList("   是","   否"));
        spinnerA.attachDataSource(srcSet);
        spinnerB.attachDataSource(typeSet);
        spinnerC.attachDataSource(ifSet);
        spinnerB.setIconChange(ichange);
        spinnerA.setIconChange(ichange);
        spinnerC.setIconChange(ichange);
        initCalendar();
    }
    class SearchData extends AsyncTask<Node,Node,Node> {
        @Override
        protected void onPreExecute() {
            loading.showDialog();
            super.onPreExecute();
        }

        @Override
        protected Node doInBackground(Node... node) {
            Node _node = Node.getAllPeople();
            return _node;
        }

        @Override
        protected void onPostExecute(Node n) {
            node = n;
            startGrid(n);
            super.onPostExecute(n);
        }
    }

    private void startGrid(Node n){
        Intent intent = new Intent(ExecuteAddActivity.this,AllGridCityDialog.class);
        intent.putExtra("",n);
        startActivityForResult(intent,0);
    }

    public GPSLocation.LocationChangeListener locationChangeListener = new GPSLocation.LocationChangeListener() {
        @Override
        public void onChange(Location location) {
            mLat.setText(location.getLatitude()+"");
            mLong.setText(location.getLongitude()+"");
//            Toast.makeText(ExecuteAddActivity.this,location_mark.getLongitude()+"-----"+location_mark.getLatitude(),Toast.LENGTH_SHORT).show();
        }
    };



}
