START TRANSACTION;

UPDATE task_history th
SET modified_member_id = (
    SELECT member_id FROM comment c WHERE c.comment_id = th.comment_id
)
WHERE th.comment_id IS NOT NULL AND th.type='COMMENT_FILE';

COMMIT;