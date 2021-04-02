package com.example.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class User {
    private String firstName;

//    qaysi qadamdaligini bilish uchun bosh menu step=0
    private int step;

    public User(String firstName) {
        this.firstName = firstName;
    }
}

