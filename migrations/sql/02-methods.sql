--liquibase formatted sql
-- ****************************************************
-- Create Table: methods
-- Author: lzbkl
-- Date: 17/12/2024
-- ****************************************************

CREATE TABLE methods (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    problem_id BIGINT REFERENCES problems(id),
    name VARCHAR(255) NOT NULL,
    description TEXT NOT NULL,
    example TEXT NOT NULL
);
