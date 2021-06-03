CREATE TABLE IF NOT EXISTS Wishlist_files
(
    id        BIGSERIAL PRIMARY KEY,
    data      BYTEA   NOT NULL,
    file_name VARCHAR NOT NULL,
    file_type VARCHAR NOT NULL
)