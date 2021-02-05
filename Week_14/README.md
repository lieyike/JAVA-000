学习笔记
workflow:   osworkflow, jbpm activity
工作流的定义， new 一个流程实例

状态机:  FSM spring statemachine

2分钟内能不能写快排

作业笔记
用数组模拟queue
writeableIndex 指明可写入的位置
readableIndex  指明可读取的位置

当readableIndex  等于 writeableIndex 说明当前queue已经空了

当 writeableIndex 等于queue的capacity 说明数组已经满了就
创建一个新数组把从readableIndex到writeableIndex或者capacity的message搬到一个新数组里
把readableIndex指为0，writeableIndex指为上个数组剩余数据的下一位.

改小了capacity 为502,代码里给生产者和消费者都加了一个Thread.sleep(),不然生产者会快速把数组沾满导致数据丢失
（因为加了锁消费者无法消费， 这里可以增加消息处理策略选择丢弃最旧的或者临时增大数组大小）
消费者的Thread sleep也是避免消费过快，以模拟中间有数据的情况不然 readableIndex 会等于writeableIndex
就不需要copy 直接从0开始写开始读

2.增加不同消费者的读索引，用一个map存储索引值，要注意的就是当数组满了，用最小的索引来作为新数组的第一个数据，
其他索引减去这个最小索引值