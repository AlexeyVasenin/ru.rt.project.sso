package ru.rt.library.controllers;

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
import ru.rt.library.domain.Book;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/library")
public class LibraryController {

    @Value("${resource-server.api.url}")
    private String libraryApiUrl;

    @Autowired
    private WebClient webClient;

    @GetMapping
    public String getAllBooks(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        DefaultOidcUser p = (DefaultOidcUser) authentication.getPrincipal();
        model.addAttribute("email", p.getClaims().get("email"));
        model.addAttribute("authorities",
                p.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.joining(", ")));

        List<Book> books = this.webClient.get()
                .uri(libraryApiUrl)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<Book>>() {
                })
                .block();
        model.addAttribute("books", books);
        return "index";
    }
}
