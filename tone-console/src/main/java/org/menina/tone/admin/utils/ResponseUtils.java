package org.menina.tone.admin.utils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by menina on 2017/9/23.
 */
public class ResponseUtils {

    public static Map<String, Object> successResult(Object result) {
        Map<String, Object> response = new HashMap<>();
        response.put("code", 0);
        response.put("msg", "");
        response.put("result", result);
        return response;
    }

    public static Map<String, Object> badResult(String message) {
        Map<String, Object> response = new HashMap<>();
        response.put("code", 100);
        response.put("msg", message);
        response.put("result", "");
        return response;
    }
}
