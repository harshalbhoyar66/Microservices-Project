package com.microservices.user.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@Table(name = "users")  // ✅ change this
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String email;
}