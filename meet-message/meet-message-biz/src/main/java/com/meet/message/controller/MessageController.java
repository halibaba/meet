package com.meet.message.controller;

import com.meet.message.handler.KafkaSendResultHandler;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * 消息发送测试
 *
 * @author caixin
 * @date 2022-10-20 08:11:17
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/message")
@Api(value = "message", tags = "消息控制层")
public class MessageController {

	@Value("${topic.name}")
	private String TOPIC_NAME;

	@Autowired
	private KafkaTemplate<String, String> kafkaTemplate;

	@Autowired
	private KafkaSendResultHandler producerListener;

//	/**
//	 * 消息发送
//	 *
//	 * @return R
//	 */
//	@GetMapping("/{sessionKey}/{content}")
//	@ApiOperation(value = "消息发送", notes = "消息发送")
//	public R send(@PathVariable("sessionKey") String sessionKey, @PathVariable("content") String content) {
//		RedisMessageDistributor messageDistributor = SpringContextHolder.getBean(RedisMessageDistributor.class);
//
//		// websocket 发送消息
//		MessageDO messageDO = new MessageDO();
//		messageDO.setNeedBroadcast(Boolean.FALSE);
//		// 给目标用户ID
//		messageDO.setSessionKeys(Lists.newArrayList(sessionKey));
//		messageDO.setMessageText(content);
//		messageDistributor.distribute(messageDO);
//		return R.ok(true);
//	}

	/**
	 * 发送消息的时候需要休眠一下，否则发送时间较长的时候会导致进程提前关闭导致无法调用回调时间。主要是因为KafkaTemplate发送消息是采取异步方式发送的
	 */
	@RequestMapping("/send")
	@SneakyThrows
	public void send() {
		kafkaTemplate.setProducerListener(producerListener);
		kafkaTemplate.send(TOPIC_NAME,  "key", "test message send~");
		Thread.sleep(1000);
	}


}
