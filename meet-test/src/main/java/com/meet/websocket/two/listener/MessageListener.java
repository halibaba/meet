package com.meet.websocket.two.listener;

import com.meet.websocket.two.handler.WorkDateSocketHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

/**
 * @program: workdata-message
 * @ClassName MessageListener
 * @description:
 * @author: MT
 * @create: 2022-12-16 15:35
 **/
@Component
@Slf4j
public class MessageListener {

	@Autowired
	private WorkDateSocketHandler workDateSocketHandler;

	//kafka的监听器，topic为"zhTest"，消费者组为"zhTestGroup"
//	@KafkaListener(topics = "#{'${topic.name}'}", groupId = "zhTestGroup")
	public void listenZhugeGroup(ConsumerRecord<String, String> record, Acknowledgment ack) {
		String value = record.value();
		System.out.println("接收到消息并进行消费:"+value);
		System.out.println("接收到消息并进行消费"+record);
		System.out.println("发送给客户端->");
		workDateSocketHandler.sendMessage("123456", value);
		//手动提交offset
		ack.acknowledge();
//		log.info("carConditionKafkaListen kafka group :{} ,topic :{}", "car-team", record.topic());
//
//		try {
//			//发送消息到websocket
//			webSocketServer.sendMessage("car-team.carCondition", record.value().toString());
//		}catch (Exception e){
//			log.error("carTeamConditionKafkaListen sendMessage to webSocket error: ", e);
//		}
	}

    /*//配置多个消费组
    @KafkaListener(topics = "zhTest",groupId = "zhTestGroup2")
    public void listenTulingGroup(ConsumerRecord<String, String> record, Acknowledgment ack) {
        String value = record.value();
        System.out.println(value);
        System.out.println(record);
        ack.acknowledge();
    }*/
}