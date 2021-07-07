package ru.rt.cinema.sevices;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.util.stream.Collectors;

/**
 * Сервис для сбора информации о пользователе с principal claims
 */
@Service
public class UserDetailsCollectorService {

    /**
     * Собирает готовое представление {@link OidcUserInfo} в модель на UI
     *
     * @param model          UI модель отображения
     * @param authentication токен аутентифицированного участника
     */
    public void getUserDetails(Model model, Authentication authentication) {
        DefaultOidcUser principal = (DefaultOidcUser) authentication.getPrincipal();
        OidcUserInfo userInfo = principal.getUserInfo();

        //String birthdate = userInfo.getBirthdate();

        model.addAttribute("user", userInfo);

        model.addAttribute("authorities",
                principal.getAuthorities().stream()
                        .map(GrantedAuthority::getAuthority)
                        .filter(authority -> authority.contains("ROLE"))
                        //.map(role -> role.replace("ROLE_", ""))
                        .collect(Collectors.joining(", ")));
    }
}
