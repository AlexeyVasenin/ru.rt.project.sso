package ru.rt.sso.controller;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import java.security.Principal;

/**
 * Класс обработки ошибок, реализующий {@link ErrorController}
 * <p>
 * Отключение дефолтных страниц ошибки в application.yml error.whitelabel.enabled: false
 *
 * @author Vyacheslav Tretyakov
 */
@Controller
public class CustomErrorController implements ErrorController {

    @GetMapping("/error")
    public String handleError(HttpServletRequest request, Model model, Principal principal) {
        String errorPage = "error";    // default
        model.addAttribute("principal", principal);
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);

        if (status != null) {
            int statusCode = Integer.parseInt(status.toString());

            if (statusCode == HttpStatus.UNAUTHORIZED.value()) {
                errorPage = "error/401";
            } else if (statusCode == HttpStatus.NOT_FOUND.value()) {
                errorPage = "error/404";
            } else if (statusCode == HttpStatus.FORBIDDEN.value()) {
                errorPage = "error/403";
            } else if (statusCode == HttpStatus.INTERNAL_SERVER_ERROR.value()) {
                errorPage = "error/500";
            }
        }

        return errorPage;
    }

    @Override
    public String getErrorPath() {
        return "/error";
    }

}
