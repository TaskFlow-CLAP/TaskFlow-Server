UPDATE member m
SET
    in_progress_task_count = COALESCE((
                                          SELECT COUNT(*)
                                          FROM task t
                                          WHERE t.processor_id = m.member_id
                                            AND t.task_status = 'IN_PROGRESS'
                                      ),0),
    in_reviewing_task_count = COALESCE((
                                           SELECT COUNT(*)
                                           FROM task t
                                           WHERE t.processor_id = m.member_id
                                             AND t.task_status = 'IN_REVIEWING'
                                       ),0)
