package com.example.core_system_rbac.entities;

//public abstract class BaseObject {
//    private String id;
//    public BaseObject(){
//
//    }
//    public BaseObject(String id){
//        this.id=id;
//    }
//    public String getId(){
//        return  id;
//    }
//    public void setId(String newId){
//        id = newId;
//    }
//}


import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.UUID;

@MappedSuperclass
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder // Dùng SuperBuilder thay vì Builder cho class có kế thừa
@FieldDefaults(level = AccessLevel.PRIVATE) // Tự động set private cho mọi thuộc tính
public abstract class BaseObject {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @JdbcTypeCode(SqlTypes.UUID)
    UUID id; // Đã bỏ chữ 'private' nhờ có @FieldDefaults
}