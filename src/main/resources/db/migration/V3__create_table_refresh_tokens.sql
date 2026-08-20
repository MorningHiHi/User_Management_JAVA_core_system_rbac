-- Tạo bảng refresh_tokens
CREATE TABLE refresh_tokens
(
    id          UUID                     NOT NULL,
    token       VARCHAR(255)             NOT NULL,
    expiry_date TIMESTAMP WITH TIME ZONE NOT NULL,
    user_id     UUID                     NOT NULL,

    -- Khóa chính
    CONSTRAINT pk_refresh_tokens PRIMARY KEY (id),

    -- Đảm bảo token sinh ra là duy nhất toàn hệ thống
    CONSTRAINT uk_refresh_tokens_token UNIQUE (token),

    -- Đảm bảo quan hệ One-to-One: Mỗi user chỉ có 1 refresh token active tại 1 thời điểm
    CONSTRAINT uk_refresh_tokens_user UNIQUE (user_id),

    -- Khóa ngoại liên kết với bảng users
    -- Sử dụng ON DELETE CASCADE để tự động xóa token nếu user bị xóa (xóa cứng)
    CONSTRAINT fk_refresh_tokens_users FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
);