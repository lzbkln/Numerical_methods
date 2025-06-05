package com.example.methods.service.equations;

import com.example.methods.service.exceptions.InitialPointsNotDeterminedException;
import com.example.methods.service.exceptions.NoRootFoundException;
import com.example.methods.service.util.FunctionParser;
import com.example.methods.service.util.SymbolicDifferentiationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class FixedChordsMethodService extends SolveEquationsProblem {

    @Autowired
    private SymbolicDifferentiationService symbolicDifferentiationService;

    private static final int MAX_ITER = 100;

    @Override
    public String solveProblem(String userFunction, double a, double b, double epsilon, Double m) {
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
            throw new NoRootFoundException(
                    "На интервале [" + a + ", " + b + "] функция не меняет знак. " +
                            "Похоже, что на этом отрезке нет корня, или их чётное число."
            );
        }

        double[] dots = determineInitialPoints(f, d2f, a, b, builder, fa, fb);
        double x0 = dots[0];
        double x1 = dots[1];

        return performFixedChordsIterations(f, x0, x1, epsilon, m, builder);
    }

    private double[] determineInitialPoints(
            Function<Double, Double> f,
            Function<Double, Double> d2f,
            double a, double b,
            SolutionMessageBuilder builder,
            double fa, double fb
    ) {
        double d2fa = d2f.apply(a);
        double d2fb = d2f.apply(b);

        double x0, x1;
        if (d2fa * fa >= 0) {
            x0 = a;
            x1 = b;
            builder.appendMessage("\\( f''(a) \\cdot f(a) \\geq 0 \\), " +
                    "поэтому начальная точка \\( x_0 = a = " + a + " \\)");
        } else if (d2fb * fb >= 0) {
            x0 = b;
            x1 = a;
            builder.appendMessage("\\( f''(b) \\cdot f(b) \\geq 0 \\), " +
                    "поэтому начальная точка \\( x_0 = b = " + b + " \\)");
        } else {
            throw new InitialPointsNotDeterminedException("Невозможно определить начальные точки x0 и x1.");
        }

        builder.appendMessage("Выбираем начальные точки: \\( x_0 = " + x0 + " \\), \\( x_1 = " + x1 + " \\)");
        return new double[]{x0, x1};
    }

    private String performFixedChordsIterations(
            Function<Double, Double> f,
            double x0,
            double x1,
            double epsilon,
            Double m,
            SolutionMessageBuilder builder
    ) {
        double fx0 = f.apply(x0);
        double x = x1;
        boolean converged = false;

        builder.appendMessage("Начинаем метод неподвижных хорд на заданном отрезке с точностью " + epsilon + ".");

        int iter = 0;
        while (iter < MAX_ITER) {
            double fx = f.apply(x);
            double x_new = x - fx * (x - x0) / (fx - fx0);

            builder.appendMessage(
                    "Итерация " + (iter + 1) +
                            ": \\( x_" + (iter + 1) + " = " + String.format("%.8f", x) +
                            " \\), \\( f(x_" + (iter + 1) + ") = " + String.format("%.8f", fx) + " \\)"
            ).appendMessage(
                    "Вычисляем новое приближение: \\( x_" + (iter + 2) + " = x_" + (iter + 1) +
                            " - \\frac{f(x_" + (iter + 1) + ") \\cdot (x_" + (iter + 1) + " - x_0)}" +
                            "{f(x_" + (iter + 1) + ") - f(x_0)} = " + String.format("%.8f", x_new) + " \\)"
            );

            if (checkStoppingCondition(f, x_new, x, epsilon, m, builder, iter)) {
                converged = true;
                break;
            }

            x = x_new;
            iter++;
        }

        if (!converged) {
            builder.appendMessage("К сожалению, метод неподвижных хорд не смог найти приближенный корень на данном отрезке " +
                            "за " + MAX_ITER + " шагов.")
                    .appendMessage("Возможно, стоит попробовать уточнить отрезок.");
        }

        return builder.build();
    }

    private boolean checkStoppingCondition(
            Function<Double, Double> f,
            double xNew,
            double x,
            double epsilon,
            Double m,
            SolutionMessageBuilder builder,
            int iter
    ) {
        if (m != null && m > 0) {
            double fAtXnew = f.apply(xNew);
            builder.appendMessage(
                    "Проверяем критерий остановки: \\( \\frac{|f(x_" + (iter + 2) + ")|}{m} = \\frac{" +
                            String.format("%.8f", Math.abs(fAtXnew)) + "}{" + m +
                            "} = " + String.format("%.8f", Math.abs(fAtXnew) / m) + " \\)"
            );
            if (Math.abs(fAtXnew) / m < epsilon) {
                builder.appendMessage("Условие \\( \\frac{|f(x_" + (iter + 2) + ")|}{m} < \\varepsilon \\) выполнено.")
                        .appendMessage("Метод неподвижных хорд завершён успешно! Приближённый корень: \\( x \\approx " +
                                String.format("%.8f", xNew) + " \\)");
                return true;
            }
        } else {
            if (Math.abs(xNew - x) < epsilon) {
                builder.appendMessage(
                        "Условие \\(|x" + (iter + 2) + " - x" + (iter + 1) + "| < \\varepsilon \\) выполнено. \\(|" +
                                String.format("%.8f", xNew - x) + "| < \\varepsilon \\)"
                ).appendMessage(
                        "Метод неподвижных хорд завершён успешно! Приближённый корень: \\( x \\approx " +
                                String.format("%.8f", xNew) + " \\)"
                );
                return true;
            }
        }
        return false;
    }
}
