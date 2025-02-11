-- Step 1: comment 테이블에 컬럼 추가
ALTER TABLE comment
    ADD COLUMN original_name VARCHAR(255),
    ADD COLUMN file_url VARCHAR(255),
    ADD COLUMN file_size VARCHAR(255);

-- Step 2: attachment 데이터 복사
START TRANSACTION;
UPDATE comment c
    JOIN attachment a ON c.comment_id = a.comment_id
    SET
        c.original_name = a.original_name,
        c.file_url = a.file_url,
        c.file_size = a.file_size,
WHERE c.attachment_id IS NOT NULL;
COMMIT;

-- Step 3: attachment 테이블에 제약조건 제거
ALTER TABLE attachment
    DROP FOREIGN KEY FKds6u1rptrsif835t89kb15cyo,
    DROP COLUMN comment_id;