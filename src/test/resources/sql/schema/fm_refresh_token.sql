CREATE SEQUENCE IF NOT EXISTS fm_refresh_token_id_seq MINVALUE 1 INCREMENT 1;
CREATE TABLE fm_refresh_token (
    id int8 NOT NULL DEFAULT nextval('fm_refresh_token_id_seq'),
    user_id int8,
    token varchar(60),
    expiry_date_time timestamp,
    PRIMARY KEY (id, user_id)
);