package com.mapuni.shangluo.manager;

import com.mapuni.shangluo.app.MyApplication;
import com.mapuni.shangluo.utils.SPUtils;

/**
 * Created by 15225 on 2017/8/19.
 */

public class PathManager {
//    public static String BaseUrl="http://113.200.60.90:8184";
    public static String BaseUrl= (String) SPUtils.getSp(MyApplication.getContextObject(), "newIP", "http://113.200.60.90:8184");
//    public static String BaseUrl="http://113.200.93.39:8184";
    //    public static String BaseUrl="http://192.168.120.123:8080";
//    public static String BaseUrl="http://192.168.120.10:8080";
//    public static String BaseUrl="http://192.168.120.14:8007";
//    public static String BaseUrl="http://192.168.120.12:8007";
//    public static String BaseUrl="http://192.168.100.60:8184";
//    public static String BaseUrl="http://192.168.120.10:8080";
    public static String PathUrl =BaseUrl+"/shangluo_gridms/";
    public static String LoginUrl= PathUrl +"login/login.do";//登录接口
    public static String LoginOutUrl= PathUrl +"login/logout.do";//退出接口
    public static String GetPotrolObjects= PathUrl +"app/getSupervisionObjectList.do";//获取监管对象
    public static String GetProblemTypes= PathUrl +"app/getPatrolProblemDictList.do";//获取问题类别
    public static String UploadPotrol= PathUrl +"app/patrol.do";//巡查上报
    public static String getDbTask= PathUrl +"app/getHandlingRecordList.do";//获取待办任务
    public static String getDbTaskDetail= PathUrl +"app/handlingRecord.do";//获取待办任务详情
    public static String getHandleProgress= PathUrl +"app/getHandledRecordDetailsByCondition.do";//获取任务处理流程
    public static String handleTask= PathUrl +"app/appUpdateHandlingRecord.do";//处理任务
    public static String getNoSignTaskDetail= PathUrl +"app/getAppCurrentUserTaskDistributionListByCondition.do";//获取待签收任务详情
    public static String getMessageList= PathUrl +"app/getNoticeList.do";//获取消息列表
    public static String getAimGrids= PathUrl +"app/getAppGridsByCurrentUser.do";//获取转办部门或者下发部门
    public static String getSignTask= PathUrl +"app/appCheckinTask.do";//签收任务
    public static String getAttachmentList= PathUrl +"app/getHandleAttachmentList.do";//附件列表
    public static String modifyPassword= PathUrl +"app/appUpdatePassword.do";//修改密码
    public static String getRecordTask= PathUrl +"app/getHandledRecordList.do";//获取待办任务
    public static String queryContact= PathUrl +"app/appQueryContact.do";//获取联系人信息
    public static String queryContactDetail= PathUrl +"app/appQueryContactDetail.do";//获取联系人信息详情
    public static String getTaskCompleteDetail= PathUrl +"app/getHandledRecord.do";//获取已办任务详情信息
    public static String getHandledRecordDetailsList= PathUrl +"app/getHandledRecordDetailsList.do";//获取已办任务处理流程
    public static String uploadLatLog= PathUrl +"app/appPatrolTrack.do";//上传轨迹坐标点
    public static String getAttachUrl= PathUrl +"handleAttachment/getHandleAttachmentRemoteUrl.do";//获取附件路径
    public static String registerUrl= PathUrl +"registerInfo/save.do";//签到
    public static String delayApplyUrl= PathUrl +"app/createDelayApply.do";//延时申请专属
    public static String getNoticeDetail= PathUrl +"notice/get.do";//获取通知详情
    public static String checkSession= PathUrl +"app/checkSession.do";//检测session是否有效
    public static String getXml= PathUrl +"apk/getXml.do";//获取xml路径
    public static String getCurrentDistribution= PathUrl + "app/getUsersWrapperListByCurrentGridUuid.do";//获取当前网格人员
    public static String appGridAndSupervisionObject=PathUrl+"app/appGridAndSupervisionObject.do";//获取监管对象
    public static String downloadApk= PathUrl +"apk/download.do?type=1";//apk下载路径
    public static String mapPath= "http://113.200.60.90:6080/arcgis/rest/services/ShangluoMap/MapServer";//arcgis网络路径
    public static String getGridUuidsAndGridNamesByHandlingRecordUuid=PathUrl+"app/getGridUuidsAndGridNamesByHandlingRecordUuid.do";//审核通过未通过调用
    public static String getGridsByCondition=PathUrl+"app/getGridsByCondition.do";//获取上报详情的下发网格单位
    public static String getPicAttachmentList = PathUrl + "app/getTaskAttachmentWrapperListByCondition.do";//下发任务附件列表接口
    public static String deleteAttachment=PathUrl+"app/deleteByTaskAttachmentUuid.do";//附件删除接口
    public static String getPicAttachUrl = PathUrl + "app/getTaskAttachmentRemoteUrl.do";//获取下发任务附件预览路径
    public static String taskDistributionCheck=PathUrl+"app/getTaskDistributionCheck.do";//待签收删除校验
    public static String judgeHandlingRecordIsExists=PathUrl+"app/judgeHandlingRecordIsExists.do";//待办任务的校验
    public static String isRegisted=PathUrl+"app/isRegisterInfoByCondition.do";//签到校验
    public static String getQiandaoRecord=PathUrl+"app/getRegisterInfoWrapperListByCondition.do";//签到记录
    public static String getKnowledgeList=PathUrl+"app/getKnowledgeWrapperListByCondition.do";//获取知识库列表
    public static String delKnowledge=PathUrl+"app/knowledgeDelete.do";//删除知识库
    public static String addKnowledge=PathUrl+"app/knowledgeSave.do";//添加知识库
    public static String getknowledgeDownload = PathUrl + "app/getKnowledgeAttachmentRemoteUrl.do";//获取知识库任务附件预览路径
    public static String deleteKnowledgeAttachment=PathUrl+"app/deleteByKnowledgeAttachmentUuid.do";//知识库附件删除接口
    public static String getknowledgeAttachmentlist=PathUrl+"app/getKnowledgeAttachmentWrapperListByCondition.do";//知识库附件列表
    public static String knowledgeUpdate = PathUrl + "app/knowledgeUpdate.do";//知识库更新

