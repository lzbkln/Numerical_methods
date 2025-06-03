--liquibase formatted sql
-- ****************************************************
-- Create Table: problems
-- Author: lzbkl
-- Date: 17/05/2025
-- ****************************************************

CREATE TABLE problems (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description TEXT NOT NULL
);

INSERT INTO problems (name, description)
VALUES ('Нелинейные уравнения', 'Описание нелинейных уравнений');