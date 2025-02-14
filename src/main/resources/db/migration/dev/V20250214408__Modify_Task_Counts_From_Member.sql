START TRANSACTION;

UPDATE member m
    LEFT JOIN (
        SELECT processor_id,
               SUM(CASE WHEN task_status = 'IN_PROGRESS' THEN 1 ELSE 0 END) AS in_progress_count,
               SUM(CASE WHEN task_status = 'IN_REVIEWING' THEN 1 ELSE 0 END) AS in_reviewing_count
        FROM task
        GROUP BY processor_id
    ) t ON m.member_id = t.processor_id
SET
    m.in_progress_task_count = COALESCE(t.in_progress_count, 0),
    m.in_reviewing_task_count = COALESCE(t.in_reviewing_count, 0)
WHERE m.status != 'DELETED';

COMMIT;

