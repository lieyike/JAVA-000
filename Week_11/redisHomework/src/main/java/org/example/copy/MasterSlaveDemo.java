package org.example.copy;

import redis.clients.jedis.Jedis;

public class MasterSlaveDemo {

    public static void main(String[] args) {
        String key = "USer";

        Jedis masterJedis = new Jedis("127.0.01", 6379);
        masterJedis.set(key,"AABB");

        Jedis slaveJedis = new Jedis("127.0.0.1", 6380);
        String value = slaveJedis.get(key);
        System.out.println("Slave get value :" + value);

        try {
            slaveJedis.set(key, "CCDD");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
