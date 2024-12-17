package com.example.methods.model;

import jakarta.persistence.Entity;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
@Builder
public class Problem {
    private Long id;
    private String name;
    private String fullDescription;
    private List<Method> methods;
}