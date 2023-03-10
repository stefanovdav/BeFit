Create database beFit;
use beFit;
CREATE TABLE IF NOT EXISTS images (
    id INT PRIMARY KEY AUTO_INCREMENT,
    title CHAR(255),
    url VARCHAR(255),
    public_id VARCHAR(255) DEFAULT "no id"
);

INSERT INTO images (title, url)
VALUES ("default-avatar_ngrsnt", "https://res.cloudinary.com/dyhaxytra/image/upload/v1673618324/beFit/default-avatar_ngrsnt.png");

INSERT INTO images (title, url)
VALUES ("cbum_vp0fcl", "https://res.cloudinary.com/dyhaxytra/image/upload/v1673454187/beFit/cbum_vp0fcl.jpg");

INSERT INTO images (title, url)
VALUES ("Arnold-Schwarzenegger", "https://res.cloudinary.com/dyhaxytra/image/upload/v1673453963/beFit/Arnold-Schwarzeneggers-Diet-and-Workout-Plan_ucwuen.jpg");

CREATE TABLE IF NOT EXISTS users (
    id INT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(255) NOT NULL UNIQUE,
    password_hash CHAR(60) NOT NULL,
    image_id INT DEFAULT 1,
    balance DECIMAL(10,2) DEFAULT 0.00,
    FOREIGN KEY (image_id) REFERENCES images(id)
);

Select * from users;

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
-- table vzeti pari i sledi kolko pari, vzel, dedline na grupa (cron job ), assign skipped fee na group
CREATE TABLE IF NOT EXISTS fitGroups (
    id INT PRIMARY KEY AUTO_INCREMENT,
    group_name VARCHAR(255) NOT NULL,
    stake DECIMAL(10,2),
    tax INT NOT NULL CHECK (tax BETWEEN 0 AND 100),
    participants INT DEFAULT 0,
    group_key VARCHAR(20) NOT NULL,
	start DATE NOT NULL,
    end DATE NOT NULL
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
    frozen_assets DECIMAL(10,2) NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (group_id) REFERENCES fitGroups(id),
    PRIMARY KEY (user_id, group_id)
);
-- foreign keys
CREATE TABLE IF NOT EXISTS comments (
   id INT NOT NULL AUTO_INCREMENT,
   post_id INT NOT NULL,
   user_id INT NOT NULL,
   content TEXT NOT NULL,
   comment_path VARCHAR(20),
   FOREIGN KEY (user_id) REFERENCES users(id),
   FOREIGN KEY (post_id) REFERENCES posts(id),
   PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS auth_tokens (
    id INT PRIMARY KEY AUTO_INCREMENT,
    token CHAR(16) NOT NULL,
    user_id INT NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users(id)
);

CREATE TABLE IF NOT EXISTS roles (
    id INT PRIMARY KEY,
    name VARCHAR(255) UNIQUE
);

INSERT INTO roles (id, name) VALUES (1, "USER");
INSERT INTO roles (id, name) VALUES (2, "ADMIN");

CREATE TABLE IF NOT EXISTS users_to_roles (
    user_id INT NOT NULL,
    role_id INT NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (role_id) REFERENCES roles(id),
    PRIMARY KEY (user_id, role_id)
);