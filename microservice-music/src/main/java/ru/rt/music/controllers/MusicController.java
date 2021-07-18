package ru.rt.music.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import ru.rt.music.handlers.RestRequestHandler;
import ru.rt.music.services.UserDetailsCollectorService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.Collections;

/**
 * Web Controller для работы с конечными точками сервиса Music
 * <p>
 *
 * @author Alexey Baidin
 * @author Vyacheslav Tretyakov
 */
@Controller
@RequestMapping
public class MusicController {

    private final UserDetailsCollectorService userDetailsCollectorService;

    private final RestRequestHandler restRequestHandler;

    @Autowired
    public MusicController(UserDetailsCollectorService userDetailsCollectorService, RestRequestHandler restRequestHandler) {
        this.userDetailsCollectorService = userDetailsCollectorService;
        this.restRequestHandler = restRequestHandler;
    }

    @GetMapping("/")
    public String mainPage(Model model, Principal principal) {
        model.addAttribute("principal", principal);
        model.addAttribute("songs", restRequestHandler.requestToGetAllSongs());
        return "index";
    }

    @GetMapping("/account")
    public String accountPage(Model model, Authentication authentication) {
        DefaultOidcUser principal = (DefaultOidcUser) authentication.getPrincipal();
        model.addAttribute("principal", principal);
        model.addAttribute("userInfo", userDetailsCollectorService.getUserInfo(principal));
        return "account";
    }

    @GetMapping("/admin")
    public ModelAndView accountPage(Principal principal) {
        return new ModelAndView("admin", Collections.singletonMap("principal", principal));
    }

    @GetMapping("/subscribe")
    public String subscribePage(Model model, Authentication authentication) {
        DefaultOidcUser principal = (DefaultOidcUser) authentication.getPrincipal();
        model.addAttribute("principal", principal);

        boolean hasSubscriberRole = userDetailsCollectorService.isHasSubscriberRole(model, principal);
        model.addAttribute("hasSubscriberRole", hasSubscriberRole);
        model.addAttribute("songs", restRequestHandler.requestToGetAllSongs());
        return "subscribe";
    }

    @GetMapping("/authors")
    public String authorsPage(Model model, Authentication authentication) {
        DefaultOidcUser principal = (DefaultOidcUser) authentication.getPrincipal();
        model.addAttribute("principal", principal);

        boolean hasAuthorRole = principal.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .anyMatch(authority -> authority.contains("ROLE_AUTHOR"));

        model.addAttribute("userName", principal.getName());
        model.addAttribute("hasAuthorRole", hasAuthorRole);
        return "authors";
    }

    @GetMapping("/back-channel-logout")
    public String logout(@RequestParam String issuer, @RequestParam String token, HttpServletRequest request) throws ServletException {
        request.setAttribute("issuer", issuer);
        request.setAttribute("back-channel-logout", true);
        request.setAttribute("token", token);
        request.logout();

        //todo редирект
        return "redirect:/model";
    }
}
