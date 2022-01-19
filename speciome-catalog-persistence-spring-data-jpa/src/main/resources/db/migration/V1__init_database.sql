CREATE SCHEMA IF NOT EXISTS catalog;

CREATE SEQUENCE hibernate_sequence START WITH 1 INCREMENT BY 1;

CREATE TABLE catalog.user_ (
    email VARCHAR(255) PRIMARY KEY NOT NULL,
    password_hash VARCHAR(255) NOT NULL
);

CREATE TABLE catalog.sample (
    id BIGSERIAL PRIMARY KEY,
    archived BOOLEAN NOT NULL,
    created_at_utc TIMESTAMP NOT NULL,
    created_at_zone VARCHAR(255) NOT NULL,
    updated_at_utc TIMESTAMP NOT NULL,
    updated_at_zone VARCHAR(255) NOT NULL
);

CREATE TABLE catalog.sample_attribute (
    id BIGSERIAL PRIMARY KEY,
    attribute VARCHAR(255) NOT NULL,
    value VARCHAR(255) NOT NULL,
    sample_id BIGINT NOT NULL REFERENCES sample(id) ON DELETE CASCADE
);

CREATE TABLE catalog.collection (
    id BIGSERIAL PRIMARY KEY,
    collection_name VARCHAR(255) NOT NULL
);
