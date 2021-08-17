package com.example.springsleuth.controllers;

import com.example.springsleuth.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    /**
     * Spring Cloud Sleuth with Brave tracer will provide instrumentation of the incoming request.
     */
    private static final Logger LOG = LoggerFactory.getLogger(UserController.class);

    private List<User> users = List.of(
            new User("Miztli", 30),
            new User("Guja", 60));

    @GetMapping
    public List<User> findUsers() {
        LOG.info("Getting users list!");
        return users;
    }
}
