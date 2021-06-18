### Инструкция по использованию (на Windows)

##### Подготовка

1. Построить проект:

*gradlew bootJar*

2. (После нового билда) Удалить существующий (если он есть) образ docker-spring-boot-postgres

*docker rmi docker-spring-boot-postgres:latest*

3. Добавить keycloak в качестве хостов (/etc/hosts на Mac/Linux, c:\Windows\System32\Drivers\etc\hosts на Windows)

*127.0.0.1 keycloak*

##### Создание и запуск контейнера

*docker-compose up*

##### Остановка и удаление контейнера

Остановка БД (Terminal): Ctrl+C 

*docker-compose down*

