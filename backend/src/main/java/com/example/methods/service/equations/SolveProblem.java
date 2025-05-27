package com.example.methods.service.equations;

import org.springframework.stereotype.Component;

@Component
abstract public class SolveProblem {

    protected static class SolutionMessageBuilder {
        private final StringBuilder messageBuilder = new StringBuilder();
        private String error;

        public SolutionMessageBuilder appendMessage(String message) {
            if (message != null) {
                messageBuilder.append(message).append("\n");
            }
            return this;
        }

        public SolutionMessageBuilder setError(String error) {
            this.error = error;
            return this;
        }

        public String build() {
            if (error != null) {
                return error;
            }
            return messageBuilder.toString();
        }
    }

    abstract public String solveProblem(String userFunction, double a, double b, double epsilon);
}
