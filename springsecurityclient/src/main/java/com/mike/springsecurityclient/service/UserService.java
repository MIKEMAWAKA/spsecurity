package com.mike.springsecurityclient.service;


import java.util.Optional;

import com.mike.springsecurityclient.entity.User;
import com.mike.springsecurityclient.entity.VerificationToken;
import com.mike.springsecurityclient.model.UserModel;

public interface UserService {

	User registerUser(UserModel userModel);

    void saveVerificationTokenForUser(String token, User user);

    String validateRegistrationToken(String token);

    VerificationToken generateNewVerificationToken(String oldToken);

    User findUserByEmail(String email);

    void createPasswordResetTokenForUser(User user, String token);

    String validatePasswordRestToken(String token);

    Optional<User> getUserByPasswordResetToken(String token);

    void changePassword(User user, String newPassword);
    
}
