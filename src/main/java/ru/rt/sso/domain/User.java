package ru.rt.sso.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table
@Getter
@Setter
@NoArgsConstructor
public class User {

    @Id
    private long id;
    private String name;
    private String birthdate;
    private String pass;
    private boolean active;


}
