CREATE TABLE author (
    id          CHAR(36)        PRIMARY KEY,
    name        VARCHAR2(300)   NOT NULL,
    surname     VARCHAR2(300)   NOT NULL
);

CREATE TABLE book (
    isbn        CHAR(36)        PRIMARY KEY,
    name        VARCHAR2(300)   NOT NULL,
    published_year   VARCHAR2(4)       NOT NULL
);

CREATE TABLE style (
    name  VARCHAR2(300)    PRIMARY KEY
);

CREATE TABLE author_book (
    author_id   CHAR(36)    NOT NULL,
    book_isbn   CHAR(36)    NOT NULL,

    PRIMARY KEY (author_id, book_isbn),
    CONSTRAINT FK_AUTHOR      FOREIGN KEY (author_id) REFERENCES author(id),
    CONSTRAINT FK_BOOK        FOREIGN KEY (book_isbn) REFERENCES book(isbn)
);

CREATE TABLE book_style (
    book_isbn   CHAR(36)    NOT NULL,
    style_name  VARCHAR2(300)    NOT NULL,

    PRIMARY KEY (book_isbn, style_name),
    CONSTRAINT FK_BOOK_ISBN         FOREIGN KEY (book_isbn)     REFERENCES book(isbn),
    CONSTRAINT FK_STYLE             FOREIGN KEY (style_name)    REFERENCES style(name)
);