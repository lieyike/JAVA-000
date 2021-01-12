学习笔记

kafka 集群
    
启动 zookeeper
.\bin\windows\zookeeper-server-start.bat .\config\zookeeper.properties


.\bin\windows\kafka-server-start.bat .\config\server-1.properties
.\bin\windows\kafka-server-start.bat .\config\server-2.properties
.\bin\windows\kafka-server-start.bat .\config\server-3.properties

建立topic
.\bin\windows\kafka-topics.bat --zookeeper localhost:2181 --create --topic test32 --partitions 3 --replication-factor 2

查看topic
.\bin\windows\kafka-topics.bat --zookeeper localhost:2181 --describe --topic test32


producer 输入message
.\bin\windows\kafka-console-producer.bat --bootstrap-server localhost:9093,localhost:9094:localhost:9095 --topic test32

consumer会接受到message
.\bin\windows\kafka-console-consumer.bat --bootstrap-server localhost:9093,localhost:9094:localhost:9095 --topic test32 --from-beginning