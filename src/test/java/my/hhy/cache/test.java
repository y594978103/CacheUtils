package my.hhy.cache;

import org.junit.Test;

import java.io.IOException;

public class test {

    /**
     * 测试set方法
     */
    @Test
    public void testSet() {
        //测试set方法
        MyLocalCache.set("mykey", "myvalue");
        Object mykey = MyLocalCache.get("mykey");
        System.out.println(mykey);
    }

    /**
     * 测试set方法（带过期时间）
     * @throws InterruptedException
     */
    @Test
    public void testSetWithTimeOut() throws InterruptedException {

        //测试set方法（带有过期时间）
        MyLocalCache.set("mykey2", "myvalue2", 1);
        String mykey2 = MyLocalCache.get("mykey2");
        System.out.println(mykey2);

        //线程睡5秒
        Thread.sleep(5000);
        //测试缓存过期是否删除
        String mykey3 = MyLocalCache.get("mykey2");
        System.out.println(mykey3);
    }

    /**
     * 测试持久化方法
     * @throws IOException
     */
    @Test
    public void testPersist() throws IOException {

        MyLocalCache.set("mykey3", "myvalue3");
        MyLocalCache.set("mykey4", "myvalue4");
        MyLocalCache.set("mykey5", "myvalue5");


        //调用持久化方法
        MyLocalCache.persist();

    }


}
