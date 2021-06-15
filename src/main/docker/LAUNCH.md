### Инструкция по использованию (на Windows)

##### Подготовка

1. Построить проект:

*gradlew bootJar*

2. Копировать jar в docker-директорию

*copy <abs_path_to_jar> <abs_path_to_project_root>\src\main\docker\build.jar*

*abs_path_to_jar = <abs_path_to_project_root>\build\libs\<jar_name>.jar

3. Удалить существующий (если он есть) образ docker-spring-boot-postgres

*docker rmi docker-spring-boot-postgres:latest*

##### Создание и запуск контейнера

*cd src\main\docker*

*docker-compose up*

##### Остановка и удаление контейнера

Остановка БД (Terminal): Ctrl+C 

*docker-compose down*

