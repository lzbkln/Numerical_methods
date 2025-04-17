package com.example.methods.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Getter
@Setter
@Builder
@Table(name = "problems")
@NoArgsConstructor
@AllArgsConstructor
public class Problem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String description;

    @OneToMany(mappedBy = "problem", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Method> methods;
}
