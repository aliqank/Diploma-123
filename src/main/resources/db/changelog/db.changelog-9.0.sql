--liquibase formatted sql

--changeset aliqan:1
CREATE TABLE attribute_group
(
    id   BIGSERIAL PRIMARY KEY,
    name VARCHAR NOT NULL,
    slug VARCHAR NOT NULL
);

--changeset aliqan:2
ALTER TABLE product_attribute
    ADD COLUMN attribute_group_id BIGINT REFERENCES attribute_group (id);

--changeset aliqan:3
CREATE TABLE product_type
(
    id   BIGSERIAL PRIMARY KEY,
    slug VARCHAR NOT NULL,
    name VARCHAR NOT NULL
);

--changeset aliqan:4
ALTER TABLE attribute_group
    ADD COLUMN product_type_id BIGINT REFERENCES product_type (id);

--changeset aliqan:5
ALTER TABLE tovar
    ADD COLUMN product_type_id BIGINT REFERENCES product_type (id)



