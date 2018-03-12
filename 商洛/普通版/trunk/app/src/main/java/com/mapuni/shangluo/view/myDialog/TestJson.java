package com.mapuni.shangluo.view.myDialog;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by yang on 2017/12/14.
 */

public class TestJson {

    public static String json="[{\"id\":\"9c4a28301d4f4fe681b672282e3442b7\",\"icon\":null,\"text\":\"庾岭镇\",\"type\":2,\"state\":\"closed\",\"leaf\":false,\"level\":3},{\"id\":\"f80b9ed085e34b47867259cc7cf623aa\",\"icon\":null,\"text\":\"商镇\",\"type\":2,\"state\":\"closed\",\"leaf\":false,\"level\":3},{\"id\":\"c6f29e9db33147e1b546fb59b5f21ac6\",\"icon\":null,\"text\":\"寺坪镇\",\"type\":2,\"state\":\"closed\",\"leaf\":false,\"level\":3},{\"id\":\"8a39dacbf06c4d79a5b4f5bbda1e15cf\",\"icon\":null,\"text\":\"蔡川镇\",\"type\":2,\"state\":\"closed\",\"leaf\":false,\"level\":3},{\"id\":\"253579fcb7904a89aa2f1fe5d1354b72\",\"icon\":null,\"text\":\"竹林关镇\",\"type\":2,\"state\":\"closed\",\"leaf\":false,\"level\":3},{\"id\":\"3ff4aef39bf54aa788854ab3fbe24ca6\",\"icon\":null,\"text\":\"棣花镇\",\"type\":2,\"state\":\"closed\",\"leaf\":false,\"level\":3},{\"id\":\"bef3f7a243524fc792a342d735e729fc\",\"icon\":null,\"text\":\"铁峪铺镇\",\"type\":2,\"state\":\"closed\",\"leaf\":false,\"level\":3},{\"id\":\"76e6f11c9eda4602a20405a33ef41444\",\"icon\":null,\"text\":\"花瓶子镇\",\"type\":2,\"state\":\"closed\",\"leaf\":false,\"level\":3},{\"id\":\"32e55cdc94ef4168a6c7596f7ce00b84\",\"icon\":null,\"text\":\"土门镇\",\"type\":2,\"state\":\"closed\",\"leaf\":false,\"level\":3},{\"id\":\"505f819750c14721803d948a6ed5d105\",\"icon\":null,\"text\":\"武关镇\",\"type\":2,\"state\":\"closed\",\"leaf\":false,\"level\":3},{\"id\":\"aeb76ebe6b814d9f9a8b5c9d96a5fb0d\",\"icon\":null,\"text\":\"峦庄镇\",\"type\":2,\"state\":\"closed\",\"leaf\":false,\"level\":3},{\"id\":\"6834d579c5504509abf12b58c2adabd1\",\"icon\":null,\"text\":\"龙驹寨街道办事处\",\"type\":2,\"state\":\"closed\",\"leaf\":false,\"level\":3}]";

    List<MenuData> list;

    public static List<MenuData> getList(String str){
        List<MenuData> list=new ArrayList<>();
        Type type = new TypeToken<ArrayList<PatrolObject>>() {}.getType();
        List<PatrolObject> objects=new Gson().fromJson(str,type);
        for (int i=0;i<objects.size();i++){
            MenuData menuData=new MenuData(i,objects.get(i).getText(),0,objects.get(i).getId(),objects.get(i).getType());
            list.add(menuData);
        }
        return list;
    }
}
