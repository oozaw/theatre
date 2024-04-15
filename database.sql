CREATE DATABASE theatre;

DROP TABLE IF EXISTS users;

CREATE TABLE users
(
    id               VARCHAR(100) NOT NULL,
    name             VARCHAR(100) NOT NULL,
    email            VARCHAR(100) NOT NULL UNIQUE,
    phone            VARCHAR(100) NOT NULL UNIQUE,
    password         VARCHAR(100) NOT NULL,
    role             VARCHAR(100) NOT NULL,
    token            VARCHAR(100) UNIQUE,
    token_expired_at BIGINT,
    created_at       TIMESTAMP    NOT NULL DEFAULT now(),
    updated_at       TIMESTAMP    NOT NULL,
    PRIMARY KEY (id)
);

SELECT *
FROM users;

DROP TABLE IF EXISTS theatres;

CREATE TABLE theatres
(
    id         VARCHAR(100) NOT NULL,
    name       VARCHAR(100) NOT NULL,
    city       VARCHAR(100) NOT NULL,
    province   VARCHAR(100) NOT NULL,
    address    TEXT         NOT NULL,
    latitude   VARCHAR(100) NOT NULL,
    longitude  VARCHAR(100) NOT NULL,
    created_at TIMESTAMP    NOT NULL,
    updated_at TIMESTAMP    NOT NULL,
    PRIMARY KEY (id)
);

SELECT *
FROM theatres;

DROP TABLE IF EXISTS movies;

CREATE TABLE movies
(
    id         VARCHAR(100) NOT NULL,
    title      VARCHAR(100) NOT NULL,
    synopsys   TEXT         NOT NULL,
    duration   VARCHAR(100) NOT NULL,
    created_at TIMESTAMP    NOT NULL,
    updated_at TIMESTAMP    NOT NULL,
    PRIMARY KEY (id)
);

SELECT *
FROM movies;