package ru.rt.cinema.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.reactive.function.client.WebClient;
import ru.rt.cinema.domain.Movie;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.net.HttpURLConnection;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping
public class CinemaController {

    @Value("${resource-server.api.url}")
    private String cinemaApiUrl;

    @Autowired
    private WebClient webClient;

    @GetMapping("/model")
    public String getAllFilms(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        DefaultOidcUser p = (DefaultOidcUser) authentication.getPrincipal();
        model.addAttribute("email", p.getClaims().get("email"));
        model.addAttribute("authorities",
                p.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.joining(", ")));

        List<Movie> movies = this.webClient.get()
                .uri(cinemaApiUrl)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<Movie>>() {
                })
                .block();
        model.addAttribute("movies", movies);
        return "model";
    }

    /*@GetMapping("/logout")
    public void logout(HttpServletRequest request) throws ServletException {
        request.logout();
    }*/
}
