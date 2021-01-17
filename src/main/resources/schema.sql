DROP TABLE IF EXISTS user_roles;
DROP TABLE IF EXISTS meals;
DROP TABLE IF EXISTS users;

CREATE TABLE users
(
    id       INTEGER AUTO_INCREMENT NOT NULL,
    name     VARCHAR                NOT NULL,
    email    VARCHAR                NOT NULL,
    password VARCHAR                NOT NULL,
    PRIMARY KEY (id)
);
CREATE UNIQUE INDEX users_email_idx ON users (email);

CREATE TABLE user_role
(
    user_id INTEGER NOT NULL,
    role    VARCHAR,
    CONSTRAINT user_roles_idx UNIQUE (user_id, role),
    FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
);

CREATE TABLE restaurant
(
    id   INTEGER AUTO_INCREMENT NOT NULL,
    name VARCHAR                NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE menu
(
    id      INTEGER AUTO_INCREMENT NOT NULL,
    date    DATE                   NOT NULL,
    rest_id INTEGER                NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT date_rest_idx UNIQUE (date, rest_id),
    FOREIGN KEY (rest_id) REFERENCES restaurant (id) ON DELETE CASCADE
);
CREATE UNIQUE INDEX date_rest_idx ON menu (date, rest_id);

CREATE TABLE dish
(
    id    INTEGER AUTO_INCREMENT NOT NULL,
    name  VARCHAR                NOT NULL,
    price DOUBLE                 NOT NULL,
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
    id      INTEGER AUTO_INCREMENT NOT NULL,
    date    TIMESTAMP              NOT NULL,
    rest_id INTEGER                NOT NULL,
    user_id INTEGER                NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT date_user_idx UNIQUE (date, user_id),
    FOREIGN KEY (rest_id) REFERENCES restaurant (id) ON DELETE CASCADE,
    FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
);
CREATE UNIQUE INDEX date_user_idx ON vote (date, user_id);

