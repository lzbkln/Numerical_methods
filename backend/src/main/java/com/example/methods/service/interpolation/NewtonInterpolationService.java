package com.example.methods.service.interpolation;

import com.example.methods.service.exceptions.InvalidInterpolationDataException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NewtonInterpolationService extends SolveInterpolationProblem {

    @Override
    public String solveInterpolationProblem(List<Double> xValues, List<Double> fxValues, List<Double> interpolationPoints) {
        StringBuilder builder = new StringBuilder();

        if (xValues.size() != fxValues.size()) {
            throw new InvalidInterpolationDataException("Количество точек x и f(x) должно совпадать.");
        }

        int n = xValues.size();
        double[][] dividedDifferences = new double[n][n];

        for (int i = 0; i < n; i++) {
            dividedDifferences[i][0] = fxValues.get(i);
        }

        for (int j = 1; j < n; j++) {
            for (int i = 0; i < n - j; i++) {
                dividedDifferences[i][j] = (dividedDifferences[i + 1][j - 1] - dividedDifferences[i][j - 1]) / (xValues.get(i + j) - xValues.get(i));
            }
        }

        builder.append(buildLowerTriangularTable(xValues, dividedDifferences));

        builder.append(buildDividedDifferencesCalculations(xValues, dividedDifferences));

        builder.append("Интерполяционный многочлен Ньютона для заданных точек:\n");
        builder.append("\\[L(x) = ").append(String.format("%.4f", fxValues.get(0))).append(" + ");

        for (int i = 1; i < n; i++) {
            for (int k = 0; k < i; k++) {
                builder.append(String.format("(x - %.2f)", xValues.get(k)));
            }
            builder.append(String.format(" \\cdot %.4f", dividedDifferences[0][i]));
            if (i < n - 1) {
                builder.append(" + ");
            }
        }
        builder.append("\\]");

        for (Double point : interpolationPoints) {
            builder.append(String.format("\nВычисление значений в точке интерполяции: \\(%.4f\\)\n", point));
            double result = calculateNewtonValue(xValues, dividedDifferences, point);
            builder.append(String.format("\\(f(%.4f) = %.6f\\)\n", point, result));
        }

        return builder.toString();
    }

    private String buildLowerTriangularTable(List<Double> xValues, double[][] dividedDifferences) {
        int n = xValues.size();
        StringBuilder tableBuilder = new StringBuilder();

        tableBuilder.append("Таблица разделённых разностей:\n");
        tableBuilder.append("\\[\\begin{array}{|c|c|");
        for (int j = 1; j < n; j++) {
            tableBuilder.append("c|");
        }
        tableBuilder.append("}\n");

        tableBuilder.append("\\hline\n");
        tableBuilder.append("x_i & f(x_i) ");
        for (int j = 1; j < n; j++) {
            tableBuilder.append("& f");
            tableBuilder.append("[x_{i}");
            for (int idx = 1; idx <= j; idx++) {
                tableBuilder.append(", x_{i+" + idx + "}");
            }
            tableBuilder.append("] ");
        }
        tableBuilder.append(" \\\\\n");
        tableBuilder.append("\\hline\n");

        for (int i = 0; i < n; i++) {
            // x_i и f(x_i)
            tableBuilder.append(String.format("%.4f & %.6f ", xValues.get(i), dividedDifferences[i][0]));
            for (int j = 1; j < n; j++) {
                if (i - j >= 0) {
                    tableBuilder.append(String.format("& %.6f ", dividedDifferences[i - j][j]));
                } else {
                    tableBuilder.append("& ");
                }
            }
            tableBuilder.append("\\\\\n");
            tableBuilder.append("\\hline\n");
        }

        tableBuilder.append("\\end{array}\\]\n");

        return tableBuilder.toString();
    }

    private String buildDividedDifferencesCalculations(List<Double> xValues, double[][] dd) {
        int n = xValues.size();
        StringBuilder calcBuilder = new StringBuilder();

        calcBuilder.append("Подробное вычисление разделённых разностей:\n\n");
        for (int j = 1; j < n; j++) {
            for (int i = 0; i < n - j; i++) {
                calcBuilder.append(String.format(
                        "\\(f[x_{%d},\\ldots,x_{%d}] = \\frac{f[x_{%d},\\ldots,x_{%d}] - f[x_{%d},\\ldots,x_{%d}]}{x_{%d} - x_{%d}} = \\frac{%.6f - %.6f}{%.4f - %.4f} = %.6f\\)\n\n",
                        i, i + j,
                        i + 1, i + j,
                        i, i + j - 1,
                        i + j, i,
                        dd[i + 1][j - 1], dd[i][j - 1],
                        xValues.get(i + j), xValues.get(i),
                        dd[i][j]
                ));
            }
        }

        return calcBuilder.toString();
    }

    private double calculateNewtonValue(List<Double> xValues, double[][] dividedDifferences, double point) {
        double result = dividedDifferences[0][0];
        double term = 1;

        for (int i = 1; i < xValues.size(); i++) {
            term *= (point - xValues.get(i - 1));
            result += term * dividedDifferences[0][i];
        }

        return result;
    }
}

