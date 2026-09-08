package com.tuanhv.tripgoapi.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tuanhv.tripgoapi.enums.Role;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "users")
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(unique = true)
    private String email;

    @JsonIgnore
    private String passwordHash;    // never return JSON response

    @Enumerated(EnumType.STRING)
    private Role role;  // USER|ADMIN
}
