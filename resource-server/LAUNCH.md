# Запуск модуля Resource-Server

1. Указать личные данные postgresql в application-local.yml
2. Создать пустую базу данных sso_resource_server локально
3. Запустить приложение
4. Для того, чтобы апдейтнуть данные в таблицах (загрузить байтовые массивы локальных изображений), необходимо прокинуть следующий запрос (например, в Postman):

HTTP /GET localhost:8080/resource-server/api/database?secret=93b7e095-43ef-426d-af76-6814d4a147e0

*Параметр запроса secret - UUID v4 из application.yml

