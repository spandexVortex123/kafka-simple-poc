package com.example.client.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.common.config.TopicConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaAdmin;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;

@Component
public class Startup {

    @Autowired
    KafkaAdmin kafkaAdmin;

    private static final String topicName = "custom-topic";

    @PostConstruct
    public void createTopics() {
        Map<String, String> topicConfig = new HashMap<>();
        topicConfig.put(TopicConfig.RETENTION_MS_CONFIG, String.valueOf(24 * 60 * 60 * 1000));
        Map<Integer, List<Integer>> ra = new HashMap<>();
        List<Integer> iList = new ArrayList<>();
        iList.add(1);
        ra.put(1, iList);
        NewTopic newTopic = new NewTopic(topicName, 10, (short) 10).configs(topicConfig);
        System.out.println(" [ START => CREATING TOPIC {" + topicName + "} ] ");
        try {
            AdminClient adminClient = AdminClient.create(kafkaAdmin.getConfigurationProperties());
            adminClient.createTopics(Collections.singleton(newTopic));
            System.out.println(" [ TOPIC SUCCESSFULLY CREATED ] ");

        } catch(Exception e) {
            System.out.println(" [ RECEIVED EXCEPTION CREATING TOPIC ] ");
            e.printStackTrace();
        }
        System.out.println(" [ END => CREATING TOPIC {" + topicName + "} ] ");
    }
    
}
