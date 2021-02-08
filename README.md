# Restaurant voting app

* ver app 1
* ver readme 1

### Resources

* git - [https://github.com/dosovma/dish-rest-order](https://github.com/dosovma/dish-rest-order)

## REST documentation

* [Swagger](http://localhost:8080/swagger-ui/) (please authorize by auth details below)
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

### Implemented:

Voting:

* CRUD
* plus you can get vote's history by user filtered by date

Restaurants:

* CRUD
* get all restaurants with or without menu
* get one restaurant with or without menu

Dishes:

* CRUD
* You can't delete the dish, only make it disable

Users (only for an admin):

* CRUD

Account for user:

* CRUD (create new user by /register)
* get vote's history filtered by date

### NOT implemented:

* cache hibernate
* validate password length due to encode

## Project description

### Stack

Spring Boot Spring MVC Spring Data JPA H2

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

User can work with urls:

* /account
* /account/votes
* /account/votes/{\\d+}
* /restaurants
* /restaurants/{\\d+}
* /restaurants/{\\d+}/menus
* /votes/**

Account (/account):

* registering
* CRUD own account
* get vote's history

Restaurants and menus (/restaurants):

* get restaurants with or without menus
* user can't get menus without restaurant

Vote:

* CRUD

### Future features (russian)

* Выделения гранд админа и админа ресторана, который работает только с данными ресторана (имя, меню, еда).
* Запрет редактирования меню и еды прошлых периодов для обычного админа, только гранд админ.
* Запретить удалять последнего админа - оставлять систему без админов.
* Запретить менять меню или еду, включенную в меню, сегодня после какого то времени, например 9-00. Чтобы не получилось,
  что пользователи голосовали за одно меню, а в нем потом подменили еду.
* Запретить удалять любые сущности, только делать их активными/неактивными.
* Нормальная смена/сброс пароля - через получение ссылки и введения нового пароля или через указание старого и нового
  пароля. Главное, чтобы пароль не отправлялся каждый раз в боди при апдейте.