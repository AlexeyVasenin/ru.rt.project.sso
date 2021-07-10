package ru.rt.cinema.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import ru.rt.cinema.handlers.RestRequestHandler;
import ru.rt.cinema.sevices.UserDetailsCollectorService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.Collections;

@Controller
@RequestMapping
public class CinemaController {

    @Autowired
    private RestRequestHandler restRequestHandler;

    @Autowired
    UserDetailsCollectorService userDetailsCollectorService;

    @GetMapping("/")
    public String mainPage(Model model, Principal principal) {
        /*Отобразится если principal, положенный в модель не пуст th:if="${principal}"*/
        model.addAttribute("principal", principal);
        model.addAttribute("movies", restRequestHandler.requestToGetAllMovies());

        return "index";
    }

    @GetMapping("/account")
    public String accountPage(Model model, Authentication authentication) {
        model.addAttribute("principal", authentication.getPrincipal());
        userDetailsCollectorService.getUserDetails(model, authentication);
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
    public ModelAndView subscribePage(Principal principal) {
        return new ModelAndView("subscribe", Collections.singletonMap("principal", principal));
    }

    @GetMapping("/about")
    public ModelAndView aboutPage(Principal principal) {
        return new ModelAndView("subscribe", Collections.singletonMap("principal", principal));
    }
}
