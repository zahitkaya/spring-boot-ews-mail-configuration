package com.spring.example.bootstrap;

import com.spring.example.service.MailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class Test implements ApplicationRunner {

    @Autowired
    MailService mailService;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        mailService.readMails(5);
    }
}
