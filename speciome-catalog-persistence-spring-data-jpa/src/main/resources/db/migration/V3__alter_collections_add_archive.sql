SET search_path TO catalog;

ALTER TABLE collections
    ADD archived BOOLEAN NOT NULL DEFAULT FALSE;
