package com.by.lizhiyoupin.app.common.utils.storage;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.by.lizhiyoupin.app.common.utils.GsonUtil;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public final class PrefsProxy {
    private SharedPreferences preferences;

    public PrefsProxy(SharedPreferences preferences) {
        this.preferences = preferences;
    }

    public String getString(String key, String defValue) {
        return preferences.getString(key, defValue);
    }

    public String getString(String key) {
        return preferences.getString(key, "");
    }

    public Set<String> getStringSet(String key, Set<String> defValues) {
        return preferences.getStringSet(key, defValues);
    }

    public Set<String> getStringSet(String key) {
        return preferences.getStringSet(key, null);
    }

    public int getInt(String key, int defValue) {
        return preferences.getInt(key, defValue);
    }

    public long getLong(String key, long defValue) {
        return preferences.getLong(key, defValue);
    }

    public float getFloat(String key, float defValue) {
        return preferences.getFloat(key, defValue);
    }

    public boolean getBoolean(String key, boolean defValue) {
        return preferences.getBoolean(key, defValue);
    }

    public boolean getIBoolean(String key, boolean defValue) {
        return getInt(key, defValue ? 1 : 0) == 1;
    }

    public boolean contains(String key) {
        return preferences.contains(key);
    }

    public Editor edit() {
        return preferences.edit();
    }

    public void putString(Map<String, Object> map) {
        if (map == null || map.isEmpty()) {
            return;
        }
        Editor editor = edit();
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            editor.putString(entry.getKey(), entry.getValue().toString());
        }
        editor.apply();
    }

    public void putString(String key, String value) {
        edit().putString(key, value).apply();
    }

    public void putStringSet(String key, Set<String> values) {
        edit().putStringSet(key, values).apply();
    }

    public void putInt(String key, int value) {
        edit().putInt(key, value).apply();
    }

    public void putLong(String key, long value) {
        edit().putLong(key, value).apply();
    }

    public void putFloat(String key, float value) {
        edit().putFloat(key, value).apply();
    }

    public void putBoolean(String key, boolean value) {
        edit().putBoolean(key, value).apply();
    }

    public void putIBoolean(String key, boolean value) {
        putInt(key, value ? 1 : 0);
    }

    /**
     * map值如果是null在用getHashMapData得到数据时会忽略改键值对
     *
     * @param key
     * @param map
     * @param <K>
     * @param <V>
     * @return
     */
    public <K, V> boolean putHashMapData(String key, Map<K, V> map) {
        boolean result;
        Editor editor = preferences.edit();
        try {
            Gson gson = new Gson();
            String json = gson.toJson(map);
            editor.putString(key, json);
            result = true;
        } catch (Exception e) {
            result = false;
            e.printStackTrace();
        }
        editor.commit();
        return result;

    }

    /**
     * map值如果是null则返回结果 会忽略该键值对
     *
     * @param key  map存储在sp的tag
     * @param clsV 返回的值value的类型  如返回String 则填String.class
     * @param <V>
     * @return
     */
    public <V> HashMap<String, V> getHashMapData(String key, Class<V> clsV) {
        String json = preferences.getString(key, "{}");
        HashMap<String, V> map = new HashMap<>();
        Gson gson = new Gson();
        JsonObject obj = new JsonParser().parse(json).getAsJsonObject();
        Set<Map.Entry<String, JsonElement>> entrySet = obj.entrySet();
        for (Map.Entry<String, JsonElement> entry : entrySet) {
            String entryKey = entry.getKey();
            JsonElement value = entry.getValue();
            map.put(entryKey, gson.fromJson(value, clsV));
        }
        return map;
    }

    public  boolean putListData(String key, List<String> list) {
        boolean result;
        if (list==null){
            return false;
        }
        Editor editor = preferences.edit();
        try {
            String json = GsonUtil.gsonString(list);
            editor.putString(key, json);
            result = editor.commit();
        } catch (Exception e) {
            result = false;
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 不能加泛型T，混淆后TypeToken的T取不到
     * @param key
     * @return
     */
    public  List<String> getListData(String key) {
        String json = preferences.getString(key, "[]");
        List<String> list = GsonUtil.gsonToBean(json, new TypeToken<List<String>>() {
        }.getType());
        if (list==null){
            return new ArrayList<>();
        }
        return list;
    }

    public void remove(String key) {
        edit().remove(key).apply();
    }

    public void clear() {
        edit().clear().apply();
    }

    public void registerOnSharedPreferenceChangeListener(SharedPreferences.OnSharedPreferenceChangeListener listener) {
        preferences.registerOnSharedPreferenceChangeListener(listener);
    }

    public void unregisterOnSharedPreferenceChangeListener(SharedPreferences.OnSharedPreferenceChangeListener listener) {
        preferences.unregisterOnSharedPreferenceChangeListener(listener);
    }
}
