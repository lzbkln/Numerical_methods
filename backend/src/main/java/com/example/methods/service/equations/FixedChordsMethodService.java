package com.example.methods.service.equations;

import com.example.methods.service.util.FunctionParser;
import com.example.methods.service.util.SymbolicDifferentiationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class FixedChordsMethodService extends SolveProblem {

    @Autowired
    private SymbolicDifferentiationService symbolicDifferentiationService;

    private static final int MAX_ITER = 100;

    @Override
    public String solveProblem(String userFunction, double a, double b, double epsilon) {
        Function<Double, Double> f = FunctionParser.parseFunction(userFunction);
        SolutionMessageBuilder builder = new SolutionMessageBuilder();

        String derivativeFormula = symbolicDifferentiationService.getDerivativeFormula(userFunction, "x");
        builder.appendMessage("Для выбора начальной точки найдем производную вашей функции:")
                .appendMessage("→ Производная: " + derivativeFormula);

        String secondDerivativeFormula = symbolicDifferentiationService.getDerivativeFormula(derivativeFormula, "x");
        builder.appendMessage("Для выбора начальной точки также найдем вторую производную вашей функции:")
                .appendMessage("→ Вторая производная: " + secondDerivativeFormula);

        Function<Double, Double> d2f = FunctionParser.parseFunction(secondDerivativeFormula);

        double fa = f.apply(a);
        double fb = f.apply(b);
        if (fa * fb > 0) {
            throw new IllegalArgumentException("На интервале [" + a + ", " + b + "] функция не меняет знак. Похоже, что на этом отрезке нет корня, или их чётное число.");
        }

        double[] dots = determineInitialPoints(f, d2f, a, b, builder, fa, fb);
        double x0 = dots[0];
        double x1 = dots[1];

        return performFixedChordsIterations(f, x0, x1, epsilon, builder);
    }

    private double[] determineInitialPoints(Function<Double, Double> f, Function<Double, Double> d2f, double a, double b, SolutionMessageBuilder builder, double fa, double fb) {
        double d2fa = d2f.apply(a);
        double d2fb = d2f.apply(b);

        double x0, x1;
        if (d2fa * fa >= 0) {
            x0 = a;
            x1 = b;
            builder.appendMessage("f''(a) * f(a) >= 0, поэтому начальная точка x₀ = a = " + a);
        } else if (d2fb * fb >= 0) {
            x0 = b;
            x1 = a;
            builder.appendMessage("f''(b) * f(b) >= 0, поэтому начальная точка x₀ = b = " + b);
        } else {
            throw new IllegalArgumentException("Невозможно определить начальные точки x0 и x1.");
        }

        builder.appendMessage("Выбираем начальные точки: x₀ = " + x0 + ", x₁ = " + x1);
        return new double[]{x0, x1};
    }

    private String performFixedChordsIterations(Function<Double, Double> f, double x0, double x1, double epsilon, SolutionMessageBuilder builder) {
        double fx0 = f.apply(x0);
        double x = x1;
        boolean converged = false;

        builder.appendMessage("Начинаем метод неподвижных хорд на заданном отрезке с точностью " + epsilon + ".");

        int iter = 0;
        while (iter < MAX_ITER) {
            double fx = f.apply(x);

            double x_new = x - fx * (x - x0) / (fx - fx0);

            builder.appendMessage("Итерация " + (iter + 1) + ": x = " + String.format("%.8f", x) + ", f(x) = " + String.format("%.8f", fx))
                    .appendMessage("  Вычисляем новую точку: x_new = x - f(x) * (x - x0) / (f(x) - f(x0)) = " + String.format("%.8f", x_new));

            if (Math.abs(x_new - x) < epsilon) {
                builder.appendMessage("Достигнута нужная точность!")
                        .appendMessage("Метод неподвижных хорд завершён успешно! Приближённый корень: x ≈ " + x_new);
                converged = true;
                break;
            }

            x = x_new;
            iter++;
        }

        if (!converged) {
            builder.appendMessage("Внимание: за " + MAX_ITER + " шагов не удалось достичь нужной точности. Возможно, стоит взять отрезок поточнее.");
        }

        return builder.build();
    }
}
