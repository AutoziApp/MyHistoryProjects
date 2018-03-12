package com.mapuni.car.mvp.searchcar.gotoview.model;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * Created by yawei on 2017/8/25.
 */

public class CarBean implements Serializable{


    /**
     * isExist : 0
     * errInfo : ��¼��ʱ�������µ�¼��
     */

    private int isExist;
    private String errInfo;
    /**
     * xszInfo : [{"checkweight":"122","carmode":"10","vin":"1123123","ownername":"123","usedquality":"10","carcardcolor":"blue","outfitweight":"1111","carcardnumber":"川A22233","checkpeople":"5","registerdate":"2016-01-01","addrarea":"420114","maxweight":"1233","carbrand":"123","enginenumber":"123","carversion":"HFJ1036WE3GV","cardtype":"02","owneraddress":"123123"}]
     * otherInfo : [{"oilsupply":"04","fuelstandard":"99","issyjhq":"1","outfactorydate":"2015-12-31","outputvolume":"","ratedspeed":"3333","engineversion":"DA471QLR","fueltype":"O","makername":"哈飞汽车股份有限公司","airinflow":"","fuelpumpform":"","isdoublefule":"01","registoffice":"","drivemode":"01"}]
     * czInfo : [{"mileagenum":"3333","agenttel":"","tel":"13333333333","cardname":"01","agentcardnumber":"","cardnumber":"133333333333333333","agentname":""}]
     * alertinfo :
     */

    private String alertinfo;
    private List<Map<String,String>> xszInfo;
    private List<Map<String,String>> otherInfo;
    private List<Map<String,String>> czInfo;
    private Map<String,String> jcInfo;

    public Map<String, String> getJcInfo() {
        return jcInfo;
    }

    public void setJcInfo(Map<String, String> jcInfo) {
        this.jcInfo = jcInfo;
    }

    public int getIsExist() {
        return isExist;
    }

    public void setIsExist(int isExist) {
        this.isExist = isExist;
    }

    public String getErrInfo() {
        return errInfo;
    }

    public void setErrInfo(String errInfo) {
        this.errInfo = errInfo;
    }

    public String getAlertinfo() {
        return alertinfo;
    }

    public void setAlertinfo(String alertinfo) {
        this.alertinfo = alertinfo;
    }

    public List<Map<String,String>> getXszInfo() {
        return xszInfo;
    }

    public void setXszInfo(List<Map<String,String>> xszInfo) {
        this.xszInfo = xszInfo;
    }

    public List<Map<String,String>> getOtherInfo() {
        return otherInfo;
    }

    public void setOtherInfo(List<Map<String,String>> otherInfo) {
        this.otherInfo = otherInfo;
    }

    public List<Map<String,String>> getCzInfo() {
        return czInfo;
    }

    public void setCzInfo(List<Map<String,String>> czInfo) {
        this.czInfo = czInfo;
    }

    public static class XszInfoBean {
        /**
         * checkweight : 122  核定载质量
         * carmode : 10  车辆种类
         * vin : 1123123
         * ownername : 123  所有人
         * usedquality : 10
         * carcardcolor : blue  颜色
         * outfitweight : 1111
         * carcardnumber : 川A22233
         * checkpeople : 5  核定人数
         * registerdate : 2016-01-01   注册日期
         * addrarea : 420114
         * maxweight : 1233   最大总质量
         * carbrand : 123  车辆品牌
         * enginenumber : 123  发动机号码
         * carversion : HFJ1036WE3GV  车辆型号
         * cardtype : 02
         * owneraddress : 123123  地址
         */

        private String checkweight;
        private String carmode;
        private String vin;
        private String ownername;
        private String usedquality;
        private String carcardcolor;
        private String outfitweight;
        private String carcardnumber;
        private String checkpeople;
        private String registerdate;
        private String addrarea;
        private String maxweight;
        private String carbrand;
        private String enginenumber;
        private String carversion;
        private String cardtype;
        private String owneraddress;

        public String getCheckweight() {
            return checkweight;
        }

        public void setCheckweight(String checkweight) {
            this.checkweight = checkweight;
        }

        public String getCarmode() {
            return carmode;
        }

        public void setCarmode(String carmode) {
            this.carmode = carmode;
        }

        public String getVin() {
            return vin;
        }

        public void setVin(String vin) {
            this.vin = vin;
        }

        public String getOwnername() {
            return ownername;
        }

        public void setOwnername(String ownername) {
            this.ownername = ownername;
        }

        public String getUsedquality() {
            return usedquality;
        }

        public void setUsedquality(String usedquality) {
            this.usedquality = usedquality;
        }

        public String getCarcardcolor() {
            return carcardcolor;
        }

        public void setCarcardcolor(String carcardcolor) {
            this.carcardcolor = carcardcolor;
        }

        public String getOutfitweight() {
            return outfitweight;
        }

        public void setOutfitweight(String outfitweight) {
            this.outfitweight = outfitweight;
        }

        public String getCarcardnumber() {
            return carcardnumber;
        }

        public void setCarcardnumber(String carcardnumber) {
            this.carcardnumber = carcardnumber;
        }

        public String getCheckpeople() {
            return checkpeople;
        }

        public void setCheckpeople(String checkpeople) {
            this.checkpeople = checkpeople;
        }

        public String getRegisterdate() {
            return registerdate;
        }

        public void setRegisterdate(String registerdate) {
            this.registerdate = registerdate;
        }

