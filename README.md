# ПРИЛОЖЕНИЕ В РАЗРАБОТКЕ
## Дата готовности ночь с воскресенья на понедельник 18.01.2021

ver app 0.1
ver readme 0.1

### Resources
* git - [https://github.com/dosovma/dish-rest-order](https://github.com/dosovma/dish-rest-order)
* heroku - 

## REST documentation
see ~/config/curl.md

## Description

## Stack
Spring Boot
Spring MVC
Spring Data JPA
H2

## Architecture
* DB
* Model
* Repository
* To and Util
* RestController

### Таблицы

Юзер: ид(pk), имя, email, password, роль

Еда: ид(pk), имя, прайс

Ресторан: ид(pk), имя, лист меню

Меню: ид(pk), дата, ресторан, лист еды

Vote: id, datetime of creation, rest_id, user_id

## Functional

Голосование:
* можно голосовать
* посмотреть историю голосов
* посмотреть конретный голос

Рестораны:
* посмотреть все рестораны вметсе с меню (едой)
* посмотреть конкретный рестран

Еще Не реализовано:
* Почти весь рест
* Регистрация юзеров
* тесты
* валидация
* обработка ошибок
* кеширование
* сортировка
* Правка ролей юзера
* N+1
* каскады
* Spring Data JPA использовать optional
* доработать работу с паролями

## Admin permissions

Admin:
* CRUD users include role
* CRUD dishes
* CRUD restaurants
* CRUD menus
* CRUD votes

# User permissions:

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

## Functionality
*Локализация
*Типы ошибок
*BeanMatcher?
*Json View?
*написано, что это не нужно
Lombok
Кеширование
Тесты JUnit
Транзакции
Индексы к таблицам (один голос в день - один пункт меню/ хотя может быть и один ресторан)
История еды и голосования
TO and entity
Spring DataJPA использовать optional
Валидация
on delete cascade dont forget
активация/деактивация еды

### Future feature
* Выделения гранд админа и админа ресторана
* Запрет редактирования меню и еды прошлых периодов для обычного админа, только гранд админ
* Дать юзеру доступ к истории его голосования, по сути к истории заказов
* Запретить удалять последнего админа - оставлять систему без админов