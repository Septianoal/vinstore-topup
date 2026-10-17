CREATE TABLE games (
    id            BIGSERIAL PRIMARY KEY,
    name          VARCHAR(100) NOT NULL,
    slug          VARCHAR(100) NOT NULL UNIQUE,
    image_url     VARCHAR(255),
    user_id_label VARCHAR(50)  NOT NULL DEFAULT 'User ID',
    active        BOOLEAN      NOT NULL DEFAULT TRUE
);

CREATE TABLE products (
    id      BIGSERIAL PRIMARY KEY,
    game_id BIGINT       NOT NULL REFERENCES games (id),
    name    VARCHAR(100) NOT NULL,
    price   NUMERIC(12, 2) NOT NULL,
    active  BOOLEAN      NOT NULL DEFAULT TRUE
);

CREATE INDEX idx_products_game_id ON products (game_id);

CREATE TABLE users (
    id            BIGSERIAL PRIMARY KEY,
    username      VARCHAR(50)  NOT NULL UNIQUE,
    password_hash VARCHAR(100) NOT NULL,
    role          VARCHAR(20)  NOT NULL DEFAULT 'ADMIN'
);

CREATE TABLE orders (
    id           BIGSERIAL PRIMARY KEY,
    order_code   VARCHAR(30)  NOT NULL UNIQUE,
    product_id   BIGINT       NOT NULL REFERENCES products (id),
    game_user_id VARCHAR(100) NOT NULL,
    contact      VARCHAR(100) NOT NULL,
    total_price  NUMERIC(12, 2) NOT NULL,
    status       VARCHAR(20)  NOT NULL DEFAULT 'PENDING',
    created_at   TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE payments (
    id          BIGSERIAL PRIMARY KEY,
    order_id    BIGINT      NOT NULL UNIQUE REFERENCES orders (id),
    gateway_ref VARCHAR(100),
    method      VARCHAR(30),
    status      VARCHAR(20) NOT NULL DEFAULT 'PENDING',
    paid_at     TIMESTAMP
);