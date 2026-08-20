package com.example.core_system_rbac.exceptions;

// Dùng khi vi phạm logic (ví dụ: Email trùng lặp) (Code 400)
public class DuplicateResourceException extends RuntimeException {
    public DuplicateResourceException(String message) {
        super(message);
    }
}
