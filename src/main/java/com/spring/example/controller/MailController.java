package com.spring.example.controller;

import com.spring.example.service.MailService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/mails")
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
@RequiredArgsConstructor
public class MailController {

    MailService mailService;

    @GetMapping("/fetch")
    public List<Map> fetchMails(@RequestParam int size){
        return mailService.readMails(size);
    }

    @GetMapping("test")
    public String test(@RequestParam String ewcServiceUrl, @RequestParam String username,@RequestParam String password,@RequestParam(required = false) String domain){
        return mailService.testMailAccount(username,password,domain,ewcServiceUrl);
    }

    @GetMapping("/fetch/unread")
    public List<Map> fetchUnreadMails(){
        return mailService.readAllUnreadMails();
    }

    @GetMapping("/send")
    public void sendMail(@RequestParam String body,@RequestParam String subject,@RequestParam String receipent,@RequestParam String from){
        mailService.sendEmailMessage(body, subject, receipent, from);
    }
}
