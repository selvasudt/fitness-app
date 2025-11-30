package com.fitness.aiservice.listener;

import com.fitness.aiservice.dto.Activity;
import com.fitness.aiservice.service.RecommendationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ActivityMessageListener {

    @Autowired
    private RecommendationService recommendationService;


    @RabbitListener(queues = "${rabbitmq.queue.name}")
    public void receiveActivityMessage(Activity activity) {
        log.info("Received activity message: {}", activity);
        recommendationService.generateAndSaveRecommendation(activity);
    }
}
