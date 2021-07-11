package ru.rt.cinema.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import ru.rt.cinema.handlers.RestRequestHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping
public class CinemaController {

    @Autowired
    private RestRequestHandler restRequestHandler;

    @GetMapping("/")
    public String mainPage(Model model, Principal principal) {
        /*Отобразится если principal, положенный в модель не пуст th:if="${principal}"*/
        model.addAttribute("principal", principal);
        model.addAttribute("movies", restRequestHandler.requestToGetAllMovies());

        return "index";
    }

    @GetMapping("/account")
    public String accountPage(Model model, Authentication authentication) {
        DefaultOidcUser principal = (DefaultOidcUser) authentication.getPrincipal();
        model.addAttribute("principal", principal);
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

        model.addAttribute("userInfo", userInfo);
        return "account";
    }

    @GetMapping("/back-channel-logout")
    public String logout(@RequestParam String issuer, @RequestParam String token, HttpServletRequest request) throws ServletException {
        request.setAttribute("back-channel-logout", true);
        request.setAttribute("issuer", issuer);
        request.setAttribute("token", token);
        request.logout();
        return "redirect:/model";
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/admin")
    public ModelAndView accountPage(Principal principal) {
        return new ModelAndView("admin", Collections.singletonMap("principal", principal));
    }

    @PreAuthorize("hasRole('ROLE_SUBSCRIBER')")
    @GetMapping("/subscribe")
    public String subscribePage(Model model, Authentication authentication) {
        DefaultOidcUser principal = (DefaultOidcUser) authentication.getPrincipal();
        model.addAttribute("principal", principal);

        boolean hasSubscriberRole = principal.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .anyMatch(authority -> authority.contains("ROLE_SUBSCRIBER"));

        if (hasSubscriberRole) {
            model.addAttribute("subscriptionEnd", principal.getUserInfo().getClaim("sub_end"));
        }

        model.addAttribute("hasSubscriberRole", hasSubscriberRole);
        model.addAttribute("movies", restRequestHandler.requestToGetAllMovies());

        return "subscribe";
    }

    @GetMapping("/about")
    public ModelAndView aboutPage(Principal principal) {
        return new ModelAndView("subscribe", Collections.singletonMap("principal", principal));
    }
}
