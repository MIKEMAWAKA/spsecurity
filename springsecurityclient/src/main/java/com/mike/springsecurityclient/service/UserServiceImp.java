package com.mike.springsecurityclient.service;

import java.util.Calendar;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.mike.springsecurityclient.entity.PasswordRestToken;
import com.mike.springsecurityclient.entity.User;
import com.mike.springsecurityclient.entity.VerificationToken;
import com.mike.springsecurityclient.model.UserModel;
import com.mike.springsecurityclient.repository.PasswordResetTokenRepository;
import com.mike.springsecurityclient.repository.UserRepository;
import com.mike.springsecurityclient.repository.VerificationTokenRepository;

@Service
public class UserServiceImp implements UserService{
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private VerificationTokenRepository verificationTokenRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private PasswordResetTokenRepository passwordResetTokenRepository;

    @Override
    public User registerUser(UserModel userModel) {
        User user = new User();
        user.setEmail(userModel.getEmail());
        user.setFirstName(userModel.getFirstName());
        user.setLastName(userModel.getLastName());
        user.setRole("USER");
        user.setPassword(passwordEncoder.encode(userModel.getPassword()));
      
        userRepository.save(user);
        return user;
    }

    @Override
    public void saveVerificationTokenForUser(String token, User user) {

       VerificationToken verificationToken = new VerificationToken(user,token);
       verificationTokenRepository.save(verificationToken);

    }

    @Override
    public String validateRegistrationToken(String token) {

        VerificationToken verificationToken 
             = verificationTokenRepository.findByToken(token);

        if(verificationToken==null)  {
            return "invalid";
        } 
        User user = verificationToken.getUser();
        Calendar calendar = Calendar.getInstance();
        if((verificationToken.getExpirationTime().getTime()
           -calendar.getTime().getTime())<=0){
            verificationTokenRepository.delete(verificationToken);
            return "expired";

           }
           user.setEnabled(true);
           userRepository.save(user);

  
        return "valid";
    }

    @Override
    public VerificationToken generateNewVerificationToken(String oldToken) {
        VerificationToken verificationToken =
           verificationTokenRepository.findByToken(oldToken);
           verificationToken.setToken(UUID.randomUUID().toString());
           verificationTokenRepository.save(verificationToken);
        return verificationToken;
    }

    @Override
    public User findUserByEmail(String email) {


        return userRepository.findByEmail(email);
    }

    @Override
    public void createPasswordResetTokenForUser(User user, String token) {
      
    PasswordRestToken passwordRestToken = new PasswordRestToken(user,token);
    passwordResetTokenRepository.save(passwordRestToken);
    }

    @Override
    public String validatePasswordRestToken(String token) {
      
        PasswordRestToken passwordRestToken 
        = passwordResetTokenRepository .findByToken(token);

        if(passwordRestToken ==null)  {
            return "invalid";
        } 
        User user = passwordRestToken .getUser();
        Calendar calendar = Calendar.getInstance();
        if((passwordRestToken .getExpirationTime().getTime()
            -calendar.getTime().getTime())<=0){
            passwordResetTokenRepository.delete(passwordRestToken );
            return "expired";

            }
            // user.setEnabled(true);
            // userRepository.save(user);


            return "valid";
    }

    @Override
    public Optional<User> getUserByPasswordResetToken(String token) {
  
        return Optional.ofNullable(passwordResetTokenRepository.findByToken(token).getUser());
        
  
    }

    @Override
    public void changePassword(User user, String newPassword) {
       user.setPassword(passwordEncoder.encode(newPassword));
       userRepository.save(user);
        
    }
    
}
