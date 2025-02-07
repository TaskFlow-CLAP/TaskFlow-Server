ALTER TABLE task ADD COLUMN attachment_count INT NOT NULL DEFAULT 0;

UPDATE task t
SET attachment_count = (
    SELECT COUNT(*)
    FROM attachment a
    WHERE a.task_id = t.task_id
    AND a.is_deleted = false
);

ALTER TABLE task MODIFY COLUMN attachment_count INT NOT NULL;