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
     * Базовые поля (стандарт), представляемые {@link OidcUserInfo} достаются через геттеры example:  ${user.getFullName()},
     * к кастомным же можно обратиться получив claim из объекта example: ${user.getClaim('sub_active')}
     * Таким образом, можно не городить свои модели, а напрямую работать с principal
     *
     * @param model          UI модель отображения
     * @param authentication токен аутентифицированного участника
     */
    public void getUserDetails(Model model, Authentication authentication) {
        DefaultOidcUser principal = (DefaultOidcUser) authentication.getPrincipal();
        OidcUserInfo userInfo = principal.getUserInfo();

        //Кладем в модель юзера
        model.addAttribute("user", userInfo);

        //Роли как String "a, b, c, d"
        model.addAttribute("roles",
                principal.getAuthorities().stream()
                        .map(GrantedAuthority::getAuthority)
                        .filter(authority -> authority.contains("ROLE"))
                        .collect(Collectors.joining(", ")));

        //Скоупы как String
        model.addAttribute("scopes",
                principal.getAuthorities().stream()
                        .map(GrantedAuthority::getAuthority)
                        .filter(authority -> authority.contains("SCOPE"))
                        .collect(Collectors.joining(", ")));
    }
}
