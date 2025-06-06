package com.example.methods.service.interpolation;

import com.example.methods.service.exceptions.InvalidInterpolationDataException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LagrangeInterpolationService extends SolveInterpolationProblem {

    @Override
    public String solveInterpolationProblem(List<Double> xValues, List<Double> fxValues, List<Double> interpolationPoints) {
        StringBuilder builder = new StringBuilder();

        if (xValues.size() != fxValues.size()) {
            throw new InvalidInterpolationDataException("Количество точек x и f(x) должно совпадать.");
        }

        int n = xValues.size();
        builder.append("Интерполяционный многочлен Лагранжа для заданных точек:\n\n");

        for (int i = 0; i < n; i++) {
            builder.append(String.format("Полином \\(L_{%d}(x)\\):\n", i));
            builder.append("\\[");
            boolean firstTerm = true;
            for (int j = 0; j < n; j++) {
                if (i != j) {
                    if (!firstTerm) {
                        builder.append(" \\cdot ");
                    }
                    builder.append(String.format("\\frac{(x - %.2f)}{(%.2f - %.2f)}", xValues.get(j), xValues.get(i), xValues.get(j)));
                    firstTerm = false;
                }
            }
            builder.append("\\]");
        }

        builder.append("\nИнтерполяционный многочлен Лагранжа L(x):");
        builder.append("\\[L(x) = ");

        for (int i = 0; i < n; i++) {
            if (i > 0) {
                builder.append(" + ");
            }

            builder.append(String.format("%.2f \\cdot ", fxValues.get(i)));
            boolean firstTerm = true;
            for (int j = 0; j < n; j++) {
                if (i != j) {
                    if (!firstTerm) {
                        builder.append(" ");
                    }
                    builder.append(String.format("\\frac{(x - %.2f)}{(%.2f - %.2f)}", xValues.get(j), xValues.get(i), xValues.get(j)));
                    firstTerm = false;
                }
            }
        }
        builder.append("\\]");

        for (Double point : interpolationPoints) {
            builder.append("\nВычисление значений в точке интерполяции: \\(").append(point).append("\\)\n");
            double result = calculateLagrangeValue(xValues, fxValues, point);
            builder.append("\\(").append(String.format("f(%.2f) = %.4f\n", point, result)).append("\\)");
        }

        return builder.toString();
    }

    private double calculateLagrangeValue(List<Double> xValues, List<Double> fxValues, double point) {
        double result = 0;
        int n = xValues.size();
        for (int i = 0; i < n; i++) {
            double term = fxValues.get(i);
            for (int j = 0; j < n; j++) {
                if (i != j) {
                    term *= (point - xValues.get(j)) / (xValues.get(i) - xValues.get(j));
                }
            }
            result += term;
        }
        return result;
    }
}
