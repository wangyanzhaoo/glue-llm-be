package com.rune.admin.domain.entity;

import com.rune.annotation.SecondEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@SecondEntity
@Table(name = "second_s")
public class Second {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id ;

    private String secondName;
}
