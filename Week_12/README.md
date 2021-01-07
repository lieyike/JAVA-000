学习笔记

1.Master Slave

startup 

.\redis-server.exe .\master_slave\master.conf

.\redis-server.exe .\master_slave\slave.conf

commond 
在master set一个数据然后在slave里拿
PS F:\workspace\JavaCourse\tool\Redis-x64-5.0.10> .\redis-cli.exe -p 6379 set key1 1
OK
PS F:\workspace\JavaCourse\tool\Redis-x64-5.0.10> .\redis-cli.exe -p 6380 get key1
"1"

2.sentinel

.\redis-server.exe .\sentinel\redis-6379.conf
.\redis-server.exe .\sentinel\redis-6380.conf
.\redis-server.exe .\sentinel\redis-6381.conf


PS F:\workspace\JavaCourse\tool\Redis-x64-5.0.10> .\redis-cli.exe -p 6379
127.0.0.1:6379> info replication
# Replication
role:master
connected_slaves:2
slave0:ip=127.0.0.1,port=6380,state=online,offset=56,lag=1
slave1:ip=127.0.0.1,port=6381,state=online,offset=56,lag=1
master_replid:bf64bf065dabe9fdd06010992d228985deb42f0c
master_replid2:0000000000000000000000000000000000000000
master_repl_offset:56
second_repl_offset:-1
repl_backlog_active:1
repl_backlog_size:1048576
repl_backlog_first_byte_offset:1
repl_backlog_histlen:56


.\redis-server.exe .\sentinel\sentinel-26379.conf --sentinel
.\redis-server.exe .\sentinel\sentinel-26380.conf --sentinel
.\redis-server.exe .\sentinel\sentinel-26381.conf --sentinel

PS F:\workspace\JavaCourse\tool\Redis-x64-5.0.10> .\redis-cli.exe -p 26379 info sentinel
# Sentinel
sentinel_masters:1
sentinel_tilt:0
sentinel_running_scripts:0
sentinel_scripts_queue_length:0
sentinel_simulate_failure_flags:0
master0:name=mymaster,status=ok,address=127.0.0.1:6379,slaves=2,sentinels=3
PS F:\workspace\JavaCourse\tool\Redis-x64-5.0.10>

关掉master
等十来秒从 6379 变成 6381

PS F:\workspace\JavaCourse\tool\Redis-x64-5.0.10> .\redis-cli.exe -p 26379 info sentinel
# Sentinel
sentinel_masters:1
sentinel_tilt:0
sentinel_running_scripts:0
sentinel_scripts_queue_length:0
sentinel_simulate_failure_flags:0
master0:name=mymaster,status=ok,address=127.0.0.1:6381,slaves=2,sentinels=3

再把 6379 启动
sentinel 会把 6379 变为 slave


3.cluster
startup
 .\redis-server.exe .\cluster\redis-7000.conf
 .\redis-server.exe .\cluster\redis-7001.conf
 .\redis-server.exe .\cluster\redis-7002.conf
 .\redis-server.exe .\cluster\redis-8000.conf
 .\redis-server.exe .\cluster\redis-8001.conf
 .\redis-server.exe .\cluster\redis-8002.conf

check nodes
PS F:\workspace\JavaCourse\tool\Redis-x64-5.0.10> .\redis-cli.exe -p 7000 cluster nodes
04be716c8739c4b67a2aeae0b16befeefe58af0b :7000@17000 myself,master - 0 0 0 connected

节点握手

127.0.0.1:7000> cluster meet 127.0.0.1 7001
OK
127.0.0.1:7000> cluster meet 127.0.0.1 7002
OK
127.0.0.1:7000> cluster meet 127.0.0.1 8000
OK
127.0.0.1:7000> cluster meet 127.0.0.1 8001
OK
127.0.0.1:7000> cluster meet 127.0.0.1 8002
OK
127.0.0.1:7000> cluster nodes
973a2d9dede8416c0f1f45c491f968f89d8bd2ad 127.0.0.1:8002@18002 master - 0 1609953184383 0 connected
66811444ce334ea583090ab4d841fe973d70c08c 127.0.0.1:7002@17002 master - 0 1609953185476 2 connected
274ebdaa8f7ad90ab59200cf1ef20bbf7cf6c457 127.0.0.1:8000@18000 master - 0 1609953183000 3 connected
8213a50d4f811cac609bd612f0e11a4d27f2f137 127.0.0.1:7001@17001 master - 0 1609953184000 5 connected
450c2f0930728f86eb68e605f6e05de0f914a577 127.0.0.1:8001@18001 master - 0 1609953183227 4 connected
04be716c8739c4b67a2aeae0b16befeefe58af0b 127.0.0.1:7000@17000 myself,master - 0 1609953183000 1 connected

