--liquibase formatted sql
-- ****************************************************
-- Create Table: problems
-- Author: lzbkl
-- Date: 17/12/2024
-- ****************************************************

CREATE TABLE problems (
    id INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description TEXT NOT NULL
);