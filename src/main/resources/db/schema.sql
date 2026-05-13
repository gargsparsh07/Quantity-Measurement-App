CREATE TABLE IF NOT EXISTS quantity_measurement_entity (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    first_quantity DOUBLE NOT NULL,
    first_unit VARCHAR(50) NOT NULL,
    second_quantity DOUBLE NOT NULL,
    second_unit VARCHAR(50) NOT NULL,
    result DOUBLE,
    operation_type VARCHAR(50) NOT NULL,
    measurement_type VARCHAR(50) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS quantity_measurement_history (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    entity_id BIGINT,
    action VARCHAR(50),
    performed_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (entity_id) REFERENCES quantity_measurement_entity(id)
);

CREATE INDEX IF NOT EXISTS idx_operation_type ON quantity_measurement_entity(operation_type);
CREATE INDEX IF NOT EXISTS idx_measurement_type ON quantity_measurement_entity(measurement_type);
CREATE INDEX IF NOT EXISTS idx_created_at ON quantity_measurement_entity(created_at);
