-- initialize admin
INSERT INTO fm_user (email, password, creation_date_time)
VALUES(
    'admin@mail.ru',
    '$2a$12$WbMnsoZ0lRlAyj9uSc3z0eJORXRxoQ5hMnSOfZq0.kK6E6FrO6Nbm',
    current_timestamp
);

INSERT INTO fm_user_role
VALUES (1, 2);