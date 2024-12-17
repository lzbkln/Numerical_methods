package com.example.methods.model;

import jakarta.persistence.Entity;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Builder
public class Method {
    private Long id;
    private String name;
    private Description description;
    private String example;
    private Problem problem;

    @Getter
    @Setter
    @Builder
    public static class Description {
        private String full;
        private String shortDescr;
    }

}
