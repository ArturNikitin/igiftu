CREATE TABLE IF NOT EXISTS wishes_boards (
    board_id BIGINT NOT NULL,
    wish_id BIGINT NOT NULL,
    CONSTRAINT board_id_fk FOREIGN KEY (board_id)
    REFERENCES boards (id),
    CONSTRAINT wish_id_fk FOREIGN KEY (wish_id)
    REFERENCES wishes (id),
    PRIMARY KEY (board_id, wish_id)
)