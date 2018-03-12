package com.mapuni.shangluo.manager;

import android.os.Handler;
import android.os.Message;

import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;
import com.zhy.http.okhttp.callback.StringCallback;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;

/**
 * Created by Administrator on 2017/8/14.
 */

public class NetManager {
    /**
     *登录
     * */
    public static void requestLogin(String userName, String passWord,String log, String lat, Callback call) {
        String url = PathManager.LoginUrl;
        Map<String, String> params = new HashMap<String, String>();
        params.put("loginName", userName);
        params.put("password", passWord);
        params.put("clientType", "1");
        params.put("longitude", log);
        params.put("latitude", lat);
        params.put("tag","0");//普通版
        NetManager.post(url, params, call);
    }
    /**
     *登录退出
     * */
    public static void loginOut(String sessionId,  Callback call) {
        String url = PathManager.LoginOutUrl;
        Map<String, String> params = new HashMap<String, String>();
        params.put("sessionId", sessionId);
        NetManager.post(url, params, call);
    }
    /**
     *检测session是否有效
     * */
    public static void checkSession(String sessionId,  Callback call) {
        String url = PathManager.checkSession;
        Map<String, String> params = new HashMap<String, String>();
        params.put("sessionId", sessionId);
        NetManager.post(url, params, call);
    }
    /**
     *获取通知详情
     * */
    public static void getNoticeDetail(String sessionId, String id, Callback call) {
        String url = PathManager.getNoticeDetail;
        Map<String, String> params = new HashMap<String, String>();
        params.put("sessionId", sessionId);
        params.put("id", id);
        NetManager.post(url, params, call);
    }
    /**
     *签到
     * */
    public static void regester(String sessionId, String longitude,String latitude,String address, Callback call) {
        String url = PathManager.registerUrl;
        Map<String, String> params = new HashMap<String, String>();
        params.put("sessionId", sessionId);
        params.put("longitude", longitude);
        params.put("latitude", latitude);
        params.put("address", address);
        NetManager.post(url, params, call);
    }
    /**
     *上传轨迹
     * */
    public static void uploadLatLog(String sessionId, String longitude,String latitude, Callback call) {
        String url = PathManager.uploadLatLog;
        Map<String, String> params = new HashMap<String, String>();
        params.put("sessionId", sessionId);
        params.put("longitude", longitude);
        params.put("latitude", latitude);
        NetManager.post(url, params, call);
    }
    /**
     *修改密码
     * */
    public static void modifyPassword(String sessionId, String oldPassword,String newPassword, Callback call) {
        String url = PathManager.modifyPassword;
        Map<String, String> params = new HashMap<String, String>();
        params.put("sessionId", sessionId);
        params.put("oldPassword", oldPassword);
        params.put("newPassword", newPassword);
        NetManager.post(url, params, call);
    }

    /**
     *获取联系人信息
     * */
    public static void queryContact(String sessionId, String id, Callback call) {
        String url = PathManager.queryContact;
        Map<String, String> params = new HashMap<String, String>();
        params.put("sessionId", sessionId);
        params.put("id", id);
        params.put("isDelete", "0");
        NetManager.post(url, params, call);
    }
    /**
     *获取联系人信息
     * */
    public static void queryContactDetail(String sessionId, String id, Callback call) {
        String url = PathManager.queryContactDetail;
        Map<String, String> params = new HashMap<String, String>();
        params.put("sessionId", sessionId);
        params.put("id", id);
        NetManager.post(url, params, call);
    }
    /**
     *  获取监管对象
     * */
    public static void requestPotrolObject(String sessionId, Callback call) {
        String url = PathManager.GetPotrolObjects;
        Map<String, String> params = new HashMap<String, String>();
        params.put("sessionId", sessionId);
        NetManager.post(url, params, call);
    }

