package com.example.methods.service.equations;

import com.example.methods.service.exceptions.InitialPointsNotDeterminedException;
import com.example.methods.service.exceptions.NoRootFoundException;
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
    public String solveProblem(String userFunction, double a, double b, double epsilon, Double m) {
        SolutionMessageBuilder builder = new SolutionMessageBuilder();

        Function<Double, Double> f = FunctionParser.parseFunction(userFunction);
        Function<Double, Double> df = getDerivativeFunction(userFunction, builder);
        Function<Double, Double> d2f = getSecondDerivativeFunction(userFunction, builder);

        double x0 = determineInitialPoint(f, d2f, a, b, builder);

        return performNewtonIterations(f, df, x0, epsilon, m, builder);
    }

    private Function<Double, Double> getDerivativeFunction(String userFunction, SolutionMessageBuilder builder) {
        String derivativeFormula = symbolicDifferentiationService.getDerivativeFormula(userFunction, "x");
        builder.appendMessage("Для выбора начальной точки найдем производную вашей функции:")
                .appendMessage("→ Производная: \\( f'(x) = " + derivativeFormula + " \\)");
        return FunctionParser.parseFunction(derivativeFormula);
    }

    private Function<Double, Double> getSecondDerivativeFunction(String userFunction, SolutionMessageBuilder builder) {
        String derivativeFormula = symbolicDifferentiationService.getDerivativeFormula(userFunction, "x");
        String secondDerivativeFormula = symbolicDifferentiationService.getDerivativeFormula(derivativeFormula, "x");
        builder.appendMessage("Для выбора начальной точки также найдем вторую производную вашей функции:")
                .appendMessage("→ Вторая производная: \\( f''(x) = " + secondDerivativeFormula + " \\)");
        return FunctionParser.parseFunction(secondDerivativeFormula);
    }

    private double determineInitialPoint(
            Function<Double, Double> f,
            Function<Double, Double> d2f,
            double a, double b,
            SolutionMessageBuilder builder
    ) {
        double fa = f.apply(a);
        double fb = f.apply(b);
        if (fa * fb >= 0) {
            throw new NoRootFoundException(
                    "На интервале [" + a + ", " + b + "] функция не меняет знак. " +
                            "Похоже, что на этом отрезке нет корня, или их чётное число."
            );
        }

        double d2fa = d2f.apply(a);
        double d2fb = d2f.apply(b);

        if (d2fa * fa >= 0) {
            builder.appendMessage("\\( f''(a) \\cdot f(a) \\geq 0 \\), " +
                    "поэтому начальная точка \\( x_0 = a = " + a + " \\)");
            return a;
        } else if (d2fb * fb >= 0) {
            builder.appendMessage("\\( f''(b) \\cdot f(b) \\geq 0 \\), " +
                    "поэтому начальная точка \\( x_0 = b = " + b + " \\)");
            return b;
        } else {
            throw new InitialPointsNotDeterminedException("Невозможно определить начальную точку x0.");
        }
    }

    private String performNewtonIterations(
            Function<Double, Double> f,
            Function<Double, Double> df,
            double x0,
            double epsilon,
            Double m,
            SolutionMessageBuilder builder
    ) {
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
                            ": Текущее значение \\( x_" + (i - 1) + " = " + String.format("%.8f", x) + " \\), " +
                            "\\( f(x_" + (i - 1) + ") = " + String.format("%.8f", fx) + " \\), " +
                            "\\( f'(x_" + (i - 1) + ") = " + String.format("%.8f", dfx) + " \\)" +
                            ".\nВычисляем новое приближение: \\( x_" + i + " = x_" + (i - 1) +
                            " - \\frac{f(x_" + (i - 1) + ")}{f'(x_" + (i - 1) + ")} = " + String.format("%.8f", x_new) + " \\)"
            );

            if (checkStoppingCondition(f, x_new, x, epsilon, m, builder, i)) {
                converged = true;
                break;
            }

            x = x_new;
        }

        if (!converged && !failByDerivative) {
            builder.appendMessage("К сожалению, метод Ньютона не смог найти приближенный корень на данном отрезке " +
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
            int i
    ) {
        if (m != null && m > 0) {
            double fAtXnew = f.apply(xNew);
            builder.appendMessage(
                    "Проверяем критерий остановки: \\( \\frac{|f(x_" + i + ")|}{m} = \\frac{" +
                            String.format("%.8f", Math.abs(fAtXnew)) + "}{" + m +
                            "} = " + String.format("%.8f", Math.abs(fAtXnew) / m) + " \\)"
            );
            if (Math.abs(fAtXnew) / m < epsilon) {
                builder.appendMessage("Условие \\( \\frac{|f(x_" + i + ")|}{m} <  \\varepsilon \\) выполнено.")
                        .appendMessage("Метод Ньютона завершён успешно! Приближённый корень: \\( x \\approx " +
                                String.format("%.8f", xNew) + " \\)");
                return true;
            }
        } else {
            if (Math.abs(xNew - x) < epsilon) {
                builder.appendMessage(
                        "Условие \\(|x" + i + " - x" + (i - 1) + "| < \\varepsilon \\) выполнено. \\(|" +
                                String.format("%.8f", xNew - x) + "| < \\varepsilon \\)"
                ).appendMessage(
                        "Метод Ньютона завершён успешно! Приближённый корень: \\( x \\approx " +
                                String.format("%.8f", xNew) + " \\)"
                );
                return true;
            }
        }
        return false;
    }
}
