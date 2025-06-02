package com.example.methods.service.util;

import org.matheclipse.core.eval.ExprEvaluator;
import org.matheclipse.core.interfaces.IExpr;

import java.util.function.Function;

public class FunctionParser {
    private static final ExprEvaluator evaluator = new ExprEvaluator(false, (short) 100);

    public static Function<Double, Double> parseFunction(String expr) {
        final String functionBody = expr;
        return x -> {
            String xValStr = Double.toString(x);
            String exprWithX = functionBody.replaceAll("\\bx\\b", "(" + xValStr + ")");
            IExpr result = evaluator.evaluate(exprWithX);
            return ((Number) result.evalDouble()).doubleValue();
        };
    }
}