    public static void setPathUrl(String newIP){
        BaseUrl =newIP;
        PathUrl =BaseUrl+"/shangluo_gridms/";
        LoginUrl= PathUrl +"login/login.do";//登录接口
        LoginOutUrl= PathUrl +"login/logout.do";//退出接口
        GetPotrolObjects= PathUrl +"app/getSupervisionObjectList.do";//获取监管对象
        GetProblemTypes= PathUrl +"app/getPatrolProblemDictList.do";//获取问题类别
        UploadPotrol= PathUrl +"app/patrol.do";//巡查上报
        getDbTask= PathUrl +"app/getHandlingRecordList.do";//获取待办任务
        getDbTaskDetail= PathUrl +"app/handlingRecord.do";//获取待办任务详情
        getHandleProgress= PathUrl +"app/getHandledRecordDetailsByCondition.do";//获取任务处理流程
        handleTask= PathUrl +"app/appUpdateHandlingRecord.do";//处理任务
        getNoSignTaskDetail= PathUrl +"app/getAppCurrentUserTaskDistributionListByCondition.do";//获取待签收任务详情
        getMessageList= PathUrl +"app/getNoticeList.do";//获取消息列表
        getAimGrids= PathUrl +"app/getAppGridsByCurrentUser.do";//获取转办部门或者下发部门
        getSignTask= PathUrl +"app/appCheckinTask.do";//签收任务
        getAttachmentList= PathUrl +"app/getHandleAttachmentList.do";//附件列表
        modifyPassword= PathUrl +"app/appUpdatePassword.do";//修改密码
        getRecordTask= PathUrl +"app/getHandledRecordList.do";//获取待办任务
        queryContact= PathUrl +"app/appQueryContact.do";//获取联系人信息
        queryContactDetail= PathUrl +"app/appQueryContactDetail.do";//获取联系人信息详情
        getTaskCompleteDetail= PathUrl +"app/getHandledRecord.do";//获取已办任务详情信息
        getHandledRecordDetailsList= PathUrl +"app/getHandledRecordDetailsList.do";//获取已办任务处理流程
        uploadLatLog= PathUrl +"app/appPatrolTrack.do";//上传轨迹坐标点
        getAttachUrl= PathUrl +"handleAttachment/getHandleAttachmentRemoteUrl.do";//获取附件路径
        registerUrl= PathUrl +"registerInfo/save.do";//签到
        delayApplyUrl= PathUrl +"app/createDelayApply.do";//延时申请专属
        getNoticeDetail= PathUrl +"notice/get.do";//获取通知详情
        checkSession= PathUrl +"app/checkSession.do";//检测session是否有效
        appGridAndSupervisionObject=PathUrl+"app/appGridAndSupervisionObject.do";//获取监管对象
        getXml= PathUrl +"apk/getXml.do";//获取xml路径
        downloadApk= PathUrl +"apk/download.do?type=1";//apk下载路径
        getCurrentDistribution= PathUrl + "app/getUsersWrapperListByCurrentGridUuid.do";//获取当前网格人员
        getGridUuidsAndGridNamesByHandlingRecordUuid=PathUrl+"app/getGridUuidsAndGridNamesByHandlingRecordUuid.do";//审核通过未通过调用
        getGridsByCondition=PathUrl+"app/getGridsByCondition.do";//获取上报详情的下发网格单位
        getPicAttachmentList = PathUrl + "app/getTaskAttachmentWrapperListByCondition.do";//下发任务附件列表接口
        deleteAttachment=PathUrl+"app/deleteByTaskAttachmentUuid.do";//附件删除接口
        getPicAttachUrl = PathUrl + "app/getTaskAttachmentRemoteUrl.do";//获取下发任务附件预览路径
        taskDistributionCheck=PathUrl+"app/getTaskDistributionCheck.do";//待签收删除校验
        judgeHandlingRecordIsExists=PathUrl+"app/judgeHandlingRecordIsExists.do";//待办任务的校验
        isRegisted=PathUrl+"app/isRegisterInfoByCondition.do";//签到校验
        getQiandaoRecord=PathUrl+"app/getRegisterInfoWrapperListByCondition.do";//签到记录
        getKnowledgeList=PathUrl+"app/getKnowledgeWrapperListByCondition.do";//获取知识库列表
        delKnowledge=PathUrl+"app/knowledgeDelete.do";//删除知识库
        addKnowledge=PathUrl+"app/knowledgeSave.do";//添加知识库
        getknowledgeDownload = PathUrl + "app/getKnowledgeAttachmentRemoteUrl.do";//获取知识库任务附件预览路径
        deleteKnowledgeAttachment=PathUrl+"app/deleteByKnowledgeAttachmentUuid.do";//知识库附件删除接口
        getknowledgeAttachmentlist=PathUrl+"app/getKnowledgeAttachmentWrapperListByCondition.do";//知识库附件列表
        knowledgeUpdate = PathUrl + "app/knowledgeUpdate.do";//知识库更新
    }
}
