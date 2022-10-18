package com.mike.springsecurityclient.entity.listener;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import com.mike.springsecurityclient.entity.User;
import com.mike.springsecurityclient.event.RegistrationCompleteevent;
import com.mike.springsecurityclient.service.UserService;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class RegistrationCompleteListener implements 
ApplicationListener<RegistrationCompleteevent>{

    @Autowired
    private UserService userService;

    @Override
    public void onApplicationEvent(RegistrationCompleteevent event) {
     //create token

     User user = event.getUser();
     String token = UUID.randomUUID().toString();

     userService.saveVerificationTokenForUser(token,user);





        //send mail to user
        String url = event.getApplicationUrl()+"/verifyRegistration?token="
           +token;

    log.info("Click the link to verify you account : {}", url);       


    }
    
}
