package com.example.methods.service.interpolation;

import org.springframework.stereotype.Component;

import java.util.List;

@Component
public abstract class SolveInterpolationProblem {
    abstract public String solveInterpolationProblem(List<Double> xValues, List<Double> fxValues, List<Double> interpolationPoints);
}
