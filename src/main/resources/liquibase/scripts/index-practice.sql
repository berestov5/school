-- liquibase formatted sql

-- changeset vcheburator:1
CREATE INDEX IF NOT EXISTS student_name_index ON student (name);

-- changeset vcheburator:2
CREATE INDEX IF NOT EXISTS faculty_name_color_index ON faculty (name, color);

