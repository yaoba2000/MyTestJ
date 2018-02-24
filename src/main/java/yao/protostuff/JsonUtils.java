package yao.protostuff;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public final class JsonUtils {
    private static final Gson gson = new GsonBuilder().create();

    public static <T> String toJsonStr(T obj) {
        String json;
        try {
            json = gson.toJson(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return json;
    }

    public static <T> T toBeanObj(String json, Class<T> type) {
        T pojo;
        try {
            pojo = gson.fromJson(json, type);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return pojo;
    }
}
