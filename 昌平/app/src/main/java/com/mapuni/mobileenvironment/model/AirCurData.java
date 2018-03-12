package com.mapuni.mobileenvironment.model;

import java.util.List;

/**
 * Created by Mai on 2016/12/22.
 */

public class AirCurData {
    @Override
    public String toString() {
        return "AirCurData{" +
                "ret=" + ret +
                ", time='" + time + '\'' +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                '}';
    }

    /**
     * ret : 0
     * time : 2016-12-22 10:51:38
     * data : [{"aQI":65,"co":-1,"coIAQI":-1,"deviceName":"044兴寿村委会","deviceid":"A1YQ5200026B0515","most":"pm10","no2":-1,"no2IAQI":-1,"o3":-1,"o3Average":-1,"o3AverageIAQI":-1,"o3IAQI":-1,"pm10":80,"pm10IAQI":65,"pm25":35,"pm25IAQI":50,"so2":-1,"so2IAQI":-1,"time":1480870800000,"type":0},{"aQI":77,"co":-1,"coIAQI":-1,"deviceName":"005阳坊中学","deviceid":"A1YQ5200042D0518","most":"pm10","no2":-1,"no2IAQI":-1,"o3":-1,"o3Average":-1,"o3AverageIAQI":-1,"o3IAQI":-1,"pm10":104,"pm10IAQI":77,"pm25":46,"pm25IAQI":64,"so2":-1,"so2IAQI":-1,"time":1480870800000,"type":0},{"aQI":81,"co":-1,"coIAQI":-1,"deviceName":"012 西辛峰村社区服务站","deviceid":"A1YQ520101BC051D","most":"pm10","no2":-1,"no2IAQI":-1,"o3":-1,"o3Average":-1,"o3AverageIAQI":-1,"o3IAQI":-1,"pm10":112,"pm10IAQI":81,"pm25":52,"pm25IAQI":71,"so2":-1,"so2IAQI":-1,"time":1480870800000,"type":0},{"aQI":54,"co":-1,"coIAQI":-1,"deviceName":"025 锥石口村社区卫生服务站","deviceid":"A1YQ520102400511","most":"pm10","no2":-1,"no2IAQI":-1,"o3":-1,"o3Average":-1,"o3AverageIAQI":-1,"o3IAQI":-1,"pm10":58,"pm10IAQI":54,"pm25":28,"pm25IAQI":40,"so2":-1,"so2IAQI":-1,"time":1480870800000,"type":0},{"aQI":98,"co":-1,"coIAQI":-1,"deviceName":"001八口村","deviceid":"A1YQ5201026C0513","most":"pm10","no2":-1,"no2IAQI":-1,"o3":-1,"o3Average":-1,"o3AverageIAQI":-1,"o3IAQI":-1,"pm10":145,"pm10IAQI":98,"pm25":67,"pm25IAQI":90,"so2":-1,"so2IAQI":-1,"time":1480870800000,"type":0},{"aQI":67,"co":-1,"coIAQI":-1,"deviceName":"019 崔村镇政府","deviceid":"A1YQ520102B80510","most":"pm10","no2":-1,"no2IAQI":-1,"o3":-1,"o3Average":-1,"o3AverageIAQI":-1,"o3IAQI":-1,"pm10":83,"pm10IAQI":67,"pm25":39,"pm25IAQI":55,"so2":-1,"so2IAQI":-1,"time":1480870800000,"type":0},{"aQI":70,"co":-1,"coIAQI":-1,"deviceName":"023麻峪上村","deviceid":"A1YQ520102F30515","most":"pm10","no2":-1,"no2IAQI":-1,"o3":-1,"o3Average":-1,"o3AverageIAQI":-1,"o3IAQI":-1,"pm10":90,"pm10IAQI":70,"pm25":41,"pm25IAQI":58,"so2":-1,"so2IAQI":-1,"time":1480870800000,"type":0},{"aQI":94,"co":-1,"coIAQI":-1,"deviceName":"004四家庄小学","deviceid":"A1YQ520103A30515","most":"pm10","no2":-1,"no2IAQI":-1,"o3":-1,"o3Average":-1,"o3AverageIAQI":-1,"o3IAQI":-1,"pm10":137,"pm10IAQI":94,"pm25":65,"pm25IAQI":88,"so2":-1,"so2IAQI":-1,"time":1480870800000,"type":0},{"aQI":75,"co":-1,"coIAQI":-1,"deviceName":"007阳坊镇政府","deviceid":"A1YQ520204770513","most":"pm10","no2":-1,"no2IAQI":-1,"o3":-1,"o3Average":-1,"o3AverageIAQI":-1,"o3IAQI":-1,"pm10":99,"pm10IAQI":75,"pm25":46,"pm25IAQI":64,"so2":-1,"so2IAQI":-1,"time":1480870800000,"type":0},{"aQI":90,"co":-1,"coIAQI":-1,"deviceName":"003后白虎涧村","deviceid":"A1YQ52030242051D","most":"pm10","no2":-1,"no2IAQI":-1,"o3":-1,"o3Average":-1,"o3AverageIAQI":-1,"o3IAQI":-1,"pm10":130,"pm10IAQI":90,"pm25":61,"pm25IAQI":83,"so2":-1,"so2IAQI":-1,"time":1480870800000,"type":0},{"aQI":54,"co":-1,"coIAQI":-1,"deviceName":"048 东新城村","deviceid":"A1YQ5203029B0515","most":"pm10","no2":-1,"no2IAQI":-1,"o3":-1,"o3Average":-1,"o3AverageIAQI":-1,"o3IAQI":-1,"pm10":58,"pm10IAQI":54,"pm25":29,"pm25IAQI":41,"so2":-1,"so2IAQI":-1,"time":1480910400000,"type":0},{"aQI":109,"co":-1,"coIAQI":-1,"deviceName":"002东贯口市村","deviceid":"A1YQ52040368051C","most":"pm10","no2":-1,"no2IAQI":-1,"o3":-1,"o3Average":-1,"o3AverageIAQI":-1,"o3IAQI":-1,"pm10":168,"pm10IAQI":109,"pm25":77,"pm25IAQI":103,"so2":-1,"so2IAQI":-1,"time":1480870800000,"type":0},{"aQI":61,"co":-1,"coIAQI":-1,"deviceName":"028 景文屯村居委会","deviceid":"A1YQ5204039F0514","most":"pm10","no2":-1,"no2IAQI":-1,"o3":-1,"o3Average":-1,"o3AverageIAQI":-1,"o3IAQI":-1,"pm10":72,"pm10IAQI":61,"pm25":35,"pm25IAQI":50,"so2":-1,"so2IAQI":-1,"time":1480870800000,"type":0},{"aQI":62,"co":-1,"coIAQI":-1,"deviceName":"045 桃林村","deviceid":"A1YQ520403A7051D","most":"pm10","no2":-1,"no2IAQI":-1,"o3":-1,"o3Average":-1,"o3AverageIAQI":-1,"o3IAQI":-1,"pm10":74,"pm10IAQI":62,"pm25":38,"pm25IAQI":54,"so2":-1,"so2IAQI":-1,"time":1480910400000,"type":0},{"aQI":10,"co":-1,"coIAQI":-1,"deviceName":"053  凯德麓语会所","deviceid":"A1YQ520404020514","most":"pm10","no2":-1,"no2IAQI":-1,"o3":-1,"o3Average":-1,"o3AverageIAQI":-1,"o3IAQI":-1,"pm10":10,"pm10IAQI":10,"pm25":5,"pm25IAQI":7,"so2":-1,"so2IAQI":-1,"time":1480910400000,"type":0},{"co":-1,"coIAQI":-1,"deviceName":"006后花园景区","deviceid":"A1YQ52050220051A","most":"-","no2":-1,"no2IAQI":-1,"o3":-1,"o3Average":-1,"o3AverageIAQI":-1,"o3IAQI":-1,"pm10":-1,"pm10IAQI":-1,"pm25":-1,"pm25IAQI":-1,"so2":-1,"so2IAQI":-1,"time":1480870800000,"type":0},{"aQI":63,"co":-1,"coIAQI":-1,"deviceName":"056 香屯村","deviceid":"A1YQ5205031F051D","most":"pm10","no2":-1,"no2IAQI":-1,"o3":-1,"o3Average":-1,"o3AverageIAQI":-1,"o3IAQI":-1,"pm10":76,"pm10IAQI":63,"pm25":37,"pm25IAQI":53,"so2":-1,"so2IAQI":-1,"time":1480910400000,"type":0},{"aQI":25,"co":-1,"coIAQI":-1,"deviceName":"063山峡社区","deviceid":"A1YQ52050369051A","most":"pm10","no2":-1,"no2IAQI":-1,"o3":-1,"o3Average":-1,"o3AverageIAQI":-1,"o3IAQI":-1,"pm10":25,"pm10IAQI":25,"pm25":12,"pm25IAQI":17,"so2":-1,"so2IAQI":-1,"time":1480910400000,"type":0},{"aQI":78,"co":-1,"coIAQI":-1,"deviceName":"043东庄村委会","deviceid":"A1YQ5205044E0515","most":"pm10","no2":-1,"no2IAQI":-1,"o3":-1,"o3Average":-1,"o3AverageIAQI":-1,"o3IAQI":-1,"pm10":106,"pm10IAQI":78,"pm25":51,"pm25IAQI":70,"so2":-1,"so2IAQI":-1,"time":1480870800000,"type":0},{"aQI":77,"co":-1,"coIAQI":-1,"deviceName":"021 真顺村居委会","deviceid":"A1YQ5206039D0518","most":"pm10","no2":-1,"no2IAQI":-1,"o3":-1,"o3Average":-1,"o3AverageIAQI":-1,"o3IAQI":-1,"pm10":103,"pm10IAQI":77,"pm25":49,"pm25IAQI":68,"so2":-1,"so2IAQI":-1,"time":1480870800000,"type":0},{"aQI":72,"co":-1,"coIAQI":-1,"deviceName":"022麻峪村村委会","deviceid":"A1YQ520603A50511","most":"pm10","no2":-1,"no2IAQI":-1,"o3":-1,"o3Average":-1,"o3AverageIAQI":-1,"o3IAQI":-1,"pm10":94,"pm10IAQI":72,"pm25":44,"pm25IAQI":61,"so2":-1,"so2IAQI":-1,"time":1480870800000,"type":0},{"aQI":88,"co":-1,"coIAQI":-1,"deviceName":"036 何营村村委会","deviceid":"A1YQ5207017E051E","most":"pm10","no2":-1,"no2IAQI":-1,"o3":-1,"o3Average":-1,"o3AverageIAQI":-1,"o3IAQI":-1,"pm10":125,"pm10IAQI":88,"pm25":57,"pm25IAQI":78,"so2":-1,"so2IAQI":-1,"time":1480870800000,"type":0},{"aQI":67,"co":-1,"coIAQI":-1,"deviceName":"057 东营村","deviceid":"A1YQ520701EF051A","most":"pm10","no2":-1,"no2IAQI":-1,"o3":-1,"o3Average":-1,"o3AverageIAQI":-1,"o3IAQI":-1,"pm10":83,"pm10IAQI":67,"pm25":40,"pm25IAQI":56,"so2":-1,"so2IAQI":-1,"time":1480910400000,"type":0},{"aQI":64,"co":-1,"coIAQI":-1,"deviceName":"000昌平环保局楼顶","deviceid":"A1YQ5207021F0514","most":"pm10","no2":-1,"no2IAQI":-1,"o3":-1,"o3Average":-1,"o3AverageIAQI":-1,"o3IAQI":-1,"pm10":77,"pm10IAQI":64,"pm25":37,"pm25IAQI":53,"so2":-1,"so2IAQI":-1,"time":1480870800000,"type":0},{"aQI":87,"co":-1,"coIAQI":-1,"deviceName":"040 营坊村","deviceid":"A1YQ5207026D0511","most":"pm10","no2":-1,"no2IAQI":-1,"o3":-1,"o3Average":-1,"o3AverageIAQI":-1,"o3IAQI":-1,"pm10":123,"pm10IAQI":87,"pm25":59,"pm25IAQI":80,"so2":-1,"so2IAQI":-1,"time":1480870800000,"type":0},{"aQI":68,"co":-1,"coIAQI":-1,"deviceName":"032 路劲社区","deviceid":"A1YQ520702A60512","most":"pm10","no2":-1,"no2IAQI":-1,"o3":-1,"o3Average":-1,"o3AverageIAQI":-1,"o3IAQI":-1,"pm10":85,"pm10IAQI":68,"pm25":38,"pm25IAQI":54,"so2":-1,"so2IAQI":-1,"time":1480870800000,"type":0},{"co":-1,"coIAQI":-1,"deviceName":"010 香堂七区","deviceid":"A1YQ520702F40517","most":"-","no2":-1,"no2IAQI":-1,"o3":-1,"o3Average":-1,"o3AverageIAQI":-1,"o3IAQI":-1,"pm10":-1,"pm10IAQI":-1,"pm25":-1,"pm25IAQI":-1,"so2":-1,"so2IAQI":-1,"time":1480870800000,"type":0},{"aQI":75,"co":-1,"coIAQI":-1,"deviceName":"042 象房村","deviceid":"A1YQ5207031D0511","most":"pm10","no2":-1,"no2IAQI":-1,"o3":-1,"o3Average":-1,"o3AverageIAQI":-1,"o3IAQI":-1,"pm10":99,"pm10IAQI":75,"pm25":45,"pm25IAQI":63,"so2":-1,"so2IAQI":-1,"time":1480870800000,"type":0},{"co":-1,"coIAQI":-1,"deviceName":"061 金隅观澜时代","deviceid":"A1YQ5207036F0514","most":"-","no2":-1,"no2IAQI":-1,"o3":-1,"o3Average":-1,"o3AverageIAQI":-1,"o3IAQI":-1,"pm10":-1,"pm10IAQI":-1,"pm25":-1,"pm25IAQI":-1,"so2":-1,"so2IAQI":-1,"time":1480910400000,"type":0},{"aQI":27,"co":-1,"coIAQI":-1,"deviceName":"064利阳大厦","deviceid":"A1YQ5207038B051E","most":"pm10","no2":-1,"no2IAQI":-1,"o3":-1,"o3Average":-1,"o3AverageIAQI":-1,"o3IAQI":-1,"pm10":27,"pm10IAQI":27,"pm25":13,"pm25IAQI":19,"so2":-1,"so2IAQI":-1,"time":1480910400000,"type":0},{"aQI":63,"co":-1,"coIAQI":-1,"deviceName":"058 西营村","deviceid":"A1YQ520703EB0510","most":"pm10","no2":-1,"no2IAQI":-1,"o3":-1,"o3Average":-1,"o3AverageIAQI":-1,"o3IAQI":-1,"pm10":76,"pm10IAQI":63,"pm25":37,"pm25IAQI":53,"so2":-1,"so2IAQI":-1,"time":1480910400000,"type":0},{"aQI":86,"co":-1,"coIAQI":-1,"deviceName":"027 张各庄村居委会","deviceid":"A1YQ520802E00519","most":"pm10","no2":-1,"no2IAQI":-1,"o3":-1,"o3Average":-1,"o3AverageIAQI":-1,"o3IAQI":-1,"pm10":121,"pm10IAQI":86,"pm25":59,"pm25IAQI":80,"so2":-1,"so2IAQI":-1,"time":1480870800000,"type":0},{"co":-1,"coIAQI":-1,"deviceName":"034 金明园社区","deviceid":"A1YQ520803660518","most":"-","no2":-1,"no2IAQI":-1,"o3":-1,"o3Average":-1,"o3AverageIAQI":-1,"o3IAQI":-1,"pm10":-1,"pm10IAQI":-1,"pm25":-1,"pm25IAQI":-1,"so2":-1,"so2IAQI":-1,"time":1480910400000,"type":0},{"aQI":74,"co":-1,"coIAQI":-1,"deviceName":"062 华庄村","deviceid":"A1YQ520803820512","most":"pm10","no2":-1,"no2IAQI":-1,"o3":-1,"o3Average":-1,"o3AverageIAQI":-1,"o3IAQI":-1,"pm10":97,"pm10IAQI":74,"pm25":46,"pm25IAQI":64,"so2":-1,"so2IAQI":-1,"time":1480910400000,"type":0},{"aQI":85,"co":-1,"coIAQI":-1,"deviceName":"037 小北哨村村委会","deviceid":"A1YQ520902E1051F","most":"pm10","no2":-1,"no2IAQI":-1,"o3":-1,"o3Average":-1,"o3AverageIAQI":-1,"o3IAQI":-1,"pm10":120,"pm10IAQI":85,"pm25":55,"pm25IAQI":75,"so2":-1,"so2IAQI":-1,"time":1480870800000,"type":0},{"aQI":62,"co":-1,"coIAQI":-1,"deviceName":"029 北郡嘉源社区","deviceid":"A1YQ5209049F051B","most":"pm10","no2":-1,"no2IAQI":-1,"o3":-1,"o3Average":-1,"o3AverageIAQI":-1,"o3IAQI":-1,"pm10":74,"pm10IAQI":62,"pm25":35,"pm25IAQI":50,"so2":-1,"so2IAQI":-1,"time":1480870800000,"type":0},{"aQI":96,"co":-1,"coIAQI":-1,"deviceName":"011 西辛峰村居委会","deviceid":"A1YQ520A02AD0512","most":"pm10","no2":-1,"no2IAQI":-1,"o3":-1,"o3Average":-1,"o3AverageIAQI":-1,"o3IAQI":-1,"pm10":141,"pm10IAQI":96,"pm25":62,"pm25IAQI":84,"so2":-1,"so2IAQI":-1,"time":1480870800000,"type":0},{"aQI":32,"co":-1,"coIAQI":-1,"deviceName":"046 桃峪口村","deviceid":"A1YQ520A02B00510","most":"pm10","no2":-1,"no2IAQI":-1,"o3":-1,"o3Average":-1,"o3AverageIAQI":-1,"o3IAQI":-1,"pm10":32,"pm10IAQI":32,"pm25":15,"pm25IAQI":21,"so2":-1,"so2IAQI":-1,"time":1480910400000,"type":0},{"aQI":105,"co":-1,"coIAQI":-1,"deviceName":"033 东营村村委会","deviceid":"A1YQ520A03E00510","most":"pm10","no2":-1,"no2IAQI":-1,"o3":-1,"o3Average":-1,"o3AverageIAQI":-1,"o3IAQI":-1,"pm10":159,"pm10IAQI":105,"pm25":73,"pm25IAQI":98,"so2":-1,"so2IAQI":-1,"time":1480870800000,"type":0},{"aQI":102,"co":-1,"coIAQI":-1,"deviceName":"015 南庄营居委会","deviceid":"A1YQ520B03000517","most":"pm10","no2":-1,"no2IAQI":-1,"o3":-1,"o3Average":-1,"o3AverageIAQI":-1,"o3IAQI":-1,"pm10":153,"pm10IAQI":102,"pm25":67,"pm25IAQI":90,"so2":-1,"so2IAQI":-1,"time":1480870800000,"type":0},{"aQI":23,"co":-1,"coIAQI":-1,"deviceName":"060 畅春园社区","deviceid":"A1YQ520B033D0515","most":"pm10","no2":-1,"no2IAQI":-1,"o3":-1,"o3Average":-1,"o3AverageIAQI":-1,"o3IAQI":-1,"pm10":23,"pm10IAQI":23,"pm25":11,"pm25IAQI":16,"so2":-1,"so2IAQI":-1,"time":1480910400000,"type":0},{"aQI":82,"co":-1,"coIAQI":-1,"deviceName":"059 肖村文化健身广场","deviceid":"A1YQ520B03E10516","most":"pm10","no2":-1,"no2IAQI":-1,"o3":-1,"o3Average":-1,"o3AverageIAQI":-1,"o3IAQI":-1,"pm10":113,"pm10IAQI":82,"pm25":53,"pm25IAQI":73,"so2":-1,"so2IAQI":-1,"time":1480910400000,"type":0},{"co":-1,"coIAQI":-1,"deviceName":"050麦庄村","deviceid":"A1YQ520C02850519","most":"-","no2":-1,"no2IAQI":-1,"o3":-1,"o3Average":-1,"o3AverageIAQI":-1,"o3IAQI":-1,"pm10":-1,"pm10IAQI":-1,"pm25":-1,"pm25IAQI":-1,"so2":-1,"so2IAQI":-1,"time":1480910400000,"type":0},{"aQI":49,"co":-1,"coIAQI":-1,"deviceName":"009 香堂村健身广场","deviceid":"A1YQ520C02C00512","most":"pm10","no2":-1,"no2IAQI":-1,"o3":-1,"o3Average":-1,"o3AverageIAQI":-1,"o3IAQI":-1,"pm10":49,"pm10IAQI":49,"pm25":22,"pm25IAQI":31,"so2":-1,"so2IAQI":-1,"time":1480870800000,"type":0},{"aQI":76,"co":-1,"coIAQI":-1,"deviceName":"038 北京风景 社区","deviceid":"A1YQ520C03280513","most":"pm10","no2":-1,"no2IAQI":-1,"o3":-1,"o3Average":-1,"o3AverageIAQI":-1,"o3IAQI":-1,"pm10":101,"pm10IAQI":76,"pm25":48,"pm25IAQI":66,"so2":-1,"so2IAQI":-1,"time":1480870800000,"type":0},{"aQI":79,"co":-1,"coIAQI":-1,"deviceName":"018西崔村","deviceid":"A1YQ520C0390051C","most":"pm10","no2":-1,"no2IAQI":-1,"o3":-1,"o3Average":-1,"o3AverageIAQI":-1,"o3IAQI":-1,"pm10":108,"pm10IAQI":79,"pm25":52,"pm25IAQI":71,"so2":-1,"so2IAQI":-1,"time":1480870800000,"type":0},{"aQI":78,"co":-1,"coIAQI":-1,"deviceName":"041 官高村","deviceid":"A1YQ520C03D00512","most":"pm10","no2":-1,"no2IAQI":-1,"o3":-1,"o3Average":-1,"o3AverageIAQI":-1,"o3IAQI":-1,"pm10":105,"pm10IAQI":78,"pm25":53,"pm25IAQI":73,"so2":-1,"so2IAQI":-1,"time":1480870800000,"type":0},{"aQI":97,"co":-1,"coIAQI":-1,"deviceName":"014 大辛峰村 垃圾站","deviceid":"A1YQ520C0427051E","most":"pm10","no2":-1,"no2IAQI":-1,"o3":-1,"o3Average":-1,"o3AverageIAQI":-1,"o3IAQI":-1,"pm10":144,"pm10IAQI":97,"pm25":68,"pm25IAQI":91,"so2":-1,"so2IAQI":-1,"time":1480870800000,"type":0},{"aQI":64,"co":-1,"coIAQI":-1,"deviceName":"051下苑村","deviceid":"A1YQ520D01A9051B","most":"pm10","no2":-1,"no2IAQI":-1,"o3":-1,"o3Average":-1,"o3AverageIAQI":-1,"o3IAQI":-1,"pm10":78,"pm10IAQI":64,"pm25":37,"pm25IAQI":53,"so2":-1,"so2IAQI":-1,"time":1480910400000,"type":0},{"aQI":87,"co":-1,"coIAQI":-1,"deviceName":"026 姜屯村村委会","deviceid":"A1YQ520D02600515","most":"pm10","no2":-1,"no2IAQI":-1,"o3":-1,"o3Average":-1,"o3AverageIAQI":-1,"o3IAQI":-1,"pm10":123,"pm10IAQI":87,"pm25":55,"pm25IAQI":75,"so2":-1,"so2IAQI":-1,"time":1480870800000,"type":0},{"aQI":71,"co":-1,"coIAQI":-1,"deviceName":"031 国惠村社区","deviceid":"A1YQ520D02770515","most":"pm10","no2":-1,"no2IAQI":-1,"o3":-1,"o3Average":-1,"o3AverageIAQI":-1,"o3IAQI":-1,"pm10":91,"pm10IAQI":71,"pm25":41,"pm25IAQI":58,"so2":-1,"so2IAQI":-1,"time":1480870800000,"type":0},{"co":-1,"coIAQI":-1,"deviceName":"035 宜山居社区","deviceid":"A1YQ520D0293051F","most":"-","no2":-1,"no2IAQI":-1,"o3":-1,"o3Average":-1,"o3AverageIAQI":-1,"o3IAQI":-1,"pm10":-1,"pm10IAQI":-1,"pm25":-1,"pm25IAQI":-1,"so2":-1,"so2IAQI":-1,"time":1480870800000,"type":0},{"aQI":51,"co":-1,"coIAQI":-1,"deviceName":"049 新西城村","deviceid":"A1YQ520D02C10514","most":"pm10","no2":-1,"no2IAQI":-1,"o3":-1,"o3Average":-1,"o3AverageIAQI":-1,"o3IAQI":-1,"pm10":51,"pm10IAQI":51,"pm25":26,"pm25IAQI":37,"so2":-1,"so2IAQI":-1,"time":1480910400000,"type":0},{"aQI":90,"co":-1,"coIAQI":-1,"deviceName":"013 大辛峰村居委会","deviceid":"A1YQ520D02E40511","most":"pm10","no2":-1,"no2IAQI":-1,"o3":-1,"o3Average":-1,"o3AverageIAQI":-1,"o3IAQI":-1,"pm10":129,"pm10IAQI":90,"pm25":64,"pm25IAQI":86,"so2":-1,"so2IAQI":-1,"time":1480870800000,"type":0},{"aQI":64,"co":-1,"coIAQI":-1,"deviceName":"030 长滩社区","deviceid":"A1YQ520D03750510","most":"pm10","no2":-1,"no2IAQI":-1,"o3":-1,"o3Average":-1,"o3AverageIAQI":-1,"o3IAQI":-1,"pm10":78,"pm10IAQI":64,"pm25":38,"pm25IAQI":54,"so2":-1,"so2IAQI":-1,"time":1480870800000,"type":0},{"aQI":9,"co":-1,"coIAQI":-1,"deviceName":"054 上西市村公共厕所","deviceid":"A1YQ520D04080518","most":"pm10","no2":-1,"no2IAQI":-1,"o3":-1,"o3Average":-1,"o3AverageIAQI":-1,"o3IAQI":-1,"pm10":9,"pm10IAQI":9,"pm25":5,"pm25IAQI":7,"so2":-1,"so2IAQI":-1,"time":1480910400000,"type":0},{"aQI":33,"co":-1,"coIAQI":-1,"deviceName":"052暴泉峪村","deviceid":"A1YQ520E0274051F","most":"pm10","no2":-1,"no2IAQI":-1,"o3":-1,"o3Average":-1,"o3AverageIAQI":-1,"o3IAQI":-1,"pm10":33,"pm10IAQI":33,"pm25":16,"pm25IAQI":23,"so2":-1,"so2IAQI":-1,"time":1480910400000,"type":0},{"aQI":78,"co":-1,"coIAQI":-1,"deviceName":"024 泰陵园村","deviceid":"A1YQ520E02C2051E","most":"pm10","no2":-1,"no2IAQI":-1,"o3":-1,"o3Average":-1,"o3AverageIAQI":-1,"o3IAQI":-1,"pm10":105,"pm10IAQI":78,"pm25":49,"pm25IAQI":68,"so2":-1,"so2IAQI":-1,"time":1480870800000,"type":0},{"aQI":67,"co":-1,"coIAQI":-1,"deviceName":"055 沙陀村","deviceid":"A1YQ520E0361051A","most":"pm10","no2":-1,"no2IAQI":-1,"o3":-1,"o3Average":-1,"o3AverageIAQI":-1,"o3IAQI":-1,"pm10":84,"pm10IAQI":67,"pm25":42,"pm25IAQI":59,"so2":-1,"so2IAQI":-1,"time":1480910400000,"type":0},{"aQI":66,"co":-1,"coIAQI":-1,"deviceName":"020 八家村村委会","deviceid":"A1YQ520F01F90512","most":"pm10","no2":-1,"no2IAQI":-1,"o3":-1,"o3Average":-1,"o3AverageIAQI":-1,"o3IAQI":-1,"pm10":81,"pm10IAQI":66,"pm25":38,"pm25IAQI":54,"so2":-1,"so2IAQI":-1,"time":1480870800000,"type":0},{"aQI":54,"co":-1,"coIAQI":-1,"deviceName":"047 辛庄村","deviceid":"A1YQ520F023C051E","most":"pm10","no2":-1,"no2IAQI":-1,"o3":-1,"o3Average":-1,"o3AverageIAQI":-1,"o3IAQI":-1,"pm10":57,"pm10IAQI":54,"pm25":29,"pm25IAQI":41,"so2":-1,"so2IAQI":-1,"time":1480910400000,"type":0},{"aQI":87,"co":-1,"coIAQI":-1,"deviceName":"016南庄村居委会","deviceid":"A1YQ520F02B40518","most":"pm10","no2":-1,"no2IAQI":-1,"o3":-1,"o3Average":-1,"o3AverageIAQI":-1,"o3IAQI":-1,"pm10":124,"pm10IAQI":87,"pm25":57,"pm25IAQI":78,"so2":-1,"so2IAQI":-1,"time":1480870800000,"type":0},{"aQI":91,"co":-1,"coIAQI":-1,"deviceName":"039 北邵洼村","deviceid":"A1YQ520F02C30518","most":"pm10","no2":-1,"no2IAQI":-1,"o3":-1,"o3Average":-1,"o3AverageIAQI":-1,"o3IAQI":-1,"pm10":132,"pm10IAQI":91,"pm25":60,"pm25IAQI":81,"so2":-1,"so2IAQI":-1,"time":1480870800000,"type":0},{"aQI":90,"co":-1,"coIAQI":-1,"deviceName":"017 东崔村居委会","deviceid":"A1YQ520F035D051E","most":"pm10","no2":-1,"no2IAQI":-1,"o3":-1,"o3Average":-1,"o3AverageIAQI":-1,"o3IAQI":-1,"pm10":130,"pm10IAQI":90,"pm25":60,"pm25IAQI":81,"so2":-1,"so2IAQI":-1,"time":1480870800000,"type":0},{"co":-1,"coIAQI":-1,"deviceName":"008香堂村社区服务站","deviceid":"A1YQ520F0360051C","most":"-","no2":-1,"no2IAQI":-1,"o3":-1,"o3Average":-1,"o3AverageIAQI":-1,"o3IAQI":-1,"pm10":-1,"pm10IAQI":-1,"pm25":-1,"pm25IAQI":-1,"so2":-1,"so2IAQI":-1,"time":1480870800000,"type":0},{"aQI":104,"co":4,"coIAQI":100,"deviceName":"草莓园站","deviceid":"TSBI813103733914","most":"pm10","no2":31.333334,"no2IAQI":39,"o3":6,"o3Average":6,"o3AverageIAQI":3,"o3IAQI":2,"pm10":157,"pm10IAQI":104,"pm25":74,"pm25IAQI":99,"so2":24.916666,"so2IAQI":25,"time":1480870800000,"type":0},{"aQI":101,"co":4.185,"coIAQI":101,"deviceName":"环保局站","deviceid":"TSBI813143435523","most":"co","no2":0,"no2IAQI":0,"o3":50,"o3Average":44.5,"o3AverageIAQI":22,"o3IAQI":16,"pm10":64,"pm10IAQI":57,"pm25":39,"pm25IAQI":55,"so2":17.958334,"so2IAQI":18,"time":1480870800000,"type":0},{"aQI":4,"co":0,"coIAQI":0,"deviceName":"昌平阳坊站","deviceid":"TSBI813152432152","most":"so2","no2":2.875,"no2IAQI":4,"o3":0,"o3Average":0,"o3AverageIAQI":0,"o3IAQI":0,"pm10":0,"pm10IAQI":0,"pm25":0,"pm25IAQI":0,"so2":3.6666667,"so2IAQI":4,"time":1480870800000,"type":0}]
     * msg : 请求成功！
     */

