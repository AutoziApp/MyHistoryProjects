package com.mapuni.caremission_ens.bean;

import java.io.Serializable;

/**
 * Created by yawei on 2017/3/31.
 */

public class FileBean implements Serializable{

    /**
     * UPLOADTIME : 2016-07-06 10:20:38
     * FILEFMT : PDF
     * FILENAME : 标准规范\HJ T289-2006 汽油车双怠速法排气污染物测量设备技术要求.pdf
     * FILEDOWNLOADPATH : http://192.168.15.96:8855/znzd/file/file-download!download.action?fileName=E:\机动车\标准规范\HJ T289-2006 汽油车双怠速法排气污染物测量设备技术要求.pdf&saveName=标准规范\HJ T289-2006 汽油车双怠速法排气污染物测量设备技术要求.pdffileName=E:\机动车\标准规范\HJ T289-2006 汽油车双怠速法排气污染物测量设备技术要求.pdf&saveName=标准规范\HJ T289-2006 汽油车双怠速法排气污染物测量设备技术要求.pdf
     */

    private String UPLOADTIME;
    private String FILEFMT;
    private String FILENAME;
    private String FILEDOWNLOADPATH;
    private String LENGTH;

    public String getLENGTH() {
        return LENGTH;
    }

    public void setLENGTH(String LENGTH) {
        this.LENGTH = LENGTH;
    }

    public String getLocalSize() {
        return LocalSize;
    }

    public void setLocalSize(String localSize) {
        LocalSize = localSize;
    }

    private String LocalSize;

    public String getUPLOADTIME() {
        return UPLOADTIME;
    }

    public void setUPLOADTIME(String UPLOADTIME) {
        this.UPLOADTIME = UPLOADTIME;
    }

    public String getFILEFMT() {
        return FILEFMT;
    }

    public void setFILEFMT(String FILEFMT) {
        this.FILEFMT = FILEFMT;
    }

    public String getFILENAME() {
        return FILENAME;
    }

    public void setFILENAME(String FILENAME) {
        this.FILENAME = FILENAME;
    }

    public String getFILEDOWNLOADPATH() {
        return FILEDOWNLOADPATH;
    }

    public void setFILEDOWNLOADPATH(String FILEDOWNLOADPATH) {
        this.FILEDOWNLOADPATH = FILEDOWNLOADPATH;
    }
}
