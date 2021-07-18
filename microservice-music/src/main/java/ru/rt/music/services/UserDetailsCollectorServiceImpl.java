package ru.rt.music.services;

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
 * Сервис для сбора информации, реализующий {@link UserDetailsCollectorService}
 * <p>
 *
 * @author Vyacheslav Tretyakov
 */
@Service
public class UserDetailsCollectorServiceImpl implements UserDetailsCollectorService {

    public List<Map.Entry<String, String>> getUserInfo(DefaultOidcUser principal) {
        OidcUserInfo oidcUserInfo = principal.getUserInfo();
        List<Map.Entry<String, String>> userInfo = new ArrayList<>();
        userInfo.add(new AbstractMap.SimpleEntry<>("Username:", oidcUserInfo.getPreferredUsername()));
        userInfo.add(new AbstractMap.SimpleEntry<>("Full name:", oidcUserInfo.getFullName()));
        userInfo.add(new AbstractMap.SimpleEntry<>("Email:", oidcUserInfo.getEmail()));
        // reviews_count - Integer
        Object musicalPreferences = oidcUserInfo.getClaim("musical_preferences");
        if (musicalPreferences != null) {
            userInfo.add(new AbstractMap.SimpleEntry<>("Musical preferences:", musicalPreferences.toString().replace("[", "").replace("]", "")));
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

    public boolean isHasSubscriberRole(Model model, DefaultOidcUser principal) {
        boolean hasSubscriberRole = principal.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .anyMatch(authority -> authority.contains("ROLE_SUBSCRIBER"));

        if (hasSubscriberRole) {
            model.addAttribute("subscriptionEnd", principal.getUserInfo().getClaim("sub_end"));
        }
        return hasSubscriberRole;
    }

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
