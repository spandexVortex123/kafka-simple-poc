package com.example.client.service;

import java.util.List;
import java.util.Properties;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.client.model.Person;
import com.example.client.repository.PersonRepository;
import com.google.gson.Gson;

@Service
public class PersonService {

    @Autowired
    private PersonRepository personRepository;

    public List<Person> getAll() {
        return personRepository.findAll();
    }

    public List<Person> getByEmail(String email) {
        List<Person> personList = personRepository.getByEmailId(email);
        System.out.println(personList);
        return personList;
    }

    public List<Person> getByFirstName(String firstName) {
        return personRepository.getByFirstName(firstName.toLowerCase());
    }

    // sends data to kafka topic
    public void getByFirstNameKafka(String firstName) {
        // set properties
        Properties properties = new Properties();
        properties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "192.168.1.14:9092");
        properties.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        
        // create producer object
        KafkaProducer<String, String> kafkaProducer = new KafkaProducer<>(properties);

        // get data
        List<Person> personList = personRepository.getByFirstName(firstName.toLowerCase());
        // convert object to stringified json
        Gson gson = new Gson();
        String jsonString = gson.toJson(personList);
        System.out.println("STRING VERSION OF DATA " + jsonString);

        // producer record
        ProducerRecord<String, String> producerRecord = new ProducerRecord<String,String>("custom-topic", firstName, jsonString);
        // send to topic
        kafkaProducer.send(producerRecord);
        kafkaProducer.flush();
        kafkaProducer.close();
    }
    
}
