CREATE TABLE follows (
    follower_id INT NOT NULL REFERENCES users(id),
    follows_id INT NOT NULL REFERENCES users(id),
    PRIMARY KEY(follower_id, follows_id)
);