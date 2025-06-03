package com.example.methods.service.equations;

import com.example.methods.service.util.FunctionParser;
import com.example.methods.service.util.SymbolicDifferentiationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class NewtonMethodService extends SolveEquationsProblem {

    @Autowired
    private SymbolicDifferentiationService symbolicDifferentiationService;

    private static final int MAX_ITER = 100;
    private static final double TOLERANCE = 1e-10;

    @Override
    public String solveProblem(String userFunction, double a, double b, double epsilon) {
        SolutionMessageBuilder builder = new SolutionMessageBuilder();

        Function<Double, Double> f = FunctionParser.parseFunction(userFunction);
        Function<Double, Double> df = getDerivativeFunction(userFunction, builder);
        Function<Double, Double> d2f = getSecondDerivativeFunction(userFunction, builder);

        double x0 = determineInitialPoint(f, d2f, a, b, builder);

        return performNewtonIterations(f, df, x0, epsilon, builder);
    }

    private Function<Double, Double> getDerivativeFunction(String userFunction, SolutionMessageBuilder builder) {
        String derivativeFormula = symbolicDifferentiationService.getDerivativeFormula(userFunction, "x");
        builder.appendMessage("Для решения задачи методом Ньютона найдем производную вашей функции:")
                .appendMessage("→ Производная: \\( " + derivativeFormula + " \\)");
        return FunctionParser.parseFunction(derivativeFormula);
    }

    private Function<Double, Double> getSecondDerivativeFunction(String userFunction, SolutionMessageBuilder builder) {
        String derivativeFormula = symbolicDifferentiationService.getDerivativeFormula(userFunction, "x");
        String secondDerivativeFormula = symbolicDifferentiationService.getDerivativeFormula(derivativeFormula, "x");
        builder.appendMessage("Для выбора начальной точки также возьмём вторую производную вашей функции:")
                .appendMessage("→ Вторая производная: \\( " + secondDerivativeFormula + " \\)");
        return FunctionParser.parseFunction(secondDerivativeFormula);
    }

    private double determineInitialPoint(Function<Double, Double> f, Function<Double, Double> d2f, double a, double b, SolutionMessageBuilder builder) {
        double fa = f.apply(a);
        double fb = f.apply(b);
        if (fa * fb >= 0) {
            throw new IllegalArgumentException("Корня в заданном отрезке нет.");
        }

        double d2fa = d2f.apply(a);
        double d2fb = d2f.apply(b);

        if (d2fa * fa >= 0) {
            builder.appendMessage("\\( f''(a) \\cdot f(a) \\geq 0 \\), поэтому начальная точка \\( x_0 = a = " + a + " \\)");
            return a;
        } else if (d2fb * fb >= 0) {
            builder.appendMessage("\\( f''(b) \\cdot f(b) \\geq 0 \\), поэтому начальная точка \\( x_0 = b = " + b + " \\)");
            return b;
        } else {
            throw new IllegalArgumentException("Невозможно определить начальную точку x0.");
        }
    }

    private String performNewtonIterations(Function<Double, Double> f, Function<Double, Double> df, double x0, double epsilon, SolutionMessageBuilder builder) {
        double x = x0;
        boolean converged = false;
        boolean failByDerivative = false;

        for (int i = 1; i <= MAX_ITER; i++) {
            double fx = f.apply(x);
            double dfx = df.apply(x);

            if (Math.abs(dfx) < TOLERANCE) {
                builder.appendMessage("Итерация " + i + ": Производная функции почти равна нулю при x = " + x + ".")
                        .appendMessage("В этой точке нельзя продолжать вычисления (деление на ноль невозможно). Останавливаемся.");
                failByDerivative = true;
                break;
            }

            double x_new = x - fx / dfx;
            builder.appendMessage(
                    "Итерация " + i +
                            ": Текущее значение \\( x_" + (i - 1) + " = " + String.format("%.8f", x) + " \\), \\( f(x_" + (i - 1) + ") = " + String.format("%.8f", fx) + " \\), \\( f'(x_" + (i - 1) + ") = " + String.format("%.8f", dfx) + " \\)" +
                            ".\nВычисляем новое приближение: \\( x_" + i + " = x_" + (i - 1) + " - \\frac{f(x_" + (i - 1) + ")}{f'(x_" + (i - 1) + ")} = " + String.format("%.8f", x_new) + " \\)"
            );

            if (Math.abs(x_new - x) < epsilon) {
                builder.appendMessage("Достигнута нужная точность! Метод Ньютона успешно завершён!")
                        .appendMessage("Ответ: \\( x \\approx " + x_new + " \\)");
                converged = true;
                break;
            }
            x = x_new;
        }

        if (!converged && !failByDerivative) {
            builder.appendMessage("К сожалению, метод Ньютона не смог найти корень на данном интервале за " + MAX_ITER + " шагов.")
                    .appendMessage("Возможно, стоит попробовать более точный отрезок.");
        }

        return builder.build();
    }
}
