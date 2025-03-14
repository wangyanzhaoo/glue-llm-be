package com.rune.admin.domain.entity;

import com.rune.annotation.PrimaryEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@PrimaryEntity
@Table(name = "primary_p")
public class Primary {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id ;

    private String primaryName;
}
