package com.csf.csflibrary.Tools;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * @author jaily.zhang
 * @version V1.3.1
 * @Description Json解析工具类
 * @date 2014-7-22 上午11:20:46
 */
public class JsonTools {

    /**
     * @param object 对象
     * @return json 字符串
     * @Description 对象转为Json
     * @author jaily.zhang
     */
    public static String objectToJsonString(Object object) {
        Gson gson = new Gson();
        return gson.toJson(object);
    }


    /**
     * @param <T>
     * @param string json 字符串
     * @param T      转为对象
     * @return 对象
     * @Description Json字符串转为对象
     * @author jaily.zhang
     */
    public static <T> T jsonStringToObject(String string, Class<T> T) {
        Gson gson = new Gson();
        return gson.fromJson(string, T);
    }

    /**
     * @param <T>
     * @param <T>
     * @param string json 字符串
     * @param T      转为对象
     * @return 对象
     * @Description Json字符串转为LinkList 对象
     * @author jaily.zhang
     */
    public static <T> LinkedList<T> jsonStringToLinkList(String string, Class<T> T) {
        try {
            Gson gson = new Gson();
            LinkedList<T> lst = new LinkedList<>();
            JsonArray array = new JsonParser().parse(string).getAsJsonArray();
            for (final JsonElement element : array) {
                lst.add(gson.fromJson(element, T));
            }
            return lst;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * @param <T>
     * @param <T>
     * @param string json 字符串
     * @param T      转为对象
     * @return 对象
     * @Description Json字符串转为Map 对象
     * @author jaily.zhang
     */
    public static <T> Map<String, List<T>> jsonStringToMap(String string, Class<T> T) {
        Gson gson = new Gson();
        Map<String, List<T>> map = gson.fromJson(string, new TypeToken<Map<String, List<T>>>() {
        }.getType());
        return map;

    }


    /**
     * @param <T>
     * @param <T>
     * @param string json 字符串
     * @param T      转为对象
     * @return 对象
     * @Description Json字符串转为Map 对象
     * @author jaily.zhang
     */
    public static <T> Map<String, T> jsonStringToMapObject(String string, Class<T> T) {
        Gson gson = new Gson();
        Map<String, T> map = gson.fromJson(string, new TypeToken<Map<String, T>>() {
        }.getType());
        return map;

    }

    /**
     * @param <T>
     * @param <T>
     * @param string json 字符串
     * @param T      转为对象
     * @return 对象
     * @Description Json字符串转为List 对象
     * @author jaily.zhang
     */
    public static <T> List<T> jsonStringToList(String string, Class<T> T) {

        try {
            Gson gson = new Gson();
            List<T> lst = new ArrayList<>();
            JsonArray array = new JsonParser().parse(string).getAsJsonArray();
            for (final JsonElement element : array) {
                lst.add(gson.fromJson(element, T));
            }
            return lst;
        } catch (Exception e) {
            return null;
        }

    }

}
