-- 부서 추가
INSERT INTO department (id, code, name, status) VALUES (1, 'DEPT001', 'IT Department', 'ACTIVE');

-- 관리자 추가
INSERT INTO member (id, name, email, nickname, is_reviewer, role, department_role, status, password, image_url, notification_enabled, department_id)
VALUES (1, 'Admin User', 'admin@example.com', 'Admin1', FALSE, 'ROLE_ADMIN', 'Admin', 'ACTIVE', 'admin123', 'http://example.com/admin.jpg', TRUE, 1);

-- 카테고리 추가
INSERT INTO category (id, code, name, description_example, is_deleted, admin_id)
VALUES (1, 'CATEGORY001', 'Development', 'Development tasks category', FALSE, 1);

-- 라벨 추가
INSERT INTO label (id, label_type, admin_id, is_deleted)
VALUES (1, 'EMERGENCY', 1, FALSE);

-- 첫 번째 관리자 추가
INSERT INTO member (id, name, email, nickname, is_reviewer, role, department_role, status, password, image_url, notification_enabled, department_id)
VALUES (2, 'Manager1', 'manager1@example.com', 'Manager1', TRUE, 'ROLE_MANAGER', 'Manager', 'ACTIVE', 'manager123', 'http://example.com/manager1.jpg', TRUE, 1);

-- 두 번째 관리자 추가
INSERT INTO member (id, name, email, nickname, is_reviewer, role, department_role, status, password, image_url, notification_enabled, department_id)
VALUES (3, 'Manager2', 'manager2@example.com', 'Manager2', TRUE, 'ROLE_MANAGER', 'Manager', 'ACTIVE', 'manager123', 'http://example.com/manager2.jpg', TRUE, 1);

-- 일반 사용자 추가
INSERT INTO member (id, name, email, nickname, is_reviewer, role, department_role, status, password, image_url, notification_enabled, department_id)
VALUES (4, 'User1', 'user1@example.com', 'User1', FALSE, 'ROLE_USER', 'User', 'ACTIVE', 'user123', 'http://example.com/user1.jpg', TRUE, 1);

-- 태스크 추가
INSERT INTO task (id, task_code, title, description, category_id, requester_id, task_status, processor_order, reviewer_id, processor_id, label_id, due_date, completed_at)
VALUES (1, 'TASK001', 'Task Title', 'Task Description', 1, 1, 'PENDING_COMPLETED', 1, 1, 2, 1, '2025-01-31 23:59:59', NULL);
