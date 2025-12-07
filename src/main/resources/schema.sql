CREATE TABLE aeroporto (
    id_aeroporto INT AUTO_INCREMENT PRIMARY KEY,
    nome_aeroporto VARCHAR(255) NOT NULL,
    codigo_iata VARCHAR(3) UNIQUE NOT NULL,
    cidade VARCHAR(255) NOT NULL,
    codigo_pais_iso VARCHAR(2) NOT NULL,
    latitude DECIMAL(10, 6) NOT NULL,
    longitude DECIMAL(10, 6) NOT NULL,
    altitude DECIMAL(10, 2) NOT NULL
);