分配槽

.\redis-cli.exe -p 7000 cluster addslots {0..5641}
.\redis-cli.exe -p 7001 cluster addslots {5641..10922}
.\redis-cli.exe -p 7002 cluster addslots {10922..15000} //不知道为什么在加10922..16383时候会报参数过长的错误所以分开两次执行
.\redis-cli.exe -p 7002 cluster addslots {15001.。16383}

设置从

.\redis-cli.exe -p 8000 cluster replicate 04be716c8739c4b67a2aeae0b16befeefe58af0b 
...


另外一种设置方法

1.先把机器全部起动起来
2.cluster create 命令  （https://redis.io/topics/cluster-tutorial）
PS F:\workspace\JavaCourse\tool\Redis-x64-5.0.10> .\redis-cli.exe --cluster create 127.0.0.1:7000 127.0.0.1:7001 127.0.0.1:7002 127.0.0.1:8000 127.0.0.1:8001 127.0.0.1:8002 --cluster-replicas 1
>>> Performing hash slots allocation on 6 nodes...
Master[0] -> Slots 0 - 5460
Master[1] -> Slots 5461 - 10922
Master[2] -> Slots 10923 - 16383
Adding replica 127.0.0.1:8001 to 127.0.0.1:7000
Adding replica 127.0.0.1:8002 to 127.0.0.1:7001
Adding replica 127.0.0.1:8000 to 127.0.0.1:7002
>>> Trying to optimize slaves allocation for anti-affinity
[WARNING] Some slaves are in the same host as their master
M: 90cf42dddca6edfa89a97ec1dfe71c9d40c89313 127.0.0.1:7000
   slots:[0-5460] (5461 slots) master
M: cc4b88dd9ef442a63e203a68c34544908989f479 127.0.0.1:7001
   slots:[5461-10922] (5462 slots) master
M: 46316c7703dc7c2a27c0313af9ecdd2f4703b0db 127.0.0.1:7002
   slots:[10923-16383] (5461 slots) master
S: 6e5a88a32bfc46475badd7175a0ea444c2d5cb2a 127.0.0.1:8000
   replicates cc4b88dd9ef442a63e203a68c34544908989f479
S: 194a041a2e35aa71ca3e50bb2ed887a789f3ef09 127.0.0.1:8001
   replicates 46316c7703dc7c2a27c0313af9ecdd2f4703b0db
S: b25ac5cafdb4374c710b8466f03a301bfda53aef 127.0.0.1:8002
   replicates 90cf42dddca6edfa89a97ec1dfe71c9d40c89313
Can I set the above configuration? (type 'yes' to accept): yes
>>> Nodes configuration updated
>>> Assign a different config epoch to each node
>>> Sending CLUSTER MEET messages to join the cluster
Waiting for the cluster to join
...
>>> Performing Cluster Check (using node 127.0.0.1:7000)
M: 90cf42dddca6edfa89a97ec1dfe71c9d40c89313 127.0.0.1:7000
   slots:[0-5460] (5461 slots) master
   1 additional replica(s)
M: cc4b88dd9ef442a63e203a68c34544908989f479 127.0.0.1:7001
   slots:[5461-10922] (5462 slots) master
   1 additional replica(s)
M: 46316c7703dc7c2a27c0313af9ecdd2f4703b0db 127.0.0.1:7002
   slots:[10923-16383] (5461 slots) master
   1 additional replica(s)
S: 194a041a2e35aa71ca3e50bb2ed887a789f3ef09 127.0.0.1:8001
   slots: (0 slots) slave
   replicates 46316c7703dc7c2a27c0313af9ecdd2f4703b0db
S: 6e5a88a32bfc46475badd7175a0ea444c2d5cb2a 127.0.0.1:8000
   slots: (0 slots) slave
   replicates cc4b88dd9ef442a63e203a68c34544908989f479
S: b25ac5cafdb4374c710b8466f03a301bfda53aef 127.0.0.1:8002
   slots: (0 slots) slave
   replicates 90cf42dddca6edfa89a97ec1dfe71c9d40c89313
[OK] All nodes agree about slots configuration.
>>> Check for open slots...
>>> Check slots coverage...
[OK] All 16384 slots covered.