    /**
     *  获取附件url
     * */
    public static void getAttachUrl(String sessionId,String handleAttachmentUuid, Callback call) {
        String url = PathManager.getAttachUrl;
        Map<String, String> params = new HashMap<String, String>();
        params.put("sessionId", sessionId);
        params.put("handleAttachmentUuid", handleAttachmentUuid);
        NetManager.post(url, params, call);
    }
    /**
     *  获取待办任务
     * */
    public static void requestDbTask(String sessionId,String rows,String page, Callback call) {
        String url = PathManager.getDbTask;
        Map<String, String> params = new HashMap<String, String>();
        params.put("sessionId", sessionId);
        params.put("rows", rows);
        params.put("page", page);
        NetManager.post(url, params, call);
    }
    /**
     *  @author Tianfy
     *  @time 2017/8/31  10:49
     *  @describe 获取已办任务列表
     */
    public static void requestRecordTask(String sessionId,String rows,String page, Callback call) {
        String url = PathManager.getRecordTask;
        Map<String, String> params = new HashMap<String, String>();
        params.put("sessionId", sessionId);
        params.put("rows", rows);
        params.put("page", page);
        NetManager.post(url, params, call);
    }
    
    /**
     *  获取待办任务详情（0 上报） （1 下发）(2 延时申请)
     * */
    public static void requestDbTaskDetail(String sessionId,String handlingRecordUuid,String taskType, Callback call) {
        String url = PathManager.getDbTaskDetail;
        Map<String, String> params = new HashMap<String, String>();
        params.put("sessionId", sessionId);
        params.put("handlingRecordUuid", handlingRecordUuid);
        params.put("taskType", taskType);
        NetManager.post(url, params, call);
    }

    /**
     *  @author Tianfy
     *  @time 2017/8/31  16:19
     *  @describe 获取已办任务详情信息
     */
    public static void requestTaskCompleteDetail(String sessionId,String handlingRecordUuid,Callback call) {
        String url = PathManager.getTaskCompleteDetail;
        Map<String, String> params = new HashMap<String, String>();
        params.put("sessionId", sessionId);
        params.put("handledRecordUuid", handlingRecordUuid);
        NetManager.post(url, params, call);
    }

    /**
     *  获取任务处理流程
     * */
    public static void requestHandleProcess(String sessionId,String handlingRecordUuid, Callback call) {
        String url = PathManager.getHandleProgress;
        Map<String, String> params = new HashMap<String, String>();
        params.put("sessionId", sessionId);
        params.put("handlingRecordUuid", handlingRecordUuid);
        NetManager.post(url, params, call);
    }

    /**
     *  @author Tianfy
     *  @time 2017/8/31  16:51
     *  @describe 获取已办任务处理流程信息
     */
    public static void requestHandleProcessComplete(String sessionId,String handlingRecordUuid, Callback call) {
        String url = PathManager.getHandledRecordDetailsList;
        Map<String, String> params = new HashMap<String, String>();
        params.put("sessionId", sessionId);
        params.put("handledRecordUuid", handlingRecordUuid);
        NetManager.post(url, params, call);
    }

    /**
     * 处理任务
     * */
    public static void handleTask(String sessionId,String handlingRecordUuid,String operationType,String opinion, String subGridsUuids,
                                  String transferGridsUuids,String startTime,String endTime,Map<String, File> files,String distributionUserUuid,Callback call) {
        if("10".equals(operationType)){//延时申请

            String url= PathManager.delayApplyUrl;
            Map<String, String> params = new HashMap<String, String>();
            params.put("sessionId", sessionId);
            params.put("opinion", opinion);
            params.put("handlingRecordUuid", handlingRecordUuid);
            params.put("startTime", startTime);
            params.put("endTime", endTime);
            NetManager.post(url, params,call);

        }else {
            String url = PathManager.handleTask;
            Map<String, String> params = new HashMap<String, String>();
            params.put("sessionId", sessionId);
            params.put("handlingRecordUuid", handlingRecordUuid);
            params.put("operationType", operationType);
            params.put("opinion", opinion);
            params.put("subGridsUuids", subGridsUuids);
            params.put("transferGridsUuids", transferGridsUuids);
            params.put("distributionUserUuid", distributionUserUuid);
            params.put("startTime", startTime);
            params.put("endTime", endTime);
            NetManager.uploadFiles(url, params,files, call);

        }
    }
     /**
     *  获取问题类别
     * */
    public static void requestProblemTypes(String sessionId, Callback call) {
        String url = PathManager.GetProblemTypes;
        Map<String, String> params = new HashMap<String, String>();
        params.put("sessionId", sessionId);
        NetManager.post(url, params, call);
    }
  /**
     *  获取转办部门或者下发部门  转办时平行单位/2 下发时地区网格/1
     * */
    public static void getAimGrids(String sessionId, String gridType,Callback call) {
        String url = PathManager.getAimGrids;
        Map<String, String> params = new HashMap<String, String>();
        params.put("sessionId", sessionId);
        params.put("gridType", gridType);
        NetManager.post(url, params, call);
    }

