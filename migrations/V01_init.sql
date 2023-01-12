CREATE TABLE IF NOT EXISTS users (
    id INT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(255) NOT NULL,
    password_hash VARCHAR(60) NOT NULL,
    image_id INT,
    balance DECIMAL(10,2) DEFAULT 0.00,
    FOREIGN KEY (image_id) REFERENCES images(id)
);

CREATE TABLE IF NOT EXISTS posts (
    id INT PRIMARY KEY AUTO_INCREMENT,
    user_id INT NOT NULL,
    image_id INT,
    content VARCHAR(255),
    votes INT NOT NULL DEFAULT 0,
    post_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    is_archived BOOLEAN NOT NULL DEFAULT FALSE,
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (image_id) REFERENCES images(id)
);

CREATE TABLE IF NOT EXISTS images (
    id INT PRIMARY KEY AUTO_INCREMENT,
    title VARCHAR(255),
    url VARCHAR(255),
    public_id VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS fitGroups (
    id INT PRIMARY KEY AUTO_INCREMENT,
    group_name VARCHAR(50) NOT NULL,
    stake DECIMAL(10,2),
    balance DECIMAL(10,2) DEFAULT 0.00
);

CREATE TABLE IF NOT EXISTS group_post (
    group_id INT NOT NULL,
    post_id INT NOT NULL,
    FOREIGN KEY (post_id) REFERENCES posts(id),
    FOREIGN KEY (group_id) REFERENCES fitGroups(id),
    PRIMARY KEY (post_id, group_id)
);

CREATE TABLE IF NOT EXISTS user_groups (
    user_id INT NOT NULL,
    group_id INT NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (group_id) REFERENCES fitGroups(id),
    PRIMARY KEY (user_id, group_id)
);

CREATE TABLE IF NOT EXISTS comments (
   id INT NOT NULL AUTO_INCREMENT,
   post_id INT NOT NULL,
   user_id INT NOT NULL,
   content TEXT NOT NULL,
   comment_path VARCHAR(20) NOT NULL,
   PRIMARY KEY (id),
   FOREIGN KEY (parent_comment_id) REFERENCES comments(id)
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
    user_id INT NOT NULL,
    role_id INT NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (role_id) REFERENCES roles(id),
    PRIMARY KEY (user_id, role_id)
);