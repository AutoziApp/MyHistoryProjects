package com.mapuni.administrator.manager;

import com.mapuni.administrator.app.MyApplication;
import com.mapuni.administrator.utils.SPUtils;

/**
 * Created by 15225 on 2017/8/19.
 */

public class PathManager {
//    public static String BaseUrl="http://113.200.93.39:8184";
//    public static String BaseUrl="http://113.200.60.90:8184";
    public static String BaseUrl= (String) SPUtils.getSp(MyApplication.getContextObject(), "newIP", "http://113.200.60.90:8184");
//    public static String BaseUrl="http://192.168.130.180:8080";
//    public static String BaseUrl="http://192.168.120.10:8080";
//    public static String BaseUrl="http://192.168.120.12:8007";
//    public static String BaseUrl="http://192.168.120.14:8007";
//    public static String BaseUrl="http://123.160.246.203:8184";
//    public static String BaseUrl="http://192.168.100.60:8184";
//    public static String BaseUrl = "http://192.168.120.98:8080";
    public static String PathUrl = BaseUrl + "/shangluo_gridms/";
    public static String LoginUrl = PathUrl + "login/login.do";//登录接口
    public static String LoginOutUrl = PathUrl + "login/logout.do";//退出接口
    public static String GetPotrolObjects = PathUrl + "app/getSupervisionObjectList.do";//获取监管对象
    public static String GetProblemTypes = PathUrl + "app/getPatrolProblemDictList.do";//获取问题类别
    public static String UploadPotrol = PathUrl + "app/patrol.do";//巡查上报
    public static String getDbTask = PathUrl + "app/getHandlingRecordList.do";//获取待办任务
    public static String getDbTaskDetail = PathUrl + "app/handlingRecord.do";//获取待办任务详情
    public static String getHandleProgress = PathUrl + "app/getHandledRecordDetailsByCondition.do";//获取任务处理流程
    public static String handleTask = PathUrl + "app/appUpdateHandlingRecord.do";//处理任务
    public static String getNoSignTaskDetail = PathUrl + "app/getAppCurrentUserTaskDistributionListByCondition.do";//获取待签收任务详情
    public static String getMessageList = PathUrl + "app/getNoticeList.do";//获取消息列表
    public static String getAimGrids = PathUrl + "app/getAppGridsByCurrentUser.do";//获取转办部门或者下发部门
    public static String getCurrentDistribution= PathUrl + "app/getUsersWrapperListByCurrentGridUuid.do";//获取当前网格人员
    public static String getSignTask = PathUrl + "app/appCheckinTask.do";//签收任务
    public static String getAttachmentList = PathUrl + "app/getHandleAttachmentList.do";//附件列表
    public static String getPicAttachmentList = PathUrl + "app/getTaskAttachmentWrapperListByCondition.do";//下发任务附件列表接口
    public static String getknowledgeAttachmentlist=PathUrl+"app/getKnowledgeAttachmentWrapperListByCondition.do";//知识库附件列表
    public static String modifyPassword = PathUrl + "app/appUpdatePassword.do";//修改密码
    public static String getRecordTask = PathUrl + "app/getHandledRecordList.do";//获取待办任务
    public static String queryContact = PathUrl + "app/appQueryContact.do";//获取联系人信息
    public static String queryContactDetail = PathUrl + "app/appQueryContactDetail.do";//获取联系人信息详情
    public static String getTaskCompleteDetail = PathUrl + "app/getHandledRecord.do";//获取已办任务详情信息
    public static String getHandledRecordDetailsList = PathUrl + "app/getHandledRecordDetailsList.do";//获取已办任务处理流程
    public static String uploadLatLog = PathUrl + "app/appPatrolTrack.do";//上传轨迹坐标点
    public static String getAttachUrl = PathUrl + "handleAttachment/getHandleAttachmentRemoteUrl.do";//获取附件路径
    public static String getPicAttachUrl = PathUrl + "app/getTaskAttachmentRemoteUrl.do";//获取下发任务附件预览路径
    public static String getknowledgeDownload = PathUrl + "app/getKnowledgeAttachmentRemoteUrl.do";//获取知识库任务附件预览路径
    public static String delayApplyUrl = PathUrl + "app/createDelayApply.do";//延时申请专属
    public static String getSubgridByCondition = PathUrl + "app/getSubgridByCondition.do";//获取区域类型
    public static String getTaskStatistics = PathUrl + "app/taskStatistics.do";//任务统计接口
    public static String getPatrolProblemDict = PathUrl + "app/getPatrolProblemDict.do";//巡查问题类别接口
    public static String getNoticeDetail = PathUrl + "notice/get.do";//获取通知详情
    public static String checkSession = PathUrl + "app/checkSession.do";//检测session是否有效
    public static String getGridsRankingStatisticsCount = PathUrl + "app/getGridsRankingStatisticsCount.do";//网格排名
    public static String variationTrend = PathUrl + "app/variationTrendStatisticsToApp.do";//变化趋势
    public static String getReportAndHandeledRecordCountByCondition = PathUrl + "app/getGridUsersReportCountByCondition.do";//网格员上报查询
    public static String getGridUsersHandeledRecordCountByCondition = PathUrl + "app/getGridUsersHandeledRecordCountByCondition.do";//网格员办结查询
    public static String getQianDaoStatic = PathUrl + "app/getCheckInStatistics.do";//获取网格员签到数据
    public static String getTaskType = PathUrl + "app/getAllTaskType.do";//获取任务类型
    public static String createOrEditTask = PathUrl + "app/save.do";//创建或编辑下发任务
    public static String knowledgeUpdate = PathUrl + "app/knowledgeUpdate.do";//知识库更新
    public static String getCreatedTaskList = PathUrl + "app/getTaskWrapperListByCondition.do";//获取已创建的任务列表
    public static String getTaskByUuid = PathUrl + "app/getTaskByUuid.do";//分配页：获取任务详情接口
    public static String getGridsByCurrentUser = PathUrl + "app/getGridsByCurrentUser.do";//获取下级网格接口
    public static String distributionTaskToGrids = PathUrl + "app/distributionTaskToGrids.do";//分配页：分配接口
    public static String delTask = PathUrl + "app/deleteTask.do";//删除任务
    public static String delDistributionTask = PathUrl + "app/deleteTaskDistribution.do";//分配页：删除接口
    public static String getTaskDistributionListByCondition = PathUrl + "app/getTaskDistributionListByCondition.do";//分配页：列表数据接口
    public static String getHandledRecordByTaskUuid = PathUrl + "app/getHandledRecordByTaskUuid.do";//分配页：查看详情接口
    public static String isAuditor = PathUrl + "app/getRoleByUser.do";//判断是否为审核员
    public static String getTaskExamineList = PathUrl + "task/getTaskExamineListByCondition.do";//获取待分配列表页
    public static String getGridUuidsAndGridNamesByHandlingRecordUuid=PathUrl+"app/getGridUuidsAndGridNamesByHandlingRecordUuid.do";//审核通过未通过调用
    public static String appGridAndSupervisionObject=PathUrl+"app/appGridAndSupervisionObject.do";//获取监管对象
    public static String getXml = PathUrl + "apk/getXml.do";//获取xml路径
    public static String downloadApk = PathUrl + "apk/download.do?type=0";//apk下载路径
    public static String mapPath= "http://113.200.60.90:6080/arcgis/rest/services/ShangluoMap/MapServer";//arcgis网络路径
    public static String getGridsByCondition=PathUrl+"app/getGridsByCondition.do";//获取上报详情的下发网格单位
    public static String deleteAttachment=PathUrl+"app/deleteByTaskAttachmentUuid.do";//附件删除接口
    public static String deleteKnowledgeAttachment=PathUrl+"app/deleteByKnowledgeAttachmentUuid.do";//知识库附件删除接口
    public static String taskDistributionCheck=PathUrl+"app/getTaskDistributionCheck.do";//待签收删除校验
    public static String taskExamineCheck=PathUrl+"app/getAppTaskExamineCheck.do";//待审核校验
    public static String judgeHandlingRecordIsExists=PathUrl+"app/judgeHandlingRecordIsExists.do";//待办任务的校验
    public static String getKnowledgeList=PathUrl+"app/getKnowledgeWrapperListByCondition.do";//获取知识库列表
    public static String delKnowledge=PathUrl+"app/knowledgeDelete.do";//删除知识库
    public static String addKnowledge=PathUrl+"app/knowledgeSave.do";//添加知识库



