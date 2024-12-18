package com.example.methods.service.equations;

import com.example.methods.service.util.FunctionParser;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class NewtonMethodService {
    private static final int MAX_ITER = 100;
    private static final double H = 1e-5;
    private static final double TOLERANCE = 1e-6;

    public static double numericalDerivative(Function<Double, Double> f, double x) {
        return (f.apply(x + H) - f.apply(x - H)) / (2 * H);
    }

    public static double numericalSecondDerivative(Function<Double, Double> f, double x) {
        return (f.apply(x + H) - 2 * f.apply(x) + f.apply(x - H)) / (H * H);
    }

    public static double newtonMethod(Function<Double, Double> f, Function<Double, Double> df, double x0, double epsilon) {
        double x = x0;
        for (int i = 0; i < MAX_ITER; i++) {
            double fx = f.apply(x);
            double dfx = df.apply(x);
            if (Math.abs(dfx) < TOLERANCE) {
                throw new IllegalArgumentException("Производная равна нулю. Метод Ньютона не может быть применен.");
            }
            double x1 = x - fx / dfx;
            if (Math.abs(x1 - x) < epsilon) { // abs(f(x))/m < eps, m=min(abs(f'(c)))
                return x1;
            }
            x = x1;
        }
        throw new IllegalArgumentException("Метод Ньютона не сходится за максимальное количество итераций.");
    }

    public static double determineInitialPoint(Function<Double, Double> f, double a, double b) {
        double fa = f.apply(a);
        double fb = f.apply(b);
        if (fa * fb >= 0){
            throw new IllegalArgumentException("Корня в заданном отрезке нет.");
        }
        double d2fa = numericalSecondDerivative(f, a);
        double d2fb = numericalSecondDerivative(f, b);

        if (d2fa * fa >= 0) {
            return a;
        } else if (d2fb * fb >= 0) {
            return b;
        } else {
            throw new IllegalArgumentException("Невозможно определить начальную точку x0.");
        }
    }

    public double findRoot(String userFunction, double a, double b, double epsilon) {
        Function<Double, Double> f = FunctionParser.parseFunction(userFunction);
        Function<Double, Double> df = x -> numericalDerivative(f, x);
        double x0 = determineInitialPoint(f, a, b);
        return newtonMethod(f, df, x0, epsilon);
    }
}
