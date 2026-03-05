CREATE TABLE leaves (
    leave_id VARCHAR(20) NOT NULL,
    username VARCHAR(50) NOT NULL,
    leave_type VARCHAR(20) NOT NULL,
    from_date DATE NOT NULL,
    to_date DATE NOT NULL,
    reason TEXT,
    status VARCHAR(20) DEFAULT 'Pending',
    PRIMARY KEY (leave_id),
    FOREIGN KEY (username) REFERENCES users(username)
);