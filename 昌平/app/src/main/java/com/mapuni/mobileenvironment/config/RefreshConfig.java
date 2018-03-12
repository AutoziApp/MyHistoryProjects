package com.mapuni.mobileenvironment.config;

import com.mapuni.mobileenvironment.utils.StringUtils;

public class RefreshConfig {
    /**
     * 风险源信息
     */
    private static final String T_Bas_RiskSource = "t_bas_risksource";
    /**
     * 接警信息
     */
    private static final String T_Bas_EventAlarm = "T_Bas_EventAlarm".toLowerCase();
    /**
     * aqi
     */
    private static final String T_Bas_AirQualityDay = "T_Bas_AirQualityDay".toLowerCase();
    /**
     * 重污染信息
     */
    private static final String T_Bas_PWeather = "T_Bas_PWeather".toLowerCase();

    /**
     * 是否更新企业数据
     */
    public static boolean isRefreshRisk;
    /**
     * 是否更新事件信息
     */
    public static boolean isRefreshEvent;
    /**
     * 是否更新重污染
     */
    public static boolean isRefreshZWR;

    public static void checkSql(String sql){
        if(StringUtils.isEmpty(sql))
            return;
        String sqlLower = sql.toLowerCase();
        if(sqlLower.contains("insert")||sqlLower.contains("update")){
            if(sqlLower.contains(T_Bas_RiskSource)){
                isRefreshRisk = true;
            }else if(sqlLower.contains(T_Bas_EventAlarm)){
                isRefreshEvent = true;
            }else if(sqlLower.contains(T_Bas_AirQualityDay)||sqlLower.contains(T_Bas_PWeather)){
                isRefreshZWR = true;
            }
        }
    }

    public static void checkTable(String table){
        if(StringUtils.isEmpty(table))
            return;
        String tableLower = table.toLowerCase();
        if(table.contains("insert")||table.contains("update")){
            if(table.contains(T_Bas_RiskSource)){
                isRefreshRisk = true;
            }else if(table.contains(T_Bas_EventAlarm)){
                isRefreshEvent = true;
            }else if(table.contains(T_Bas_AirQualityDay)||table.contains(T_Bas_PWeather)){
                isRefreshZWR = true;
            }
        }
    }
}
