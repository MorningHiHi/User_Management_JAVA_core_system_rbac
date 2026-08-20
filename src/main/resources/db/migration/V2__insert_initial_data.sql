-- ============================================================
-- V2__insert_initial_data.sql
-- Insert initial data: Roles, Permissions, Users and mappings
-- ============================================================

-- 1. Insert data into Roles table
INSERT INTO roles (role_name, role_code, is_system, description)
VALUES ('System Administrator', 'ADMIN', TRUE, 'Full system control'),
       ('Store Manager', 'MANAGER', FALSE, 'Manage menu, personnel, and revenue statistics'),
       ('Sales Staff', 'STAFF', FALSE, 'Create orders, process payments, and view transaction history');

-- 2. Insert data into Permissions table
INSERT INTO permissions (name, "group", description)
VALUES
    -- USER_MANAGEMENT Group
    ('View user list', 'USER_MANAGEMENT', 'Permission to view account list'),
    ('Add new user', 'USER_MANAGEMENT', 'Permission to create staff account'),
    ('Update user', 'USER_MANAGEMENT', 'Permission to edit account info'),
    ('Delete/Lock user', 'USER_MANAGEMENT', 'Permission to disable account'),

    -- PRODUCT_MANAGEMENT Group
    ('View product list', 'PRODUCT_MANAGEMENT', 'Permission to view food/drink menu'),
    ('Add product', 'PRODUCT_MANAGEMENT', 'Permission to add new items to menu'),
    ('Update product', 'PRODUCT_MANAGEMENT', 'Permission to edit item price and name'),

    -- ORDER_MANAGEMENT Group
    ('Create order', 'ORDER_MANAGEMENT', 'Permission to create new order for customer'),
    ('View order list', 'ORDER_MANAGEMENT', 'Permission to view order history'),
    ('Cancel order', 'ORDER_MANAGEMENT', 'Permission to cancel order upon error');

-- 3. Insert data into Users table
-- Note: Password is '123456' encoded with BCrypt.
INSERT INTO users (name, password, phone_number, email, birthday)
VALUES ('System Admin', '$2a$10$1xQ7O.WjPOfM/8JgD1wHWeS.wB.v5W1O0O.nNqI.c3C4q5i9Q1y', '0901111111', 'admin@fnb.com',
        '1995-01-01'),
       ('Manager User', '$2a$10$1xQ7O.WjPOfM/8JgD1wHWeS.wB.v5W1O0O.nNqI.c3C4q5i9Q1y', '0902222222', 'manager@fnb.com',
        '1998-05-15'),
       ('Staff User', '$2a$10$1xQ7O.WjPOfM/8JgD1wHWeS.wB.v5W1O0O.nNqI.c3C4q5i9Q1y', '0903333333', 'staff@fnb.com',
        '2001-10-20');

-- 4. Map Permissions to Roles (Role_Permissions) dynamically
-- 4.1. ADMIN has ALL permissions
INSERT INTO role_permissions (id_roles, id_permission)
SELECT r.id, p.id
FROM roles r,
     permissions p
WHERE r.role_code = 'ADMIN';

-- 4.2. MANAGER has Product and Order management permissions
INSERT INTO role_permissions (id_roles, id_permission)
SELECT r.id, p.id
FROM roles r,
     permissions p
WHERE r.role_code = 'MANAGER'
  AND p."group" IN ('PRODUCT_MANAGEMENT', 'ORDER_MANAGEMENT');

-- 4.3. STAFF has permissions to View/Create orders and View menu
INSERT INTO role_permissions (id_roles, id_permission)
SELECT r.id, p.id
FROM roles r,
     permissions p
WHERE r.role_code = 'STAFF'
  AND p.name IN ('Create order', 'View order list', 'View product list');

-- 5. Map Roles to Users (User_Role) dynamically
-- Assign Admin account
INSERT INTO user_role (id_user, id_role)
SELECT u.id, r.id
FROM users u,
     roles r
WHERE u.email = 'admin@fnb.com'
  AND r.role_code = 'ADMIN';

-- Assign Manager account
INSERT INTO user_role (id_user, id_role)
SELECT u.id, r.id
FROM users u,
     roles r
WHERE u.email = 'manager@fnb.com'
  AND r.role_code = 'MANAGER';

-- Assign Staff account
INSERT INTO user_role (id_user, id_role)
SELECT u.id, r.id
FROM users u,
     roles r
WHERE u.email = 'staff@fnb.com'
  AND r.role_code = 'STAFF';