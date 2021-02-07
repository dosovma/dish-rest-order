INSERT INTO DISH (NAME, PRICE, ENABLED)
VALUES ('potato', 4005, true),
       ('pea soup', 3500, true),
       ('tea', 1000, true),
       ('bread', 510, true),
       ('rice', 3000, true),
       ('buckwheat', 3570, true),
       ('oatmeal', 2000, true),
       ('meat', 4500, true),
       ('wine', 2550, true),
       ('salad', 2760, true),
       ('sandwich', 1000, true),
       ('meat soup', 1200, true),
       ('cabbage soup', 1200, true),
       ('steak well done', 5050, true),
       ('steak rare', 5000, true),
       ('schnitzel', 3000, true),
       ('beef', 4630, true),
       ('cake', 560, true);

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