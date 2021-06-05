package com.king.kingcloud.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @program: kingcloud
 * @description: redis 数据库list 操作帮助类
 * @author: King
 * @create: 2021-06-05 18:57
 */
@Repository
public class RedisUtil {
    @Autowired
    private StringRedisTemplate redisTemplate;

    //TODO HashMap
    /**
     * 插入一个 map
     *
     * @param key
     * @param map
     */
    public void insert(String key, HashMap map) {
        redisTemplate.boundHashOps(key).putAll(map);
    }

    /**
     * 提取所有的小key
     * <p>
     * 2、通过BoundValueOperations获取值
     * BoundHashOperations hashKey = redisTemplate.boundHashOps("HashKey");
     * Set keys2 = hashKey.keys();
     * 3、通过ValueOperations获取值
     * HashOperations hashOps = redisTemplate.opsForHash();
     * Set keys3 = hashOps.keys("HashKey");
     *
     * @param key
     */
    public Set<Object> getLKey(String key) {
        return redisTemplate.boundHashOps(key).keys();
    }

    /**
     * 提取所有的value值
     * <p>
     * 2、通过BoundValueOperations获取值
     * BoundHashOperations hashKey = redisTemplate.boundHashOps("HashKey");
     * List values2 = hashKey.values();
     * <p>
     * 3、通过ValueOperations获取值
     * HashOperations hashOps = redisTemplate.opsForHash();
     * List values3 = hashOps.values("HashKey");
     *
     * @param key
     * @return
     */
    public List<Object> getValue(String key) {
        return redisTemplate.boundHashOps(key).values();
    }

    /**
     * 根据key提取value值
     * <p>
     * 2、通过BoundValueOperations获取值
     * BoundHashOperations hashKey = redisTemplate.boundHashOps("HashKey");
     * String value2 = (String) hashKey.get("SmallKey");
     * <p>
     * 3、通过ValueOperations获取值
     * HashOperations hashOps = redisTemplate.opsForHash();
     * String value3 = (String) hashOps.get("HashKey", "SmallKey");
     *
     * @param key
     * @param smallKey
     * @return
     */
    public String getValue(String key, String smallKey) {
        //1、通过redisTemplate获取
        return (String) redisTemplate.boundHashOps(key).get(smallKey);
    }

    /**
     * 删除小key
     *
     * @param key
     * @param smallKey
     * @return
     */
    public Long delete(String key, String smallKey) {
        //删除小key
        return redisTemplate.boundHashOps(key).delete(smallKey);
    }

    /**
     * 删除大key
     */
    public Boolean delete(String key) {
        return redisTemplate.delete(key);
    }

    /**
     * 获取所有的键值对集合
     * <p>
     * 2、通过BoundValueOperations获取值
     * BoundHashOperations hashKey = redisTemplate.boundHashOps("HashKey");
     * Map entries1 = hashKey.entries();
     * <p>
     * 3、通过ValueOperations获取值
     * HashOperations hashOps = redisTemplate.opsForHash();
     * Map entries2 = hashOps.entries("HashKey");
     *
     * @param key
     * @return
     */
    public Map getMap(String key) {
        return redisTemplate.boundHashOps("HashKey").entries();
    }

    public boolean hasKey(String key, String smallKey) {
        return redisTemplate.boundHashOps(key).hasKey(smallKey);
    }
    public void push() {
        redisTemplate.opsForList().rightPush("list4", "中文");
        redisTemplate.opsForList().rightPush("list4", "中文");
        redisTemplate.opsForList().rightPush("list4", "中文");
        redisTemplate.opsForList().rightPush("list4", "four");
        List<String> list4 = redisTemplate.opsForList().range("list4", 0, -1);

    }
    public void push(List<Object> list,String listName) {

        List<String> list4 = redisTemplate.opsForList().range("list4", 0, -1);

    }

    public boolean lSet(String key, List<Object> value) {

        try {

           // redisTemplate.opsForList().rightPushAll(key, value);

            return true;

        } catch (Exception e) {

            e.printStackTrace();

            return false;

        }

    }
}
