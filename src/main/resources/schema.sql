CREATE TABLE IF NOT EXISTS watch_catalogue (
    id VARCHAR(255) NOT NULL,
    name VARCHAR(255) NOT NULL,
    unit_price INTEGER NOT NULL,
    discount_expression VARCHAR(255),
    PRIMARY KEY (id)
);
