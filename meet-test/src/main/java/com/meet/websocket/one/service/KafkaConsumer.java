  //package com.meet.websocket.one.service;
//
//import lombok.extern.slf4j.Slf4j;
//import org.apache.kafka.clients.consumer.ConsumerRecord;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.kafka.annotation.KafkaListener;
//import org.springframework.kafka.support.Acknowledgment;
//import org.springframework.stereotype.Component;
//
///**
// * @program: workdata-message
// * @ClassName KafkaConsumer
// * @description:
// * @author: MT
// * @create: 2022-12-15 16:31
// **/
//@Component
//@Slf4j
//public class KafkaConsumer {
//
//	@Autowired
//	private WebSocketService webSocketServer;
//
//	//kafka的监听器，topic为"zhTest"，消费者组为"zhTestGroup"
//	@KafkaListener(topics = "zhTest", groupId = "zhTestGroup")
//	public void listenZhugeGroup(ConsumerRecord<String, String> record, Acknowledgment ack) {
//		String value = record.value();
//		System.out.println("消费消息？"+value);
//		System.out.println("消费消息？"+record);
//		webSocketServer.sendMessage("123456", value);
//		//手动提交offset
//		ack.acknowledge();
////		log.info("carConditionKafkaListen kafka group :{} ,topic :{}", "car-team", record.topic());
////
////		try {
////			//发送消息到websocket
////			webSocketServer.sendMessage("car-team.carCondition", record.value().toString());
////		}catch (Exception e){
////			log.error("carTeamConditionKafkaListen sendMessage to webSocket error: ", e);
////		}
//	}
//
//    /*//配置多个消费组
//    @KafkaListener(topics = "zhTest",groupId = "zhTestGroup2")
//    public void listenTulingGroup(ConsumerRecord<String, String> record, Acknowledgment ack) {
//        String value = record.value();
//        System.out.println(value);
//        System.out.println(record);
//        ack.acknowledge();
//    }*/
//}