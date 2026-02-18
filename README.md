# SQL Homework

## Запуск инфраструктуры
```bash
# Запустить MySQL в Docker
docker-compose up -d
```

## Запуск приложения

### В отдельном терминале запустить SUT

```bash
java -jar app-deadline.jar \
-P:jdbc.url=jdbc:mysql://localhost:3306/app \
-P:jdbc.user=app \
-P:jdbc.password=pass
```

## Запуск тестов

```bash
# В третьем терминале выполнить
./gradlew test
```

## Доступ
* Приложение: http://localhost:9999
* MySQL: localhost:3306 (логин: app, пароль: pass)

## Структура проекта
* `docker-compose.yml` - конфигурация MySQL
* `schema.sql` - схема базы данных
* `app-deadline.jar` - тестируемое приложение
* `src/test/` - автотесты