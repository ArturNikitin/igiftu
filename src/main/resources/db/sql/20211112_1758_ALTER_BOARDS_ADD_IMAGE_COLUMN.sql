CREATE TABLE IF NOT EXISTS Boards
(
    id                 BIGSERIAL PRIMARY KEY,
    created_date       TIMESTAMP   NOT NULL,
    last_modified_date TIMESTAMP   NOT NULL,
    name               VARCHAR(100) NOT NULL,
    user_id            BIGINT      NOT NULL,
    CONSTRAINT user_id_fk FOREIGN KEY (user_id)
    REFERENCES users (id)
);

ALTER TABLE boards
    ADD COLUMN image_id BIGINT,
    ADD CONSTRAINT images_id_fk FOREIGN KEY (image_id) REFERENCES images (id);