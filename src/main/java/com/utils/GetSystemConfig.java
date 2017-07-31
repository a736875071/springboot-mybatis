package com.utils;//package com.suncd.epm.cm.service.cm.quartz.utils;

import com.utils.exception.MsgException;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.ho.yaml.Yaml;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Map;

/**
 * 获取系统配置
 *
 * @author YangQing
 * @version 1.0.0
 */

@Configuration
public class GetSystemConfig {

    @Autowired
    private Environment environment;

    /**
     * 获取yml配置
     *
     * @return zkIp
     */
    public static String systemYmlConfig() {
        String zkIp = null;
        try {
            Yaml yaml = new Yaml();
            String prefix = "bootstrap";
            String suffix = ".yml";
            String filePath = prefix + suffix;
            URL url = GetSystemConfig.class.getClassLoader().getResource(filePath);
            if (url == null) {
                throw new MsgException("请确认是否存在" + filePath);
            }
            //也可以将值转换为Map
            Map map = (Map) yaml.load(new FileInputStream(url.getFile()));
            Object map1 = ((Map) ((Map) map.get("spring")).get("profiles")).get("active");
            System.out.println(map);
            System.out.println(map1);
            String profileName = prefix + "-" + map1 + suffix;
            System.out.println(profileName);
            URL profileUrl1 = GetSystemConfig.class.getClassLoader().getResource(profileName);
            if (profileUrl1 == null) {
                throw new MsgException("请确认是否存在" + profileName);
            }
            Map mapn = (Map) yaml.load(new FileInputStream(profileUrl1.getFile()));
            Map zkmap = (Map) ((Map) ((Map) mapn.get("spring")).get("cloud")).get("zookeeper");
            zkIp = (String) zkmap.get("connectString");
            System.out.println(zkIp);
            //通过map我们取值就可以了.

        } catch (Exception e) {
            throw new MsgException("请确认配置文件是否正确");
        }
        return zkIp;
    }

    /**
     * 获取定时任务zk配置
     *
     * @return url, name, password
     */
    public static String[] zKConfig() {
        ZooKeeper zk = null;
        String username = "";
        String url = "";
        String password = "";
        try {
            zk = new ZooKeeper(systemYmlConfig(),
                    2000, new Watcher() {
                // 监控所有被触发的事件
                public void process(WatchedEvent event) {
                    System.out.println("已经触发了" + event.getType() + "事件！");
                }
            });
            String path = "/configurations/microserver/commonconfig/spring/quartz";
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
