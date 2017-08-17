package com.taobao.rigel.rap.common.utils;

/**
 *
 * @author chen
 */
public class MemoryCache {

    private final static String TAG = "MemoryCache";
    private final String value;
    private final long expireTime;

    public MemoryCache(String value) {
        this.value = value;
        expireTime = 0;
    }

    public MemoryCache(String value, int expireInSecs) {
        this.value = value;
        this.expireTime = expireInSecs * 1000 + localTime();
    }

    public static long localTime() {
        return System.nanoTime() / 1000000;
    }

    public String getValue() {
        return value;
    }

    public boolean isExpire() {
        if (expireTime != 0)
        {
            return localTime() > expireTime;
        }
        return false;
    }

}
