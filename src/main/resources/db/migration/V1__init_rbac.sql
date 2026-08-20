-- ============================================================
-- V4__update_rbac_and_users_schema.sql
-- Cập nhật schema: Thêm bảng user_role, bổ sung thuộc tính
-- ============================================================

-- 1. Bảng Users (Người dùng) - Đã bỏ id_role và thêm các trường mới
CREATE TABLE users
(
    id           UUID PRIMARY KEY      DEFAULT gen_random_uuid(),
    name         VARCHAR(100) NOT NULL,
    password     VARCHAR(100) NOT NULL,
    phone_number VARCHAR(100),
    email        VARCHAR(100) NOT NULL UNIQUE,
    birthday     TIMESTAMP,
    created_at   TIMESTAMP    NOT NULL DEFAULT now(),
    is_delete    BOOLEAN               DEFAULT FALSE, -- Mặc định khi mới tạo là chưa xóa
    is_active    BOOLEAN               DEFAULT TRUE   -- Mặc định khi mới tạo là đang hoạt động
);

-- 2. Bảng Roles (Nhóm quyền) - Bổ sung cờ trạng thái
CREATE TABLE roles
(
    id          UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    role_name   VARCHAR(100) NOT NULL,
    role_code   VARCHAR(100) NOT NULL UNIQUE,
    is_system   BOOLEAN          DEFAULT FALSE,
    description VARCHAR(100),
    is_delete   BOOLEAN          DEFAULT FALSE,
    is_active   BOOLEAN          DEFAULT TRUE
);

-- 3. Bảng Permissions (Các hành động cụ thể) - Bổ sung cờ trạng thái
CREATE TABLE permissions
(
    id          UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name        VARCHAR(100) NOT NULL,
    "group"     VARCHAR(100), -- Dùng ngoặc kép vì 'group' là từ khóa hệ thống
    description VARCHAR(100),
    is_delete   BOOLEAN          DEFAULT FALSE,
    is_active   BOOLEAN          DEFAULT TRUE
);

-- 4. Bảng User_Role (BẢNG MỚI: Trung gian n-n giữa users và roles)
CREATE TABLE user_role
(
    id      UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    id_user UUID NOT NULL REFERENCES users (id) ON DELETE CASCADE,
    id_role UUID NOT NULL REFERENCES roles (id) ON DELETE CASCADE,
    -- Đảm bảo một user không bị gán trùng một role nhiều lần
    CONSTRAINT unique_user_role UNIQUE (id_user, id_role)
);

-- 5. Bảng Role_Permissions (Trung gian n-n giữa roles và permissions)
CREATE TABLE role_permissions
(
    id            UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    id_roles      UUID NOT NULL REFERENCES roles (id) ON DELETE CASCADE,
    id_permission UUID NOT NULL REFERENCES permissions (id) ON DELETE CASCADE,
    CONSTRAINT unique_role_permission UNIQUE (id_roles, id_permission)
);

-- ============================================================
-- Tạo Index để tối ưu truy vấn
-- ============================================================
CREATE INDEX idx_users_email ON users (email);
CREATE INDEX idx_roles_code ON roles (role_code);
CREATE INDEX idx_user_role_user ON user_role (id_user);
CREATE INDEX idx_user_role_role ON user_role (id_role);
CREATE INDEX idx_role_perms_role ON role_permissions (id_roles);
CREATE INDEX idx_role_perms_perm ON role_permissions (id_permission);