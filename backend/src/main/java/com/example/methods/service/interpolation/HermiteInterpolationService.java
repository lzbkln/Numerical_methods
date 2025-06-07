package com.example.methods.service.interpolation;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class HermiteInterpolationService {

    public String solveInterpolationProblem(double[] nodes, int[] multiplicities, double[] fValues, List<List<Double>> derivatives) {
        StringBuilder output = new StringBuilder();

        List<Double> nodesMulti = new ArrayList<>();
        List<Double> fValuesMulti = new ArrayList<>();
        int N = 0;

        for (int m : multiplicities) {
            N += m;
        }

        for (int i = 0; i < nodes.length; i++) {
            for (int j = 0; j < multiplicities[i]; j++) {
                nodesMulti.add(nodes[i]);
                fValuesMulti.add(fValues[i]);
            }
        }

        double[][] table = new double[N][N];

        for (int i = 0; i < N; i++) {
            table[i][0] = fValuesMulti.get(i);
        }

        for (int j = 1; j < N; j++) {
            for (int i = j; i < N; i++) {
                if (nodesMulti.get(i).equals(nodesMulti.get(i - j))) {
                    int nodeIndex = findNodeIndex(nodes, nodesMulti.get(i));
                    table[i][j] = derivatives.get(nodeIndex).get(j - 1) / factorial(j);
                } else {
                    table[i][j] = (table[i][j - 1] - table[i - j][j - 1]) / (nodesMulti.get(i) - nodesMulti.get(i - j));
                }
            }
        }

        output.append("\nТаблица разделённых разностей:\n");
        output.append("\\begin{array}{|c|c|");
        for (int j = 1; j < N; j++) {
            output.append("c|");
        }
        output.append("}\n\\hline\n");

        output.append("x_i & f(x_i) ");
        for (int j = 1; j < N; j++) {
            output.append(" & f[");
            for (int idx = 0; idx <= j; idx++) {
                if (idx > 0) output.append(", ");
                output.append(String.format("x_{%d}", idx + 1));
            }
            output.append("] ");
        }
        output.append(" \\\\\n\\hline\n");

        for (int i = 0; i < N; i++) {
            output.append(String.format("%.6f", nodesMulti.get(i))).append(" & ");
            output.append(String.format("%.6f", table[i][0]));
            for (int j = 1; j <= i; j++) {
                output.append(" & ").append(String.format("%.6f", table[i][j]));
            }
            output.append(" \\\\\n\\hline\n");
        }
        output.append("\\end{array}");

        output.append("\nИнтерполяционный многочлен Эрмита:\n");
        output.append("\\[ H(x) = ");
        output.append(String.format("%.6f", table[0][0]));

        for (int i = 1; i < N; i++) {
            output.append(" + ").append(String.format("%.6f", table[i][i]));
            for (int j = 0; j < i; j++) {
                output.append("(x - ").append(String.format("%.6f", nodesMulti.get(j))).append(")");
            }
        }
        output.append(" \\]\n");

        return output.toString();
    }

    private int findNodeIndex(double[] nodes, double z) {
        for (int i = 0; i < nodes.length; i++) {
            if (Double.compare(nodes[i], z) == 0) {
                return i;
            }
        }
        throw new IllegalArgumentException("Node not found");
    }

    private int factorial(int n) {
        if (n <= 1) return 1;
        return n * factorial(n - 1);
    }
}
