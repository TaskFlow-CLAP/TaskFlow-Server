ALTER TABLE api_log
    MODIFY COLUMN response_body TEXT NOT NULL,
    MODIFY COLUMN request_body TEXT NOT NULL;