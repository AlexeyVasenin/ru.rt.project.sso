package ru.rt.library.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import ru.rt.library.handlers.RestRequestHandler;
import ru.rt.library.handlers.RestRequestHandlerImpl;
import ru.rt.library.services.UserDetailsCollectorService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.Collections;

/**
 * Web Controller для работы с конечными точками сервиса Library
 * <p>
 *
 * @author Alexey Baidin
 * @author Vyacheslav Tretyakov
 */
@Controller
@RequestMapping
public class LibraryController {

    private final UserDetailsCollectorService userDetailsCollectorService;

    private final RestRequestHandler restRequestHandler;

    @Autowired
    public LibraryController(UserDetailsCollectorService userDetailsCollectorService, RestRequestHandler restRequestHandler) {
        this.userDetailsCollectorService = userDetailsCollectorService;
        this.restRequestHandler = restRequestHandler;
    }

    @GetMapping("/")
    public String mainPage(Model model, Principal principal) {
        model.addAttribute("principal", principal);
        model.addAttribute("books", restRequestHandler.requestToGetAllBooks());
        return "index";
    }

    @GetMapping("/account")
    public String accountPage(Model model, Authentication authentication) {
        DefaultOidcUser principal = (DefaultOidcUser) authentication.getPrincipal();
        model.addAttribute("principal", principal);
        model.addAttribute("userInfo", userDetailsCollectorService.getUserInfo(principal));
        return "account";
    }

    @GetMapping("/subscribe")
    public String subscribePage(Model model, Authentication authentication) {
        DefaultOidcUser principal = (DefaultOidcUser) authentication.getPrincipal();
        model.addAttribute("principal", principal);

        boolean hasSubscriberRole = userDetailsCollectorService.isHasSubscriberRole(model, principal);
        model.addAttribute("hasSubscriberRole", hasSubscriberRole);
        model.addAttribute("books", restRequestHandler.requestToGetAllBooks());

        return "subscribe";
    }

    @GetMapping("/admin")
    public ModelAndView accountPage(Principal principal) {
        return new ModelAndView("admin", Collections.singletonMap("principal", principal));
    }

    @GetMapping("/back-channel-logout")
    public String logout(@RequestParam String issuer, @RequestParam String token, HttpServletRequest request) throws ServletException {
        request.setAttribute("back-channel-logout", true);
        request.setAttribute("issuer", issuer);
        request.setAttribute("token", token);
        request.logout();

        // WARN: redirect по внешнему запросу не срабатывает!
        return "redirect:/model";
    }
}