    /**
     *  巡查上报
     * */
    public static void uploadPotrol(String sessionId, String supervisionObjectUuid, String problemUuid, String longitude, String latitude,
                                    String address, String description, Map<String, File> files, Callback call) {
        String url = PathManager.UploadPotrol;
        Map<String, String> params = new HashMap<String, String>();
        params.put("sessionId", sessionId);
        params.put("supervisionObjectUuid", supervisionObjectUuid);
        params.put("problemUuid", problemUuid);
        params.put("longitude", longitude);
        params.put("latitude", latitude);
        params.put("address", address);
        params.put("description", description);
        NetManager.uploadFiles(url, params,files, call);
        
    }


    /**
     *  获取待签收任务
     * */
    public static void requestNoSignTask(String sessionId,String rows,String page, Callback call) {
        String url = PathManager.getNoSignTaskDetail;
        Map<String, String> params = new HashMap<String, String>();
        params.put("sessionId", sessionId);
        params.put("rows", rows);
        params.put("page", page);
        params.put("taskExamineStatus", "2");
        NetManager.post(url, params, call);
    }

    /**
     *  获取消息列表
     * */
    public static void requestMessageList(String sessionId,String rows,String page, Callback call) {
        String url = PathManager.getMessageList;
        Map<String, String> params = new HashMap<String, String>();
        params.put("sessionId", sessionId);
        params.put("rows", rows);
        params.put("page", page);
        NetManager.post(url, params, call);
    }
    
    public static void requestAttachmentList(String sessionId,String handledRecordUuid,Callback call){
        String url= PathManager.getAttachmentList;
        Map<String, String> params = new HashMap<String, String>();
        params.put("handledRecordUuid", handledRecordUuid);
        params.put("sessionId", sessionId);
        NetManager.post(url, params, call);
    }
    

    public static void get(String url, Callback call) {
        OkHttpUtils.get().url(url).build().execute(call);
    }

    public static void post(String url, Map<String, String> params, Callback call) {
        OkHttpUtils.post().url(url).params(params).build().execute(call);
    }

    public static void uploadFiles(String url, Map<String, String> params,Map<String, File> files, Callback call) {
        OkHttpUtils.post().url(url).files("files",files).params(params).build().execute(call);
    }
    public static void downloadFiles(String url, Map<String, String> params,Map<String, File> files, Callback call) {
        OkHttpUtils.post().url(url).files("files",files).params(params).build().execute(call);
    }

    /**
     *  @author Tianfy
     *  @time 2017/8/30  17:16
     *  @describe 请求签收任务
     */
    public static void requestSignTask(String sessionId, String uuid,Callback signTaskCall) {
        String url= PathManager.getSignTask;
        Map<String, String> params = new HashMap<String, String>();
        params.put("sessionId", sessionId);
        params.put("taskDistributionUuid", uuid);
        NetManager.post(url, params, signTaskCall);
    }
    /**
     *  @author Tianfy
     *  @time 2017/9/5  10:19
     *  @describe 请求催办接口 
     *  @param 
     */
    public static void requestRemind(String remindTime, String finishTime,String remindOpinion,String handledRecordUuid
            ,String sessionId, Callback call) {
        String url = PathManager.modifyPassword;
        Map<String, String> params = new HashMap<String, String>();
        params.put("sessionId", sessionId);
        params.put("remindTime", remindTime);
        params.put("finishTime", finishTime);
        params.put("remindOpinion", remindOpinion);
        params.put("handledRecordUuid", handledRecordUuid);
        NetManager.post(url, params, call);
    }

