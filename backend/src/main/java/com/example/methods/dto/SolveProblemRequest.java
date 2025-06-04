package com.example.methods.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class SolveProblemRequest {
    private String methodId;
    private String userFunction;
    private double a;
    private double b;
    private double epsilon;
    private double m;
}