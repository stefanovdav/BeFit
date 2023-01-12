CREATE TABLE IF NOT EXISTS users (
    id INT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(255) NOT NULL,
    password_hash VARCHAR(60) NOT NULL,
    avatarURL VARCHAR(255),
    balance DECIMAL(10,2) DEFAULT 0.00
);

CREATE TABLE IF NOT EXISTS posts (
    id INT PRIMARY KEY AUTO_INCREMENT,
    user_id INT NOT NULL,
    imageURL VARCHAR(255),
    content VARCHAR(255),
    votes INT NOT NULL,
    postTime DATETIME DEFAULT CURRENT_TIMESTAMP,
    isArchived BOOLEAN NOT NULL DEFAULT FALSE,
    FOREIGN KEY (user_id) REFERENCES users(id)
);

CREATE TABLE IF NOT EXISTS fitGroups (
    id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(50) NOT NULL,
    stake DECIMAL(10,2)
);

CREATE TABLE IF NOT EXISTS user_groups (
    user_id INT NOT NULL,
    group_id INT NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (group_id) REFERENCES fitGroups(id)
);

CREATE TABLE IF NOT EXISTS comments (
   id INT NOT NULL AUTO_INCREMENT,
   post_id INT NOT NULL,
   user_id INT NOT NULL,
   content TEXT NOT NULL,
   PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS sub_comments (
   id INT NOT NULL AUTO_INCREMENT,
   comment_id INT NOT NULL,
   user_id INT NOT NULL,
   content TEXT NOT NULL,
   PRIMARY KEY (id),
   FOREIGN KEY (comment_id) REFERENCES comments(id)
);

CREATE TABLE IF NOT EXISTS auth_tokens (
    id INT PRIMARY KEY AUTO_INCREMENT,
    token VARCHAR(255) NOT NULL,
    user_id INT NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users(id)
);

CREATE TABLE IF NOT EXISTS roles (
    id INT PRIMARY KEY,
    name VARCHAR(255) UNIQUE
);

CREATE TABLE IF NOT EXISTS users_to_roles (
    user_id INT,
    role_id INT,
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (role_id) REFERENCES roles(id),
    PRIMARY KEY (user_id, role_id)
);

INSERT INTO roles(id, name) VALUES (1, 'USER');
INSERT INTO roles(id, name) VALUES (2, 'ADMIN');

INSERT INTO credentials(id, username, password_hash) VALUES (1, 'admin', '$2a$10$aXkejBeA6PgfnucD4Sq5aeK6b/jAW0bcQVJXzqYqriGCCDfB4.7By');
INSERT INTO credentials_to_roles (credentials_id, role_id) VALUES (1, 2)