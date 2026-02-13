package com.example;

/**
 * Enum representing the types of data: INTEGER, FLOAT, or STRING.
 */
public enum DataType {
    INTEGER("integers"),
    FLOAT("floats"),
    STRING("strings");

    private final String fileName;

    DataType(String fileName) {
        this.fileName = fileName;
    }

    public String getFileName() {
        return fileName;
    }
}
