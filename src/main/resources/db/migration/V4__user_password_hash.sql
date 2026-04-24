ALTER TABLE users
    DROP COLUMN password;

ALTER TABLE users
    ADD COLUMN password_hash VARCHAR(255) NOT NULL;
