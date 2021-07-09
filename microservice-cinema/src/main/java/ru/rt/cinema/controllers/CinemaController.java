package ru.rt.cinema.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.reactive.function.client.WebClient;
import ru.rt.cinema.domain.Movie;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping
public class CinemaController {

    @Value("${resource-server.api.url}")
    private String cinemaApiUrl;

    private final WebClient webClient;

    @Autowired
    public CinemaController(WebClient webClient) {
        this.webClient = webClient;
    }

    @GetMapping("/model")
    public String getAllFilms(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String sessionId = ((WebAuthenticationDetails) authentication.getDetails()).getSessionId();
        DefaultOidcUser p = (DefaultOidcUser) authentication.getPrincipal();
        model.addAttribute("email", p.getClaims().get("email"));
        model.addAttribute("authorities",
                p.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.joining(", ")));
        model.addAttribute("sessionId", sessionId);

        List<Movie> movies = this.webClient.get()
                .uri(cinemaApiUrl)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<Movie>>() {
                })
                .block();
        model.addAttribute("movies", movies);
        return "model";
    }

    @GetMapping("/test")
    public String getTestPage() {
        return "test";
    }

    @GetMapping("/back-channel-logout")
    public String logout(@RequestParam String issuer, @RequestParam String token, HttpServletRequest request) throws ServletException {
        request.setAttribute("back-channel-logout", true);
        request.setAttribute("issuer", issuer);
        request.setAttribute("token", token);
        request.logout();
        return "redirect:/model";
    }
}
