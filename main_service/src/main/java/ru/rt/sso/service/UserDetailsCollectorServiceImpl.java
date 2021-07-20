package ru.rt.sso.service;

import com.netflix.discovery.shared.Application;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Сервис для сбора информации о пользователе с principal claims
 */
@Service
public class UserDetailsCollectorServiceImpl implements UserDetailsCollectorService {

    private final RestRequestHandler restRequestHandler;

    @Autowired
    public UserDetailsCollectorServiceImpl(RestRequestHandler restRequestHandler) {
        this.restRequestHandler = restRequestHandler;
    }

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
    @Override
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

    @Override
    public List<Map.Entry<String, String>> getUserInfo(DefaultOidcUser principal) {
        OidcUserInfo oidcUserInfo = principal.getUserInfo();
        List<Map.Entry<String, String>> userInfo = new ArrayList<>();
        userInfo.add(new AbstractMap.SimpleEntry<>("Username:", oidcUserInfo.getPreferredUsername()));
        userInfo.add(new AbstractMap.SimpleEntry<>("Full name:", oidcUserInfo.getFullName()));
        userInfo.add(new AbstractMap.SimpleEntry<>("Email:", oidcUserInfo.getEmail()));
        // reviews_count - Integer
        Object reviewCounts = oidcUserInfo.getClaim("reviews_count");
        if (reviewCounts != null) {
            userInfo.add(new AbstractMap.SimpleEntry<>("Review counts:", reviewCounts.toString()));
        }
        // sub_active - Boolean
        Object subActive = oidcUserInfo.getClaim("sub_active");
        if (subActive != null) {
            userInfo.add(new AbstractMap.SimpleEntry<>("Is subscription active?", (Boolean) subActive ? "Yes" : "No"));
        }

        List<String> roles = principal.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .filter(authority -> authority.contains("ROLE"))
                .collect(Collectors.toList());
        List<String> scopes = principal.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .filter(authority -> authority.contains("SCOPE"))
                .collect(Collectors.toList());

        if (roles.contains("ROLE_SUBSCRIBER")) {
            userInfo.add(new AbstractMap.SimpleEntry<>("End of subscription:", oidcUserInfo.getClaim("sub_end")));
            userInfo.add(new AbstractMap.SimpleEntry<>("Level of subscription:", oidcUserInfo.getClaim("sub_lvl")));
        }
        userInfo.add(new AbstractMap.SimpleEntry<>("Roles:", String.join(", ", roles)));
        userInfo.add(new AbstractMap.SimpleEntry<>("Scopes:", String.join(", ", scopes)));
        return userInfo;
    }

    @Override
    public List<Map.Entry<String, String>> getApplicationInfo() {
        List<Map.Entry<String, String>> applicationInfo = new ArrayList<>();

        List<Application> allApplications = restRequestHandler.getAllApplications();

        allApplications.forEach(x -> {
            String name = x.getName();
            String url = x.getInstances().get(0).getStatusPageUrl().replace("/actuator/info", "");

            if (!name.equals("MAIN_SERVICE")) {
                applicationInfo.add(new AbstractMap.SimpleEntry<>(name, url));
            }
        });

        return applicationInfo;
    }
}
