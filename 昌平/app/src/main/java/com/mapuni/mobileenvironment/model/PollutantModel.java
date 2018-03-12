package com.mapuni.mobileenvironment.model;

import android.util.Log;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.mapuni.mobileenvironment.utils.DateUtils;
/**
 * Created by yawei on 2016/12/13.
 */
public class PollutantModel {
    /**
     * result : 1
     * EntCode : 130300005
     * Output : [{"iType":1,"OutportCode":"130300005101","OutportName":"京能废水排口","pollutant":[{"PollutantCode":"001","PollutantName":"pH值"},{"PollutantCode":"011","PollutantName":"COD"},{"PollutantCode":"060","PollutantName":"氨氮"},{"PollutantCode":"b01","PollutantName":"流量"}]},{"iType":1,"OutportCode":"130300005102","OutportName":"京2","pollutant":[]},{"iType":2,"OutportCode":"130300005201","OutportName":"京能废气排口","pollutant":[{"PollutantCode":"01","PollutantName":"烟尘"},{"PollutantCode":"021","PollutantName":"COD"},{"PollutantCode":"b02","PollutantName":"流量"},{"PollutantCode":"s02","PollutantName":"流速"},{"PollutantCode":"zs01","PollutantName":"折算烟尘"},{"PollutantCode":"zs02","PollutantName":"折算二氧化硫"}]}]
     * Pollutants : {"OutportCode":130300005101,"pollutant":"001"}
     * WarmRange : [{"WarmType":2,"WarnLower":null,"WarnUpper":2.99}]
     * MonitorTime :
     * Value :
     */

    private int result;
    private int EntCode;
    private PollutantsBean Pollutants;
    private String MonitorTime;
    private String Value;
    private List<OutputBean> Output;
    private List<WarmRangeBean> WarmRange;
    public static int WATERTYPE = 1;
    public static int AIRTYPE = 2;
    public static int ALLTYPE = 3;
    private Map<String,String[]> waterSelect;
    private Map<String,String[]> airSelect;
    private Map<String,Map> mapSelect;
    private Map<String,String> outportMap;
    private Map<String,String> pollutantCodeMap;
    private int TotalRows;
    public int getTotalRows() {
        return TotalRows;
    }

    public void setTotalRows(int TotalRows) {
        this.TotalRows = TotalRows;
    }
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

//    public String getMonitorTime() {
//        return MonitorTime;
//    }

    public void setMonitorTime(String MonitorTime) {
        this.MonitorTime = MonitorTime;
    }

//    public String getValue() {
//        return Value;
//    }

    public void setValue(String Value) {
        this.Value = Value;
    }

    public List<OutputBean> getOutput() {
        return Output;
    }

    public void setOutput(List<OutputBean> Output) {
        this.Output = Output;
    }

    public List<WarmRangeBean> getWarmRange() {
        return WarmRange;
    }

    public void setWarmRange(List<WarmRangeBean> WarmRange) {
        this.WarmRange = WarmRange;
    }

    public static class PollutantsBean {
        /**
         * OutportCode : 130300005101
         * pollutant : 001
         */

        private long OutportCode;
        private String pollutant;

        public long getOutportCode() {
            return OutportCode;
        }

        public void setOutportCode(long OutportCode) {
            this.OutportCode = OutportCode;
        }

        public String getPollutant() {
            return pollutant;
        }

        public void setPollutant(String pollutant) {
            this.pollutant = pollutant;
        }
    }

    public static class OutputBean {
        /**
         * iType : 1
         * OutportCode : 130300005101
         * OutportName : 京能废水排口
         * pollutant : [{"PollutantCode":"001","PollutantName":"pH值"},{"PollutantCode":"011","PollutantName":"COD"},{"PollutantCode":"060","PollutantName":"氨氮"},{"PollutantCode":"b01","PollutantName":"流量"}]
         */

        private int iType;
        private String OutportCode;
        private String OutportName;
        private List<PollutantBean> pollutant;

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
         * WarnLower : null
         * WarnUpper : 2.99
         */

        private int WarmType;
        private Object WarnLower;
        private double WarnUpper;

        public int getWarmType() {
            return WarmType;
        }

        public void setWarmType(int WarmType) {
            this.WarmType = WarmType;
        }

        public Object getWarnLower() {
            return WarnLower;
        }

        public void setWarnLower(Object WarnLower) {
            this.WarnLower = WarnLower;
        }

        public double getWarnUpper() {
            return WarnUpper;
        }

        public void setWarnUpper(double WarnUpper) {
            this.WarnUpper = WarnUpper;
        }
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

    public Map<String,Map> getSelect(){
        if(waterSelect==null||airSelect==null||mapSelect==null){
            waterSelect = new HashMap<>();
            airSelect = new HashMap<>();
            mapSelect = new HashMap<>();
            for(int i=0;i<Output.size();i++){
                OutputBean bean= Output.get(i);
                String[] s=new String[bean.getPollutant().size()];
                if(bean.iType==1){
                    for(int ii=0;ii<bean.getPollutant().size();ii++){
                        OutputBean.PollutantBean _bean = bean.getPollutant().get(ii);
                        s[ii]=_bean.getPollutantName();
                        Log.i("Lybin","s[ii]"+s[ii]);

                    }
                    waterSelect.put(bean.getOutportName(),s);
                }
                if(bean.iType==2){
                    for(int ii=0;ii<bean.getPollutant().size();ii++){
                        OutputBean.PollutantBean _bean = bean.getPollutant().get(ii);
                        s[ii]=_bean.getPollutantName();
                        Log.i("Lybin","s[ii]"+s[ii]);

                    }
                    airSelect.put(bean.getOutportName(),s);
                }
            }
            mapSelect.put("air",airSelect);
            mapSelect.put("water",waterSelect);
        }
        return  mapSelect;
    }
    public String[] getValue() {
        if(Value==null||Value.length()==0){
            return null;
        }
        String[] values;
        Value = Value.replace("\"|[|]","-");
        Value = Value.replace("/","-");
        Value = Value.substring(1,Value.length()-2);
        Value=Value.replace('"',' ');
        values = Value.split(",");
        return values;
    }

    public String[] getMonitorTime() {
        if(MonitorTime==null||MonitorTime.length()==0){
            return null;
        }
        String[] monitorTimes;
        MonitorTime = MonitorTime.replace("/","-");
        MonitorTime = MonitorTime.replace("["," ");
        MonitorTime = MonitorTime.replace("]"," ");
//        MonitorTime = MonitorTime.substring(1,MonitorTime.length()-1);
        MonitorTime = MonitorTime.replace('"',' ');
        monitorTimes = MonitorTime.split(",");
        for(int i=0;i<monitorTimes.length;i++){
            String s = monitorTimes[i];
            s = DateUtils.formatData(s);
            monitorTimes[i] = s;
        }
        return monitorTimes;
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
            for(int i=0;i<Output.size();i++){
                OutputBean bean = Output.get(i);
                for(int ii=0;ii<bean.getPollutant().size();ii++){
                    OutputBean.PollutantBean _Bean = bean.getPollutant().get(ii);
                    pollutantCodeMap.put(_Bean.getPollutantName(),_Bean.getPollutantCode());
                }
            }
        }
        return pollutantCodeMap.get(s);
    }
}
