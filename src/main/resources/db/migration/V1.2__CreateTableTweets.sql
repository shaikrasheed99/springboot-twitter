CREATE TABLE tweets (
    id INT NOT NULL PRIMARY KEY,
    description VARCHAR(100) NOT NULL,
    author_id INT NOT NULL REFERENCES users(id)
);