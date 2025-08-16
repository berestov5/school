-- Создание таблицы "машина"
CREATE TABLE car (
    id BIGSERIAL PRIMARY KEY,
    brand VARCHAR(100) NOT NULL,
    model VARCHAR(100) NOT NULL,
    cost NUMERIC(12, 2) NOT NULL
);

-- Создание таблицы "человек"
CREATE TABLE person (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    age INTEGER NOT NULL,
    has_driver_license BOOLEAN NOT NULL,
    car_id BIGINT REFERENCES car(id)
);
