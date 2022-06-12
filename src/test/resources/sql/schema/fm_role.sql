CREATE SEQUENCE IF NOT EXISTS fm_role_id_seq MINVALUE 1 INCREMENT 1;
CREATE TABLE fm_role (
    id integer NOT NULL DEFAULT nextval('fm_role_id_seq'),
    name varchar(50),
    PRIMARY KEY (id)
);

-- initialize roles
INSERT INTO fm_role (name)
VALUES
    ('ROLE_USER'),
    ('ROLE_ADMIN');