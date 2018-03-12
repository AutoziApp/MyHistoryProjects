package com.mapuni.mobileenvironment.app;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.mapuni.mobileenvironment.R;
import com.mapuni.mobileenvironment.utils.JsonUtil;
import com.mapuni.mobileenvironment.utils.SqliteUtil;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;

/**
 * Created by yawei on 2016/12/6.
 */
public class DataFactory {
    public static int[] TaskDetailInfoName = {R.string.task_title,R.string.task_src,
            R.string.task_time,R.string.task_grid,R.string.task_creater,R.string.task_isSure,
            R.string.task_describe,R.string.task_options,R.string.task_create_time,R.string.task_type_,
            R.string.task_time,R.string.task_adress,R.string.task_station};
    public static String[] TaskDetailFlowName = {"张三","李四"};
    private static HashMap<String,ArrayList> listNameMap;
    private static HashMap<String,ArrayList> KeysMap;
    private static HashMap<String,ArrayList> selectKeysMap;
    private static ArrayList<ArrayList> titleData;
    private  FetchData fetchData;
    public static int TWO_COLUMN = 1;
    public static int THREE_COLUMN = 2;
    public static int ONE_SELECT = 3;
    public static int LEFT_SELECT = 4;
    public static int RIGHT_SELECT = 5;
//    企业基本信息
    private final String INFO_LIST_SQL = "select enter.PolSorCode,enter.EntName,enter.PolluterAbbreviation,enter.EntAddress,StreetTown.Name,Village.Name name1," +
            "enter.Longitude,enter.Latitude,Industry.name name2,enter.FK_EIADirectoryIndustry,IndustryType.name name3,PolluteType.name name4," +
            "EntControlType.name name5,SourceType.name name6,enter.FK_ReceivingWater,EntStatu.name name7,enter.LegalIDCardNum,enter.LegalPersonTel" +
            ",enter.EnvPorPersonName,enter.EnvPorPersonTel,enter.IfRiskSourceEnterprise,enter.FK_EntAttributeType,enter.UpdateDate," +
            "enter.DataSource from PUB_BAS_EnterpriseInfo enter " +
            " LEFT JOIN PUB_CODE_Region StreetTown ON enter.FK_StreetTown = StreetTown.Code" +
            " LEFT JOIN PUB_CODE_Region Village ON enter.FK_Village = Village.Code" +
            " LEFT JOIN PUB_CODE_Industry Industry ON enter.FK_NationalEIndustry = Industry.Code" +
            " LEFT JOIN PUB_CODE_Industry IndustryType ON enter.FK_KeyIndustryType = IndustryType.Code" +
            " LEFT JOIN PUB_CODE_PolluteType PolluteType ON enter.FK_PolluteType = PolluteType.Code" +
            " LEFT JOIN PUB_CODE_EntControlType EntControlType ON enter.FK_PolluterSuperviseType = EntControlType.Code" +
            " LEFT JOIN PUB_CODE_PollutionSourceType SourceType ON enter.FK_PolSoType = SourceType.Code" +
            " LEFT JOIN ENT_CODE_EntStatu EntStatu ON enter.FK_EntStatus = EntStatu.Code" +
            " WHERE enter.PolSorCode=";
//  建设项目环评项目名称
    private final String BUILD_ITEMNAME_SQL = "SELECT id,itemname from XMSP_BUS_ProjectApproval where PolSorCode =";
//  建设项目验收项目名称
    private final String ACCEPT_ITEMNAME_SQL = "SELECT id,ProjectName from XMYS_BUS_ProjectAcceptance where PolSorCode =";
//  建设项目环评项目信息
public static final String BUILD_ASSESS_INFO_SQL = "SELECT xmsp.ConstructionUnitName,atype.Name atypename,ApprovalNumber,ApprovalDate,bulid.Name buildname,data.Name dataname FROM XMSP_BUS_ProjectApproval xmsp" +
        " LEFT JOIN PUB_CODE_DataSource data ON data.Code=xmsp.DataSource" +
        " LEFT JOIN XM_CODE_ApprovalType atype ON atype.Code=xmsp.FK_EIAType" +
        " LEFT JOIN XM_CODE_BuildProperty bulid ON bulid.Code=xmsp.FK_BuildNature where xmsp.ID =";
    public static final String BUILD_ACCEPT_INFO_SQL = "SELECT EntName,AcceptNumber,ApprReplyNumber,ProjectOverview,EPMCompleteStatus,AcceReplyNumber,AcceptanceOpinion," +
            "acc.Name ASFK_AccType,AcceptanceDate,procpt.UpdateDate UpdateDate,ds.Name AS DataSource" +
            " FROM XMYS_BUS_ProjectAcceptance procpt LEFT JOIN XM_CODE_AcceType AS acc ON procpt.FK_AccType = acc.Code" +
            " LEFT JOIN PUB_CODE_DataSource AS ds ON procpt.DataSource = ds.Code where procpt.ID =";
    public final String ENVIRONMENT_OUT_YEAR_SQL = "SELECT NF FROM  HJTJ_BAS_GYQY WHERE  glid=";
    public final String ENVIRONMENT_OUT_INFO_SQL="SELECT SYW_YCPFL, FQ_PFL_EYHL,SYW_SCGYGZZPFL,FS_PFL_HXXYL,FS_PFL_AD FROM  HJTJ_BAS_GYQY WHERE glid ='ITEM1' and NF = 'ITEM2'";
    public interface FetchData{
        public void fetchBefore();
        public void getData(Object obj);
        public void fetchFail();
    };
    public void setFetchData(FetchData fc){
        fetchData = fc;
    }

