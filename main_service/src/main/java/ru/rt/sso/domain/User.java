package ru.rt.sso.domain;

import lombok.Data;
import javax.persistence.Id;

import java.util.List;



@Data
public class User {

    @Id
    private String id;
    private String username;
    private String email;
    private List<String> roles;



}
