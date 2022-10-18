package com.mike.springsecurityclient.event;

import org.springframework.context.ApplicationEvent;

import com.mike.springsecurityclient.entity.User;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegistrationCompleteevent extends ApplicationEvent{

    public User user;
    public String applicationUrl;
    public RegistrationCompleteevent(User user,String applicationUrl) {
        super(user);
        this.user = user;
        this.applicationUrl = applicationUrl;
       
    }
    
}
