package com.example.methods.service.systems;

import com.example.methods.dto.LinearSystemRequestDto;
import com.example.methods.service.exceptions.ZeroPivotException;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;

@Service
public class GaussianSolverService {

    public String solve(LinearSystemRequestDto req) {
        double[][] A = deepCopyMatrix(req.getA());
        double[] b = req.getB().clone();
        int n = A.length;

        double[][] B = new double[n][n];
        double[][] C = new double[n][n];

        for (int i = 0; i < n; i++) {
            C[i][i] = 1.0;
        }

        StringBuilder sb = new StringBuilder();
        DecimalFormat df = new DecimalFormat("#.####");

        sb.append("Исходная матрица A: \\\\ \n");
        sb.append("\\begin{pmatrix}\n");
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                sb.append(df.format(A[i][j])).append(" & ");
            }
            sb.append("\\\\ \n");
        }
        sb.append("\\end{pmatrix}\n");

        try {
            for (int k = 0; k < n - 1; k++) {

                B[k][k] = A[k][k];
                if (Math.abs(B[k][k]) < 1e-12) {
                    throw new ZeroPivotException("Нулевой или слишком маленький опорный элемент в строке " + k + " матрицы B.");
                }

                for (int j = k + 1; j < n; j++) {
                    C[k][j] = A[k][j] / A[k][k];
                }

                for (int i = k + 1; i < n; i++) {
                    double factor = A[i][k] / A[k][k];
                    B[i][k] = A[i][k];

                    for (int j = k; j < n; j++) {
                        A[i][j] -= factor * A[k][j];
                    }
                    b[i] -= factor * b[k];
                }
            }

            B[n - 1][n - 1] = A[n - 1][n - 1];

            sb.append("Матрица B: \\\\ \n");
            sb.append("\\begin{pmatrix}\n");
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    sb.append(df.format(B[i][j])).append(" & ");
                }
                sb.append("\\\\ \n");
            }
            sb.append("\\end{pmatrix}\n");

            if (Math.abs(B[n - 1][n - 1]) < 1e-12) {
                throw new ZeroPivotException("Нулевой или слишком маленький опорный элемент в строке " + (n - 1) + " матрицы B.");
            }

            sb.append("Матрица C: \\\\ \n");
            sb.append("\\begin{pmatrix}\n");
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    sb.append(df.format(C[i][j])).append(" & ");
                }
                sb.append("\\\\ \n");
            }
            sb.append("\\end{pmatrix}\n");

            double[] x = new double[n];
            for (int i = n - 1; i >= 0; i--) {
                double sum = 0.0;
                for (int j = i + 1; j < n; j++) {
                    sum += A[i][j] * x[j];
                }
                x[i] = (b[i] - sum) / A[i][i];
            }

            sb.append("Решение системы: \\ \n");
            sb.append("\\begin{pmatrix}\n");
            for (int i = 0; i < n; i++) {
                sb.append(df.format(x[i])).append("\\\\ \n");
            }
            sb.append("\\end{pmatrix}\n");

        } catch (ZeroPivotException e) {
            sb.append(e.getMessage());
        }

        return sb.toString();
    }

    private double[][] deepCopyMatrix(double[][] src) {
        int n = src.length;
        double[][] dst = new double[n][];
        for (int i = 0; i < n; i++) {
            dst[i] = src[i].clone();
        }
        return dst;
    }
}