    public static void downLoadWord(String url,Callback call) {
        NetManager.get(url,call);
    }

    /**
     * 请求分配人员接口
     * @author tianfy
     * @param sessionId
     * @param distributionUserUuid 分配人员的ID
     * @param call
     */
    public static void getDistribution(String sessionId, String distributionUserUuid,Callback call) {
        String url = PathManager.getCurrentDistribution;
        Map<String, String> params = new HashMap<String, String>();
        params.put("sessionId", sessionId);
        params.put("distributionUserUuid", distributionUserUuid);
        NetManager.post(url, params, call);
    }

    //下发审核通过不通过时获取下级网格调用
    public static void requestGridUuidsAndGridNamesByHandlingRecordUuid(String sessionId, String handlingRecordUuid,Callback call) {
        String url = PathManager.getGridUuidsAndGridNamesByHandlingRecordUuid;
        Map<String, String> params = new HashMap<String, String>();
        params.put("sessionId", sessionId);
        params.put("handlingRecordUuid", handlingRecordUuid);
        NetManager.post(url, params, call);
    }



    public static void getJsonByPost(String sessionId, String id, final Handler handler, final int arg1){
        Map<String,String> map=new HashMap<>();
        map.put("sessionId",sessionId);
        map.put("id",id);
        String url=PathManager.appGridAndSupervisionObject;
        OkHttpUtils.post().url(url).params(map).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                Message msg=Message.obtain();
                msg.arg1=5;
                handler.sendMessage(msg);
            }
            @Override
            public void onResponse(String response, int id) {
                Message msg=Message.obtain();
                msg.arg1=arg1;
                msg.obj=response;
                handler.sendMessage(msg);
            }
        });

    }

    /**
     * 请求上报详情的任务下发网格
     */
    public static void getGridsByCondition(String sessionId, String taskUuid,Callback call) {
        String url = PathManager.getGridsByCondition;
        Map<String, String> params = new HashMap<String, String>();
        params.put("sessionId", sessionId);
        params.put("taskUuid", taskUuid);
        params.put("gridType", "1");
        NetManager.post(url, params, call);
    }

    public static void requestPicAttachmentList(String taskUuid,Callback call){
        String url= PathManager.getPicAttachmentList;
        Map<String, String> params = new HashMap<String, String>();
        params.put("taskUuid", taskUuid);
        NetManager.post(url, params, call);
    }

    /**
     *附件删除接口
     * */
    public static void deleteAttachment(String sessionId, String attachmentUuid, Callback call) {
        String url = PathManager.deleteAttachment;
        Map<String, String> params = new HashMap<String, String>();
        params.put("sessionId", sessionId);
        params.put("attachmentUuid", attachmentUuid);
        NetManager.post(url, params, call);
    }

    /**
     *  获取Pic附件url
     * */
    public static void getPicAttachUrl(String sessionId,String taskAttachmentUuid, Callback call) {
        String url = PathManager.getPicAttachUrl;
        Map<String, String> params = new HashMap<String, String>();
        params.put("sessionId", sessionId);
        params.put("taskAttachmentUuid", taskAttachmentUuid);
        NetManager.post(url, params, call);
    }

    /**
     * 待签收的删除校验
     * */
    public static void taskDistributionCheck(String sessionId, String taskDistributionUuid, Callback call) {
        String url = PathManager.taskDistributionCheck;
        Map<String, String> params = new HashMap<String, String>();
        params.put("sessionId", sessionId);
        params.put("taskDistributionUuid", taskDistributionUuid);
        NetManager.post(url, params, call);
    }

    /**
     * 待办任务校验
     * */
    public static void judgeHandlingRecordIsExists(String sessionId, String handlingRecordUuid, Callback call) {
        String url = PathManager.judgeHandlingRecordIsExists;
        Map<String, String> params = new HashMap<String, String>();
        params.put("sessionId", sessionId);
        params.put("handlingRecordUuid", handlingRecordUuid);
        NetManager.post(url, params, call);
    }

    /**
     * 签到校验
     * */
    public static void isRegisted(String sessionId, Callback call) {
        String url = PathManager.isRegisted;
        Map<String, String> params = new HashMap<String, String>();
        params.put("sessionId", sessionId);
        NetManager.post(url, params, call);
    }

    /**
     * 签到记录
     * */
    public static void getQiandaoRecord(String sessionId,String date, Callback call) {
        String url = PathManager.getQiandaoRecord;
        Map<String, String> params = new HashMap<String, String>();
        params.put("sessionId", sessionId);
        params.put("date", date);
        NetManager.post(url, params, call);
    }

    /**
     * 审核不通过
     */
    public static void getKnowledgeList(String sessionId, String page, String rows, String title, String content, Callback call) {
        String url = PathManager.getKnowledgeList;
        Map<String, String> params = new HashMap<String, String>();
        params.put("sessionId", sessionId);
        params.put("page", page);
        params.put("rows", rows);
        params.put("title", title);
        params.put("content", content);
        NetManager.post(url, params, call);
    }

    /**
     * 删除知识库
     */
    public static void delKnowledge(String sessionId, String uuid, Callback call) {
        String url = PathManager.delKnowledge;
        Map<String, String> params = new HashMap<String, String>();
        params.put("sessionId", sessionId);
        params.put("uuid", uuid);
        NetManager.post(url, params, call);
    }


    /**
     * 添加知识库
     * */
    public static void addKnowledge(String sessionId, String title,String content,Map<String, File> files, Callback call) {
        String url = PathManager.addKnowledge;
        Map<String, String> params = new HashMap<String, String>();
        params.put("sessionId", sessionId);
        params.put("title", title);
        params.put("content", content);
        NetManager.uploadFiles(url, params,files, call);
    }


    /**
     * 获取知识库附件url
     */
    public static void getKnowledgeAttachUrl(String sessionId, String taskAttachmentUuid, Callback call) {
        String url = PathManager.getknowledgeDownload + "?sessionId=" + sessionId + "&attachmentUuid=" + taskAttachmentUuid;
//        Map<String, String> params = new HashMap<String, String>();
//        params.put("sessionId", sessionId);
//        params.put("attachmentUuid", taskAttachmentUuid);
//        NetManager.post(url, params, call);
        NetManager.get(url, call);
    }

    /**
     * 知识库附件删除接口
     */
    public static void deleteKnowledgeAttachment(String sessionId, String attachmentUuid, Callback call) {
        String url = PathManager.deleteKnowledgeAttachment;
        Map<String, String> params = new HashMap<String, String>();
        params.put("sessionId", sessionId);
        params.put("attachmentUuid", attachmentUuid);
        NetManager.post(url, params, call);
    }


    //请求知识库附件列表
    public static void requestKnowledgeAttachmentList(String sessionId, String knowledgeUuid, Callback call) {
        String url = PathManager.getknowledgeAttachmentlist;
        Map<String, String> params = new HashMap<String, String>();
        params.put("sessionId", sessionId);
        params.put("knowledgeUuid", knowledgeUuid);
        NetManager.post(url, params, call);
    }

    /**
     * 知识库更新
     */
    public static void updateKnowledgeTask(String sessionId, String uuid, String title, String content, Map<String, File> files, Callback call) {
        String url = PathManager.knowledgeUpdate;
        Map<String, String> params = new HashMap<String, String>();
        params.put("sessionId", sessionId);
        params.put("uuid", uuid);
        params.put("title", title);
        params.put("content", content);
//        NetManager.post(url, params, call);
        NetManager.uploadFiles(url, params, files, call);
    }
}
