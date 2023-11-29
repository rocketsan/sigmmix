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
2. Настроить телеграм бота через `@BotFather`, получить token
3. Переименовать файл `application.example` в `application.properties` и прописать токен и имя бота 
4. Запустить метод `main()` в классе `ru.sigma.sigmmix.MainApp`
5. Перейти по ссылке http://127.0.0.1:8080/ и удостовериться, что приложение работает нормально

**Внимание:** В приложении есть встроенная учетная запись с правами администратора: `admin:admin`

# Техническая информация по проекту

## Список endpoint'ов

| Endpoint      | Description                 | Roles          |
|---------------|-----------------------------|----------------|
| /             | Главная страница дашборда   | ADMIN, MONITOR |
| /users        | Список пользовтелей         | ADMIN          |
| /hosts        | Список хостов               | ADMIN          |
| /subscription | Список рассылок уведомлений | ADMIN          |
| /login        | Страница входа              | ALL            |
| /logout       | Выход из системы            | ADMIN, MONITOR |

## Todo
1. Сделать график на главной странице правдоподобным!

## Как сконфигурировать SNMP сервер?
1. Установить пакет:

    `yum install net-snmp`
2. Сконфигурировать /etc/snmpd.conf: 

    **edit:** `view     systemview    included   .1.3.6.1        80`

    `systemctl restart snmpd`
3. Добавить сервис snmp в firewall:

    `firewall-cmd --permanent --add-service=snmp`

    `firewall-cmd --reload`
4. Проверка:

    `snmpwalk -v 2c -c public 127.0.0.1 1.3.6.1.4.1.2021.10.1.3.1`

## Как нагрузить процессор?
`yes > /dev/null`

Выход по Ctrl+C

## Возможные проблемы и пути их решения
### 1. При запуске возникает исключение SunCertPathBuilderException 
`PKIX path building failed: sun.security.provider.certpath.SunCertPathBuilderException: unable to find valid certification path to requested targe`

**Возможная причина**: скорее всего корпоративный Касперский подсовывает свой недостоверный ssl-сертификат

**Способ исправления**: Запустить повторно? (мне помогло)

### 2. Возникает исключение TelegramApiRequestException
`Error executing org.telegram.telegrambots.meta.api.methods.updates.GetUpdates query: [409] Conflict: terminated by other getUpdates request; make sure that only one bot instance is running`

**Возможная причина**: Тут очевидно, бота юзает какой-то другой клиент

**Способ исправления**: Отключить сервис (naumb)

## Useful links
* https://logomaster.ai/ - генератор логотипов по ключевым словам
* https://realfavicongenerator.net/ - генератор favicon из картинки
* https://icons.getbootstrap.com/ - Bootstrap-иконки 
