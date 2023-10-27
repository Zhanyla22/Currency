package com.example.exchangerates.entity.base;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@MappedSuperclass
@NoArgsConstructor
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    /**
     * uuid используется для манипуляции данными вместо id для более безопасного зарпоса
     */
    @Column(name = "uuid")
    UUID uuid;

    @Column(name = "created_date")
    LocalDateTime createdDate;

    @Column(name = "updated_date")
    LocalDateTime updatedDate;

    /**
     * при создании новой записи
     * берет дату(LocalDateTime) на данный момент и сохраняет на поле createdDate
     */
    @PrePersist
    public void prePersist() {
        this.createdDate = LocalDateTime.now();
    }

    /**
     * при обновлении  записи
     * берет дату(LocalDateTime) на данный момент и сохраняет на поле updatedDate
     */
    @PreUpdate
    public void preUpdate() {
        this.updatedDate = LocalDateTime.now();
    }
}
