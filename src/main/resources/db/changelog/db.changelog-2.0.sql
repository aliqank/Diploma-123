--liquibase formatted sql
--changeset aliqan:1
CREATE TABLE IF NOT EXISTS category
(
    id BIGSERIAL primary key ,
    name VARCHAR

);
--changeset aliqan:2
ALTER TABLE product
    ADD COLUMN category_id bigint REFERENCES category(id);