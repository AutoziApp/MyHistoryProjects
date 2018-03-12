package com.mapuni.administrator.utils;

/**
 * Created by Administrator on 2017/8/29.
 */

public class TypeUtil {
    
    
    public static boolean isImageType(String name){
        if (    "jpg".equals(name)
                ||"png".equals(name)
                ||"jpeg".equals(name)
                ||"bmp".equals(name)
                ||"JPG".equals(name)
                ||"PNG".equals(name)
                ||"JPEG".equals(name)
                ||"BMP".equals(name)){
            return true;
        }
        return false;
    }
    public static boolean isWordType(String name){
        if (    "pptx".equals(name)
                ||"ppt".equals(name)
                ||"pdf".equals(name)
                ||"doc".equals(name)
                ||"docx".equals(name)
                ||"xls".equals(name)
                ||"xlsx".equals(name)
                ||"txt".equals(name) 
                ||"PPTX".equals(name)
                ||"PPT".equals(name)
                ||"PDF".equals(name)
                ||"DOC".equals(name)
                ||"DOCX".equals(name)
                ||"XLS".equals(name)
                ||"XLSX".equals(name)
                ||"TXT".equals(name)){
            return true;
        }
        return false;
    }

    public static boolean isMP3Type(String name){
        if (    "mp3".equals(name)
                ||"MP3".equals(name)){
            return true;
        }
        return false;
    }

    public static boolean isVideoType(String name) {
        if ("rmvb".equals(name)
                ||"avi".equals(name)
                ||"mkv".equals(name)
                ||"3GP".equals(name)
                ||"wmv".equals(name)){
            return true;
        }
        return false;
    }
}
