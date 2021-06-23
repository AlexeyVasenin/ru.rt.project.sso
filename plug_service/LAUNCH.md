### Инструкция по использованию (на Windows)

##### Подготовка (текущая директория: <project_dir>/plug_service)

1. Построить проект:

*gradlew bootJar*

2. (После нового билда) Удалить существующий (если он есть) образ springio/gs-spring-boot-docker

*docker rmi springio/gs-spring-boot-docker:latest*

##### Создание и запуск контейнеров всех сервисов заглушек (detached mode)

*docker-compose up -d*

##### Остановка и удаление контейнера

*docker-compose down*