INSERT INTO DISH (NAME, PRICE)
VALUES ('potato', 40.05),
       ('pea soup', 35.00),
       ('tea', 10.00),
       ('bread', 5.10),
       ('rice', 30.00),
       ('buckwheat', 35.70),
       ('oatmeal', 20.00),
       ('meat', 45.00),
       ('wine', 25.50),
       ('salad', 27.60),
       ('sandwich', 10.00),
       ('meat soup', 12.00),
       ('cabbage soup', 12.00),
       ('steak well done', 50.50),
       ('steak rare', 50.00),
       ('schnitzel', 30.00),
       ('beef', 46.30),
       ('cake', 5.60);

INSERT INTO USERS (NAME, EMAIL, PASSWORD)
VALUES ('Mike', 'mike@mail.ru', '{noop}mikepassword'),
       ('John', 'john@mail.ru', '{noop}johnpassword'),
       ('Kate', 'kate@mail.ru', '{noop}katepassword'),
       ('Admin', 'admin@mail.ru', '{noop}adminpassword');

INSERT INTO USER_ROLE (USER_ID, ROLE)
VALUES (1, 'USER'),
       (2, 'USER'),
       (3, 'USER'),
       (4, 'USER'),
       (4, 'ADMIN');

INSERT INTO RESTAURANT (NAME)
VALUES ('White House'),
       ('ENOT'),
       ('Green tree');

INSERT INTO MENU (DATE, REST_ID)
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

INSERT INTO VOTE (DATE, REST_ID, USER_ID)
VALUES ('2021-01-12 10:00:00', 1, 1),
       ('2021-01-12 10:00:00', 1, 2),
       ('2021-01-12 10:00:00', 3, 3),
       ('2021-01-13 10:00:00', 2, 1),
       ('2021-01-13 10:00:00', 2, 2),
       ('2021-01-13 10:00:00', 1, 3);


