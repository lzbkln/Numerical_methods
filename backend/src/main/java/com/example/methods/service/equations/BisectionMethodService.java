package com.example.methods.service.equations;

import com.example.methods.service.util.FunctionParser;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class BisectionMethodService extends SolveProblem {

    private static final int MAX_ITER = 100;

    @Override
    public String solveProblem(String userFunction, double a, double b, double epsilon) {
        Function<Double, Double> f = FunctionParser.parseFunction(userFunction);
        SolutionMessageBuilder builder = new SolutionMessageBuilder();

        double fa = f.apply(a);
        double fb = f.apply(b);

        if (fa * fb > 0) {
            return builder.setError("На отрезке [" + a + ", " + b + "] функция не меняет знак. Корня нет или их чётное количество.")
                    .build();
        }

        builder.appendMessage("Стартуем метод дихотомии с интервалом [" + a + ", " + b + "] и точностью " + epsilon + ".");
        int iter = 0;
        double mid = a;
        boolean found = false;

        while ((b - a) / 2 > epsilon && iter < MAX_ITER) {
            mid = (a + b) / 2;
            double fmid = f.apply(mid);

            builder.appendMessage("Итерация " + (iter + 1) + ":")
                    .appendMessage("  a = " + a + ", b = " + b + ", mid = " + mid)
                    .appendMessage("  f(a) = " + fa + ", f(b) = " + fb + ", f(mid) = " + fmid);

            if (Math.abs((b - a) / 2) < epsilon) {
                builder.appendMessage("(|(b - a) / 2| < epsilon). Корень найден!");
                found = true;
                break;
            }

            if (fa * fmid < 0) {
                builder.appendMessage("  f(a) * f(mid) < 0 ⇒ корень между a и mid. Сдвигаем b к mid.");
                b = mid;
                fb = fmid;
            } else {
                builder.appendMessage("  f(a) * f(mid) > 0 ⇒ корень между mid и b. Сдвигаем a к mid.");
                a = mid;
                fa = fmid;
            }
            iter++;
        }

        builder.appendMessage("Метод завершён за " + (iter + (found ? 1 : 0)) + " шагов.")
                .appendMessage("Приближённое значение корня: x ≈ " + mid);

        if (!found && iter >= MAX_ITER && (b - a) / 2 > epsilon) {
            builder.appendMessage("Внимание: за " + MAX_ITER + " шагов не удалось достичь нужной точности. Попробуйте задать отрезок поточнее.");
        }

        return builder.build();
    }
}
