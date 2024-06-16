package com.example.client.contorller;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutionException;

import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.ListTopicsResult;
import org.apache.kafka.common.KafkaFuture;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaAdmin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.client.model.Person;
import com.example.client.service.PersonService;

@RestController
@RequestMapping("/person")
public class PersonController {
    
    @Autowired
    private PersonService personService;

    @Autowired
    private KafkaAdmin kafkaAdmin;

    @GetMapping("/all")
    public ResponseEntity<Response<List<Person>>> getAll() {
        Response<List<Person>> response = new Response<>();
        response.setData(personService.getAll());
        response.setMessage("Message");
        return ResponseEntity.ok(response);
    }

    @GetMapping("/email")
    public ResponseEntity<Response<List<Person>>> getByEmail(@RequestParam String email) {
        Response<List<Person>> response = new Response<>();
        List<Person> personList = personService.getByEmail(email);
        System.out.println(personList);
        response.setData(personList);
        response.setMessage("Message email");
        return ResponseEntity.ok(response);
    }

    @GetMapping("/firstName")
    public ResponseEntity<Response<List<Person>>> getByFirstName(@RequestParam String firstName) {
        Response<List<Person>> response = new Response<>();
        response.setData(personService.getByFirstName(firstName));
        response.setMessage("Message first name");
        return ResponseEntity.ok(response);
    }

    @GetMapping("/topics")
    public ResponseEntity<List<String>> getTopics() {

        AdminClient adminClient = AdminClient.create(kafkaAdmin.getConfigurationProperties());
        ListTopicsResult listTopicsResult = adminClient.listTopics();
        KafkaFuture<Set<String>> kafkaFuture = listTopicsResult.names();
        List<String> topicNameList = new LinkedList<>();
        try {
            Set<String> topicNames = kafkaFuture.get();
            for(String name : topicNames) {
                System.out.println("Topic Name => " + name);
                topicNameList.add(name);
            }
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (ExecutionException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return ResponseEntity.ok(topicNameList);
    }

    @GetMapping("/kafka/firstName")
    public void getByFirstNameKafka(@RequestParam String firstName) {
        personService.getByFirstNameKafka(firstName);
    }
}
