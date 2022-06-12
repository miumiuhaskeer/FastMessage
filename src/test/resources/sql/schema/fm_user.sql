CREATE SEQUENCE IF NOT EXISTS fm_user_id_seq MINVALUE 1 INCREMENT 1;
CREATE TABLE fm_user (
    id int8 NOT NULL DEFAULT nextval('fm_user_id_seq'),
    email varchar(50),
    password varchar(120),
    creation_date_time timestamp,
    PRIMARY KEY (id)
);