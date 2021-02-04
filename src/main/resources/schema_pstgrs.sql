DROP TABLE IF EXISTS dish_menu;
DROP TABLE IF EXISTS dish;
DROP TABLE IF EXISTS menu;
DROP TABLE IF EXISTS vote;
DROP TABLE IF EXISTS restaurant;
DROP TABLE IF EXISTS user_roles;
DROP TABLE IF EXISTS users CASCADE;
DROP SEQUENCE IF EXISTS users_id_seq;
DROP SEQUENCE IF EXISTS restaurant_id_seq;
DROP SEQUENCE IF EXISTS menu_id_seq;
DROP SEQUENCE IF EXISTS dish_id_seq;
DROP SEQUENCE IF EXISTS vote_id_seq;

CREATE SEQUENCE users_id_seq;
CREATE TABLE users
(
    id       INTEGER PRIMARY KEY DEFAULT nextval('users_id_seq'),
    name     VARCHAR                          NOT NULL,
    email    VARCHAR                          NOT NULL,
    password VARCHAR                          NOT NULL,
    enabled  BOOL                DEFAULT TRUE NOT NULL
);
CREATE UNIQUE INDEX users_email_idx ON users (email);

CREATE TABLE user_roles
(
    user_id INTEGER NOT NULL,
    role    VARCHAR,
    CONSTRAINT user_roles_idx UNIQUE (user_id, role),
    FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
);

CREATE SEQUENCE restaurant_id_seq;
CREATE TABLE restaurant
(
    id      INTEGER PRIMARY KEY DEFAULT nextval('restaurant_id_seq'),
    name    VARCHAR                          NOT NULL,
    enabled BOOLEAN             DEFAULT TRUE NOT NULL
);

CREATE SEQUENCE menu_id_seq;
CREATE TABLE menu
(
    id      INTEGER PRIMARY KEY DEFAULT nextval('menu_id_seq'),
    date    DATE                             NOT NULL,
    rest_id INTEGER                          NOT NULL,
    enabled BOOL                DEFAULT TRUE NOT NULL,
    FOREIGN KEY (rest_id) REFERENCES restaurant (id) ON DELETE CASCADE
);
CREATE UNIQUE INDEX date_rest_idx ON menu (date, rest_id);

CREATE SEQUENCE dish_id_seq;
CREATE TABLE dish
(
    id      INTEGER PRIMARY KEY DEFAULT nextval('dish_id_seq'),
    name    VARCHAR                          NOT NULL,
    price   INTEGER                          NOT NULL,
    enabled BOOL                DEFAULT TRUE NOT NULL
);

CREATE TABLE dish_menu
(
    menu_id INTEGER NOT NULL,
    dish_id INTEGER NOT NULL,
    FOREIGN KEY (menu_id) REFERENCES menu (id) ON DELETE CASCADE,
    FOREIGN KEY (dish_id) REFERENCES dish (id) ON DELETE CASCADE
);

CREATE SEQUENCE vote_id_seq;
CREATE TABLE vote
(
    id      INTEGER PRIMARY KEY DEFAULT nextval('vote_id_seq'),
    date    TIMESTAMP                        NOT NULL,
    rest_id INTEGER                          NOT NULL,
    user_id INTEGER                          NOT NULL,
    enabled BOOL                DEFAULT TRUE NOT NULL,
    FOREIGN KEY (rest_id) REFERENCES restaurant (id) ON DELETE CASCADE,
    FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
);
CREATE UNIQUE INDEX date_user_idx ON vote (date, user_id);

