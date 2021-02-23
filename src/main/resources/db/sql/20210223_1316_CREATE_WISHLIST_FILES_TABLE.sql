CREATE TABLE IF NOT EXISTS Wishlist_files
(
    id        SERIAL PRIMARY KEY,
    data      BYTEA   NOT NULL,
    file_name VARCHAR NOT NULL,
    file_type VARCHAR NOT NULL
)