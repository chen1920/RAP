package com.taobao.rigel.rap.common.utils;

import java.util.Map;

/**
 *
 * @author chen
 */
public class MemoryCacheTool {

    private final static String TAG = "MemoryCacheTool";
    private final Map<String, MemoryCache> caches;

    public MemoryCacheTool() {
        this.caches = new java.util.concurrent.ConcurrentHashMap<String, MemoryCache>();
    }

    public void flushAll() {
        System.out.println("flushAll");
        caches.clear();
    }

    public void del(String[] keys) {
        if (keys != null)
        {
            for (int i = 0; i < keys.length; i++)
            {
                System.out.println("del keys " + keys[i]);
                caches.remove(keys[i]);
            }
        }
    }

    public void del(String cacheKey) {
        System.out.println("del key " + cacheKey);
        caches.remove(cacheKey);
    }

    public String get(String cacheKey) {
        System.out.println("get key " + cacheKey);
        MemoryCache cache = caches.get(cacheKey);
        if (cache == null)
        {
            return null;
        }
        if (cache.isExpire())
        {
            System.out.println(cacheKey + " Expire");
            caches.remove(cacheKey);
            return null;
        }
        return cache.getValue();
    }

    public void set(String cacheKey, String value, int expireInSecs) {
        System.out.println("set key " + cacheKey + " to " + value);
        caches.put(cacheKey, new MemoryCache(value, expireInSecs));
    }

}
