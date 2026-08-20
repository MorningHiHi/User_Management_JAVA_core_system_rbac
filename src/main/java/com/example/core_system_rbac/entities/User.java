package com.example.core_system_rbac.entities;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

//public class User extends BaseObject{
//    private String name;
//    private String password;
//    private String phoneNumber;
//    private String email;
//    private LocalDateTime birthday;
//    private LocalDateTime createdAt;
//    private boolean isActive;
//    private boolean isDeleted;
//
//    // --- GETTERS & SETTERS ---
//
//    public String getName() {
//        return name;
//    }
//
//    public void setName(String name) {
//        this.name = name;
//    }
//
//    public String getPassword() {
//        return password;
//    }
//
//    public void setPassword(String password) {
//        this.password = password;
//    }
//
//    public String getPhoneNumber() {
//        return phoneNumber;
//    }
//
//    public void setPhoneNumber(String phoneNumber) {
//        this.phoneNumber = phoneNumber;
//    }
//
//    public String getEmail() {
//        return email;
//    }
//
//    public void setEmail(String email) {
//        this.email = email;
//    }
//
//    public LocalDateTime getBirthday() {
//        return birthday;
//    }
//
//    public void setBirthday(LocalDateTime birthday) {
//        this.birthday = birthday;
//    }
//
//    public LocalDateTime getCreatedAt() {
//        return createdAt;
//    }
//
//    public void setCreatedAt(LocalDateTime createdAt) {
//        this.createdAt = createdAt;
//    }
//
//    public boolean isActive() {
//        return isActive;
//    }
//
//    public void setActive(boolean active) {
//        isActive = active;
//    }
//
//    public boolean isDeleted() {
//        return isDeleted;
//    }
//
//    public void setDeleted(boolean deleted) {
//        isDeleted = deleted;
//    }
//}

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder // Tích hợp trơn tru với SuperBuilder của BaseObject
@FieldDefaults(level = AccessLevel.PRIVATE) // Tự động set private cho mọi thuộc tính
public class User extends BaseObject {

    @Column(name = "name", nullable = false, length = 100)
    String name; // Bỏ chữ 'private'

    @Column(name = "password", nullable = false, length = 255)
    String password;

    @Column(name = "phone_number", unique = true, length = 20)
    String phoneNumber;

    @Column(name = "email", nullable = false, unique = true, length = 100)
    String email;

    @Column(name = "birthday")
    LocalDateTime birthday;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    LocalDateTime createdAt;

    @Column(name = "is_active", nullable = false)
    Boolean isActive;

    @Column(name = "is_delete", nullable = false)
    Boolean isDeleted;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    Set<UserRole> userRoles = new HashSet<>();
}
