package com.example.methods.service.equations;

import com.example.methods.service.util.FunctionParser;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class BisectionMethodService {

    private static final int MAX_ITER = 100;
    private static final double H = 1e-5;
    private static final double NEWTON_TOLERANCE = 1e-6;

//    // Метод для численного дифференцирования
//    public static double numericalDerivative(Function<Double, Double> f, double x) {
//        return (f.apply(x - 2 * H) - 8 * f.apply(x - H) + 8 * f.apply(x + H) - f.apply(x + 2 * H)) / (12 * H);
//    }
//
//    // Метод для нахождения отрезка, содержащего корень
//    public static double[] findInterval(Function<Double, Double> equation, double x0) {
//        double DELTA = 1.0;
//        double a = x0;
//        double b = x0 + DELTA;
//        double f_a = equation.apply(a);
//        double f_b = equation.apply(b);
//
//        for (int i = 0; i < MAX_ITER; i++) {
//            if (f_a * f_b < 0) {
//                // Проверка знака производной внутри отрезка
//                double df_a = numericalDerivative(equation, a);
//                double df_b = numericalDerivative(equation, b);
//                if (df_a * df_b >= 0) {
//                    return new double[]{a, b};
//                }
//            }
//
//            if (Math.abs(f_a) < Math.abs(f_b)) {
//                a -= DELTA;
//                f_a = equation.apply(a);
//            } else {
//                b += DELTA;
//                f_b = equation.apply(b);
//            }
//
//            DELTA *= 2;
//        }
//
//        throw new IllegalArgumentException("Интервал не найден за максимальное количество итераций.");
//    }

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

    public double findRoot(String userFunction, double a, double b, double epsilon) {
        Function<Double, Double> f = FunctionParser.parseFunction(userFunction);
        double fa = f.apply(a);
        double fb = f.apply(b);
        if (fa * fb >= 0) {
            throw new IllegalArgumentException("Корня в заданном отрезке нет.");
        }
        //Function<Double, Double> df = x -> numericalDerivative(f, x);
        //double[] interval = findInterval(f, -10);

        return bisectionMethod(f, a, b, epsilon);
    }
}
