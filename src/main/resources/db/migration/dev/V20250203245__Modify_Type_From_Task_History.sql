alter table task_history
    modify type enum ('TASK_TERMINATED', 'COMMENT', 'COMMENT_FILE', 'PROCESSOR_ASSIGNED', 'PROCESSOR_CHANGED', 'STATUS_SWITCHED') not null;