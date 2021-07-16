# Запуск модуля Resource-Server

1. Указать личные данные postgresql в application-local.yml
2. Создать пустую базу данных sso_resource_server локально
3. Запустить приложение
4. Для того, чтобы апдейтнуть данные в таблицах (загрузить байтовые массивы локальных изображений), необходимо прокинуть следующий запрос (например, в Postman):

HTTP /GET localhost:8080/resource-server/api/database?secret=93b7e095-43ef-426d-af76-6814d4a147e0

*Параметр запроса secret - UUID v4 из application.yml

# Проброс запроса в БД на OpenShift

1. https://oauth-openshift.apps.okd.stage.digital.rt.ru/oauth/token/request - получить токен для oc
2. ввести полученный токен в терминал
3. ввести команду: *oc port-forward <идентификатор пода БД> 5433:5432*

где p1:p2 - это прокидываемые порты (p1 - локально, p2 - из контейнера)

4. Поменять в application.yml активный профиль на okd; засетить значения секретов БД в application-okd.yml
5. Запустить приложение
6. Сделать запрос:

HTTP /GET localhost:8080/resource-server/api/database?secret=93b7e095-43ef-426d-af76-6814d4a147e0

