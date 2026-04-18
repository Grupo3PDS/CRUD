-- Insert sample zones
INSERT IGNORE INTO zone (id, name, description, capacity) VALUES
(1, 'Savana Africana', 'Zona con ambiente similar a la sabana africana', 50),
(2, 'Selva Tropical', 'Zona con clima tropical y vegetación densa', 75),
(3, 'Ártico', 'Zona con clima frío simulando el ártico', 30),
(4, 'Desierto', 'Zona desértica con arena y cactus', 40),
(5, 'Acuario', 'Zona acuática con piscinas y fuentes', 100);

-- Insert sample creatures
INSERT IGNORE INTO creature (id, name, species, size, danger_level, health_status, zone_id) VALUES
(1, 'Leo', 'León', 2.5, 8, 'Saludable', 1),
(2, 'Girafa', 'Jirafa', 5.5, 2, 'Saludable', 1),
(3, 'Cebra', 'Cebra', 2.3, 3, 'Buena', 1),
(4, 'Serpiente Python', 'Serpiente', 3.0, 7, 'Saludable', 2),
(5, 'Mono Capuchino', 'Mono', 0.6, 2, 'Excelente', 2),
(6, 'Oso Polar', 'Oso', 2.8, 9, 'Saludable', 3),
(7, 'Pingüino', 'Pingüino', 1.2, 1, 'Buena', 3),
(8, 'Camello', 'Camello', 2.0, 3, 'Saludable', 4),
(9, 'Delfín', 'Delfín', 2.5, 2, 'Excelente', 5),
(10, 'Tiburón', 'Tiburón', 3.5, 10, 'Saludable', 5);
