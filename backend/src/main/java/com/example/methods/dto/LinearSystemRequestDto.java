package com.example.methods.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@Builder
public class LinearSystemRequestDto {
    private int n;
    private double[][] A;
    private double[] b;
    private double[] initialGuess;
    private double tolerance = 1e-6;
    private int maxIterations = 1000;
}
