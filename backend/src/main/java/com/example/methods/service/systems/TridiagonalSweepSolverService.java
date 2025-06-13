package com.example.methods.service.systems;

import com.example.methods.dto.LinearSystemRequestDto;
import com.example.methods.service.exceptions.NotDiagonalDominanceException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TridiagonalSweepSolverService {

    public String solve(LinearSystemRequestDto req) {
        double[][] A = req.getA();
        double[] d = req.getB();
        int n = A.length;
        StringBuilder sb = new StringBuilder();

        double[] a = new double[n + 1];
        double[] b = new double[n + 1];
        double[] c = new double[n + 1];
        double[] rhs = new double[n + 1];

        for (int i = 1; i <= n; i++) {
            b[i] = A[i - 1][i - 1];
            rhs[i] = d[i - 1];
            if (i > 1) {
                a[i] = A[i - 1][i - 2];
            } else {
                a[i] = 0.0;
            }
            if (i < n) {
                c[i] = A[i - 1][i];
            } else {
                c[i] = 0.0;
            }
        }

        sb.append("Проверка диагонального преобладания: \\\\");
        for (int i = 1; i <= n; i++) {
            double sumOffDiag = Math.abs(a[i]) + Math.abs(c[i]);
            sb.append(String.format("\n\\(\\left| b_{%d} \\right| = %.6f > \\left| a_{%d} \\right| + \\left| c_{%d} \\right| = %.6f\\)",
                    i - 1, Math.abs(b[i]), i - 1, i - 1, sumOffDiag));
            if (Math.abs(b[i]) <= sumOffDiag) {
                throw new NotDiagonalDominanceException("Матрица не имеет строгого диагонального преобладания в строке " + (i - 1));
            }
            sb.append("\\\\");
        }

        double[] lambda = new double[n + 2]; // lambda[1..n+1]
        double[] mu = new double[n + 2];     // mu[1..n+1]
        double[] solution = new double[n + 1]; // solution[1..n]

        List<String> steps = new ArrayList<>();
        steps.add("\nНачинаем прямой ход прогонки");

        lambda[1] = 0.0;
        mu[1] = 0.0;
        steps.add(String.format("\n\\(\\lambda_{1} = %.6f, \\mu_{1} = %.6f\\)", lambda[1], mu[1]));

        for (int i = 2; i <= n + 1; i++) {
            int j = i - 1;
            double denom = a[j] * lambda[i - 1] + b[j];
            if (Math.abs(denom) < 1e-12) {
                throw new IllegalArgumentException("Деление на ноль или близкое к нулю при прямом ходе, шаг " + i);
            }
            lambda[i] = -c[j] / denom;
            mu[i] = (rhs[j] - a[j] * mu[i - 1]) / denom;

            if (i <= n) {
                steps.add(String.format("\n\\(\\lambda_{%d} = \\frac{-c_{%d}}{a_{%d} \\lambda_{%d} + b_{%d}} = \\frac{-%.6f}{%.6f \\times %.6f + %.6f} = %.6f\\)",
                        i, j, j, i - 1, j, c[j], a[j], lambda[i - 1], b[j], lambda[i]));
                steps.add(String.format("\n\\(\\mu_{%d} = \\frac{d_{%d} - a_{%d} \\mu_{%d}}{a_{%d} \\lambda_{%d} + b_{%d}} = \\frac{%.6f - %.6f \\times %.6f}{%.6f \\times %.6f + %.6f} = %.6f\\)",
                        i, j, j, i - 1, j, i - 1, j, rhs[j], a[j], mu[i - 1], a[j], lambda[i - 1], b[j], mu[i]));
            } else {
                steps.add(String.format("\n\\(\\lambda_{%d} = %.6f\\)", i, lambda[i]));
                steps.add(String.format("\n\\(\\mu_{%d} = %.6f\\)", i, mu[i]));
            }
        }

        steps.add("\nНачинаем обратный ход прогонки");

        solution[n] = mu[n + 1];
        steps.add(String.format("\n\\(x_{%d} = \\mu_{%d} = %.6f\\)", n - 1, n + 1, solution[n]));

        for (int i = n - 1; i >= 1; i--) {
            solution[i] = lambda[i + 1] * solution[i + 1] + mu[i + 1];
            steps.add(String.format("\n\\(x_{%d} = \\lambda_{%d} x_{%d} + \\mu_{%d} = %.6f \\times %.6f + %.6f = %.6f\\)",
                    i - 1, i + 1, i, i + 1, lambda[i + 1], solution[i + 1], mu[i + 1], solution[i]));
        }

        for (String step : steps) {
            sb.append(step);
        }

        return sb.toString();
    }
}
