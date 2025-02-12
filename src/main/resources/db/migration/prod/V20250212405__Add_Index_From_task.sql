CREATE INDEX idx_task_processor_created
    ON task (processor_id ASC, created_at DESC);
CREATE INDEX idx_task_requester_created
    ON task (requester_id ASC, created_at DESC);
CREATE INDEX idx_task_status_created
    ON task (task_status ASC, created_at DESC);
CREATE INDEX idx_task_created
    ON task (created_at DESC);