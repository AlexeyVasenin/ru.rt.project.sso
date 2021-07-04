### Инструкция по использованию (на Windows)

#### Подготовка 

1. Построить проект:

gradlew assemble

2. (После нового билда) Удалить измененные образы в Docker

##### Создание и запуск контейнера основного сервиса (detached mode)

#### Запуск 2 контейнеров

##### Создание и запуск контейнера с микросервисами (detached mode)

docker-compose up -d

##### Остановка и удаление первого контейнера

docker-compose down

##### Запустить второй docker-compose (с keycloak и БД)

docker-compose -f docker-compose-keycloak.yml -p sso_project_keycloak up -d

##### Остановка и удаление второго контейнера

docker-compose -f docker-compose-keycloak.yml -p sso_project_keycloak down
