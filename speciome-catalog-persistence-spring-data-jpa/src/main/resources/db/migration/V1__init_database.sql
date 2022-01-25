SET search_path TO catalog;

CREATE SEQUENCE hibernate_sequence START WITH 1 INCREMENT BY 1;

CREATE TABLE users (
    email VARCHAR PRIMARY KEY NOT NULL,
    password_hash VARCHAR(255) NOT NULL
);

CREATE TABLE samples (
    sample_id BIGSERIAL PRIMARY KEY,
    archived BOOLEAN NOT NULL,
    created_at_utc TIMESTAMP NOT NULL,
    created_at_zone VARCHAR(255) NOT NULL,
    updated_at_utc TIMESTAMP NOT NULL,
    updated_at_zone VARCHAR(255) NOT NULL
);

CREATE TABLE sample_attributes (
    sample_attribute_id BIGSERIAL PRIMARY KEY,
    attribute VARCHAR(255) NOT NULL,
    value VARCHAR NOT NULL,
    sample_id BIGINT REFERENCES samples(sample_id) ON DELETE CASCADE
);

CREATE TABLE collections (
    collection_id BIGSERIAL PRIMARY KEY,
    collection_name VARCHAR(255) NOT NULL
);
