### Инструкция по использованию (на Windows)

##### Подготовка (текущая директория: <project_dir>/main_service)

1. Построить проект:

*gradlew bootJar*

2. (После нового билда) Удалить существующий (если он есть) образ docker-spring-boot-postgres

*docker rmi docker-spring-boot-postgres:latest*

##### Создание и запуск контейнера основного сервиса (detached mode)

*docker-compose up -d*

##### Панель администратора Keycloak

*https://localhost:8180/auth/*

##### Остановка и удаление контейнера

*docker-compose down*

