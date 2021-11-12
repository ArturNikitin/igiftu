ALTER TABLE boards
    ADD COLUMN image_id BIGINT,
    ADD CONSTRAINT images_id_fk FOREIGN KEY (image_id) REFERENCES images (id);