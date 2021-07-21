### Инструкция по использованию (на Windows)

#### Подготовка

1. Построить проект:

```
gradlew assemble
```

2. Удалить существующие образы в Docker (при необходимости)

##### Создание и запуск контейнеров

1. Контейнеризация keycloak и БД [docker-compose-keycloak](#docker-compose-keycloak.ym)

```
docker-compose -f docker-compose-keycloak.yml -p sso_project_keycloak up
```

2. Контейнеризация микросервисов [docker-compose](docker-compose.yml)

```
docker-compose up --build
```

* Рекомендация: запускать в detached mode ```-d```

##### Остановка и удаление контейнеров

```
docker-compose down
```

##### Остановка и удаление keycloak и БД

```
docker-compose -f docker-compose-keycloak.yml -p sso_project_keycloak down
```