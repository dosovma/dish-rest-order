INSERT INTO DISH (NAME, PRICE, ENABLED)
VALUES ('potato', 40.05, true),
       ('pea soup', 35.00, true),
       ('tea', 10.00, true),
       ('bread', 5.10, true),
       ('rice', 30.00, true),
       ('buckwheat', 35.70, true),
       ('oatmeal', 20.00, true),
       ('meat', 45.00, true),
       ('wine', 25.50, true),
       ('salad', 27.60, true),
       ('sandwich', 10.00, true),
       ('meat soup', 12.00, true),
       ('cabbage soup', 12.00, true),
       ('steak well done', 50.50, true),
       ('steak rare', 50.00, true),
       ('schnitzel', 30.00, true),
       ('beef', 46.30, true),
       ('cake', 5.60, true);

INSERT INTO USERS (NAME, EMAIL, PASSWORD)
VALUES ('Mike', 'mike@mail.ru', '{noop}mikepassword'),
       ('John', 'john@mail.ru', '{noop}johnpassword'),
       ('Kate', 'kate@mail.ru', '{noop}katepassword'),
       ('Admin', 'admin@mail.ru', '{noop}adminpassword');

INSERT INTO USER_ROLES (USER_ID, ROLES)
VALUES (1, 'USER'),
       (2, 'USER'),
       (3, 'USER'),
       (4, 'USER'),
       (4, 'ADMIN');

INSERT INTO RESTAURANT (NAME)
VALUES ('White House'),
       ('ENOT'),
       ('Green tree');

INSERT INTO MENU (MENU_DATE, REST_ID)
VALUES ('2021-01-12', 1),
       ('2021-01-12', 2),
       ('2021-01-12', 3),
       ('2021-01-13', 1),
       ('2021-01-13', 2),
       ('2021-01-13', 3);

INSERT INTO DISH_MENU (MENU_ID, DISH_ID)
VALUES (1, 1),
       (1, 2),
       (1, 3),
       (2, 4),
       (2, 5),
       (2, 6),
       (3, 7),
       (3, 8),
       (3, 9),
       (4, 10),
       (4, 11),
       (4, 12),
       (5, 13),
       (5, 14),
       (5, 15),
       (6, 16),
       (6, 17),
       (6, 18),
       (6, 13),
       (4, 3);

INSERT INTO VOTE (VOTE_DATE, REST_ID, USER_ID)
VALUES ('2021-01-12', 1, 1),
       ('2021-01-12', 1, 2),
       ('2021-01-12', 3, 3),
       ('2021-01-13', 2, 1),
       ('2021-01-13', 2, 2),
       ('2021-01-13', 1, 3);


