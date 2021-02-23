CREATE TABLE IF NOT EXISTS Users
(
    id                 SERIAL PRIMARY KEY,
    created_date       TIMESTAMP   NOT NULL,
    email              VARCHAR     UNIQUE,
    password           VARCHAR     NOT NULL,
    isEnabled          BOOLEAN     NOT NULL,
    isAccountNonLocked BOOLEAN     NOT NULL,
    role               VARCHAR     NOT NULL,
    login              VARCHAR(50) UNIQUE
)