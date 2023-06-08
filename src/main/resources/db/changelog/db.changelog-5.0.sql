--liquibase formatted sql
--changeset aliqan:1

CREATE TABLE IF NOT EXISTS brand
(
    id      BIGSERIAL primary key,
    slug    VARCHAR,
    name    VARCHAR,
    image    VARCHAR,
    country     VARCHAR
);

--changeset aliqan:2
ALTER TABLE tovar
    ADD COLUMN
        brand_id BIGINT REFERENCES brand(id);

--changeset aliqan:3
ALTER TABLE tovar
    ADD COLUMN
        excerpt VARCHAR;

--changeset aliqan:4
ALTER TABLE tovar
    ADD COLUMN
        description VARCHAR;

--changeset aliqan:5
ALTER TABLE tovar
    ADD COLUMN category_id bigint REFERENCES category(id);

--changeset aliqan:6
ALTER TABLE tovar
    ADD COLUMN part_number VARCHAR;
--changeset aliqan:7
ALTER TABLE tovar
    ADD COLUMN compatibility VARCHAR;
--changeset aliqan:8
ALTER TABLE tovar
    ADD COLUMN tags VARCHAR;
--changeset aliqan:9
ALTER TABLE tovar
    ADD COLUMN compare_at_price BIGINT;
--changeset aliqan:10
ALTER TABLE tovar
    ADD COLUMN stock VARCHAR;