    public static ArrayList<String> getTitleList(int i){

        if(titleData==null||titleData.size()==0){
            titleData = new ArrayList();
            ArrayList<String> aTitle = new ArrayList<>(Arrays.asList(DataApplication.App.getResources().getStringArray(R.array.info_title)));
            ArrayList<String> bTitle = new ArrayList<>(Arrays.asList(DataApplication.App.getResources().getStringArray(R.array.build_title)));
            ArrayList<String> cTitle = new ArrayList<>(Arrays.asList(DataApplication.App.getResources().getStringArray(R.array.environment_title)));
            ArrayList<String> dTitle = new ArrayList<>(Arrays.asList(DataApplication.App.getResources().getStringArray(R.array.grain_title)));
            ArrayList<String> eTitle = new ArrayList<>(Arrays.asList(DataApplication.App.getResources().getStringArray(R.array.letter_title)));
            titleData.add(aTitle);
            titleData.add(bTitle);
            titleData.add(cTitle);
            titleData.add(dTitle);
            titleData.add(eTitle);
        }
        return titleData.get(i);
    }
    public static ArrayList getData(String s,HashMap<String,Object> map){
        ArrayList list = new ArrayList();
        HashMap _Map = new HashMap();
        ArrayList<String> infoList = new ArrayList<>(Arrays.asList(DataApplication.App.getResources().getStringArray(R.array.info_list)));
        ArrayList<String> infoListKey = new ArrayList<>(Arrays.asList(DataApplication.App.getResources().getStringArray(R.array.info_list_key)));
        for(int i=0;i<infoListKey.size();i++){
            String value = (String) map.get(infoListKey.get(i));
            _Map.put("name",infoList.get(i));
            _Map.put("value",value);
        }
        list.add(_Map);
        return  list;
    }
    public static ArrayList getSelectKeysByName(String s){
        if(selectKeysMap==null||selectKeysMap.size()==0){
            selectKeysMap = new HashMap<>();
            ArrayList<String> buildAssessItemNameKeys = new ArrayList<>(Arrays.asList(DataApplication.App.getResources().getStringArray(R.array.build_assess_itemname_key)));
            ArrayList<String> buildAcceptItemNameKeys = new ArrayList<>(Arrays.asList(DataApplication.App.getResources().getStringArray(R.array.build_accept_itemname_key)));

            selectKeysMap.put("建设项目验收",buildAcceptItemNameKeys);
            selectKeysMap.put("建设项目环评",buildAssessItemNameKeys);
        }

        ArrayList list = selectKeysMap.get(s);
        if(list==null){
            return  null;
        }
        return list;
    }
    public static ArrayList getKeysByName(String s){
        if(KeysMap==null||KeysMap.size()==0){
            KeysMap = new HashMap<>();
            ArrayList<String> infoListKeys = new ArrayList<>(Arrays.asList(DataApplication.App.getResources().getStringArray(R.array.info_list_key)));
            ArrayList<String> buildAssessInfoKeys = new ArrayList<>(Arrays.asList(DataApplication.App.getResources().getStringArray(R.array.build_assess_key)));
            ArrayList<String> buildAcceptInfoKeys = new ArrayList<>(Arrays.asList(DataApplication.App.getResources().getStringArray(R.array.build_accept_info_key)));
            ArrayList<String> environmentOutInfoKeys = new ArrayList<>(Arrays.asList(DataApplication.App.getResources().getStringArray(R.array.environment_out_list_key)));

// ArrayList<String> environmentExpendList = new ArrayList<>(Arrays.asList(DataApplication.App.getResources().getStringArray(R.array.environment_expend_list)));
//            ArrayList<String> environmentRubbishOutList = new ArrayList<>(Arrays.asList(DataApplication.App.getResources().getStringArray(R.array.environment_rubbish_out_list)));
            KeysMap.put("企业基本信息",infoListKeys);
            KeysMap.put("建设项目环评",buildAssessInfoKeys);
            KeysMap.put("建设项目验收",buildAcceptInfoKeys);
            KeysMap.put("污染物排放分析",environmentOutInfoKeys);
//            listNameMap.put("项目变更",buildAssessList);
//            listNameMap.put("污染物排放分析",environmentOutList);
//            listNameMap.put("能源消耗分析",environmentExpendList);
//            listNameMap.put("固废、危废排放分析",environmentRubbishOutList);
        }

        ArrayList list = KeysMap.get(s);
        if(list==null){
           return  null;
        }
        return list;
    }
    public static ArrayList getListByName(String s){
        if(listNameMap==null||listNameMap.size()==0){
            listNameMap = new HashMap<>();
            ArrayList<String> infoList = new ArrayList<>(Arrays.asList(DataApplication.App.getResources().getStringArray(R.array.info_list)));
            ArrayList<String> buildAssessList = new ArrayList<>(Arrays.asList(DataApplication.App.getResources().getStringArray(R.array.build_assess_list)));
            ArrayList<String> buildAcceptList = new ArrayList<>(Arrays.asList(DataApplication.App.getResources().getStringArray(R.array.build_accept_list)));
            ArrayList<String> environmentOutList = new ArrayList<>(Arrays.asList(DataApplication.App.getResources().getStringArray(R.array.environment_out_list)));
            ArrayList<String> environmentExpendList = new ArrayList<>(Arrays.asList(DataApplication.App.getResources().getStringArray(R.array.environment_expend_list)));
            ArrayList<String> environmentRubbishOutList = new ArrayList<>(Arrays.asList(DataApplication.App.getResources().getStringArray(R.array.environment_rubbish_out_list)));

            listNameMap.put("企业基本信息",infoList);
            listNameMap.put("建设项目环评",buildAssessList);
            listNameMap.put("建设项目验收",buildAcceptList);
            listNameMap.put("项目变更",buildAcceptList);
            listNameMap.put("污染物排放分析",environmentOutList);
            listNameMap.put("能源消耗分析",environmentExpendList);
            listNameMap.put("固废、危废排放分析",environmentRubbishOutList);
        }

        ArrayList list = listNameMap.get(s);
        if(list==null){
            list = new ArrayList();
            for(int i=0;i<10;i++){
                list.add("201609");
            }
        }
        return list;
    }
    public  void getData(String s,String type){

        new MyTask().execute(new String[]{s,type});
//        fetchData.getData(null);
    }
    private int queryResultTypeBySql(String s){
        if(s.contains(INFO_LIST_SQL)||s.contains(BUILD_ASSESS_INFO_SQL)||s.contains(BUILD_ACCEPT_INFO_SQL)||
                s.contains(ENVIRONMENT_OUT_INFO_SQL)){
            return TWO_COLUMN;
        }else if(s.contains(BUILD_ITEMNAME_SQL)||s.contains(ACCEPT_ITEMNAME_SQL)){
            return ONE_SELECT;
        }else if(s.contains(ENVIRONMENT_OUT_YEAR_SQL)){
            return LEFT_SELECT;
        }
        return TWO_COLUMN;
    }
    private String queryTableNameBySql(String s){
        if(s.contains(INFO_LIST_SQL)){
            return  "企业基本信息";
        }else if(s.contains(BUILD_ITEMNAME_SQL)||s.contains(BUILD_ASSESS_INFO_SQL)){
            return "建设项目环评";
        }else if(s.contains(ACCEPT_ITEMNAME_SQL)||s.contains(BUILD_ACCEPT_INFO_SQL)){
            return "建设项目验收";
        }else if(s.contains(ENVIRONMENT_OUT_YEAR_SQL)|| s.contains(ENVIRONMENT_OUT_INFO_SQL)){
            return "污染物排放分析";
        }
        return "";
    }
    String[] SQLs;
    String[] detailSQL;
    public  void getData(String code,Handler handler){
        if(SQLs==null||SQLs.length==0){
            SQLs = new String[]{INFO_LIST_SQL+"'"+code+"'",BUILD_ITEMNAME_SQL+"'"+"0014086"+"'",ACCEPT_ITEMNAME_SQL+"'"+"0014086"+"'", ENVIRONMENT_OUT_YEAR_SQL +"'0028942'"};
        }
        for(int i=0;i<SQLs.length;i++){
            queryTable(SQLs[i],queryTableNameBySql(SQLs[i]),
                    queryResultTypeBySql(SQLs[i]),handler);
        }
    }
    public  void getDetailData(ArrayList items,Handler handler){
        if(detailSQL==null||detailSQL.length==0){
            detailSQL = new String[]{BUILD_ASSESS_INFO_SQL , BUILD_ACCEPT_INFO_SQL,ENVIRONMENT_OUT_INFO_SQL};
        }
        for(int i=0;i<detailSQL.length;i++){
            String tableName = queryTableNameBySql(detailSQL[i]);
            int resultType = queryResultTypeBySql(detailSQL[i]);
            Object obj = items.get(i);
            if(obj instanceof HashMap){
                detailSQL[i] = detailSQL[i].replace("ITEM1", (CharSequence) ((HashMap) obj).get("ITEM1"));
                detailSQL[i] = detailSQL[i].replace("ITEM2", (CharSequence) ((HashMap) obj).get("ITEM2"));
                queryTable(detailSQL[i],tableName, resultType,handler);
                continue;
            }
            queryTable(detailSQL[i]+"'"+items.get(i)+"'",tableName, resultType,handler);
        }
    }
    private void queryTable(final String s, final String tableName,final int type, final Handler handler){
        handler.post(new Runnable() {
            @Override
            public void run() {
                Object obj=null;
                if(type==TWO_COLUMN){
                    obj= SqliteUtil.getInstance().queryBySqlReturnHashMap(s);

                }else{
                    obj= SqliteUtil.getInstance().queryBySqlReturnArrayListHashMap(s);
                }
                Message msg = new Message();
                Bundle bd = new Bundle();
                bd.putSerializable("data",(Serializable) obj);
                bd.putString("tableName",tableName);
                msg.setData(bd);
                msg.what = type;
                handler.sendMessage(msg);
            }
        });
    }

