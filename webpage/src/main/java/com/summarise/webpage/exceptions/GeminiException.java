package com.summarise.webpage.exceptions;

public class GeminiException extends RuntimeException {

        public enum ErrorType {
            INVALID_PROMPT,
            MODEL_OUTPUT_EMPTY,
            PARSING_FAILED,
            THIRD_PARTY_UNAVAILABLE,
            RATE_LIMITED
        }

        private final ErrorType type;

        public GeminiException(ErrorType type, String message) {
            super(message);
            this.type = type;
        }

        public ErrorType getType() {
            return type;
        }
}