    private int ret;
    private String time;
    private String msg;
    private List<DataBean> data;

    public int getRet() {
        return ret;
    }

    public void setRet(int ret) {
        this.ret = ret;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * aQI : 65
         * co : -1
         * coIAQI : -1
         * deviceName : 044兴寿村委会
         * deviceid : A1YQ5200026B0515
         * most : pm10
         * no2 : -1
         * no2IAQI : -1
         * o3 : -1
         * o3Average : -1
         * o3AverageIAQI : -1
         * o3IAQI : -1
         * pm10 : 80
         * pm10IAQI : 65
         * pm25 : 35
         * pm25IAQI : 50
         * so2 : -1
         * so2IAQI : -1
         * time : 1480870800000
         * type : 0
         */

        private double aQI;
        private double co;
        private double coIAQI;
        private String deviceName;
        private String deviceid;
        private String most;
        private double no2;
        private double no2IAQI;
        private double o3;
        private double o3Average;
        private double o3AverageIAQI;
        private double o3IAQI;
        private double pm10;
        private double pm10IAQI;
        private double pm25;
        private double pm25IAQI;
        private double so2;
        private double so2IAQI;
        private long time;
        private double type;

        public double getAQI() {
            return aQI;
        }

        public void setAQI(int aQI) {
            this.aQI = aQI;
        }

        public double getCo() {
            return co;
        }

        public void setCo(int co) {
            this.co = co;
        }

        public double getCoIAQI() {
            return coIAQI;
        }

        public void setCoIAQI(int coIAQI) {
            this.coIAQI = coIAQI;
        }

        public String getDeviceName() {
            return deviceName;
        }

        public void setDeviceName(String deviceName) {
            this.deviceName = deviceName;
        }

        public String getDeviceid() {
            return deviceid;
        }

        public void setDeviceid(String deviceid) {
            this.deviceid = deviceid;
        }

        public String getMost() {
            return most;
        }

        public void setMost(String most) {
            this.most = most;
        }

        public double getNo2() {
            return no2;
        }

        public void setNo2(int no2) {
            this.no2 = no2;
        }

        public double getNo2IAQI() {
            return no2IAQI;
        }

        public void setNo2IAQI(int no2IAQI) {
            this.no2IAQI = no2IAQI;
        }

        public double getO3() {
            return o3;
        }

        public void setO3(int o3) {
            this.o3 = o3;
        }

        public double getO3Average() {
            return o3Average;
        }

        public void setO3Average(int o3Average) {
            this.o3Average = o3Average;
        }

        public double getO3AverageIAQI() {
            return o3AverageIAQI;
        }

        public void setO3AverageIAQI(int o3AverageIAQI) {
            this.o3AverageIAQI = o3AverageIAQI;
        }

        public double getO3IAQI() {
            return o3IAQI;
        }

        public void setO3IAQI(int o3IAQI) {
            this.o3IAQI = o3IAQI;
        }

        public double getPm10() {
            return pm10;
        }

        public void setPm10(int pm10) {
            this.pm10 = pm10;
        }

        public double getPm10IAQI() {
            return pm10IAQI;
        }

        public void setPm10IAQI(int pm10IAQI) {
            this.pm10IAQI = pm10IAQI;
        }

        public double getPm25() {
            return pm25;
        }

        public void setPm25(int pm25) {
            this.pm25 = pm25;
        }

        public double getPm25IAQI() {
            return pm25IAQI;
        }

        public void setPm25IAQI(int pm25IAQI) {
            this.pm25IAQI = pm25IAQI;
        }

        public double getSo2() {
            return so2;
        }

        public void setSo2(int so2) {
            this.so2 = so2;
        }

        public double getSo2IAQI() {
            return so2IAQI;
        }

        public void setSo2IAQI(int so2IAQI) {
            this.so2IAQI = so2IAQI;
        }

        public long getTime() {
            return time;
        }

        public void setTime(long time) {
            this.time = time;
        }

        public double getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }
    }
}
