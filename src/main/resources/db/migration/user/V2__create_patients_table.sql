CREATE TABLE IF NOT EXISTS patients (
    id BIGSERIAL PRIMARY KEY,
    full_name VARCHAR(100) NOT NULL,
    citizen_id VARCHAR(12) UNIQUE,
    created_at TIMESTAMP
);
