1) JVM
    

    1.JVM首先得理解java 如何编译，把代码编译成字节码JVM再运行字节码，所以会有字节码增加技术。
    2.掌握java类加载器了解类的加载过程类的加载时机，三类类加载器（启动，扩展，应用），学会自定义和使用类加载器
    3.内存模型，内存模型组成栈，堆，非堆，jvm自己。堆的组成年轻代（新生代,S0,S1）,老年代。非堆（元数据,代码缓存...）
    4.会使用jdk自带一些工具查看内存线程。
    5.JVM如何做垃圾回收(GC), 暂停内存，扫描，标记没有被引用对象，清理这些对象。几种GC基本原理


2) NIO
    

    1.IO类型， 5种IO BIO blocking IO 阻塞型IO NIO Non-blocking IO 非阻塞IO,
       I/0复用 信号驱动 异步
    2.Netty框架 BESC  bootstrap, EventGroup, Socket , Channel 
    3.了解一些网关Spring cloud gateway Zuul OpenResty
    4.难点，网关路由，网关过滤器

3) 并发编程


    1.理解锁机制 synchronized， 方法上的锁是对象锁，静态方法的就是类锁， 
    2.锁类 Lock, ReentrantLock（可重入锁对占用锁线程可再入） 加锁考虑问题，锁粒度，加锁后对性能的影响，锁能否再入
      线程获取时是否公平。
    3.锁的使用细节 最小使用 减少使用范围， 细分粒度
      老师讲三点：1. 永远只在更新对象的成员变量时加锁
                2. 永远只在访问可变的成员变量时加锁
                3. 永远不在调用其他对象的方法时加锁 
                （但在其他对象方法中调用时可能存在另外的对象绕过这个锁调用方法，如果每个调用这个方法的地方都加锁不容易管理，
                  因此这个锁相关的方法要聚合在一起方便使用和管理，可以加多一层类在这一层类中处理锁）
    4. 无锁 CAS （乐观锁） 自旋重试得注意写入线程的数量压力大时候不断重试会消耗更多资源
    5. 信号量 
       Semaphore 指定数量限制通过 
       CountdownLatch countdown就减一 控制流程确保一个个做完非常有用
       CyclicBarrier 所有任务完成后回调
       理解一个线程屏障原理，这些类用于控制同步点
    6.Future 任务结果  FutureTask接受一个任务返回任务结果 CompletableFuture
    7.有趣的作业题用10方法启动线程拿回方法值。10种可以做到的。
    8.集合类， 重点CopyOnWriteArrayList, 在副本写入再替换回去。
      jdk7 Concurrent 使用分段锁
      jdk8 使用大数组+CAS
    9.ThreadLocal
    10.Stream 

4) Spring 和 ORM 等框架
    

    spring AOP 对于接口可以使用jdkProxy 或者 cglib, 对于非接口类使用 cglib
    
    spring Messaging
    
    springboot 1.configuration 配置
               2.spring-boot-starter

    spring jdbc template 在 update 或者batchupate的时候可以指定对象组的数据类型这种指定可以优化数据库更新效率

    MyBatis和Hibernate, Hibernate简单场景无需写SQL复杂场景下其实还是用SQL比较好,Mybatis 原生SQL比较友好
    
    JPA(java persistence API) 是接口和规范，将实体对象持续到数据库中。


5) MySQL 数据库和 SQL


    mysql 对SQL执行顺序是from,on,join,where,group by, having+聚会函数,select,order by, 
    limit在写sql的时候可以根据顺序做优化。选择数据库引擎时候考虑要不要事务，需要事务选择innodb,如果做数据压缩的
    可以考虑myisam或者archive。数据类型选择尽量越小越好例如一个bool类型实际存的可以是0或1，Y或者N,不用存true或false。
    数据库的悲观锁select * from for update,会阻塞其他更新操作执行
    乐观锁 通过一个version值来判断修改的值在拿到原值到改回去这个中间过程有没有被改过。
    作业里面在写入100w数据时在mysql的url中加入rewriteBatchedStatements=true执行才能快速写入


6) 分库分表 分布式数据库 


    mysql技术演进，主从复制（主从复制原理Binlog）多台从库解决读压力，使用脚本等一些方式在主库故障是修改从库为主库，给与一定的高可用性。
    再到MGR。
    读写分离，分离数据库压力，1）手动配置两个源，2）使用数据库框架管理，3）使用中间件
    高可用 MGR, 分布式 mysql cluster 
    单机容量问题 使用分库分表解决
    解决问题的方法扩展立方体，x轴部署集群，y轴拆分业务，z轴数据分片
    分库分表拆分方式垂直拆分按照业务把一块业务例如一个电商系统把订单，商品，用户拆分开来，不同的类型的数据自己拥有一个数据库。
    水平拆分通过一些算法来做分片，不同的部分放在不同的数据库上
    
    分库分表的选择，IO为瓶颈时候，使用分库分到不同实例或者是不同磁盘提升并行处理能力，使用分表用于解决容量问题，降低单表数据库量间接的也挺高操作性能。
    此外分库都是在同一台机器同个磁盘上面没有办法起到的作用,
    分表无法使用外键，一般数据库中不使用外键的理由 https://www.cnblogs.com/youngdeng/p/12857093.html
    为什么不用uuid 作为主键，因为无序无法基于主键优化


7) RPC 和微服务
     
   
     RPC定义remote procedure call 远程过程调用。之前误解rpc觉得restful call这种很像，其实重点在使用接口封装了
     调用内容，即像本地方法一样调用远程方法。
     RPC的简化核心原理 
                    1.本地代理存根
                    2.本地序列化反序化
                    3.网络通信
                    4.远程序列化
                    5.远程服务存根
                    6.调用实际服务
                    7.返回服务结果
                    8.返回结果给本地调用方
 
     rpc与分布式服务化的区别是什么？rpc是技术概念，分布式服务化是业务语义讲的是业务与系统的集成

     分布式服务化与SOA/ESB区分在于ESB作为一个中间层与前后服务交互，分布式服务化把配置与注册发现独立出来，
     前后服务去注册发现处查找服务在直接去访问对应系统

     微服务发展过程，1.单体架构，一个项目没有使用任何框架背后接一个数据
                  2.垂直架构，引入spring mvc 这类框架ssh.
                  3.SOA架构，项目比较大因此用过服务提供功能，ESB作为服务编排组合。
                  4.微服务架构，把每项服务分的比较细基于restful
     微服务还是需要一定代价的因此在整个系统复杂度比较小的时候没必要引入微服务
     spring cloud是一套微服务解决方案通过选择不同的组件来建立微服务架构

     

8) 分布式缓存

     
     使用缓存原因：为了加速数据处理,因为数据的类型和使用频率不同因此可以有缓存。
     热数据读写比较大的数据可以做缓存。
     缓存历程，本地缓存，使用框架缓存，到远程缓存redis hazelcast

     使用redis分布式锁的缺点
     https://www.cnblogs.com/youngdeng/p/12883790.html

9) 分布式消息队列

    
    消息开始是队列然后到消息服务
    消息处理保障一般建议使用At least once然后在应用层去做幂等处理，这样做能确保数据不会丢失。
    JMS是应用层的API协议是一系列的接口，具体是使用它的实现。
    第一代MQ ActiveMQ RabbitMQ 提供queue和Topic
    第二代MQ kafka 和 rocketMQ 相比与第一代提供了堆积的功能。
    第三代MQ pulsar 分离了计算节点存储节点
    