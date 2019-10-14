package utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class JsonUtil {
    public static Map<String, List<String>> parseHeaders(JSONObject header) throws JSONException {
        Iterator<String> iterable = header.keys();
        Map<String, List<String>> map = new LinkedHashMap<>();
        while (iterable.hasNext()) {
            String key = iterable.next();
            JSONArray array = header.getJSONArray(key);
            List<String> list = new ArrayList<>();
            for (int i = 0; i < array.length(); i++) {
                list.add(array.getString(i));
            }
            map.put(key, list);
        }
        return map;
    }

    public static Map<String, String> parseFields(JSONObject field) throws JSONException {
        Map<String, String> map = new LinkedHashMap<>();
        if (field == null) return map;
        Iterator<String> iterable = field.keys();
        while (iterable.hasNext()) {
            String key = iterable.next();
            String value = field.getString(key);
            map.put(key, value);
        }
        return map;
    }
}
