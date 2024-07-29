package com.meet.websocket.two.handler;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.springframework.kafka.support.ProducerListener;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

/**
 * @program: meet-boot
 * @ClassName KafkaSendResultHandler
 * @description:
 * @author: MT
 * @create: 2022-12-17 09:25
 **/
@Component
@Slf4j
public class KafkaSendResultHandler implements ProducerListener {

    @Override
    public void onSuccess(ProducerRecord producerRecord, RecordMetadata recordMetadata) {
        log.info("Message send success : " + producerRecord.toString());
    }

    public void onError(ProducerRecord producerRecord, @Nullable RecordMetadata recordMetadata, Exception exception) {
        log.info("Message send error : " + producerRecord.toString());
    }
}