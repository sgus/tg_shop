--liquibase formatted sql
-- для указания Liquibase, что этот файл является SQL-скриптом в формате Liquibase.

--create table categories
--changeset George:7
--preconditions onFail:HALT onError:HALT
--precondition-sql-check expectedResult:0 (SELECT COUNT(*) FROM information_schema.tables WHERE table_name='categories')
CREATE TABLE categories
(
    id                 SERIAL PRIMARY KEY,
    name               VARCHAR(50) NOT NULL,
    parent_category_id INTEGER REFERENCES categories (id)
);
--rollback drop table categories;



--create table files
--changeset George:3
--preconditions onFail:HALT onError:HALT
--precondition-sql-check expectedResult:0 (SELECT COUNT(*) FROM information_schema.tables WHERE table_name='files')
CREATE TABLE files
(
    id   SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    path VARCHAR(255),
    link VARCHAR(255),
    type VARCHAR(20)  NOT NULL
);
--rollback drop table files;

--create table user_status
--changeset George:0
--preconditions onFail:HALT onError:HALT
--precondition-sql-check expectedResult:0 (SELECT COUNT(*) FROM information_schema.tables WHERE table_name='user_status')
CREATE TABLE user_status
(
    id               SERIAL PRIMARY KEY,
    name             VARCHAR(50) NOT NULL,
    discount_percent INTEGER     NOT NULL
);
--rollback drop table user_status;



--create table users
--changeset George:4
--preconditions onFail:HALT onError:HALT
--precondition-sql-check expectedResult:0 (SELECT COUNT(*) FROM information_schema.tables WHERE table_name='users')
CREATE TABLE users
(
    id           SERIAL PRIMARY KEY,
    telegram_id  INTEGER      NOT NULL,
    username     VARCHAR(255) NOT NULL,
    first_name   VARCHAR(255) NOT NULL,
    last_name    VARCHAR(255),
    phone_number VARCHAR(20),
    balance      NUMERIC(10, 2) DEFAULT 0.00,
    status_id    INTEGER NOT NULL REFERENCES user_status (id)
);
--rollback drop table users;

--create table orders
--changeset George:5
--preconditions onFail:HALT onError:HALT
--precondition-sql-check expectedResult:0 (SELECT COUNT(*) FROM information_schema.tables WHERE table_name='orders')
CREATE TABLE orders
(
    id          SERIAL PRIMARY KEY,
    user_id     INTEGER        NOT NULL REFERENCES users (id),
    date        TIMESTAMP      NOT NULL DEFAULT CURRENT_TIMESTAMP,
    status      VARCHAR(20)    NOT NULL,
    total_price NUMERIC(10, 2) NOT NULL
);
--rollback drop table orders;


--changeset George:2
--create table products
--preconditions onFail:HALT onError:HALT
--precondition-sql-check expectedResult:0 (SELECT COUNT(*) FROM information_schema.tables WHERE table_name='products')
CREATE TABLE products
(
    id          SERIAL PRIMARY KEY,
    name        VARCHAR(255)   NOT NULL,
    description TEXT,
    price       NUMERIC(10, 2) NOT NULL,
    quantity    INTEGER        NOT NULL,
    category_id INTEGER REFERENCES Categories (id),
    file_id     INTEGER REFERENCES Files (id)
);
--rollback drop table products;


-- может хранимку?
--create table order_items
--changeset George:6
--preconditions onFail:HALT onError:HALT
--precondition-sql-check expectedResult:0 (SELECT COUNT(*) FROM information_schema.tables WHERE table_name='order_items')
CREATE TABLE order_items
(
    order_id   INTEGER        NOT NULL REFERENCES orders (id),
    product_id INTEGER        NOT NULL REFERENCES products (id),
    quantity   INTEGER        NOT NULL,
    price      NUMERIC(10, 2) NOT NULL,
    PRIMARY KEY (order_id, product_id)
);
--rollback drop table order_items;


--insert table categories
--changeset George:8
--preconditions onFail:HALT onError:HALT
--precondition-sql-check expectedResult:0 (SELECT COUNT(*) FROM public.categories WHERE name='Папка 1')
INSERT INTO public.categories (id, name, parent_category_id)
VALUES (DEFAULT, 'Папка 1', null);
--rollback delete from test1 where name = 'Папка 1';

--insert table categories
--changeset George:10
--preconditions onFail:HALT onError:HALT
--precondition-sql-check expectedResult:0 (SELECT COUNT(*) FROM public.categories WHERE name='Папка 1`')
INSERT INTO public.categories (id, name, parent_category_id)
VALUES (DEFAULT, 'Папка 1', null);
--rollback delete from test1 where name = 'Папка 1';