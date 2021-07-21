package ru.rt.library.services;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.ui.Model;

import java.util.List;
import java.util.Map;

/**
 * Интерфейс, предоставляющий методы для сбора информации о пользователе
 * <p>
 *
 * @author Vyacheslav Tretyakov
 */
public interface UserDetailsCollectorService {

    /**
     * Собирает информацию о пользователе в модель
     * <p>
     *
     * @param principal объект {@link DefaultOidcUser}
     * @return объект {@link List} userInfo, содержащий необходимую информацию о пользователе
     * @author Alexey Baidin
     */
    List<Map.Entry<String, String>> getUserInfo(DefaultOidcUser principal);

    /**
     * Проверяет наличие роли Subscriber
     * @param model модель аттрибутов
     * @param principal объект {@link DefaultOidcUser}
     * @return булево значение: наличие роли
     */
    boolean isHasSubscriberRole(Model model, DefaultOidcUser principal);

    /**
     * Собирает готовое представление {@link OidcUserInfo} в модель на UI
     * <p>
     * Базовые поля (стандарт), представляемые {@link OidcUserInfo} достаются через геттеры example:  ${user.getFullName()},
     * к кастомным же можно обратиться получив claim из объекта example: ${user.getClaim('sub_active')}
     * Таким образом, можно не городить свои модели, а напрямую работать с principal
     *
     * @param model          UI модель отображения
     * @param authentication токен аутентифицированного участника
     * @author Vyachelav Tretyakov
     */
    @Deprecated
    void getUserDetails(Model model, Authentication authentication);
}
