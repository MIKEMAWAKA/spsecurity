package com.mike.springsecurityclient.entity;

import javax.persistence.Entity;

import java.util.Calendar;
import java.util.Date;

import javax.persistence.*;

import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Data
public class PasswordRestToken {
    private static final int EXPRIRATION_TIME = 10;


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id",nullable = false)
    private Long id;

    private String token;

    private Date expirationTime;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id",nullable = false,
    foreignKey = @ForeignKey(name="FK_USER_PASSWORD_TOKEN"))
    public User user;

    public PasswordRestToken(User user,String token){
        super();
        this.token = token;
        this.user = user;
        this.expirationTime = calculateExpiration(EXPRIRATION_TIME);
    }

    public PasswordRestToken(String token){
        super();
        this.token = token;
        this.expirationTime = calculateExpiration(EXPRIRATION_TIME);
    }

    private Date calculateExpiration(int exprirationTime) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(new Date().getTime());
        calendar.add(Calendar.MINUTE, EXPRIRATION_TIME);

        return new Date(calendar.getTime().getTime());
    }


    
    
}
