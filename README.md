# springboot-mybatis
1.项目目前集成zk,mq,quartz,redis插件,配置文件在zk上,使用zkui图形界面进行配置

1.启动zk
        E:\zookeeper-3.4.9\bin 下启动zkServer.cmd
2.启动zkui图形界面用于配置插件参数
        E:\zkui  执行命令 Java -jar zkui-2.0-SNAPSHOT-jar-with-dependencies.jar
        访问路径:127.0.0.1:9090
3.启动mq
        E:\Java\env\RabbitMQ Server\rabbitmq_server-3.6.9\sbin
        rabbitmq-service install 安装服务
        rabbitmq-service start 开始服务
        Rabbitmq-service stop  停止服务
        Rabbitmq-service enable 使服务有效
        Rabbitmq-service disable 使服务无效
        rabbitmq-service help 帮助
    mq图形界面
        http://127.0.0.1:15672/   guest  guest

4.quartz
       注意配置文件正确

5.启动redis
E:\Java\Redis-x64-3.2.100 下执行命令 redis-server.exe redis.windows.conf

6.集成logback日志
在配置文件中可以直接写
logging:
    level:
        root:
         debug
 或logback-spring.xml 在zk上面配置
 两种都存在以第一种为准

 7.集成eureka
 现目前对eureka服务没做任何多余编码对以下两种启动方法都没有影响，效果都一样
 1.导入eureka-service项目并启动

 2.直接将eureka-service项目打成jar，通过命令直接启动
 java -jar eureka-server-0.0.1-SNAPSHOT.jar --spring.config.location=F:\javaExam\bootstrap-release.yml
 其中：--spring.config.location=F:\javaExam\bootstrap-release.yml是配置文件路径，在启动jar时会读取这个配置文件，实现了动态配置

 注意：可能出现注册发现服务出现down情况,原因可能是mq或redis没有启动造成,启动即可
