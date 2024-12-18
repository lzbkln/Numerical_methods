package com.example.methods.service.util;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.util.function.Function;

public class FunctionParser {

    public static Function<Double, Double> parseFunction(String userFunction) {
        ScriptEngineManager manager = new ScriptEngineManager();
        ScriptEngine engine = manager.getEngineByName("JavaScript");

        return x -> {
            try {
                engine.put("x", x);
                return (double) engine.eval(userFunction);
            } catch (ScriptException e) {
                throw new RuntimeException("Ошибка при вычислении функции: " + e.getMessage(), e);
            }
        };
    }
}
