package org.example.lock;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.params.Params;
import redis.clients.jedis.params.SetParams;

import java.util.Collections;

public class RedisLock {

    private static final JedisPool jedisPool = new JedisPool("127.0.0.1", 6379);
    private final static String LOCK_KEY = "lock";
    private final static long TIMEOUT = 1000;

    public boolean lock(String id) {
        Jedis jedis = jedisPool.getResource();
        Long start = System.currentTimeMillis();
        SetParams params = SetParams.setParams().nx().px(5);
        try {
            for(;;) {
                String lock = jedis.set(LOCK_KEY, id, params);
                if ("OK".equals(lock)) {
                    return true;
                }

                if (System.currentTimeMillis() - start > TIMEOUT) {
                    return false;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            jedis.close();
        }
    }

    public boolean unlock(String id) {
        String script = "if redis.call('get', KEYS[1]) == ARGV[1] then" +
                        "  return redis.call('del', KEYS[1])" +
                        "else" +
                        "  return 0 " +
                        "end";

        Jedis jedis = jedisPool.getResource();
        try {
            String result = jedis.eval(script, Collections.singletonList(LOCK_KEY), Collections.singletonList(id)).toString();
            return "1".equals(result);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            jedis.close();
        }
    }
}
