package ru.rt.sso.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.rt.sso.service.UserDetailsCollectorServiceImpl;

import java.security.Principal;

/**
 * Web Controller для работы с конечными точками сервиса Admin client
 * */
@Controller
@RequestMapping
public class ClientController {

    private final UserDetailsCollectorServiceImpl userDetailsCollectorServiceImpl;

    public ClientController(UserDetailsCollectorServiceImpl userDetailsCollectorServiceImpl) {
        this.userDetailsCollectorServiceImpl = userDetailsCollectorServiceImpl;
    }

    @GetMapping("/")
    public String mainPage(Model model, Principal principal) {
        model.addAttribute("principal", principal);

        model.addAttribute("applicationInfo", userDetailsCollectorServiceImpl.getApplicationInfo());

        return "index";
    }

    @GetMapping("/account")
    public String accountPage(Model model, Authentication authentication) {
        DefaultOidcUser principal = (DefaultOidcUser) authentication.getPrincipal();
        model.addAttribute("principal", principal);
        model.addAttribute("userInfo", userDetailsCollectorServiceImpl.getUserInfo(principal));
        return "account";
    }
}
