### MQ
* Kafka

---
#### Kafka
* 如果需要订阅一个Topic上所有的消息，则需要订阅这个Topic的所有的Partition
* 单机不建议分多个partition也不建议大量topic，因为有大量的partition和topic，文件很多，所以一会儿写这个文件，一会儿写那个文件，就会造成文件的随机IO读写
* kafka消息的获取是拉取的方式