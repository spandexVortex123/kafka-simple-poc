package com.example.client;

import java.lang.reflect.Type;
import java.time.Duration;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

@Component
public class Utils {
    
    public List<Person> getData(String firstName) {
        String topic = "custom-topic";
        Properties properties = new Properties();
        properties.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "192.168.1.14:9092");
        properties.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.setProperty(ConsumerConfig.GROUP_ID_CONFIG, "custom-topic-group-id");
        // properties.setProperty(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

        // create consumer
        KafkaConsumer<String, String> kafkaConsumer = new KafkaConsumer<>(properties);
        kafkaConsumer.subscribe(Collections.singleton(topic));

        // call api
        RestTemplate restTemplate = new RestTemplate();
        String url = "http://192.168.1.14:8080/person/kafka/firstName?firstName=" + firstName;
        restTemplate.getForEntity(url, Void.class);

        // poll indefinitely
        while (true) {
            ConsumerRecords<String, String> consumerRecords = kafkaConsumer.poll(Duration.ofMillis(100));
            for (ConsumerRecord<String, String> consumerRecord : consumerRecords) {
                System.out.println(" KEY => " + consumerRecord.key());
                // System.out.println(" VALUE => " + consumerRecord.value());
                String value = consumerRecord.value();
                if (value != null && value != "") {
                    Gson gson = new Gson();
                    Type listType = new TypeToken<List<Person>>() {}.getType();
                    List<Person> personList = gson.fromJson(value, listType);
                    kafkaConsumer.close();
                    return personList;
                }
            }
        }
    }
}
