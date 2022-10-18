package com.mike.springsecurityclient.entity;

import java.util.Calendar;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
public class VerificationToken {

    private static final int EXPRIRATION_TIME = 10;


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id",nullable = false)
    private Long id;

    private String token;

    private Date expirationTime;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id",nullable = false,
    foreignKey = @ForeignKey(name="FK_USER_VERIFY_TOKEN"))
    public User user;

    public VerificationToken(User user,String token){
        super();
        this.token = token;
        this.user = user;
        this.expirationTime = calculateExpiration(EXPRIRATION_TIME);
    }

    public VerificationToken(String token){
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
