![logo](https://s1.hostingkartinok.com/uploads/images/2023/10/3c99cb671406a906c724ea00fac0c96b.png)     
# Описание проекта
Приложение Sigmmix предназначено для мониторинга параметров хостов в локальной сети. 

Приложение создано в рамках курсовой работы по курсу "[Промышленная разработка на Java](https://study.naumen.ru/sd/operator/#uuid:course$3177602)".

Работа выполнена студентами: Заяц №1 и Волк №2.

# Установка приложения

1. Создать базу данных и пользователя; настроить права:
```sql
CREATE DATABASE springboot;
CREATE USER spring WITH PASSWORD 'spring';
GRANT ALL PRIVILEGES ON DATABASE springboot TO spring;    
```
2. Переименовать файл `application.example` в `application.properties` и настроить коннекцию к БД
3. Запустить метод `main()` в классе `ru.sigma.sigmmix.MainApp`
4. Перейти по ссылке http://127.0.0.1:8080/ и удостовериться, что приложение работает нормально

**Внимание:** В приложении есть встроенная учетная запись с правами администратора: `admin:admin`

# Техническая информация по проекту

## Список endpoint'ов

| Endpoint      | Description                 |
|---------------|-----------------------------|
| /             | Главная страница дашборда   |
| /users        | Список пользовтелей         |
| /hosts        | Список хостов               |
| /templates    | Список шаблонов             |
| /subscription | Список рассылок уведомлений |
| /login        | Страница входа              |
| /logout       | Выход из системы            |


## Useful links
* https://logomaster.ai/ - генератор логотипов по ключевым словам
* https://realfavicongenerator.net/ - генератор favicon из картинки
* https://icons.getbootstrap.com/ - Bootstrap-иконки 
