SET search_path TO catalog;

ALTER TABLE collections
RENAME collection_name TO name;

ALTER TABLE collections
ADD COLUMN created_at TIMESTAMPTZ,
ADD COLUMN updated_at TIMESTAMPTZ,
ADD COLUMN owner_email TEXT;