    public static void setBaseUrl(String newIP) {
        BaseUrl = newIP;
        PathUrl = BaseUrl + "/shangluo_gridms/";
        LoginUrl = PathUrl + "login/login.do";//登录接口
        LoginOutUrl = PathUrl + "login/logout.do";//退出接口
        GetPotrolObjects = PathUrl + "app/getSupervisionObjectList.do";//获取监管对象
        GetProblemTypes = PathUrl + "app/getPatrolProblemDictList.do";//获取问题类别
        UploadPotrol = PathUrl + "app/patrol.do";//巡查上报
        getDbTask = PathUrl + "app/getHandlingRecordList.do";//获取待办任务
        getDbTaskDetail = PathUrl + "app/handlingRecord.do";//获取待办任务详情
        getHandleProgress = PathUrl + "app/getHandledRecordDetailsByCondition.do";//获取任务处理流程
        handleTask = PathUrl + "app/appUpdateHandlingRecord.do";//处理任务
        getNoSignTaskDetail = PathUrl + "app/getAppCurrentUserTaskDistributionListByCondition.do";//获取待签收任务详情
        getMessageList = PathUrl + "app/getNoticeList.do";//获取消息列表
        getAimGrids = PathUrl + "app/getAppGridsByCurrentUser.do";//获取转办部门或者下发部门
        getSignTask = PathUrl + "app/appCheckinTask.do";//签收任务
        getAttachmentList = PathUrl + "app/getHandleAttachmentList.do";//附件列表
        modifyPassword = PathUrl + "app/appUpdatePassword.do";//修改密码
        getRecordTask = PathUrl + "app/getHandledRecordList.do";//获取待办任务
        queryContact = PathUrl + "app/appQueryContact.do";//获取联系人信息
        queryContactDetail = PathUrl + "app/appQueryContactDetail.do";//获取联系人信息详情
        getTaskCompleteDetail = PathUrl + "app/getHandledRecord.do";//获取已办任务详情信息
        getHandledRecordDetailsList = PathUrl + "app/getHandledRecordDetailsList.do";//获取已办任务处理流程
        uploadLatLog = PathUrl + "app/appPatrolTrack.do";//上传轨迹坐标点
        getAttachUrl = PathUrl + "handleAttachment/getHandleAttachmentRemoteUrl.do";//获取附件路径
        delayApplyUrl = PathUrl + "app/createDelayApply.do";//延时申请专属
        getSubgridByCondition = PathUrl + "app/getSubgridByCondition.do";//获取区域类型
        getTaskStatistics = PathUrl + "app/taskStatistics.do";//任务统计接口
        getPatrolProblemDict = PathUrl + "app/getPatrolProblemDict.do";//巡查问题类别接口
        getNoticeDetail = PathUrl + "notice/get.do";//获取通知详情
        checkSession = PathUrl + "app/checkSession.do";//检测session是否有效
        getGridsRankingStatisticsCount = PathUrl + "app/getGridsRankingStatisticsCount.do";//网格排名
        variationTrend = PathUrl + "app/variationTrendStatisticsToApp.do";//变化趋势
        getReportAndHandeledRecordCountByCondition = PathUrl + "app/getGridUsersReportCountByCondition.do";//网格员上报查询
        getGridUsersHandeledRecordCountByCondition = PathUrl + "app/getGridUsersHandeledRecordCountByCondition.do";//网格员办结查询
        getQianDaoStatic = PathUrl + "app/getCheckInStatistics.do";//获取网格员签到数据
        getTaskType = PathUrl + "app/getAllTaskType.do";//获取任务类型
        createOrEditTask = PathUrl + "app/save.do";//创建或编辑下发任务
        getCreatedTaskList = PathUrl + "app/getTaskWrapperListByCondition.do";//获取已创建的任务列表
        getTaskByUuid = PathUrl + "app/getTaskByUuid.do";//分配页：获取任务详情接口
        getGridsByCurrentUser = PathUrl + "app/getGridsByCurrentUser.do";//获取下级网格接口
        distributionTaskToGrids = PathUrl + "app/distributionTaskToGrids.do";//分配页：分配接口
        delTask = PathUrl + "app/deleteTask.do";//删除任务
        delDistributionTask = PathUrl + "app/deleteTaskDistribution.do";//分配页：删除接口
        getTaskDistributionListByCondition = PathUrl + "app/getTaskDistributionListByCondition.do";//分配页：列表数据接口
        getHandledRecordByTaskUuid = PathUrl + "app/getHandledRecordByTaskUuid.do";//分配页：查看详情接口
        isAuditor = PathUrl + "app/getRoleByUser.do";//判断是否为审核员
        getTaskExamineList = PathUrl + "task/getTaskExamineListByCondition.do";//获取待分配列表页
        getGridUuidsAndGridNamesByHandlingRecordUuid=PathUrl+"app/getGridUuidsAndGridNamesByHandlingRecordUuid.do";//审核通过未通过调用
        appGridAndSupervisionObject=PathUrl+"app/appGridAndSupervisionObject.do";//获取监管对象
        getXml = PathUrl + "apk/getXml.do";//获取xml路径
        downloadApk = PathUrl + "apk/download.do?type=0";//apk下载路径
        getCurrentDistribution= PathUrl + "app/getUsersWrapperListByCurrentGridUuid.do";//获取当前网格人员
        getPicAttachUrl=PathUrl+"app/getTaskAttachmentRemoteUrl.do";//获取下发任务附件预览路径
        getPicAttachmentList = PathUrl + "app/getTaskAttachmentWrapperListByCondition.do";//下发任务附件列表接口
        getGridsByCondition=PathUrl+"app/getGridsByCondition.do";//获取上报详情的下发网格单位
        deleteAttachment=PathUrl+"app/deleteByTaskAttachmentUuid.do";//附件删除接口
        taskDistributionCheck=PathUrl+"app/getTaskDistributionCheck.do";//待签收删除校验
        taskExamineCheck=PathUrl+"app/getAppTaskExamineCheck.do";//待审核校验
        judgeHandlingRecordIsExists=PathUrl+"app/judgeHandlingRecordIsExists.do";//待办任务的校验
        getKnowledgeList=PathUrl+"app/getKnowledgeWrapperListByCondition.do";//获取知识库列表
        getknowledgeAttachmentlist=PathUrl+"app/getKnowledgeAttachmentWrapperListByCondition.do";//知识库附件列表
        delKnowledge=PathUrl+"app/knowledgeDelete.do";//删除知识库
        getknowledgeDownload = PathUrl + "app/getKnowledgeAttachmentRemoteUrl.do";//获取知识库任务附件预览路径
        deleteKnowledgeAttachment=PathUrl+"app/deleteByKnowledgeAttachmentUuid.do";//知识库附件删除接口
        addKnowledge=PathUrl+"app/knowledgeSave.do";//添加知识库
        knowledgeUpdate = PathUrl + "app/knowledgeUpdate.do";//知识库更新

    }

}
