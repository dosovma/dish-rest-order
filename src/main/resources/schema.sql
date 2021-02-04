DROP TABLE IF EXISTS dish_menu;
DROP TABLE IF EXISTS dish;
DROP TABLE IF EXISTS menu;
DROP TABLE IF EXISTS vote;
DROP TABLE IF EXISTS restaurant;
DROP TABLE IF EXISTS user_roles;
DROP TABLE IF EXISTS users;

CREATE TABLE users
(
    id       INTEGER AUTO_INCREMENT NOT NULL,
    name     VARCHAR                NOT NULL,
    email    VARCHAR                NOT NULL,
    password VARCHAR                NOT NULL,
    enabled  BOOL DEFAULT TRUE,
    PRIMARY KEY (id)
);
CREATE UNIQUE INDEX users_email_idx ON users (email);

CREATE TABLE user_roles
(
    user_id INTEGER NOT NULL,
    roles   VARCHAR,
    CONSTRAINT user_roles_idx UNIQUE (user_id, roles),
    FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
);

CREATE TABLE restaurant
(
    id      INTEGER AUTO_INCREMENT NOT NULL,
    name    VARCHAR                NOT NULL,
    enabled BOOLEAN DEFAULT TRUE,
    CONSTRAINT restaurant_idx UNIQUE (name),
    PRIMARY KEY (id)
);

CREATE TABLE menu
(
    id        INTEGER AUTO_INCREMENT NOT NULL,
    menu_date DATE                   NOT NULL,
    rest_id   INTEGER                NOT NULL,
    enabled   BOOL DEFAULT TRUE,
    PRIMARY KEY (id),
    FOREIGN KEY (rest_id) REFERENCES restaurant (id) ON DELETE CASCADE
);
CREATE UNIQUE INDEX date_rest_idx ON menu (menu_date, rest_id);

CREATE TABLE dish
(
    id      INTEGER AUTO_INCREMENT NOT NULL,
    name    VARCHAR                NOT NULL,
    price   INTEGER                NOT NULL,
    enabled BOOL DEFAULT TRUE      NOT NULL,
    CONSTRAINT dish_idx UNIQUE (name),
    PRIMARY KEY (id)
);

CREATE TABLE dish_menu
(
    menu_id INTEGER NOT NULL,
    dish_id INTEGER NOT NULL,
    FOREIGN KEY (menu_id) REFERENCES menu (id) ON DELETE CASCADE,
    FOREIGN KEY (dish_id) REFERENCES dish (id) ON DELETE CASCADE
);

CREATE TABLE vote
(
    id        INTEGER AUTO_INCREMENT NOT NULL,
    vote_date DATE                   NOT NULL,
    rest_id   INTEGER                NOT NULL,
    user_id   INTEGER                NOT NULL,
    enabled   BOOL DEFAULT TRUE,
    PRIMARY KEY (id),
    FOREIGN KEY (rest_id) REFERENCES restaurant (id) ON DELETE CASCADE,
    FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
);
CREATE UNIQUE INDEX date_user_idx ON vote (vote_date, user_id);

