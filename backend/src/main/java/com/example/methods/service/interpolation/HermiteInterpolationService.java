package com.example.methods.service.interpolation;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class HermiteInterpolationService {

    public String solveInterpolationProblem(List<Double> xValues, List<Double> fxValues, List<Integer> multiplicities, List<List<Double>> derivativesList) {
        StringBuilder builder = new StringBuilder();

        int n = xValues.size();

        if (xValues.size() != fxValues.size() || xValues.size() != multiplicities.size() || xValues.size() != derivativesList.size()) {
            throw new IllegalArgumentException("All input lists must have the same size.");
        }

        List<Double> z = new ArrayList<>();
        List<Double> Q = new ArrayList<>();
        int m = 0;

        // Construct the extended list of nodes and function values
        for (int i = 0; i < n; i++) {
            int multiplicity = multiplicities.get(i);
            m += multiplicity;
            z.add(xValues.get(i));
            Q.add(fxValues.get(i));

            for (int k = 0; k < multiplicity - 1; k++) {
                z.add(xValues.get(i));
                Q.add(derivativesList.get(i).get(k));
            }
        }

        double[][] dividedDifferences = new double[m][m];

        // Initialize the first column of divided differences
        for (int i = 0; i < m; i++) {
            dividedDifferences[i][0] = Q.get(i);
        }

        // Calculate the first divided differences
        for (int i = 1; i < m; i++) {
            if (z.get(i).equals(z.get(i - 1))) {
                dividedDifferences[i - 1][1] = Q.get(i);
            } else {
                dividedDifferences[i - 1][1] = (dividedDifferences[i][0] - dividedDifferences[i - 1][0]) / (z.get(i) - z.get(i - 1));
            }
        }

        // Calculate the remaining divided differences
        for (int j = 2; j < m; j++) {
            for (int i = 0; i < m - j; i++) {
                if (z.get(i).equals(z.get(i + j))) {
                    dividedDifferences[i][j] = dividedDifferences[i + 1][j - 1] / factorial(j);
                } else {
                    dividedDifferences[i][j] = (dividedDifferences[i + 1][j - 1] - dividedDifferences[i][j - 1]) / (z.get(i + j) - z.get(i));
                }
            }
        }

        // Build the Hermite interpolation polynomial
        builder.append("Интерполяционный многочлен Эрмита:\n");
        builder.append("\\[H(x) = ").append(String.format("%.6f", dividedDifferences[0][0]));

        for (int i = 1; i < m; i++) {
            builder.append(" + ");
            for (int k = 0; k < i; k++) {
                builder.append(String.format("(x - %.6f)", z.get(k)));
            }
            builder.append(String.format(" \\cdot %.6f", dividedDifferences[0][i]));
        }
        builder.append("\\]\n");

        return builder.toString();
    }

    private int factorial(int n) {
        if (n == 0 || n == 1) return 1;
        int result = 1;
        for (int i = 2; i <= n; i++) {
            result *= i;
        }
        return result;
    }
}
