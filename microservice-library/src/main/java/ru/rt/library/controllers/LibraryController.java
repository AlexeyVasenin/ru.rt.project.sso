package ru.rt.library.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.servlet.ModelAndView;
import ru.rt.library.domain.Book;
import ru.rt.library.handlers.RestRequestHandler;
import ru.rt.library.services.UserDetailsCollectorService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.annotation.security.RolesAllowed;
import java.security.Principal;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping
public class LibraryController {

    @Autowired
    private UserDetailsCollectorService userDetailsCollectorService;

    @Autowired
    private RestRequestHandler restRequestHandler;

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
        model.addAttribute("books", restRequestHandler.requestToGetAllBooks());

        return "subscribe";
    }

    @GetMapping("/about")
    public ModelAndView aboutPage(Principal principal) {
        return new ModelAndView("subscribe", Collections.singletonMap("principal", principal));
    }
}
