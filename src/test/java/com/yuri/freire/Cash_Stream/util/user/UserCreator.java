package com.yuri.freire.Cash_Stream.util.user;

import com.yuri.freire.Cash_Stream.Authentication.entities.User;

public class UserCreator {
    public static User createValidUser(){
        return User.builder()
                .id(1)
                .firstname("Yuri")
                .lastname("Freire")
                .username("Yuri Freire")
                .email("yuri@email.com")
                .password("Ke6pqw84#")
                .build();
    }

    public static User createUserToBeSaved(){
        return User.builder()
                .firstname("Yuri")
                .lastname("Freire")
                .username("Yuri Freire")
                .email("yuri@email.com")
                .password("Ke6pqw84#")
                .build();
    }
}
