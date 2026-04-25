CREATE TABLE IF NOT EXISTS products (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255),
    price NUMERIC(19, 2),
    status VARCHAR(255),
    category_id BIGINT,
    CONSTRAINT fk_products_category
        FOREIGN KEY (category_id)
        REFERENCES category (id)
);
