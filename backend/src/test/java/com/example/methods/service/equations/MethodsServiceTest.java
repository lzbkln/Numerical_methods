package com.example.methods.service.equations;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.matheclipse.core.expression.S;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

//@SpringBootTest(classes = {MethodsApplication.class})
//fix
public class MethodsServiceTest {
    BisectionMethodService bisectionMethodService = new BisectionMethodService();
    NewtonMethodService newtonMethodService = new NewtonMethodService();
    FixedChordsMethodService fixedChordsMethodService = new FixedChordsMethodService();
    NoFixedChordsMethodService noFixedChordsMethodService = new NoFixedChordsMethodService();

    @Test
    @DisplayName("Test for solving a nonlinear equation using the Dichotomy method.")
    public void testForSolvingNonlinearEquationUsingTheDichotomyMethod() {

        String equation = "Math.exp(-x) - 1.9 + x * x";
        double epsilon = 0.00001;
        double a = 1;
        double b = 2;
        String result = bisectionMethodService.solveProblem(equation, a, b, epsilon);

        String resultString = String.format("%.5f", result);
        assertEquals("1,27277", resultString);
    }

    @Test
    @DisplayName("Test for NO solving a nonlinear equation using the Dichotomy method.")
    public void testForNOSolvingNonlinearEquationUsingTheDichotomyMethod() {
        String equation = "Math.exp(-x) - 1.9 + x * x";
        double epsilon = 0.00001;
        double a = 100;
        double b = 101;

        assertThrows(IllegalArgumentException.class, () -> {
            bisectionMethodService.solveProblem(equation, a, b, epsilon);
        });
    }

    @Test
    @DisplayName("Test for solving a nonlinear equation using the Newton method.")
    public void testForSolvingNonlinearEquationUsingTheNewtonMethod() {

        String equation = "Math.exp(-x) - 1.9 + x * x";
        double epsilon = 0.00001;
        double a = 1;
        double b = 2;
        String result = newtonMethodService.solveProblem(equation, a, b, epsilon);

        String resultString = String.format("%.5f", result);
        assertEquals("1,27277", resultString);
    }

    @Test
    @DisplayName("Test for NO solving a nonlinear equation using the Newton method.")
    public void testForNOSolvingNonlinearEquationUsingTheNewtonMethod() {

        String equation = "Math.exp(-x) - 1.9 + x * x";
        double epsilon = 0.00001;
        double a = 101;
        double b = 102;

        assertThrows(IllegalArgumentException.class, () -> {
            newtonMethodService.solveProblem(equation, a, b, epsilon);
        });
    }

    @Test
    @DisplayName("Test for solving a nonlinear equation using the Fixed Chords method.")
    public void testForSolvingNonlinearEquationUsingTheFixedChordsMethod() {

        String equation = "Math.exp(-x) - 1.9 + x * x";
        double epsilon = 0.00001;
        double a = 1;
        double b = 2;
        String result = fixedChordsMethodService.solveProblem(equation, a, b, epsilon);

        String resultString = String.format("%.5f", result);
        assertEquals("1,27277", resultString);
    }


    @Test
    @DisplayName("Test for NO solving a nonlinear equation using the Fixed Chords method.")
    public void testForNOSolvingNonlinearEquationUsingTheFixedChordsMethod() {

        String equation = "Math.exp(-x) - 1.9 + x * x";
        double epsilon = 0.00001;
        double a = 101;
        double b = 102;

        assertThrows(IllegalArgumentException.class, () -> {
            fixedChordsMethodService.solveProblem(equation, a, b, epsilon);
        });
    }

    @Test
    @DisplayName("Test for solving a nonlinear equation using the No Fixed Chords method.")
    public void testForSolvingNonlinearEquationUsingTheNoFixedChordsMethod() {

        String equation = "Math.exp(-x) - 1.9 + x * x";
        double epsilon = 0.00001;
        double a = 1;
        double b = 2;
        String result = noFixedChordsMethodService.solveProblem(equation, a, b, epsilon);

        String resultString = String.format("%.5f", result);
        assertEquals("1,27277", resultString);
    }


    @Test
    @DisplayName("Test for NO solving a nonlinear equation using the No Fixed Chords method.")
    public void testForNOSolvingNonlinearEquationUsingTheNoFixedChordsMethod() {

        String equation = "Math.exp(-x) - 1.9 + x * x";
        double epsilon = 0.00001;
        double a = 101;
        double b = 102;

        assertThrows(IllegalArgumentException.class, () -> {
            noFixedChordsMethodService.solveProblem(equation, a, b, epsilon);
        });
    }
}