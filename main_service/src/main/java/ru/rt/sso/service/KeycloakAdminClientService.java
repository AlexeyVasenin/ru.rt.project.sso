package ru.rt.sso.service;

import org.keycloak.representations.idm.ClientRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;

import java.util.List;

/**
 * Интерфейс предоставляет методы для работы из Admin client<br>
 * Для получения информации из системы и внесения изменений
 *
 * @author Алексей Васенин
 * */

public interface KeycloakAdminClientService {

    /**
     * Создание нового пользователя в системе. <br>
     * Принимает два параметра
     *
     * @param userName логин пользователя
     * @param pass     пароль
     * @return объект в виде нового пользователя со всеми параметрами системы Keycloak
     */
    Object addUser(String userName, String pass);

    /**
     * Обновление данных пользователя
     */
    @Deprecated
    String updateUser(String userName, String description);

    /**
     * Удаление пользователя из системы
     *
     * @param userName логин пользователя
     */
    String deleteUser(String userName);

    /**
     * Получение всех пользователей в системе
     *
     * @return Список всех пользователей из система
     */
    List<UserRepresentation> getUsers();

    /**
     * Получение всех клиентов(сервисов)
     *
     * @return Список клиентов(сервисов) в системе
     */
    List<ClientRepresentation> getAllClient();

    /**
     * Создание нового клиента(сервиса) в системе
     *
     * @param clientId название клиента(сервиса)
     */
    Object createClient(String clientId);

    /**
     * Создание Роли в клиенте(сервисе)
     *
     * @param roleName название роли
     * @param clientId Id клиента(сервиса)
     */
    void createRoleInClient(String roleName, String clientId);

    /**
     * Назначить роль клиента пользователю
     *
     * @param userName логин пользователя
     * @param clientId Id клиента(сервиса). Пример: (cda63aba-3337-49de-b2b9-e7e04756df29)
     * @param role     название роли
     */
    String assingAClientRoleToTheUser(String userName, String clientId, String role);

    /**
     * Отключить роли сервиса у пользователя
     *
     * @param userName логин пользователя
     * @param clientId Id клиента(сервиса) Пример: (cda63aba-3337-49de-b2b9-e7e04756df29)
     * @param role     название роли
     */
    String deleteAClientRoleToTheUser(String userName, String clientId, String role);

    /**
     * Получение ролей Клиента(Сервиса)
     *
     * @param clientId имя клиента(сервиса)
     * @return Список ролей в клиенте(сервисе)
     */
    List<RoleRepresentation> getTheClientRoles(String clientId);

    /**
     * Получение списка ролей пользователя в определенном клиенте
     *
     * @param userName логин пользователя
     * @param clientId имя клиента(сервиса)
     * @return Список ролей пользователя в сервисе
     * @see RoleRepresentation
     */
    Object getRolesByUsername(String userName, String clientId);

}
