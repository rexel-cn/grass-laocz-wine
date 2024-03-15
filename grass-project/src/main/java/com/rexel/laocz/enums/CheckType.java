package com.rexel.laocz.enums;

public enum CheckType {
    EQUALS {
        @Override
        public boolean checkValue(String actual, String expected) {
            return actual.equals(expected);
        }
    },
    GREATER_THAN {
        @Override
        public boolean checkValue(String actual, String expected) {
            return Double.parseDouble(actual) > Double.parseDouble(expected);
        }
    },
    LESS_THAN {
        @Override
        public boolean checkValue(String actual, String expected) {
            return Double.parseDouble(actual) < Double.parseDouble(expected);
        }
    };

    public abstract boolean checkValue(String actual, String expected);
}
