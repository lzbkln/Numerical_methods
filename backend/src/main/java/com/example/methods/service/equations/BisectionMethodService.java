package com.example.methods.service.equations;

import com.example.methods.service.util.FunctionParser;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class BisectionMethodService extends SolveProblem {

    public static double bisectionMethod(Function<Double, Double> f, double a, double b, double epsilon) {
        double mid;
        while ((b - a) / 2 > epsilon) {
            mid = (a + b) / 2;
            if (f.apply(mid) == 0) {
                return mid;
            }
            if (f.apply(a) * f.apply(mid) < 0) {
                b = mid;
            } else {
                a = mid;
            }
        }
        return (a + b) / 2;
    }

    @Override
    public double solveProblem(String userFunction, double a, double b, double epsilon) {
        Function<Double, Double> f = FunctionParser.parseFunction(userFunction);
        double fa = f.apply(a);
        double fb = f.apply(b);
        if (fa * fb >= 0) {
            throw new IllegalArgumentException("Корня в заданном отрезке нет.");
        }
        return bisectionMethod(f, a, b, epsilon);
    }
}
