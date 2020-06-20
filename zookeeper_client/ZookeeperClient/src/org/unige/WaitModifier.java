package org.unige;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.x.async.AsyncCuratorFramework;

public class WaitModifier {

    private static final String PATH = "/project";

    public static void main(String[] argv){
        String zkConnString = "192.168.101.2:2181,192.168.101.3:2181,192.168.101.4:2181";
        CuratorFramework client = CuratorFrameworkFactory.newClient(zkConnString,
                new ExponentialBackoffRetry(1000, 3));
        client.start();

        AsyncCuratorFramework async = AsyncCuratorFramework.wrap(client);
        String key = PATH;

        async.create().forPath(key);

        System.out.println("Started");
        Thread me = Thread.currentThread();
        while(true) {
            async.watched().getData().forPath(PATH).event().thenAccept(watchedEvent -> {
                System.out.println("Triggered! " + watchedEvent);
                (new Thread(new Updater(zkConnString, PATH, client))).start();
                synchronized (me) {
                    me.notify();
                }
            });
            synchronized (me) {
                try {
                    me.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
