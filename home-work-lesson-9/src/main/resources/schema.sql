CREATE TABLE author (
    id          CHAR(36)        PRIMARY KEY,
    name        VARCHAR2(300)   NOT NULL,
    surname     VARCHAR2(300)   NOT NULL
);

CREATE TABLE book (
    id               CHAR(36)        PRIMARY KEY,
    name             VARCHAR2(300)   NOT NULL,
    published_year   VARCHAR2(4)       NOT NULL
);

CREATE TABLE style (
    id      VARCHAR2(300)    PRIMARY KEY
);