--liquibase formatted sql

--changeset aliqan:1
CREATE TABLE IF NOT EXISTS category_sub
(
    id      BIGSERIAL primary key,
    type    VARCHAR,
    name    VARCHAR,
    slug    VARCHAR,
    image    VARCHAR,
    category_id bigint REFERENCES category(id)
);

--changeset aliqan:12
ALTER TABLE IF EXISTS category
    DROP COLUMN parent_id;

--changeset aliqan:3
ALTER TABLE category
    ADD COLUMN  layout VARCHAR;

--changeset aliqan:4
ALTER TABLE category_sub
    ADD COLUMN  layout VARCHAR;