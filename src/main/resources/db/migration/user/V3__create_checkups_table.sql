CREATE TABLE IF NOT EXISTS checkups (
    id BIGSERIAL PRIMARY KEY,
    diagnosis VARCHAR(255),
    patient_id BIGINT,
    CONSTRAINT fk_checkups_patient
        FOREIGN KEY (patient_id)
        REFERENCES patients (id)
);
