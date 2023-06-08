--liquibase formatted sql
--changeset aliqan:1

CREATE TABLE IF NOT EXISTS tovar
(
    id      BIGSERIAL primary key,
    name    VARCHAR,
    slug    VARCHAR,
    sku     VARCHAR,
    price   INTEGER,
    rating  INTEGER,
    reviews INTEGER,
    availability VARCHAR

--     image_id BIGINT REFERENCES product_image(id)


);

--changeset aliqan:3
ALTER TABLE category
    ADD COLUMN slug VARCHAR(64);

