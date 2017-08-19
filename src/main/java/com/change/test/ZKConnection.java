package com.change.test;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

import java.io.IOException;
import java.util.List;

/**
 * @author YangQing
 * @version 1.0.0
 */

public class ZKConnection
{
    public static void main(String[] args) {

        String[] a=zKConfig();
        for (int i=0;i<a.length;i++){
            System.out.println(a[i]);

        }
    }
        public static String[] zKConfig() {
        ZooKeeper zk = null;
        String username = "";
        String url = "";
        String password = "";
        try {
            zk = new ZooKeeper("192.168.2.200:2182",
                    2000, new Watcher() {
                // 监控所有被触发的事件
                public void process(WatchedEvent event) {
                    System.out.println("已经触发了" + event.getType() + "事件！");
                }
            });
            String path = "/configurations/microserver/commonconfig/spring/datasource";
            List<String> data = zk.getChildren(path, false);
            for (String a : data) {
                byte[] da = zk.getData(path + "/" + a, false, null);
                if (a.equals("url")) {
                    url = new String(da);
                }
                if (a.equals("username")) {
                    username = new String(da);
                }
                if (a.equals("password")) {
                    password = new String(da);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (KeeperException e) {
            e.printStackTrace();
        }
        String[] zkConfig = {url, username, password};
        return zkConfig;
    }
}
