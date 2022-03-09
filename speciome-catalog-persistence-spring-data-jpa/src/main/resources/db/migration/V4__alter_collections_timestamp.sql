SET search_path TO catalog;

ALTER TABLE collections
ALTER COLUMN created_at TYPE TIMESTAMP,
ALTER COLUMN updated_at TYPE TIMESTAMP;

ALTER TABLE collections
RENAME created_at TO created_at_utc;

ALTER TABLE collections
RENAME updated_at TO updated_at_utc;