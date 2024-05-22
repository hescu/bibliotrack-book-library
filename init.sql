CREATE DATABASE IF NOT EXISTS bibliotrack;

USE bibliotrack;

CREATE TABLE IF NOT EXISTS authority (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    authority ENUM('ROLE_ADMIN', 'ROLE_USER') NOT NULL
);

CREATE TABLE IF NOT EXISTS wishlist (
    id BIGINT AUTO_INCREMENT PRIMARY KEY
);

CREATE TABLE IF NOT EXISTS book (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    isbn VARCHAR(255) NOT NULL,
    title VARCHAR(255) NOT NULL,
    thumbnail VARCHAR(255),
    publisher VARCHAR(255),
    published_date VARCHAR(255),
    page_count INT,
    UNIQUE (isbn)
);

CREATE TABLE IF NOT EXISTS user (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    wishlist_id BIGINT NOT NULL,
    FOREIGN KEY (wishlist_id) REFERENCES wishlist(id)
);

CREATE TABLE IF NOT EXISTS userprincipal (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    account_non_expired BOOLEAN NOT NULL,
    account_non_locked BOOLEAN NOT NULL,
    credentials_non_expired BOOLEAN NOT NULL,
    enabled BOOLEAN NOT NULL,
    user_id BIGINT NOT NULL,
    FOREIGN KEY (user_id) REFERENCES user(id)
);

CREATE TABLE IF NOT EXISTS wishlist_book (
    wishlist_id BIGINT NOT NULL,
    book_id BIGINT NOT NULL,
    PRIMARY KEY (wishlist_id, book_id),
    FOREIGN KEY (wishlist_id) REFERENCES wishlist(id),
    FOREIGN KEY (book_id) REFERENCES book(id)
);

CREATE TABLE IF NOT EXISTS userprincipal_authority (
    userprincipal_id BIGINT NOT NULL,
    authority_id BIGINT NOT NULL,
    PRIMARY KEY (userprincipal_id, authority_id),
    FOREIGN KEY (userprincipal_id) REFERENCES userprincipal(id),
    FOREIGN KEY (authority_id) REFERENCES authority(id)
);

CREATE TABLE IF NOT EXISTS bookshelf (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) DEFAULT 'Bookshelf',
    user_id BIGINT NOT NULL,
    FOREIGN Key (user_id) REFERENCES user(id)
);

CREATE TABLE IF NOT EXISTS bookshelf_book (
    bookshelf_id BIGINT NOT NULL,
    book_id BIGINT NOT NULL,
    PRIMARY KEY (bookshelf_id, book_id),
    FOREIGN KEY (bookshelf_id) REFERENCES bookshelf(id),
    FOREIGN KEY (book_id) REFERENCES book(id)
);

INSERT INTO authority (authority) VALUES ('ROLE_ADMIN'), ('ROLE_USER');

INSERT INTO wishlist VALUES (DEFAULT);

SET @wishlist_id = LAST_INSERT_ID();

INSERT INTO user (wishlist_id) VALUES (@wishlist_id);

SET @user_id = LAST_INSERT_ID();

INSERT INTO userprincipal (username, password, account_non_expired, account_non_locked, credentials_non_expired, enabled, user_id)
VALUES ('admin', '$2a$10$fvd41HJNVoQlf5WTw2RTUObUNs5fwqBnCGF3wWN42qU5lO284/AXG', true, true, true, true, @user_id);

SET @admin_user_id = LAST_INSERT_ID();

SELECT @wishlist_id, @admin_user_id;

SELECT * FROM userprincipal;

INSERT INTO userprincipal_authority (userprincipal_id, authority_id)
SELECT @admin_user_id, id FROM authority WHERE authority = 'ROLE_ADMIN';

INSERT INTO bookshelf (user_id)
VALUES (@user_id);