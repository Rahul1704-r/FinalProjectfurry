CREATE EXTENSION IF NOT EXISTS postgis;

CREATE TABLE IF NOT EXISTS provider (
  id SERIAL PRIMARY KEY,
  name VARCHAR(200) NOT NULL,
  phone VARCHAR(50),
  location geometry(Point, 4326) NOT NULL
);

CREATE TABLE IF NOT EXISTS provider_service_map (
  provider_id INT NOT NULL REFERENCES provider(id) ON DELETE CASCADE,
  service_type_id INT NOT NULL REFERENCES service_type(id) ON DELETE CASCADE,
  PRIMARY KEY (provider_id, service_type_id)
);

