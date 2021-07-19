package ru.rt.sso.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.rt.sso.service.UserDetailsCollectorService;

import java.security.Principal;

// todo описание
@Controller
@RequestMapping
public class ClientController {

    private final UserDetailsCollectorService userDetailsCollectorService;

    public ClientController(UserDetailsCollectorService userDetailsCollectorService) {
        this.userDetailsCollectorService = userDetailsCollectorService;
    }

    @GetMapping("/")
    public String mainPage(Model model, Principal principal) {
        model.addAttribute("principal", principal);
        return "index";
    }

    @GetMapping("/account")
    public String accountPage(Model model, Authentication authentication) {
        DefaultOidcUser principal = (DefaultOidcUser) authentication.getPrincipal();
        model.addAttribute("principal", principal);
        model.addAttribute("userInfo", userDetailsCollectorService.getUserInfo(principal));
        return "account";
    }
}
