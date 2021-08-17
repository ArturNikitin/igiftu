ALTER TABLE images
    ADD COLUMN file_name VARCHAR;

ALTER TABLE images
    ALTER file_name SET NOT NULL;