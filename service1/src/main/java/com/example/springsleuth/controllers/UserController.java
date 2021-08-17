package com.example.springsleuth.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/users")
public class UserController {

    /**
     * Spring Cloud Sleuth with Brave tracer will provide instrumentation of the incoming request.
     */
    private static final Logger LOG = LoggerFactory.getLogger(UserController.class);

    @Resource
    private WebClient webClient;

    @Resource
    private IntensiveProcessingService intensiveProcessingService;

    @GetMapping
    public ResponseEntity<List> findUsers() throws InterruptedException {
        LOG.info("Getting users list!");
        final var users = webClient
                .get()
                .uri("/users")
                .exchange()
                .block()
                .toEntity(List.class)
                .block();

        LOG.info("Got [{}] users", users.getBody().size());
        LOG.info("Got headers:");
        users.getHeaders()
                .entrySet()
                .stream()
                .forEach(header ->
                        LOG.info("key: [{}] value: [{}]",
                                header.getKey(),
                                header.getValue().stream().collect(Collectors.joining(","))));
        ;

        intensiveProcessingService.cpuIntensiveMethodSimulation();

        return users;
    }
}
