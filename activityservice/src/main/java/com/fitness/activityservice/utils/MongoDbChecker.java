package com.fitness.activityservice.utils;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

@Component
public class MongoDbChecker {
    public MongoDbChecker(MongoTemplate mongoTemplate) {
        System.out.println("MongoDB database in use: " + mongoTemplate.getDb().getName());
    }
}
