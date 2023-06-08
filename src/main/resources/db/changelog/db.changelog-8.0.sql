--liquibase formatted sql

--changeset aliqan:1
CREATE TABLE review
(
    id       BIGSERIAL PRIMARY KEY,
    product_id BIGINT REFERENCES tovar (id),
    date     DATE,
    author   VARCHAR(255),
    rating   INTEGER,
    content  TEXT
);