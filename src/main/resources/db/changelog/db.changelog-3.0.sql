--liquibase formatted sql
--changeset aliqan:1
ALTER TABLE product
    ADD COLUMN image VARCHAR(64);

--changeset aliqan:2
CREATE TABLE IF NOT EXISTS image
(
    id  BIGSERIAL primary key,
    url VARCHAR
);
--changeset aliqan:3
ALTER TABLE image
    ADD COLUMN name VARCHAR(64);

