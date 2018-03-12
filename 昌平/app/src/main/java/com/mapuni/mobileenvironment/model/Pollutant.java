package com.mapuni.mobileenvironment.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by yawei on 2016/12/13.
 */
public class Pollutant {
    /**
     * result : 1
     * EntCode : 130300005
     * Output : [{"iType":1,"OutportCode":"130300005101","OutportName":"京能废水排口"},{"iType":2,"OutportCode":"130300005201","OutportName":"京能废气排口"}]
     * Pollutants : {"OutportCode":"130300005101","pollutant":[{"PollutantCode":"001","PollutantName":"pH值"},{"PollutantCode":"011","PollutantName":"COD"},{"PollutantCode":"060","PollutantName":"氨氮"},{"PollutantCode":"b01","PollutantName":"流量"}]}
     * WarmRange : {"WarmType":2,"WarnLower":"","WarnUpper":"2.990000"}
     * MonitorTime : [2016/12/13 0:00:00,2016/12/13 1:00:00,2016/12/13 2:00:00,2016/12/13 3:00:00,2016/12/13 4:00:00,2016/12/13 5:00:00,2016/12/13 6:00:00,2016/12/13 7:00:00,2016/12/13 8:00:00,2016/12/13 9:00:00,2016/12/13 10:00:00,2016/12/13 11:00:00,2016/12/13 12:00:00,2016/12/13 13:00:00,2016/12/13 14:00:00,2016/12/13 15:00:00,]
     * Value : [0.775000,1.775000,1.266667,1.566667,1.816667,1.633333,2.125000,1.600000,1.375000,1.408333,1.200000,1.725000,1.708333,1.458333,1.600000,1.266667,]
     */

    private int result;
    private int EntCode;
    private PollutantsBean Pollutants;
    private WarmRangeBean WarmRange;
    private String MonitorTime;
    private String Value;
    private List<OutputBean> Output;
    private List<OutputBean> waterOutput;
    private List<OutputBean> airOutput;
    private Map<String,String> outportMap;
    private Map<String,String> pollutantCodeMap;
    public static int WATERTYPE = 1;
    public static int AIRTYPE = 2;
    public static int ALLTYPE = 3;

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public int getEntCode() {
        return EntCode;
    }

    public void setEntCode(int EntCode) {
        this.EntCode = EntCode;
    }

    public PollutantsBean getPollutants() {
        return Pollutants;
    }

    public void setPollutants(PollutantsBean Pollutants) {
        this.Pollutants = Pollutants;
    }

    public WarmRangeBean getWarmRange() {
        return WarmRange;
    }

    public void setWarmRange(WarmRangeBean WarmRange) {
        this.WarmRange = WarmRange;
    }

    public String[] getMonitorTime() {
        String[] monitorTimes;
        MonitorTime = MonitorTime.replace("\"|[|]","-");
        MonitorTime = MonitorTime.replace("/","-");
        MonitorTime = MonitorTime.substring(1,MonitorTime.length()-2);
        monitorTimes = MonitorTime.split(",");
        return monitorTimes;
    }

    public void setMonitorTime(String MonitorTime) {
        this.MonitorTime = MonitorTime;
    }

    public String[] getValue() {
        String[] values;
        Value = Value.replace("\"|[|]","-");
        Value = Value.replace("/","-");
        Value = Value.substring(1,Value.length()-2);
        values = Value.split(",");
        return values;
    }

    public void setValue(String Value) {
        this.Value = Value;
    }

