package ru.rt.sso.domain;


import lombok.Data;

import java.util.List;

/**
 * Класс для создания и изменения пользователя через Admin client<br>
 * Пока не используется
 * @author Алексей Васенин
 * */
@Data
@Deprecated
public class User {

    private String userId;
    private String username;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private List<String> roles;

}
