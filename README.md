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

<p>

- [gitlab-ci](.gitlab-ci.yml) - объемлющая конфигурация с переменными и pipeline цепочкой
- [sso-apps.gitlab-ci](.sso-apps.gitlab-ci.yml) - конфигурация сборки
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
                - application-okd.yml - properties для OKD профиля
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

## Развертывание

Развертывание происходит на [OKD](https://console.apps.okd.stage.digital.rt.ru/topology/ns/java-school?view=graph)

Для модульного проекта подготовлен файл [***gitlab-ci***](.gitlab-ci.yml), включающий:

- Необходимые глобальные переменные
- Цепочку bridge, позволяющая поочередно развертывать модули

---
* Глобальные переменные наследуются во включенные файлы (_include_) ***inherit.variables: true***
* Для каждого модуля необходимо передать дополнительную переменную ***variables.APP_NAME: "module-name"***
* ***Важно!*** порядок развертывания должен быть соблюден (_eureka->resource->services_)
* Отключен локальный активный профиль ***spring.application.profiles.active: local***

Основные конфигурационные настройки стадий build, docker_build, deploy, clean содержаться в 
[***sso-apps.gitlab-ci***](.sso-apps.gitlab-ci.yml)

Локальный запуск и контейнеризация с Docker доступны в ветке:

- [master](https://git.digital.rt.ru/java_school/auth3/ru.project.sso/-/tree/master)

## Ресурсы проекта

- [Trello](https://trello.com/b/JrZkSplq/authboard)

## Команда и контакты

- Байдин Алексей [Telegram@aleksey_baidin](https://t.me/aleksey_baidin)
- Васенин Алексей [Telegram]()
- Третьяков Вячеслав [Telegram@korpatiy](https://t.me/korpatiy)
