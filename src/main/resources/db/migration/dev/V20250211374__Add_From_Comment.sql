ALTER TABLE comment
    ADD COLUMN original_name VARCHAR(255),
    ADD COLUMN file_url VARCHAR(255),
    ADD COLUMN file_size VARCHAR(255);