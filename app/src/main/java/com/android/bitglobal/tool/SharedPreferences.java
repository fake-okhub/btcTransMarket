package com.android.bitglobal.tool;

import android.content.Context;
import android.content.SharedPreferences.Editor;

import com.android.bitglobal.common.AppContext;


/**
 * xiezuofei
 * 2016-06-24 16:20
 * 793169940@qq.com
 *
 */
public class SharedPreferences {

    private static final String SP_NAME = "android";
    public static final String KEY_LOGIN_TOKEN = "login_token";
    public static final String KEY_LOGIN_TYPE = "login_type";
    public static final String KEY_LOGIN_NAME = "login_name";
    public static final String KEY_USER_LOCK = "user_lock";
    public static final String APP_LANGUAGE = "app_language";
    /**
     * app存放法币标识
     */
    public static final String APP_CURRENCY = "app_currency";
    public static final String PUSH_OPEN = "push_open";
    public static final String DEFAULT_USER = "default_user";
    /**
     * 首次打开app，用于ip获取法币标识
     */
    public static final String IS_FIRST_TO_APP = "IS_FIRST_TO_APP";
    /**
     * 缓存交易总览页标识
     */
    public static final String CACHE_TRADE_OVERVIEW = "CACHE_TRADE_OVERVIEW";

    private static SharedPreferences instance = new SharedPreferences();

    public SharedPreferences() {
    }

    private static synchronized void syncInit() {
        if (instance == null) {
            instance = new SharedPreferences();
        }
    }

    public static SharedPreferences getInstance() {
        if (instance == null) {
            syncInit();
        }
        return instance;
    }

    private android.content.SharedPreferences getSp() {
        return AppContext.getInstance().getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
    }

    public int getInt(String key, int def) {
        try {
            android.content.SharedPreferences sp = getSp();
            if (sp != null)
                def = sp.getInt(key, def);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return def;
    }

    public void putInt(String key, int val) {
        try {
            android.content.SharedPreferences sp = getSp();
            if (sp != null) {
                Editor e = sp.edit();
                e.putInt(key, val);
                e.commit();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public long getLong(String key, long def) {
        try {
            android.content.SharedPreferences sp = getSp();
            if (sp != null)
                def = sp.getLong(key, def);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return def;
    }

    public void putLong(String key, long val) {
        try {
            android.content.SharedPreferences sp = getSp();
            if (sp != null) {
                Editor e = sp.edit();
                e.putLong(key, val);
                e.commit();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getString(String key, String def) {
        try {
            android.content.SharedPreferences sp = getSp();
            if (sp != null)
                def = sp.getString(key, def);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return def;
    }

    public void putString(String key, String val) {
        try {
            android.content.SharedPreferences sp = getSp();
            if (sp != null) {
                Editor e = sp.edit();
                e.putString(key, val);
                e.commit();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean getBoolean(String key, boolean def) {
        try {
            android.content.SharedPreferences sp = getSp();
            if (sp != null)
                def = sp.getBoolean(key, def);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return def;
    }

    public void putBoolean(String key, boolean val) {
        try {
            android.content.SharedPreferences sp = getSp();
            if (sp != null) {
                Editor e = sp.edit();
                e.putBoolean(key, val);
                e.commit();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void remove(String key) {
        try {
            android.content.SharedPreferences sp = getSp();
            if (sp != null) {
                Editor e = sp.edit();
                e.remove(key);
                e.commit();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
