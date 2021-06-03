CREATE TABLE IF NOT EXISTS user_tokens
(
    id                       BIGSERIAL PRIMARY KEY,
    password_token           VARCHAR   NOT NULL UNIQUE,
    user_id                  BIGINT    NOT NULL UNIQUE,
    expiration_password_date TIMESTAMP NOT NULL,
    CONSTRAINT user_id_fk FOREIGN KEY (user_id)
        REFERENCES users (id)
)