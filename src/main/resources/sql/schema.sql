-- Create Zone table
CREATE TABLE IF NOT EXISTS zone (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE,
    description VARCHAR(500),
    capacity INT NOT NULL CHECK (capacity > 0),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Create Creature table
CREATE TABLE IF NOT EXISTS creature (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    species VARCHAR(100) NOT NULL,
    size DOUBLE NOT NULL CHECK (size > 0),
    danger_level INT NOT NULL CHECK (danger_level >= 0 AND danger_level <= 10),
    health_status VARCHAR(50) NOT NULL,
    zone_id BIGINT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (zone_id) REFERENCES zone(id) ON DELETE SET NULL,
    UNIQUE KEY unique_creature_zone (name, zone_id)
);

-- Create indexes for better query performance
CREATE INDEX idx_creature_zone_id ON creature(zone_id);
CREATE INDEX idx_creature_species ON creature(species);
CREATE INDEX idx_zone_name ON zone(name);
