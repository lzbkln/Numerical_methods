package com.example.methods.service.equations;

import com.example.methods.service.util.FunctionParser;
import com.example.methods.service.util.SymbolicDifferentiationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class NoFixedChordsMethodService extends SolveEquationsProblem {

    @Autowired
    private SymbolicDifferentiationService symbolicDifferentiationService;

    private static final int MAX_ITER = 100;

    @Override
    public String solveProblem(String userFunction, double a, double b, double epsilon) {
        Function<Double, Double> f = FunctionParser.parseFunction(userFunction);
        SolutionMessageBuilder builder = new SolutionMessageBuilder();

        String derivativeFormula = symbolicDifferentiationService.getDerivativeFormula(userFunction, "x");
        builder.appendMessage("Для выбора начальной точки найдем производную вашей функции:")
                .appendMessage("→ Производная: \\( f'(x) = " + derivativeFormula + " \\)");

        String secondDerivativeFormula = symbolicDifferentiationService.getDerivativeFormula(derivativeFormula, "x");
        builder.appendMessage("Для выбора начальной точки также найдем вторую производную вашей функции:")
                .appendMessage("→ Вторая производная: \\( f''(x) = " + secondDerivativeFormula + " \\)");

        Function<Double, Double> d2f = FunctionParser.parseFunction(secondDerivativeFormula);

        double fa = f.apply(a);
        double fb = f.apply(b);
        if (fa * fb > 0) {
            throw new IllegalArgumentException("На интервале [" + a + ", " + b + "] функция не меняет знак. Похоже, что на этом отрезке нет корня, или их чётное число.");
        }

        double[] initialPoints = determineInitialPoints(f, d2f, a, b, builder);
        double x0 = initialPoints[0];
        double x1 = initialPoints[1];

        return performNoFixedChordsIterations(f, x0, x1, epsilon, builder);
    }

    private double[] determineInitialPoints(Function<Double, Double> f, Function<Double, Double> d2f, double a, double b, SolutionMessageBuilder builder) {
        double fa = f.apply(a);
        double fb = f.apply(b);
        double d2fa = d2f.apply(a);
        double d2fb = d2f.apply(b);

        double x0, x1;
        if (d2fa * fa >= 0) {
            x0 = a;
            x1 = b;
            builder.appendMessage("\\( f''(a) \\cdot f(a) \\geq 0 \\), поэтому начальная точка \\( x_0 = a = " + a + " \\)");
        } else if (d2fb * fb >= 0) {
            x0 = b;
            x1 = a;
            builder.appendMessage("\\( f''(b) \\cdot f(b) \\geq 0 \\), поэтому начальная точка \\( x_0 = b = " + b + " \\)");
        } else {
            throw new IllegalArgumentException("Невозможно определить начальные точки \\( x_0 \\) и \\( x_1 \\).");
        }

        builder.appendMessage("Выбираем начальные точки: \\( x_0 = " + x0 + " \\), \\( x_1 = " + x1 + " \\)");
        return new double[]{x0, x1};
    }

    private String performNoFixedChordsIterations(Function<Double, Double> f, double x0, double x1, double epsilon, SolutionMessageBuilder builder) {
        boolean converged = false;

        builder.appendMessage("Запускаем метод подвижных хорд.");

        int iter = 0;
        while (iter < MAX_ITER) {
            double fx0 = f.apply(x0);
            double fx1 = f.apply(x1);

            if (Math.abs(fx1 - fx0) < 1e-12) {
                builder.appendMessage("Итерация " + (iter + 1) + ": знаменатель в формуле стал слишком мал. Деление на почти ноль невозможно, останавливаемся.");
                break;
            }

            double x_new = x1 - fx1 * (x1 - x0) / (fx1 - fx0);

            builder.appendMessage("Итерация " + (iter + 1) + ":")
                    .appendMessage("  \\( x_" + iter + " = " + String.format("%.8f", x0) + " \\), \\( x_" + (iter + 1) + " = " + String.format("%.8f", x1) + " \\)")
                    .appendMessage("  \\( f(x_" + iter + ") = " + String.format("%.8f", fx0) + " \\), \\( f(x_" + (iter + 1) + ") = " + String.format("%.8f", fx1) + " \\)")
                    .appendMessage("  Новая точка: \\( x_" + (iter + 2) + " = x_" + (iter + 1) + " - \\frac{f(x_" + (iter + 1) + ")(x_" + (iter + 1) + " - x_" + iter + ")}{f(x_" + (iter + 1) + ") - f(x_" + iter + ")} = " + String.format("%.8f", x_new) + " \\)");

            if (Math.abs(x_new - x1) < epsilon) {
                builder.appendMessage("Достигнута нужная точность!")
                        .appendMessage("Метод подвижных хорд завершён успешно! Приближённый корень: \\( x \\approx " + x_new + " \\)");
                converged = true;
                break;
            }

            x0 = x1;
            x1 = x_new;
            iter++;
        }

        if (!converged) {
            builder.appendMessage("Внимание: за " + MAX_ITER + " шагов не удалось достичь нужной точности. Возможно, стоит изменить интервал или увеличить MAX_ITER.");
        }

        return builder.build();
    }
}
