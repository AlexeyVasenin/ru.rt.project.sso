package ru.rt.sso.domain;


import lombok.Data;

import java.util.List;

// todo описание
@Data
public class User {

    private String userId;
    private String username;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private List<String> roles;

}