    public List<OutputBean> getOutput() {
        return Output;
    }
    public List<OutputBean> getWaterOutput() {
        if(waterOutput==null)
            waterOutput = new ArrayList<>();
        for(int i=0;i<Output.size();i++){
            OutputBean bean = Output.get(i);
            if(bean.iType==1){
                waterOutput.add(bean);
            }
        }
        return waterOutput;
    }
    public List<OutputBean> getAirOutput() {
        if(airOutput==null)
            airOutput = new ArrayList<>();
        for(int i=0;i<Output.size();i++){
            OutputBean bean = Output.get(i);
            if(bean.iType==2){
                airOutput.add(bean);
            }
        }
        return airOutput;
    }
    public int getType(){
        int type=0;
        for(int i=0;i<Output.size();i++){
            OutputBean bean = Output.get(i);
            if(bean.iType==WATERTYPE&&type==0){
                type = WATERTYPE;
            }
            if (bean.iType==AIRTYPE&&type==0){
                type = AIRTYPE;
            }
            if(bean.iType==WATERTYPE&&type==AIRTYPE||bean.iType==AIRTYPE&&type==WATERTYPE){
                return ALLTYPE;
            }
        }
        return type;
    }

    public String getOutportCode(String s){
        if(outportMap==null){
            outportMap = new HashMap<>();
            for(int i=0;i<Output.size();i++){
                OutputBean bean = Output.get(i);
                outportMap.put(bean.getOutportName(),bean.getOutportCode());
            }
        }
        return  outportMap.get(s);
    }

    public String getpollutantCode(String s){
        if(pollutantCodeMap==null){
            pollutantCodeMap = new HashMap<>();
            for(int i=0;i<Pollutants.getPollutant().size();i++){
                PollutantsBean.PollutantBean bean =Pollutants.getPollutant().get(i);
                pollutantCodeMap.put(bean.getPollutantName(),bean.getPollutantCode());
            }
        }
        return pollutantCodeMap.get(s);
    }


    public void setOutput(List<OutputBean> Output) {
        this.Output = Output;
    }

    public static class PollutantsBean {
        /**
         * OutportCode : 130300005101
         * pollutant : [{"PollutantCode":"001","PollutantName":"pH值"},{"PollutantCode":"011","PollutantName":"COD"},{"PollutantCode":"060","PollutantName":"氨氮"},{"PollutantCode":"b01","PollutantName":"流量"}]
         */

        private String OutportCode;
        private List<PollutantBean> pollutant;

        public String getOutportCode() {
            return OutportCode;
        }

        public void setOutportCode(String OutportCode) {
            this.OutportCode = OutportCode;
        }

        public List<PollutantBean> getPollutant() {
            return pollutant;
        }

        public void setPollutant(List<PollutantBean> pollutant) {
            this.pollutant = pollutant;
        }

        public static class PollutantBean {
            /**
             * PollutantCode : 001
             * PollutantName : pH值
             */

            private String PollutantCode;
            private String PollutantName;

            public String getPollutantCode() {
                return PollutantCode;
            }

            public void setPollutantCode(String PollutantCode) {
                this.PollutantCode = PollutantCode;
            }

            public String getPollutantName() {
                return PollutantName;
            }

            public void setPollutantName(String PollutantName) {
                this.PollutantName = PollutantName;
            }
        }
    }

    public static class WarmRangeBean {
        /**
         * WarmType : 2
         * WarnLower :
         * WarnUpper : 2.990000
         */

        private int WarmType;
        private String WarnLower;
        private String WarnUpper;

        public int getWarmType() {
            return WarmType;
        }

        public void setWarmType(int WarmType) {
            this.WarmType = WarmType;
        }

        public String getWarnLower() {
            return WarnLower;
        }

        public void setWarnLower(String WarnLower) {
            this.WarnLower = WarnLower;
        }

        public String getWarnUpper() {
            return WarnUpper;
        }

        public void setWarnUpper(String WarnUpper) {
            this.WarnUpper = WarnUpper;
        }
    }

    public static class OutputBean {
        /**
         * iType : 1
         * OutportCode : 130300005101
         * OutportName : 京能废水排口
         */

        private int iType;
        private String OutportCode;
        private String OutportName;

        public int getIType() {
            return iType;
        }

        public void setIType(int iType) {
            this.iType = iType;
        }

        public String getOutportCode() {
            return OutportCode;
        }

        public void setOutportCode(String OutportCode) {
            this.OutportCode = OutportCode;
        }

        public String getOutportName() {
            return OutportName;
        }

        public void setOutportName(String OutportName) {
            this.OutportName = OutportName;
        }
    }
}
