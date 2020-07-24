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

CREATE TABLE comment (
    id          CHAR(36)        PRIMARY KEY,
    text        VARCHAR2(1000)  NOT NULL,
    id_book     CHAR(36)        NOT NULL,
    CONSTRAINT FK_BOOK_FROM_COMMENT FOREIGN KEY (id_book) REFERENCES book(id)
)