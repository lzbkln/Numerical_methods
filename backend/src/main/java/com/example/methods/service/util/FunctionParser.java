package com.example.methods.service.util;

import com.example.methods.service.exceptions.InvalidExpressionException;
import org.matheclipse.core.eval.ExprEvaluator;
import org.matheclipse.core.eval.exception.ArgumentTypeException;
import org.matheclipse.core.interfaces.IExpr;

import java.util.function.Function;

public class FunctionParser {
    private static final ExprEvaluator evaluator = new ExprEvaluator(false, (short) 100);

    public static Function<Double, Double> parseFunction(String expr) {
        final String functionBody = expr;
        return x -> {
            String xValStr = Double.toString(x);
            String exprWithX = functionBody.replaceAll("\\bx\\b", "(" + xValStr + ")");
            try {
                IExpr result = evaluator.evaluate(exprWithX);
                return ((Number) result.evalDouble()).doubleValue();
            } catch (ArgumentTypeException e) {
                throw new InvalidExpressionException("Данная функция введена некорректно, смотрите пример ввода!");
            }
        };
    }
}
