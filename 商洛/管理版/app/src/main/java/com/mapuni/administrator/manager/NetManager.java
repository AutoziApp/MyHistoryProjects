package com.mapuni.administrator.manager;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

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
     * 登录
     */
    public static void requestLogin(String userName, String passWord, String log, String lat, Callback call) {
        String url = PathManager.LoginUrl;
        Map<String, String> params = new HashMap<String, String>();
        params.put("loginName", userName);
        params.put("password", passWord);
        params.put("clientType", "1");
        params.put("longitude", log);
        params.put("latitude", lat);
        params.put("tag", "1");//管理版
        NetManager.post(url, params, call);
    }

    /**
     * 登录退出
     */
    public static void loginOut(String sessionId, Callback call) {
        String url = PathManager.LoginOutUrl;
        Map<String, String> params = new HashMap<String, String>();
        params.put("sessionId", sessionId);
        NetManager.post(url, params, call);
    }

    /**
     * 检测session是否有效
     */
    public static void checkSession(String sessionId, Callback call) {
        String url = PathManager.checkSession;
        Map<String, String> params = new HashMap<String, String>();
        params.put("sessionId", sessionId);
        NetManager.post(url, params, call);
    }

    /**
     * 获取通知详情
     */
    public static void getNoticeDetail(String sessionId, String id, Callback call) {
        String url = PathManager.getNoticeDetail;
        Map<String, String> params = new HashMap<String, String>();
        params.put("sessionId", sessionId);
        params.put("id", id);
        NetManager.post(url, params, call);
    }

    /**
     * 上传轨迹
     */
    public static void uploadLatLog(String sessionId, String longitude, String latitude, Callback call) {
        String url = PathManager.uploadLatLog;
        Map<String, String> params = new HashMap<String, String>();
        params.put("sessionId", sessionId);
        params.put("longitude", longitude);
        params.put("latitude", latitude);

        Log.i("qqq","url: "+url);
        Log.i("qqq","sessionId: "+sessionId);
        Log.i("qqq","longitude: "+longitude);
        Log.i("qqq","latitude: "+latitude);
        NetManager.post(url, params, call);
    }

    /**
     * 获取附件url
     */
    public static void getAttachUrl(String sessionId, String handleAttachmentUuid, Callback call) {
        String url = PathManager.getAttachUrl;
        Map<String, String> params = new HashMap<String, String>();
        params.put("sessionId", sessionId);
        params.put("handleAttachmentUuid", handleAttachmentUuid);
        NetManager.post(url, params, call);
    }

    /**
     * 获取Pic附件url
     */
    public static void getPicAttachUrl(String sessionId, String taskAttachmentUuid, Callback call) {
        String url = PathManager.getPicAttachUrl;
        Map<String, String> params = new HashMap<String, String>();
        params.put("sessionId", sessionId);
        params.put("taskAttachmentUuid", taskAttachmentUuid);
        NetManager.post(url, params, call);
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
     * 修改密码
     */
    public static void modifyPassword(String sessionId, String oldPassword, String newPassword, Callback call) {
        String url = PathManager.modifyPassword;
        Map<String, String> params = new HashMap<String, String>();
        params.put("sessionId", sessionId);
        params.put("oldPassword", oldPassword);
        params.put("newPassword", newPassword);
        NetManager.post(url, params, call);
    }

    /**
     * 获取联系人信息
     */
    public static void queryContact(String sessionId, String id, Callback call) {
        String url = PathManager.queryContact;
        Map<String, String> params = new HashMap<String, String>();
        params.put("sessionId", sessionId);
        params.put("id", id);
        params.put("isDelete", "0");
        NetManager.post(url, params, call);
    }

    /**
     * 变化趋势
     */
    public static void variationTrend(String sessionId, String startTime, String endTime, String patrolProblemDictUuids, String gridUuid, Callback call) {
        String url = PathManager.variationTrend;
        Map<String, String> params = new HashMap<String, String>();
        params.put("sessionId", sessionId);
        params.put("startTime", startTime);
        params.put("endTime", endTime);
        params.put("patrolProblemDictUuids", patrolProblemDictUuids);
        params.put("gridUuid", gridUuid);
        NetManager.post(url, params, call);
    }

    /**
     * 获取网格员上报数据
     */
    public static void getReportAndHandeledRecordCountByCondition(String sessionId, String startTime, String endTime, String gridUuid, int page, Callback call) {
        String url = PathManager.getReportAndHandeledRecordCountByCondition;
        Map<String, String> params = new HashMap<String, String>();
        params.put("sessionId", sessionId);
        params.put("startTime", startTime);
        params.put("endTime", endTime);
        params.put("gridUuid", gridUuid);
        params.put("rows", "20");
        params.put("page", page + "");
        NetManager.post(url, params, call);
    }

    /**
     * 获取网格员签到率
     */
    public static void getQianDaoStatics(String sessionId, String startTime, String endTime, String gridUuid, int page, Callback call) {
        String url = PathManager.getQianDaoStatic;
        Map<String, String> params = new HashMap<String, String>();
        params.put("sessionId", sessionId);
        params.put("startTime", startTime);
        params.put("endTime", endTime);
        params.put("gridUuid", gridUuid);
        params.put("rows", "20");
        params.put("page", page + "");
        NetManager.post(url, params, call);
    }

    /**
     * 获取网格员办结率
     */
    public static void getBanJieStatics(String sessionId, String startTime, String endTime, String gridUuid, int page, Callback call) {
        String url = PathManager.getGridUsersHandeledRecordCountByCondition;
        Map<String, String> params = new HashMap<String, String>();
        params.put("sessionId", sessionId);
        params.put("startTime", startTime);
        params.put("endTime", endTime);
        params.put("gridUuid", gridUuid);
        params.put("rows", "20");
        params.put("page", page + "");
        NetManager.post(url, params, call);
    }

    /**
     * 获取联系人信息
     */
    public static void queryContactDetail(String sessionId, String id, Callback call) {
        String url = PathManager.queryContactDetail;
        Map<String, String> params = new HashMap<String, String>();
        params.put("sessionId", sessionId);
        params.put("id", id);
        NetManager.post(url, params, call);
    }

    /**
     * 获取监管对象
     */
    public static void requestPotrolObject(String sessionId, Callback call) {
        String url = PathManager.GetPotrolObjects;
        Map<String, String> params = new HashMap<String, String>();
        params.put("sessionId", sessionId);
        NetManager.post(url, params, call);
    }

    /**
     * 获取待办任务
     */
    public static void requestDbTask(String sessionId, String rows, String page, Callback call) {
        String url = PathManager.getDbTask;
        Map<String, String> params = new HashMap<String, String>();
        params.put("sessionId", sessionId);
        params.put("rows", rows);
        params.put("page", page);
        NetManager.post(url, params, call);
    }

    /**
     * @author Tianfy
     * @time 2017/8/31  10:49
     * @describe 获取已办任务列表
     */
    public static void requestRecordTask(String sessionId, String rows, String page, Callback call) {
        String url = PathManager.getRecordTask;
        Map<String, String> params = new HashMap<String, String>();
        params.put("sessionId", sessionId);
        params.put("rows", rows);
        params.put("page", page);
        NetManager.post(url, params, call);
    }

    /**
     * 获取待办任务详情（0 上报） （1 下发）(2 延时申请)
     */
    public static void requestDbTaskDetail(String sessionId, String handlingRecordUuid, String taskType, Callback call) {
        String url = PathManager.getDbTaskDetail;
        Map<String, String> params = new HashMap<String, String>();
        params.put("sessionId", sessionId);
        params.put("handlingRecordUuid", handlingRecordUuid);
        params.put("taskType", taskType);
        NetManager.post(url, params, call);
    }

    /**
     * @author Tianfy
     * @time 2017/8/31  16:19
     * @describe 获取已办任务详情信息
     */
    public static void requestTaskCompleteDetail(String sessionId, String handlingRecordUuid, Callback call) {
        String url = PathManager.getTaskCompleteDetail;
        Map<String, String> params = new HashMap<String, String>();
        params.put("sessionId", sessionId);
        params.put("handledRecordUuid", handlingRecordUuid);
        NetManager.post(url, params, call);
    }

    /**
     * @author Tianfy
     * @describe 获取分配任务详情信息
     */
    public static void requestDistributionTask(String taskUuid, Callback call) {
        String url = PathManager.getTaskByUuid;
        Map<String, String> params = new HashMap<String, String>();
        params.put("taskUuid", taskUuid);
        NetManager.post(url, params, call);
    }

    /**
     * 获取任务处理流程
     */
    public static void requestHandleProcess(String sessionId, String handlingRecordUuid, Callback call) {
        String url = PathManager.getHandleProgress;
        Map<String, String> params = new HashMap<String, String>();
        params.put("sessionId", sessionId);
        params.put("handlingRecordUuid", handlingRecordUuid);
        NetManager.post(url, params, call);
    }

    /**
     * @author Tianfy
     * @time 2017/8/31  16:51
     * @describe 获取已办任务处理流程信息
     */
    public static void requestHandleProcessComplete(String sessionId, String handlingRecordUuid, Callback call) {
        String url = PathManager.getHandledRecordDetailsList;
        Map<String, String> params = new HashMap<String, String>();
        params.put("sessionId", sessionId);
        params.put("handledRecordUuid", handlingRecordUuid);
        NetManager.post(url, params, call);
    }

    /**
     * 处理任务
     */
    public static void handleTask(String sessionId, String handlingRecordUuid, String operationType, String opinion, String subGridsUuids,
                                  String transferGridsUuids, String startTime, String endTime, Map<String, File> files, String distributionUserUuid, Callback call) {
        if ("10".equals(operationType)) {//延时申请

            String url = PathManager.delayApplyUrl;
            Map<String, String> params = new HashMap<String, String>();
            params.put("sessionId", sessionId);
            params.put("opinion", opinion);
            params.put("handlingRecordUuid", handlingRecordUuid);
            params.put("startTime", startTime);
            params.put("endTime", endTime);
            NetManager.post(url, params, call);

        } else {
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
            NetManager.uploadFiles(url, params, files, call);

        }
    }

    /**
     * 获取问题类别
     */
    public static void requestProblemTypes(String sessionId, Callback call) {
        String url = PathManager.GetProblemTypes;
        Map<String, String> params = new HashMap<String, String>();
        params.put("sessionId", sessionId);
        NetManager.post(url, params, call);
    }

    /**
     * 获取转办部门或者下发部门  转办时平行单位/2 下发时地区网格/1
     */
    public static void getAimGrids(String sessionId, String gridType, Callback call) {
        String url = PathManager.getAimGrids;
        Map<String, String> params = new HashMap<String, String>();
        params.put("sessionId", sessionId);
        params.put("gridType", gridType);
        NetManager.post(url, params, call);
    }

    /**
     * 请求分配人员接口
     *
     * @param sessionId
     * @param distributionUserUuid 分配人员的ID
     * @param call
     * @author tianfy
     */
    public static void getDistribution(String sessionId, String distributionUserUuid, Callback call) {
        String url = PathManager.getCurrentDistribution;
        Map<String, String> params = new HashMap<String, String>();
        params.put("sessionId", sessionId);
        params.put("distributionUserUuid", distributionUserUuid);
        NetManager.post(url, params, call);
    }

    /**
     * 巡查上报
     */
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
        NetManager.uploadFiles(url, params, files, call);

    }


    /**
     * 获取待签收任务
     */
    public static void requestNoSignTask(String sessionId, String rows, String page, Callback call) {
        String url = PathManager.getNoSignTaskDetail;
        Map<String, String> params = new HashMap<String, String>();
        params.put("sessionId", sessionId);
        params.put("rows", rows);
        params.put("page", page);
        params.put("taskExamineStatus", "2");
        NetManager.post(url, params, call);
    }

    /**
     * 获取消息列表
     */
    public static void requestMessageList(String sessionId, String rows, String page, Callback call) {
        String url = PathManager.getMessageList;
        Map<String, String> params = new HashMap<String, String>();
        params.put("sessionId", sessionId);
        params.put("rows", rows);
        params.put("page", page);
        NetManager.post(url, params, call);
    }

    public static void requestAttachmentList(String handledRecordUuid, Callback call) {
        String url = PathManager.getAttachmentList;
        Map<String, String> params = new HashMap<String, String>();
        params.put("handledRecordUuid", handledRecordUuid);
        NetManager.post(url, params, call);
    }

    public static void requestPicAttachmentList(String taskUuid, Callback call) {
        String url = PathManager.getPicAttachmentList;
        Map<String, String> params = new HashMap<String, String>();
        params.put("taskUuid", taskUuid);
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

    public static void get(String url, Callback call) {
        OkHttpUtils.get().url(url).build().execute(call);
    }

    public static void post(String url, Map<String, String> params, Callback call) {
        OkHttpUtils.post().url(url).params(params).build().execute(call);
    }

    public static void uploadFiles(String url, Map<String, String> params, Map<String, File> files, Callback call) {
        OkHttpUtils.post().url(url).files("files", files).params(params).build().execute(call);
    }

    /**
     * @author Tianfy
     * @time 2017/8/30  17:16
     * @describe 请求签收任务
     */
    public static void requestSignTask(String sessionId, String uuid, Callback signTaskCall) {
        String url = PathManager.getSignTask;
        Map<String, String> params = new HashMap<String, String>();
        params.put("sessionId", sessionId);
        params.put("taskDistributionUuid", uuid);
        NetManager.post(url, params, signTaskCall);
    }

    /**
     * @param
     * @author Tianfy
     * @time 2017/9/5  10:19
     * @describe 请求催办接口
     */
    public static void requestRemind(String remindTime, String finishTime, String remindOpinion, String handledRecordUuid
            , String sessionId, Callback call) {
        String url = PathManager.modifyPassword;
        Map<String, String> params = new HashMap<String, String>();
        params.put("sessionId", sessionId);
        params.put("remindTime", remindTime);
        params.put("finishTime", finishTime);
        params.put("remindOpinion", remindOpinion);
        params.put("handledRecordUuid", handledRecordUuid);
        NetManager.post(url, params, call);
    }

    public static void downLoadWord(String url, Callback call) {
        NetManager.get(url, call);
    }

    /**
     * @param id 父节点id
     * @author Tianfy
     * @time 2017/9/14  16:53
     * @describe 请求区域类型
     */
    public static void requestCityData(String id, Callback call) {
        String url = PathManager.getSubgridByCondition;
        Map<String, String> params = new HashMap<String, String>();
        params.put("id", id);
        params.put("isDelete", 0 + "");
        NetManager.post(url, params, call);
    }

    /**
     * @param startTime            开始时间
     * @param endTime              结束时间
     * @param gridUuid             选择网格uuid
     * @param patroProblemDictUuid 问题类别uuid
     * @author Tianfy
     * @time 2017/9/18  9:05
     * @describe 任务统计
     */
    public static void requestAreaData(String startTime, String endTime, String gridUuid, String patroProblemDictUuid, int searchType, Callback call) {
        String url = PathManager.getTaskStatistics;
        Map<String, String> params = new HashMap<String, String>();
        params.put("startTime", startTime);
        params.put("endTime", endTime);
        params.put("gridUuid", gridUuid);
        params.put("patrolProblemDictUuid", patroProblemDictUuid);
        params.put("searchType", searchType + "");
        NetManager.post(url, params, call);
    }

    public static void requestTaskTypeData(Callback call) {
        String url = PathManager.getPatrolProblemDict;
        NetManager.get(url, call);
    }

    /**
     * @param startTime            开始时间
     * @param endTime              结束时间
     * @param gridUuid             选择网格uuid
     * @param patroProblemDictUuid 问题类别uuid
     * @author Tianfy
     * @time 2017/9/21  8:48
     * @describe 任务统计
     */
    public static void requestGridNumberData(String startTime, String endTime, String gridUuid, String patroProblemDictUuid, Callback call) {
        String url = PathManager.getGridsRankingStatisticsCount;
        Map<String, String> params = new HashMap<String, String>();
        params.put("startTime", startTime);
        params.put("endTime", endTime);
        params.put("gridUuid", gridUuid);
        params.put("patrolProblemDictUuid", patroProblemDictUuid);
        NetManager.post(url, params, call);
    }

    /**
     * 获取任务类型
     */
    public static void requestTaskType(String sessionId, Callback call) {
        String url = PathManager.getTaskType;
        Map<String, String> params = new HashMap<String, String>();
        params.put("sessionId", sessionId);
        NetManager.post(url, params, call);
    }

    /**
     * 创建下发任务
     */
    public static void createTask(String sessionId, String name, String taskType, String startTime, String endTime, String description, String status, Map<String, File> files, Callback call) {
        String url = PathManager.createOrEditTask;
        Map<String, String> params = new HashMap<String, String>();
        params.put("sessionId", sessionId);
        params.put("name", name);
        params.put("taskType", taskType);
        params.put("startTime", startTime);
        params.put("endTime", endTime);
        params.put("description", description);
        params.put("status", status);
//        NetManager.post(url, params, call);
        NetManager.uploadFiles(url, params, files, call);
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

    /**
     * 更新下发任务
     */
    public static void editTask(String sessionId, String uuid, String name, String taskType, String startTime, String endTime, String description, Map<String, File> files, Callback call) {
        String url = PathManager.createOrEditTask;
        Map<String, String> params = new HashMap<String, String>();
        params.put("sessionId", sessionId);
        params.put("uuid", uuid);
        params.put("name", name);
        params.put("taskType", taskType);
        params.put("startTime", startTime);
        params.put("endTime", endTime);
        params.put("description", description);
//        NetManager.post(url, params, call);
        NetManager.uploadFiles(url, params, files, call);
    }

    /**
     * 更新下发任务
     */
    public static void examineTask(String sessionId, String uuid, String name, String taskType, String startTime, String endTime, String description, Callback call) {
        String url = PathManager.createOrEditTask;
        Map<String, String> params = new HashMap<String, String>();
        params.put("sessionId", sessionId);
        params.put("uuid", uuid);
        params.put("name", name);
        params.put("taskType", taskType);
        params.put("startTime", startTime);
        params.put("endTime", endTime);
        params.put("description", description);
        params.put("status", "2");
        NetManager.post(url, params, call);
    }

    /**
     * 获取已创建的下发任务列表
     */
    public static void getCreatedTaskList(String sessionId, String rows, String page, Callback call) {
        String url = PathManager.getCreatedTaskList;
        Map<String, String> params = new HashMap<String, String>();
        params.put("sessionId", sessionId);
        params.put("rows", rows);
        params.put("page", page);
        NetManager.post(url, params, call);
    }

    /**
     * 获取待审核的下发任务列表
     */
    public static void getTaskExamineList(String sessionId, String rows, String page, Callback call) {
        String url = PathManager.getTaskExamineList;
        Map<String, String> params = new HashMap<String, String>();
        params.put("sessionId", sessionId);
        params.put("rows", rows);
        params.put("page", page);
        NetManager.post(url, params, call);
    }

    /**
     * 获取下级网格列表
     *
     * @param taskUuid
     * @param call
     * @author tianfy
     */
    public static void requestGridsByCurrentUser(String taskUuid, Callback call) {

        String url = PathManager.getGridsByCurrentUser;
        Map<String, String> params = new HashMap<String, String>();
        params.put("taskUuid", taskUuid);
        params.put("gridType", "1");
        NetManager.post(url, params, call);
    }

    /**
     * 分配页：分配接口
     *
     * @param taskUuid
     * @param gridUuids
     * @param call
     * @author tianfy
     */
    public static void requestDistributionTaskToGrids(String taskUuid, String gridUuids, Callback call) {
        String url = PathManager.distributionTaskToGrids;
        Map<String, String> params = new HashMap<String, String>();
        params.put("taskUuid", taskUuid);
        params.put("gridUuids", gridUuids);
        NetManager.post(url, params, call);
    }

    /**
     * 删除创建的任务
     */
    public static void delTask(String sessionId, String taskUuid, Callback call) {
        String url = PathManager.delTask;
        Map<String, String> params = new HashMap<String, String>();
        params.put("sessionId", sessionId);
        params.put("taskUuid", taskUuid);
        NetManager.post(url, params, call);
    }

    /**
     * 判断是否为审核员
     */
    public static void isAuditor(String sessionId, Callback call) {
        String url = PathManager.isAuditor;
        Map<String, String> params = new HashMap<String, String>();
        params.put("sessionId", sessionId);
        NetManager.post(url, params, call);
    }

    /**
     * 分配页：删除接口
     *
     * @param taskDistributionUuid
     * @param call
     * @author tianfy
     */
    public static void requestDeleteDistribution(String taskDistributionUuid, Callback call) {
        String url = PathManager.delDistributionTask;
        Map<String, String> params = new HashMap<String, String>();
        params.put("taskDistributionUuid", taskDistributionUuid);
        NetManager.post(url, params, call);
    }

    /**
     * 分配页：请求列表数据接口
     *
     * @param taskUuid
     * @param call
     * @author tianfy
     */
    public static void requestDistributionListData(String sessionId, String taskUuid, Callback call) {
        String url = PathManager.getTaskDistributionListByCondition;
        Map<String, String> params = new HashMap<String, String>();
        params.put("sessionId", sessionId);
        params.put("taskUuid", taskUuid);
        params.put("taskExamineStatus", "1");
        NetManager.post(url, params, call);
    }

    public static void requestDistributionDetail(String taskUuid, String taskType, Callback call) {
        String url = PathManager.getHandledRecordByTaskUuid;
        Map<String, String> params = new HashMap<String, String>();
        params.put("taskUuid", taskUuid);
        params.put("taskType", taskType);
        NetManager.post(url, params, call);
    }

    //下发审核通过不通过时获取下级网格调用
    public static void requestGridUuidsAndGridNamesByHandlingRecordUuid(String sessionId, String handlingRecordUuid, Callback call) {
        String url = PathManager.getGridUuidsAndGridNamesByHandlingRecordUuid;
        Map<String, String> params = new HashMap<String, String>();
        params.put("sessionId", sessionId);
        params.put("handlingRecordUuid", handlingRecordUuid);
        NetManager.post(url, params, call);
    }


    public static void getJsonByPost(String sessionId, String id, final Handler handler, final int arg1) {
        Map<String, String> map = new HashMap<>();
        map.put("sessionId", sessionId);
        map.put("id", id);
        String url = PathManager.appGridAndSupervisionObject;
        OkHttpUtils.post().url(url).params(map).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                Message msg = Message.obtain();
                msg.arg1 = 5;
                handler.sendMessage(msg);
            }

            @Override
            public void onResponse(String response, int id) {
                Message msg = Message.obtain();
                msg.arg1 = arg1;
                msg.obj = response;
                handler.sendMessage(msg);
            }
        });

    }

    /**
     * 请求上报详情的任务下发网格
     */
    public static void getGridsByCondition(String sessionId, String taskUuid, Callback call) {
        String url = PathManager.getGridsByCondition;
        Map<String, String> params = new HashMap<String, String>();
        params.put("sessionId", sessionId);
        params.put("taskUuid", taskUuid);
        params.put("gridType", "1");
        NetManager.post(url, params, call);
    }

    /**
     * 附件删除接口
     */
    public static void deleteAttachment(String sessionId, String attachmentUuid, Callback call) {
        String url = PathManager.deleteAttachment;
        Map<String, String> params = new HashMap<String, String>();
        params.put("sessionId", sessionId);
        params.put("attachmentUuid", attachmentUuid);
        NetManager.post(url, params, call);
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

    /**
     * 待签收的删除校验
     */
    public static void taskDistributionCheck(String sessionId, String taskDistributionUuid, Callback call) {
        String url = PathManager.taskDistributionCheck;
        Map<String, String> params = new HashMap<String, String>();
        params.put("sessionId", sessionId);
        params.put("taskDistributionUuid", taskDistributionUuid);
        NetManager.post(url, params, call);
    }

    /**
     * 待审核校验
     */
    public static void taskExamineCheck(String sessionId, String taskUuid, Callback call) {
        String url = PathManager.taskExamineCheck;
        Map<String, String> params = new HashMap<String, String>();
        params.put("sessionId", sessionId);
        params.put("taskUuid", taskUuid);
        NetManager.post(url, params, call);
    }

    /**
     * 待办任务校验
     */
    public static void judgeHandlingRecordIsExists(String sessionId, String handlingRecordUuid, Callback call) {
        String url = PathManager.judgeHandlingRecordIsExists;
        Map<String, String> params = new HashMap<String, String>();
        params.put("sessionId", sessionId);
        params.put("handlingRecordUuid", handlingRecordUuid);
        NetManager.post(url, params, call);
    }

    /**
     * 审核不通过
     */
    public static void examineNotPass(String sessionId, String uuid, Callback call) {
        String url = PathManager.createOrEditTask;
        Map<String, String> params = new HashMap<String, String>();
        params.put("sessionId", sessionId);
        params.put("uuid", uuid);
        params.put("status", "0");
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

}
