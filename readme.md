![logo](https://s1.hostingkartinok.com/uploads/images/2023/10/3c99cb671406a906c724ea00fac0c96b.png)
# Описание проекта
Приложение Sigmmix для мониторинга параметров хостов в локальной сети. 

Приложение создано в рамках курсовой работы по курсу "[Промышленная разработка на Java](https://study.naumen.ru/sd/operator/#uuid:course$3177602)".

Работа выполнена студентами: Заяц №1 и Волк №2.

# Установка приложения

1. Переименовать файл application.example в application.properties и настроить коннекцию к БД
2. Запустить метод main() в классе ru.sigma.sigmmix.MainApplication
3. Зайти по ссылке http://127.0.0.1:8080/ и удостовериться, что приложение работает нормально

# Техническая информация по проекту

## Список endpoint'ов

| Endpoint      | Description                 |
|---------------|-----------------------------|
| /             | Главная страница дашборда   |
| /users        | Список пользовтелей         |
| /hosts        | Список хостов               |
| /templates    | Список шаблонов             |
| /notification | Список рассылок уведомлений |
| /login        | Страница входа              |
| /logout       | Выход из системы            |


## Useful links
* https://logomaster.ai/ - генератор логотипов по ключевым словам
* https://realfavicongenerator.net/ - генератор favicon из картинки
* https://icons.getbootstrap.com/ - Bootstrap-иконки 
