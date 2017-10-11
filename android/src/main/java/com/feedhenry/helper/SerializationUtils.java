package com.feedhenry.helper;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.ReadableMapKeySetIterator;
import com.facebook.react.bridge.WritableArray;
import com.facebook.react.bridge.WritableMap;
import com.feedhenry.sdk.FHResponse;

import org.json.fh.JSONArray;
import org.json.fh.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public abstract class SerializationUtils {
    private final static JSONObject fhResponseToJSON(FHResponse response) {
        if (response == null) {
            throw new IllegalArgumentException("Cannot convert `null` to JSONObject");
        }

        String raw = response.getRawResponse();
        if (raw.startsWith("[")) {
            return new JSONArray(raw);
        } else {
            return new JSONObject(raw);
        }
    }

    private final static WritableArray jsonToArray(JSONArray json) {
        WritableArray result = Arguments.createArray();

        for (int i = 0; i < json.length(); i++) {
            Object value = json.get(i);

            if (value == null) {
                result.pushNull();
            } else if (value instanceof String) {
                result.pushString((String) value);
            } else if (value instanceof Integer) {
                result.pushInt((Integer) value);
            } else if (value instanceof Double) {
                result.pushDouble((Double) value);
            } else if (value instanceof Boolean) {
                result.pushBoolean((Boolean) value);
            } else if (value instanceof JSONArray) {
                result.pushArray(jsonToArray((JSONArray) value));
            } else if (value instanceof JSONObject) {
                result.pushMap(jsonToMap((JSONObject) value));
            }
        }

        return result;
    }

    private final static WritableMap jsonToMap(JSONObject json) {
        WritableMap result = Arguments.createMap();

        Iterator<String> keys = json.keys();
        while (keys.hasNext()) {
            String key = keys.next();
            Object value = json.get(key);

            if (value == null) {
                result.putNull(key);
            } else if (value instanceof JSONObject) {
                result.putMap(key, jsonToMap((JSONObject) value));
            } else if (value instanceof JSONArray) {
                result.putArray(key, jsonToArray((JSONArray) value));
            } else if (value instanceof String) {
                result.putString(key, (String) value);
            } else if (value instanceof Integer) {
                result.putInt(key, (Integer) value);
            } else if (value instanceof Double) {
                result.putDouble(key, (Double) value);
            } else if (value instanceof Boolean) {
                result.putBoolean(key, (Boolean) value);
            }
        }

        return result;
    }

    public static final WritableMap fhResponseToMap(FHResponse response) {
        JSONObject json = fhResponseToJSON(response);
        return jsonToMap(json);
    }
}