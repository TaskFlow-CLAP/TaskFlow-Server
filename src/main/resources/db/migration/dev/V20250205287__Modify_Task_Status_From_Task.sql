alter table task
    modify task_status enum ('COMPLETED', 'IN_PROGRESS', 'IN_REVIEWING', 'REQUESTED', 'TERMINATED') not null;