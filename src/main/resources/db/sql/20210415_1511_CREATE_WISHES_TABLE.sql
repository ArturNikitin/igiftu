CREATE TABLE IF NOT EXISTS Wishes
(
    id                 SERIAL PRIMARY KEY,
    created_date       TIMESTAMP   NOT NULL,
    last_modified_date TIMESTAMP   NOT NULL,
    name               VARCHAR(50) NOT NULL,
    is_booked          BOOLEAN     NOT NULL,
    is_completed       BOOLEAN     NOT NULL,
    is_analog_possible BOOLEAN     NOT NULL,
    access             VARCHAR(20) NOT NULL,
    price              MONEY,
    location           VARCHAR,
    details            VARCHAR(2048),
    link               VARCHAR(2048),
    user_id            BIGINT      NOT NULL,
    CONSTRAINT user_id_fk FOREIGN KEY (user_id)
        REFERENCES users (id)
)