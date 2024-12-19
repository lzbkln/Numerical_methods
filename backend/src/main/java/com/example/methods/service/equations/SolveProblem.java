package com.example.methods.service.equations;

import org.springframework.stereotype.Component;

@Component
abstract public class SolveProblem {
    abstract public double solveProblem(String userFunction, double a, double b, double epsilon);
}
