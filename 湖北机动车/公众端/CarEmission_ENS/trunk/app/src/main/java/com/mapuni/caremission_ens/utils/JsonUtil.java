package com.mapuni.caremission_ens.utils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by: K on 2015/11/24.
 * Description:
 */
public class JsonUtil {
    private static Gson gson=null;
    static{
        if(gson==null){
            gson=new Gson();
        }
    }

    private JsonUtil(){}

    /**
     * 将对象转换成json格式
     * @param ts
     * @return
     */
    public static String objectToJson(Object ts){
        String jsonStr=null;
        if(gson!=null){
            jsonStr=gson.toJson(ts);
        }
        return jsonStr;
    }

    /**
     * 将对象转换成json格式(并自定义日期格式)
     * @param ts 对象
     * @return json串
     */
    public static String objectToJsonDateSerializer(Object ts,final String dateformat){
        String jsonStr=null;
        gson=new GsonBuilder().registerTypeHierarchyAdapter(Date.class, new JsonSerializer<Date>() {
            public JsonElement serialize(Date src, Type typeOfSrc,
                                         JsonSerializationContext context) {
                SimpleDateFormat format = new SimpleDateFormat(dateformat);
                return new JsonPrimitive(format.format(src));
            }
        }).setDateFormat(dateformat).create();
        if(gson!=null){
            jsonStr=gson.toJson(ts);
        }
        return jsonStr;
    }
    /**
     * 将json格式转换成list对象
     * @param jsonStr
     * @return
     */
    public static List<?> jsonToList(String jsonStr){
        List<?> objList=null;
        if(gson!=null){
            Type type=new com.google.gson.reflect.TypeToken<List<?>>(){}.getType();
            objList=gson.fromJson(jsonStr, type);
        }
        return objList;

    }
    public static <T> ArrayList<T> fromJsonList(String json, Class<T> cls) {
        ArrayList<T> mList = new ArrayList<T>();
        JsonArray array = new JsonParser().parse(json).getAsJsonArray();
        for(final JsonElement elem : array){
            mList.add(gson.fromJson(elem, cls));
        }
        return mList;
    }



    /**
     * 将json格式转换成List<HashMap<String,Object>>对象
     * @param jsonStr json串
     * @return ArrayList<HashMap<String, Object>>集合
     */
    public static ArrayList<HashMap<String, Object>> jsonToListMap(String jsonStr){
        try{
            //jsonStr = jsonStr.replaceAll("\"", "\\\\\"");
            jsonStr = jsonStr.replaceAll("\"\\\\\"", "\"\"");
            ArrayList<HashMap<String, Object>> objList=null;
            if(gson!=null){
                Type type=new com.google.gson.reflect.TypeToken<ArrayList<HashMap<String, Object>>>(){}.getType();
                objList=gson.fromJson(jsonStr, type);
            }

            return objList;
        }catch (Exception e){
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    /**
     * arrayList<Map> 转化为gson
     * @param data
     * @return
     */
    public static String parseMapToJson(ArrayList<HashMap<String,String>> data){
        try {
            Type type=new com.google.gson.reflect.TypeToken<ArrayList<HashMap<String, String>>>(){}.getType();
            return gson.toJson(data);
        }catch (Exception e){
            return "";
        }
    }

    /**
     * 将json格式转换成map对象
     * @param jsonStr json串
     * @return 对象
     */
    public static Map<?,?> jsonToMap(String jsonStr){
        Map<?,?> objMap=null;
        if(gson!=null){
            Type type=new com.google.gson.reflect.TypeToken<Map<?,?>>(){}.getType();
            objMap=gson.fromJson(jsonStr, type);
        }
        return objMap;
    }

    /**
     * 将json转换成bean对象
     * @param jsonStr
     * @return
     */
    public static Object  jsonToBean(String jsonStr,Class<?> cl){
        Object obj=null;
        if(gson!=null){
            obj=gson.fromJson(jsonStr, cl);
        }
        return obj;
    }
    /**
     * 将json转换成bean对象
     * @param jsonStr
     * @param cl
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <T> T  jsonToBeanDateSerializer(String jsonStr,Class<T> cl,final String pattern){
        Object obj=null;
        gson=new GsonBuilder().registerTypeAdapter(Date.class, new JsonDeserializer<Date>() {
            public Date deserialize(JsonElement json, Type typeOfT,
                                    JsonDeserializationContext context)
                    throws JsonParseException {
                SimpleDateFormat format=new SimpleDateFormat(pattern);
                String dateStr=json.getAsString();
                try {
                    return format.parse(dateStr);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                return null;
            }
        }).setDateFormat(pattern).create();
        if(gson!=null){
            obj=gson.fromJson(jsonStr, cl);
        }
        return (T)obj;
    }
    /**
     * 根据
     * @param jsonStr
     * @param key
     * @return
     */
    public static Object  getJsonValue(String jsonStr,String key){
        Object rulsObj=null;
        Map<?,?> rulsMap=jsonToMap(jsonStr);
        if(rulsMap!=null&&rulsMap.size()>0){
            rulsObj=rulsMap.get(key);
        }
        return rulsObj;
    }
}
