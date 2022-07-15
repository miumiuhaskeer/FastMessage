--liquibase formatted sql
--changeset miumiuhaskeer:3

CREATE SEQUENCE IF NOT EXISTS fm_user_id_seq MINVALUE 1 INCREMENT 1;
CREATE TABLE IF NOT EXISTS fm_user (
    id int8 NOT NULL DEFAULT nextval('fm_user_id_seq'),
    email varchar(50) NOT NULL,
    password varchar(120) NOT NULL,
    creation_date_time timestamp,
    PRIMARY KEY (id),
    CONSTRAINT email_unique UNIQUE (email)
);