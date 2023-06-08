--liquibase formatted sql

--changeset aliqan:1
CREATE TABLE IF NOT EXISTS product_attribute_value
(
    id      BIGSERIAL primary key,
    name    VARCHAR,
    slug    VARCHAR
);

--changeset aliqan:2
CREATE TABLE IF NOT EXISTS product_attribute
(
    id      BIGSERIAL primary key,
    name    VARCHAR,
    slug    VARCHAR,
    featured BOOLEAN
);

--changeset aliqan:3
ALTER TABLE product_attribute
    ADD COLUMN tovar_id bigint REFERENCES tovar(id);

--changeset aliqan:4
ALTER TABLE product_attribute
    ADD COLUMN attribute_value_id bigint REFERENCES product_attribute_value(id);


--changeset aliqan:5
ALTER TABLE category
    ADD COLUMN type VARCHAR;

--changeset aliqan:6
ALTER TABLE category
    ADD COLUMN image VARCHAR;

--changeset aliqan:7
ALTER TABLE category
    ADD COLUMN items INTEGER;

--changeset aliqan:8
ALTER TABLE category
    ADD COLUMN  parent_id bigint REFERENCES category (id);

--changeset aliqan:9
ALTER TABLE category
    ADD CONSTRAINT fk_parent FOREIGN KEY (parent_id)
        REFERENCES category (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION;

--changeset aliqan:10
ALTER TABLE product_image
    ADD COLUMN  tovar_id bigint