package com.example.methods.service.equations;

import com.example.methods.service.util.FunctionParser;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class FixedChordsMethodService extends SolveProblem {
    private static final int MAX_ITER = 100;
    private static final double H = 1e-5;
    private static final double TOLERANCE = 1e-6;

    public static double numericalSecondDerivative(Function<Double, Double> f, double x) {
        return (f.apply(x + H) - 2 * f.apply(x) + f.apply(x - H)) / (H * H);
    }

    public static double[] determineInitialPoints(Function<Double, Double> f, double a, double b) {
        double fa = f.apply(a);
        double fb = f.apply(b);
        if (fa * fb >= 0) {
            throw new IllegalArgumentException("Корня в заданном отрезке нет.");
        }
        double d2fa = numericalSecondDerivative(f, a);
        double d2fb = numericalSecondDerivative(f, b);

        double x0, x1;
        if (fa * d2fa > 0) {
            x0 = a;
            x1 = b;
        } else if (fb * d2fb > 0) {
            x0 = b;
            x1 = a;
        } else {
            throw new IllegalArgumentException("Невозможно определить начальные точки x0 и x1.");
        }

        return new double[]{x0, x1};
    }

    public static double chordMethod(Function<Double, Double> f, double x0, double x1, double epsilon) {
        double x = x1;
        for (int i = 0; i < MAX_ITER; i++) {
            double fx = f.apply(x);
            double fx0 = f.apply(x0);
            if (Math.abs(fx - fx0) < TOLERANCE) {
                throw new IllegalArgumentException("Метод неподвижных хорд не может быть применен.");
            }
            double x1_new = x - (fx * (x - x0)) / (fx - fx0);
            if (Math.abs(x1_new - x) < epsilon) {
                return x1_new;
            }
            x = x1_new;
        }
        throw new IllegalArgumentException("Метод неподвижных хорд не сходится за максимальное количество итераций.");
    }

    @Override
    public double solveProblem(String userFunction, double a, double b, double epsilon) {
        Function<Double, Double> f = FunctionParser.parseFunction(userFunction);
        double[] initialPoints = determineInitialPoints(f, a, b);
        double x0 = initialPoints[0];
        double x1 = initialPoints[1];
        return chordMethod(f, x0, x1, epsilon);
    }
}