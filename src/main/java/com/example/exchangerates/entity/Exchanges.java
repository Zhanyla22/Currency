package com.example.exchangerates.entity;

import com.example.exchangerates.entity.base.BaseEntity;
import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "exchangess")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Exchanges extends BaseEntity {

    String site;

    LocalDateTime date;

    String name;

    Double value;
}