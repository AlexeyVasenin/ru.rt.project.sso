package ru.rt.sso.domain;

import lombok.Data;
import javax.persistence.Id;

import java.util.List;



@Data
public class User {

    @Id
    private String userId;
    private String username;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private List<String> roles;



}
