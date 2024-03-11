package com.goldensnitch.qudditch.controller;

import com.goldensnitch.qudditch.service.EmailService;
import com.goldensnitch.qudditch.service.TestService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/test")
public class TestController {
    private final TestService testService;
    private final EmailService emailService;

    @Autowired
    public TestController(TestService testService, EmailService emailService) {
        this.testService = testService;
        this.emailService = emailService;
    }

    @GetMapping("")
    public String test() {
        String test = testService.test();
        log.info("TestController.test: {}", test);
        emailService.sendSingleEmail("kdt@dawoony.com");

        return test;
    }
}
