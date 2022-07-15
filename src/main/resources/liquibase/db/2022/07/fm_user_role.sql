--liquibase formatted sql
--changeset miumiuhaskeer:3

CREATE TABLE IF NOT EXISTS fm_user_role (
    user_id int8 NOT NULL,
    role_id int NOT NULL,
    PRIMARY KEY (user_id, role_id)
);