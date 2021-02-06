# ПРИЛОЖЕНИЕ В РАЗРАБОТКЕ

ver app 1
ver readme 0.9

### Resources
* git - [https://github.com/dosovma/dish-rest-order](https://github.com/dosovma/dish-rest-order)

## REST documentation
* [Swagger](http://localhost:8080/swagger-ui/)
* [REST documentation by postman](https://documenter.getpostman.com/view/13586382/TVzYeZBP#83fedd18-83bc-4d5f-966b-bf59cfe3f65f)

### Auth details:
#### Admin:
- login - admin@mail.ru
- password - adminpassword

#### User:

- login - mike@mail.ru
- password - mikepassword


- login - john@mail.ru
- password - mjohnpassword

## Functional

### Еще Не реализовано:
* тесты
* cache hibernate
* english version of README.md

### Реализовано:
Голосование:
* можно голосовать
* посмотреть историю голосов
* посмотреть конретный голос
* CRUD

Рестораны:
* посмотреть все рестораны вместе с меню (едой)
* посмотреть конкретный рестран
* CRUD

Блюда:
* CRUD
* Активация/деактивация еды

Юзеры (админ часть):
* CRUD

Юзеры (аккаунт):
* регистрация
* просмотр своих данных
* просмотр истории голосов с фильтром по датам
* просмотр конретного голоса
* редактивароние своих данных, кроме роли (роли редактирует только админ)
* удаление своего аккаунта (юзера)

## Project description

### Stack
Spring Boot
Spring MVC
Spring Data JPA
H2

### Architecture
* DB
* Model
* Repository
* To and Util
* RestController

### Admin permissions

Admin:
* CRUD users include role
* CRUD dishes
* CRUD restaurants
* CRUD menus
* CRUD votes

### User permissions:

Account:
* регистрация (.../users post)
* рид свой профиль (.../users/id get)
* апдейт своих данных (.../users/id post)
* удаление своего профиля (.../users/id delete)

Restaurants and menus:
* рид рестораны и их меню на сегодня (.../restaurants/menus get)

Vote:
* создание голоса или эдит голоса (.../user/id/votes/rest_id get) в базу направлят один запрос по ответу из базы решать
  это создание или редактивароние
* рид сегодняшний голос (.../users/votes/current get)
* делит голоса (.../user/votes/id delete)
* рид историю голосов (.../user/votes get)

### Future feature
* Выделения гранд админа и админа ресторана
* Запрет редактирования меню и еды прошлых периодов для обычного админа, только гранд админ
* Запретить удалять последнего админа - оставлять систему без админов
* Запретить менять меню сеогдя после какого то времени, например 9-00
* Запретить удалять меню
* Нормальная смена/сброс пароля - через получение ссылки и введения нового пароля или через указание старого и нового пароля.
Главное, чтобы пароль не отправлялся каждый раз в боди при апдейте.