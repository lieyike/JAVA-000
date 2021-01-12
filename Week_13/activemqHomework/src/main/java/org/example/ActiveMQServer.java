package org.example;

import org.apache.activemq.broker.BrokerService;

public class ActiveMQServer {

    public ActiveMQServer() {


    }

    public void start () throws Exception {
        // 尝试用java代码启动一个ActiveMQ broker server
        // 然后用前面的测试demo代码，连接这个嵌入式的server

        BrokerService brokerService = new BrokerService();
        brokerService.setBrokerName("fred");
        brokerService.addConnector("tcp://localhost:61616");
        brokerService.start();
    }

    public static void main(String[] args) throws Exception {
        ActiveMQServer activeMQServer = new ActiveMQServer();
        activeMQServer.start();

        Thread.sleep(20000);
    }

}
