-- Ограничение возраста студента (не менее 16 лет)
ALTER TABLE student ADD CONSTRAINT age_chek CHECK (age >= 16);

-- Ограничение уникальности имени студента и запрет NULL
ALTER TABLE student ADD CONSTRAINT name_unique UNIQUE (name);
ALTER TABLE student ALTER COLUMN name SET NOT NULL;

-- Ограничение уникальности пары "название - цвет" для факультета
ALTER TABLE faculty ADD CONSTRAINT name_color_unique UNIQUE (name, color);

-- Значение по умолчанию для возраста (20 лет)
ALTER TABLE student ALTER COLUMN age SET DEFAULT 20;