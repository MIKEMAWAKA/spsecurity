package com.mike.springsecurityclient.controller;

import java.util.Optional;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mike.springsecurityclient.entity.User;
import com.mike.springsecurityclient.entity.VerificationToken;
import com.mike.springsecurityclient.event.RegistrationCompleteevent;
import com.mike.springsecurityclient.model.PasswordModel;
import com.mike.springsecurityclient.model.UserModel;
import com.mike.springsecurityclient.service.UserService;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
public class RegistrationController {

    @Autowired
    private UserService userService;

    @Autowired
    private ApplicationEventPublisher publisher;

    @PostMapping("/register")
    public String registerUser(@RequestBody UserModel userModel,final HttpServletRequest request){

        User user =userService.registerUser(userModel);
        publisher.publishEvent(new RegistrationCompleteevent(user,applicationurl(request)));
        return "Success";

    }

    private String applicationurl(HttpServletRequest request) {
        return "http://"+
              request.getServerName()+
              ":"+request.getServerPort()+
              request.getContextPath();
    }

    @GetMapping("/verifyRegistration")
    public String verifyRegistration(@RequestParam("token") String token){
        String result= userService.validateRegistrationToken(token);
        if(result.equalsIgnoreCase("valid")){
            return  "User Verifies Successfully";

        }return "Bad User";
    }

    @GetMapping("/resendVerificationToken")
    public String resendVerificationToken(@RequestParam("token") String oldToken,
                                          HttpServletRequest request){
        VerificationToken verificationToken
                =  userService.generateNewVerificationToken(oldToken);                                  
        User user = verificationToken.getUser();
        resendVerificationTokenMail(user,applicationurl(request),verificationToken);
        return "Verification Link sent";


    }

    private void resendVerificationTokenMail(User user, String applicationurl, VerificationToken verificationToken) {

        //send mail to user
        String url = applicationurl+"/verifyRegistration?token="
           +verificationToken.getToken();

    log.info("Click the link to verify you account : {}", url);       


    }

    @PostMapping("/resetPassword")
    public  String resetPassword(@RequestBody PasswordModel passwordModel,HttpServletRequest request){
        String url = "";
        User user  = userService.findUserByEmail(passwordModel.getEmail());

        if(user !=null){
            String token = UUID.randomUUID().toString();
            userService.createPasswordResetTokenForUser(user,token);
            url = passwordResetTokenMail(user,applicationurl(request),token);
        }
        return url;

  

//  log.info("Click the link to verify you account : {}", url); 

    }

    @PostMapping("/savePassword")
    public String savePassword(@RequestParam("token") String token,@RequestBody PasswordModel passwordModel){
        String result = userService.validatePasswordRestToken(token);
        if(!result.equalsIgnoreCase("valid")){
            return "Invalid Token";
        }
        Optional<User> user = userService.getUserByPasswordResetToken(token);
        if(user.isPresent()){
            userService.changePassword(user.get(),passwordModel.getNewPassword());
            return "Password Rest Successfull";
        }else{
            return "Invalid Token";

        }
    

    }

    private String passwordResetTokenMail(User user, String applicationurl, String token) {

       
        log.info("MIKE");

           //send mail to user
           String url = applicationurl+"/savePassword?token="
           +token;

    log.info("Click the link to Reset your Password : {}", url);
    return url;       


    }

    
}
