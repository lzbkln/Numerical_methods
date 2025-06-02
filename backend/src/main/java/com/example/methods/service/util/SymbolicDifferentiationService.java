package com.example.methods.service.util;

import org.matheclipse.core.eval.ExprEvaluator;
import org.matheclipse.core.interfaces.IExpr;
import org.springframework.stereotype.Service;

@Service
public class SymbolicDifferentiationService {
    private final ExprEvaluator evaluator = new ExprEvaluator(false, (short) 100);

    public String getDerivativeFormula(String function, String variable) {
        String expr = String.format("D(%s, %s)", function, variable);
        IExpr result = evaluator.evaluate(expr);
        return result.toString();
    }
}
