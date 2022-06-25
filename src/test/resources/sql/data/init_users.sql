-- initialize admin
INSERT INTO fm_user (email, password, creation_date_time)
VALUES
(
    'admin@mail.ru',
    '$2a$12$WUJ6sBGthvVl1Q.4EdjL3.wRvBJVcJ8IizUJ74whBPtzwAR1gkqry',
    current_timestamp
),
(
    'user1@mail.ru',
    '$2a$12$WUJ6sBGthvVl1Q.4EdjL3.wRvBJVcJ8IizUJ74whBPtzwAR1gkqry',
    current_timestamp
),
(
    'user2@mail.ru',
    '$2a$12$WUJ6sBGthvVl1Q.4EdjL3.wRvBJVcJ8IizUJ74whBPtzwAR1gkqry',
    current_timestamp
);

INSERT INTO fm_user_role
VALUES (1, 2);