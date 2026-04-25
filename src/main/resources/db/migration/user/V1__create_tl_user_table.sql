CREATE TABLE IF NOT EXISTS tl_user (
    id VARCHAR(36) PRIMARY KEY,
    name VARCHAR(255),
    address VARCHAR(30),
    phone VARCHAR(10),
    email VARCHAR(255) UNIQUE,
    username VARCHAR(255),
    password VARCHAR(255)
);
