CREATE DATABASE theatre;

CREATE TABLE users
(
    id               VARCHAR(100) NOT NULL,
    name             VARCHAR(100) NOT NULL,
    email            VARCHAR(100) NOT NULL UNIQUE,
    phone            VARCHAR(100) NOT NULL UNIQUE,
    password         VARCHAR(100) NOT NULL,
    token            VARCHAR(100) UNIQUE,
    token_expired_at BIGINT,
    created_at       TIMESTAMP    NOT NULL DEFAULT now(),
    updated_at       TIMESTAMP    NOT NULL,
    PRIMARY KEY (id)
);

SELECT *
FROM users;