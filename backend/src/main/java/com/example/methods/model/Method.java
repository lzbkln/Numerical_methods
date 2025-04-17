package com.example.methods.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@Table(name = "methods")
@NoArgsConstructor
@AllArgsConstructor
public class Method {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "problem_id", nullable = false)
    private Problem problem;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String example;
}
