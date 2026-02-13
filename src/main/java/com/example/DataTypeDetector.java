package com.example;

/**
 * Utility class to detect the data type of a string value.
 */
public class DataTypeDetector {

    /**
     * Detects whether a string represents an integer, float, or string.
     * Returns null if the type cannot be determined (treated as string).
     */
    public static DataType detectType(String value) {
        if (value == null || value.trim().isEmpty()) {
            return DataType.STRING;
        }

        value = value.trim();

        // Try to parse as integer
        try {
            Long.parseLong(value);
            return DataType.INTEGER;
        } catch (NumberFormatException e) {
            // Not an integer, continue
        }

        // Try to parse as double
        try {
            Double.parseDouble(value);
            return DataType.FLOAT;
        } catch (NumberFormatException e) {
            // Not a double, treat as string
        }

        // Default to string
        return DataType.STRING;
    }
}
