ALTER TABLE task_history
    ADD COLUMN is_deleted BOOLEAN DEFAULT FALSE;