package com.example.methods.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class SolveProblemRequest {
    private String problemName;
    private String methodName;
    private String userFunction;
    private double a;
    private double b;
    private double epsilon;
}