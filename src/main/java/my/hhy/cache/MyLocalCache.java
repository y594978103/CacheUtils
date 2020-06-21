package my.hhy.cache;

import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author hhy
 */
public class MyLocalCache {

    private static final Map<String, String> MAP_CACHE;
    private static final long BECOME_SECOND = 1000;
    private static final ReentrantLock LOCK = new ReentrantLock();
    private static final Timer TIMER;


    /*
      初始化操作
     */
    static {
        MAP_CACHE = Collections.synchronizedMap(new HashMap<>());
        TIMER = new Timer();
    }

    static class ExpriedTimerTask extends TimerTask {

        private String key;

        public ExpriedTimerTask(String key) {
            this.key = key;
        }

        @Override
        public void run() {
            MyLocalCache.remove(key);
        }
    }

    /**
     * 设置缓存
     *
     * @param key   键
     * @param value 值
     */
    public static void set(String key, String value) {
        LOCK.lock();
        try {
            MAP_CACHE.put(key, value);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            LOCK.unlock();
        }
    }


    /**
     * 设置缓存，带过期时间
     *
     * @param key     键
     * @param value   值
     * @param timeout 有效时间（秒）
     */
    public static void set(String key, String value, int timeout) {
        LOCK.lock();
        try {
            MAP_CACHE.put(key, value);
            TIMER.schedule(new ExpriedTimerTask(key), timeout * BECOME_SECOND);
        } finally {
            LOCK.unlock();
        }
    }

    /**
     * 获取缓存
     *
     * @param key 键
     * @return
     */
    public static String get(String key) {
        LOCK.lock();
        try {
            return MAP_CACHE.get(key);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("获取错误！");
        } finally {
            LOCK.unlock();
        }

    }

    /**
     * 删除缓存
     *
     * @param key 键值
     */
    public static void remove(String key) {
        LOCK.lock();
        try {
            MAP_CACHE.remove(key);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            LOCK.unlock();
        }
    }


    /**
     * 缓存持久化方法
     * @throws IOException
     */
    public static void persist() throws IOException {

        StringBuffer buffer = new StringBuffer();
        FileWriter writer = new FileWriter("cache-file.txt");
        for(Map.Entry entry:MAP_CACHE.entrySet()){
            String key = (String) entry.getKey();
            String value = (String) entry.getValue();
            buffer.append(key + "=" + value).append(System.getProperty("line.separator"));
        }
        writer.write(buffer.toString());
        writer.close();

    }
}
