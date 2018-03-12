package com.mapuni.administrator.bean;

/**
 * Created by 15225 on 2017/8/19.
 */

public class PatrolObject {


    /**
     * uuid : 4028b88e5e0e911d015e0e9332260000
     * name : 龙岩路
     * supervisionTypeName : 各类道路
     * supervisionTypeUuid : 4028f8fb5de36841015de3f2ee35000b
     * address : null
     * createTime : 2017-08-23 18:11:21
     * contacts : null
     * telephone : null
     * longitude : null
     * latitude : null
     * legalPerson : null
     */

    private String uuid;
    private String name;
    private String supervisionTypeName;
    private String supervisionTypeUuid;
    private String address;
    private String createTime;
    private String contacts;
    private String telephone;
    private String longitude;
    private String latitude;
    private String legalPerson;

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSupervisionTypeName() {
        return supervisionTypeName;
    }

    public void setSupervisionTypeName(String supervisionTypeName) {
        this.supervisionTypeName = supervisionTypeName;
    }

    public String getSupervisionTypeUuid() {
        return supervisionTypeUuid;
    }

    public void setSupervisionTypeUuid(String supervisionTypeUuid) {
        this.supervisionTypeUuid = supervisionTypeUuid;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getContacts() {
        return contacts;
    }

    public void setContacts(String contacts) {
        this.contacts = contacts;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLegalPerson() {
        return legalPerson;
    }

    public void setLegalPerson(String legalPerson) {
        this.legalPerson = legalPerson;
    }
}
