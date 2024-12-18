package com.example.methods.service.equations;

public interface NonlinearEquation {
    double findRoot(String userFunction, double a, double b, double epsilon);
}