        public String getAddrarea() {
            return addrarea;
        }

        public void setAddrarea(String addrarea) {
            this.addrarea = addrarea;
        }

        public String getMaxweight() {
            return maxweight;
        }

        public void setMaxweight(String maxweight) {
            this.maxweight = maxweight;
        }

        public String getCarbrand() {
            return carbrand;
        }

        public void setCarbrand(String carbrand) {
            this.carbrand = carbrand;
        }

        public String getEnginenumber() {
            return enginenumber;
        }

        public void setEnginenumber(String enginenumber) {
            this.enginenumber = enginenumber;
        }

        public String getCarversion() {
            return carversion;
        }

        public void setCarversion(String carversion) {
            this.carversion = carversion;
        }

        public String getCardtype() {
            return cardtype;
        }

        public void setCardtype(String cardtype) {
            this.cardtype = cardtype;
        }

        public String getOwneraddress() {
            return owneraddress;
        }

        public void setOwneraddress(String owneraddress) {
            this.owneraddress = owneraddress;
        }
    }

    public static class OtherInfoBean {
        /**
         * oilsupply : 04
         * fuelstandard : 99
         * issyjhq : 1
         * outfactorydate : 2015-12-31
         * outputvolume :
         * ratedspeed : 3333
         * engineversion : DA471QLR
         * fueltype : O
         * makername : 哈飞汽车股份有限公司
         * airinflow :
         * fuelpumpform :
         * isdoublefule : 01
         * registoffice :
         * drivemode : 01
         */

        private String oilsupply;
        private String fuelstandard;
        private String issyjhq;
        private String outfactorydate;
        private String outputvolume;
        private String ratedspeed;
        private String engineversion;
        private String fueltype;
        private String makername;
        private String airinflow;
        private String fuelpumpform;
        private String isdoublefule;
        private String registoffice;
        private String drivemode;

        public String getOilsupply() {
            return oilsupply;
        }

        public void setOilsupply(String oilsupply) {
            this.oilsupply = oilsupply;
        }

        public String getFuelstandard() {
            return fuelstandard;
        }

        public void setFuelstandard(String fuelstandard) {
            this.fuelstandard = fuelstandard;
        }

        public String getIssyjhq() {
            return issyjhq;
        }

        public void setIssyjhq(String issyjhq) {
            this.issyjhq = issyjhq;
        }

        public String getOutfactorydate() {
            return outfactorydate;
        }

        public void setOutfactorydate(String outfactorydate) {
            this.outfactorydate = outfactorydate;
        }

        public String getOutputvolume() {
            return outputvolume;
        }

        public void setOutputvolume(String outputvolume) {
            this.outputvolume = outputvolume;
        }

        public String getRatedspeed() {
            return ratedspeed;
        }

        public void setRatedspeed(String ratedspeed) {
            this.ratedspeed = ratedspeed;
        }

        public String getEngineversion() {
            return engineversion;
        }

        public void setEngineversion(String engineversion) {
            this.engineversion = engineversion;
        }

        public String getFueltype() {
            return fueltype;
        }

        public void setFueltype(String fueltype) {
            this.fueltype = fueltype;
        }

        public String getMakername() {
            return makername;
        }

        public void setMakername(String makername) {
            this.makername = makername;
        }

        public String getAirinflow() {
            return airinflow;
        }

        public void setAirinflow(String airinflow) {
            this.airinflow = airinflow;
        }

        public String getFuelpumpform() {
            return fuelpumpform;
        }

        public void setFuelpumpform(String fuelpumpform) {
            this.fuelpumpform = fuelpumpform;
        }

        public String getIsdoublefule() {
            return isdoublefule;
        }

        public void setIsdoublefule(String isdoublefule) {
            this.isdoublefule = isdoublefule;
        }

        public String getRegistoffice() {
            return registoffice;
        }

        public void setRegistoffice(String registoffice) {
            this.registoffice = registoffice;
        }

        public String getDrivemode() {
            return drivemode;
        }

        public void setDrivemode(String drivemode) {
            this.drivemode = drivemode;
        }
    }

    public static class CzInfoBean {
        /**
         * mileagenum : 3333
         * agenttel :
         * tel : 13333333333
         * cardname : 01
         * agentcardnumber :
         * cardnumber : 133333333333333333
         * agentname :
         */

        private String mileagenum;
        private String agenttel;
        private String tel;
        private String cardname;
        private String agentcardnumber;
        private String cardnumber;
        private String agentname;

        public String getMileagenum() {
            return mileagenum;
        }

        public void setMileagenum(String mileagenum) {
            this.mileagenum = mileagenum;
        }

        public String getAgenttel() {
            return agenttel;
        }

        public void setAgenttel(String agenttel) {
            this.agenttel = agenttel;
        }

        public String getTel() {
            return tel;
        }

        public void setTel(String tel) {
            this.tel = tel;
        }

        public String getCardname() {
            return cardname;
        }

        public void setCardname(String cardname) {
            this.cardname = cardname;
        }

        public String getAgentcardnumber() {
            return agentcardnumber;
        }

        public void setAgentcardnumber(String agentcardnumber) {
            this.agentcardnumber = agentcardnumber;
        }

        public String getCardnumber() {
            return cardnumber;
        }

        public void setCardnumber(String cardnumber) {
            this.cardnumber = cardnumber;
        }

        public String getAgentname() {
            return agentname;
        }

        public void setAgentname(String agentname) {
            this.agentname = agentname;
        }
    }
}