    private class MyTask extends AsyncTask<String,Integer,Object>{
        @Override
        protected Object doInBackground(String... params) {
//            String enterpriseInfo = "select t.entname,t.rowid from PUB_BAS_EnterpriseInfo t";
            Object obj = null;
            if(params[1].equals("map")){
                obj= SqliteUtil.getInstance().queryBySqlReturnHashMap(params[0]);
            }else{
                obj= SqliteUtil.getInstance().queryBySqlReturnArrayListHashMap(params[0]);
            }

            return obj;
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            fetchData.fetchBefore();
        }
        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            fetchData.getData(o);
        }
    }

    public void updateDataBase(final String tableName, final Handler handler){

        OkHttpUtils.get().url("http://192.168.15.101:8080/huanbaoms/heilj/OneEnterParse/OneEnterParseUpdateData")
                .addParams("tableName",tableName).addParams("updateTime","2016-01-21 16:47:42").build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                Log.i("Lybin","updateDataBase&---"+tableName+"--"+e.getMessage().toString());
            }

            @Override
            public void onResponse(String response, int id) {
                Log.i("Lybin","updateDataBase&--"+tableName+"---"+response);
                List<HashMap<String,Object>> data= JsonUtil.jsonToListMap(response);
                Map map = JsonUtil.jsonToMap(response);
                if(map!=null&&map.size()>=1){
                    final ArrayList list = (ArrayList) map.get("rows");

                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            updateTable(tableName,list,handler);
                        }
                    });
                }else{
                    handler.sendEmptyMessage(1);
                }
            }
        });
    }
    private synchronized void updateTable(final String tableName,ArrayList list,Handler handler){
        for(int i=0;i<list.size();i++){
            ArrayList _Data = (ArrayList) list.get(i);
            StringBuffer sb = new StringBuffer();
            for(int ii=0;ii<_Data.size();ii++){
                Object obj = (Object) _Data.get(ii);
                final String value = obj+"";
                if(ii==_Data.size()-1&&obj!=null){
                    sb.append("'"+obj+"'");
                }else if(ii<_Data.size()-1&&obj!=null){
                    sb.append("'"+obj+"',");
                }
                if(ii==_Data.size()-1&&obj==null){
                    sb.append(obj);
                }else if(ii<_Data.size()-1&&obj==null){
                    sb.append(obj+",");
                }
//                Log.i("Lybin",obj+"");
                if(ii==0&&tableName.equals("PUB_BAS_EnterpriseInfo")){
                    SqliteUtil.getInstance().deleteCompanyBySql("delete from "+tableName+" where id='"+value+"'");
                }
            }
            Log.i("Lybin",sb.toString());
            final String valueSet = sb.toString();
            if(tableName.equals("PUB_BAS_EnterpriseInfo")){
                SqliteUtil.getInstance().execute("INSERT INTO "+tableName+" VALUES ("+valueSet+")");
            }
        }

        handler.sendEmptyMessage(1);
    }
}
