### Инструкция по использованию (на Windows)

##### Подготовка

1. Построить проект:

*gradlew bootJar*

2. (После нового билда) Удалить существующий (если он есть) образ docker-spring-boot-postgres

*docker rmi docker-spring-boot-postgres:latest*
d
3. Добавить keycloak в качестве хостов (/etc/hosts на Mac/Linux, c:\Windows\System32\Drivers\etc\hosts на Windows)

*127.0.0.1 keycloak*

##### Создание и запуск контейнера (detached mode)

*docker-compose up -d*

##### Панель администратора Keycloak

*https://localhost:8180/auth/*

##### Остановка и удаление контейнера

*docker-compose down*

