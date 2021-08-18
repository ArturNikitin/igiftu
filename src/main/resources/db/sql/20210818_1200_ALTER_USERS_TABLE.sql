ALTER TABLE users
    ADD COLUMN registration_type VARCHAR(50);

ALTER TABLE users
    ALTER password DROP NOT NULL
