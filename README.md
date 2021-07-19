# <h1 align="center">Автори3ация</h1>

- [Автори3ация](#автори3ация-1)
    - [Описание / ТЗ проекта](#описание-/-ТЗ-проекта)
    - [Структура проекта](#структура-проекта)
    - [Технологии](#технологии)
    - [Ресурсы проекта](#ресурсы-проекта)
    - [Команда и контакты](#команда-и-контакты)

## Описание / ТЗ проекта

_Есть несколько различных систем (с разными адресами). Необходимо разработать единую точку входа, чтобы когда
пользователь авторизовался в одной системе он автоматический получал доступ во все связанные системы. Необходимо
предусмотреть наличие системы прав и учесть что профиль пользователя может быть расширен в конкретных системах набором
специфичных полей_

## Структура проекта

**Объемлющий проект**:

- ***Модули***:

    - ***eureka-server*** - service discovery для микросервисов (клиентов)
    - ***resource-server*** - защищает ресурсы (стат., дин.) с помощью токенов OAuth
    - ***main_service*** - сервис администрирования
    - ***microservice-cinema*** - сервис-заглушка
    - ***microservice-library*** - сервис-заглушка
    - ***microservice-music*** - сервис-заглушка

- [docker-compose](docker-compose.yml) - развертывание сервисов
- [docker-compose-keycloak](docker-compose-keycloak.yml) - развертывание keycloak & database
- [readme](README.md) - current readme
- [launch](LAUNCH.md) - инструкция развертки с использованием Docker

Структура проектов по gradle:

- **microservice**
    - src
        - main
            - java
                - ru.rt.microservice:
                    - configs
                    - controllers
                    - domain
                    - handles
                    - services
            - resources:
                - static:
                    - css - pages style
                    - js - scripts
                - templates:
                    - error - страницы ошибок
                - application.yml - properties file
                - application-local.yml - properties для локального профиля
    - Dockerfile
- ****resource-server***
    - sources:
        - +repos - JPA репозитории
        - +utils - утилиты
    - resources:
        - +db.changelog- логи БД
        - +static.images - статические картинки (перенесены в БД)

## Технологии

**ЯП**: Java v11

**SDK**: Java SDK v11

**СУБД**: PostgreSQL

**Система управления миграциями БД**: Liquibase

**Система управления идентификацией и доступом (Сервер Авторизации)**: Keycloack

**Фреймворки**:

- Spring
    - Boot
        - OAuth2 client / server
        - JPA
    - Cloud
        - Netflix Eureka client / server
    - Security
- Swagger

**Инструменты разработки**: Lombok

**Web шаблонизатор**: Thymeleaf

**Развертывание и контейнеризация**: Docker, OpenShift

## Установка и запуск

- [Docker Launch](LAUNCH.md)

Организация Gitlab CI и развертывание на OpenShift доступны в ветке:

- [Stage](https://git.digital.rt.ru/java_school/auth3/ru.project.sso/-/tree/stage)

## Ресурсы проекта

- [Trello](https://trello.com/b/JrZkSplq/authboard)

## Команда и контакты

- Байдин Алексей [Telegram@aleksey_baidin](https://t.me/aleksey_baidin)
- Васенин Алексей [Telegram]()
- Третьяков Вячеслав [Telegram@korpatiy](https://t.me/korpatiy)
