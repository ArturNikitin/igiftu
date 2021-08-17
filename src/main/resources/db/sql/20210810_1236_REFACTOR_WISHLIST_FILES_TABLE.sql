DROP TABLE Wishlist_files;

CREATE TABLE IF NOT EXISTS images
(
    id        BIGSERIAL PRIMARY KEY,
    file_type VARCHAR
);

ALTER TABLE users
    ADD COLUMN image_id BIGINT,
    ADD CONSTRAINT images_id_fk FOREIGN KEY (image_id) REFERENCES images (id);

ALTER TABLE wishes
    ADD COLUMN image_id BIGINT,
    ADD CONSTRAINT images_id_fk FOREIGN KEY (image_id) REFERENCES images (id);