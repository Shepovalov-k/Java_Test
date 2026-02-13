package com.example;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.io.OutputStreamWriter;

/**
 * File filtering utility that separates mixed data types into different files.
 */
public class FilteringUtility {
    private String outputPath = ".";
    private String prefix = "";
    private boolean appendMode = false;
    private boolean briefStatistics = false;
    private boolean fullStatistics = false;
    private List<String> inputFiles = new ArrayList<>();
    private Map<DataType, PrintWriter> writers = new HashMap<>();
    private Map<DataType, Statistics> statistics = new HashMap<>();

    public static void main(String[] args) {
        FilteringUtility utility = new FilteringUtility();
        try {
            utility.run(args);
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }

    private void run(String[] args) throws Exception {
        parseArguments(args);

        if (inputFiles.isEmpty()) {
            System.err.println("Error: No input files specified");
            printUsage();
            System.exit(1);
        }

        try {
            // Initialize statistics map
            for (DataType type : DataType.values()) {
                statistics.put(type, new Statistics());
            }

            // Process input files
            for (String inputFile : inputFiles) {
                processFile(inputFile);
            }

            // Print statistics
            printStatistics();
        } finally {
            closeAllWriters();
        }
    }

    private void parseArguments(String[] args) {
        for (int i = 0; i < args.length; i++) {
            String arg = args[i];

            if (arg.equals("-o") && i + 1 < args.length) {
                outputPath = args[++i];
            } else if (arg.equals("-p") && i + 1 < args.length) {
                prefix = args[++i];
            } else if (arg.equals("-a")) {
                appendMode = true;
            } else if (arg.equals("-s")) {
                briefStatistics = true;
            } else if (arg.equals("-f")) {
                fullStatistics = true;
            } else if (!arg.startsWith("-")) {
                inputFiles.add(arg);
            }
        }
    }

    private void processFile(String filePath) throws Exception {
        File file = new File(filePath);
        if (!file.exists()) {
            System.err.println("Warning: File not found: " + filePath);
            return;
        }

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null) {
                processLine(line);
            }
        } catch (IOException e) {
            System.err.println("Warning: Error reading file " + filePath + ": " + e.getMessage());
        }
    }

    private void processLine(String line) throws Exception {
        DataType type = DataTypeDetector.detectType(line);
        String trimmedLine = line.trim();

        // Write to appropriate file
        PrintWriter writer = getWriter(type);
        writer.println(trimmedLine);

        // Update statistics
        Statistics stats = statistics.get(type);
        if (type == DataType.INTEGER) {
            try {
                long value = Long.parseLong(trimmedLine);
                stats.addInteger(value);
            } catch (NumberFormatException e) {
                // Should not happen, but log it
                System.err.println("Warning: Failed to parse integer: " + trimmedLine);
            }
        } else if (type == DataType.FLOAT) {
            try {
                double value = Double.parseDouble(trimmedLine);
                stats.addDouble(value);
            } catch (NumberFormatException e) {
                // Should not happen, but log it
                System.err.println("Warning: Failed to parse float: " + trimmedLine);
            }
        } else {
            stats.addString(trimmedLine);
        }
    }

    private PrintWriter getWriter(DataType type) throws Exception {
        if (!writers.containsKey(type)) {
            String fileName = prefix + type.getFileName() + ".txt";
            Path outputFilePath = Paths.get(outputPath, fileName);

            // Create output directory if it doesn't exist
            Files.createDirectories(outputFilePath.getParent());

            FileOutputStream fos = new FileOutputStream(outputFilePath.toFile(), appendMode);
            PrintWriter writer = new PrintWriter(
                    new OutputStreamWriter(fos, StandardCharsets.UTF_8), true);
            writers.put(type, writer);
        }
        return writers.get(type);
    }

    private void printStatistics() {
        boolean hasStatistics = briefStatistics || fullStatistics;
        if (!hasStatistics) {
            return;
        }

        System.out.println("\n=== Statistics ===");

        for (DataType type : DataType.values()) {
            Statistics stats = statistics.get(type);
            if (!stats.hasData()) {
                continue;
            }

            System.out.println("\n" + type.getFileName().toUpperCase() + ":");
            System.out.println("  Count: " + stats.getCount());

            if (fullStatistics) {
                if (type == DataType.INTEGER) {
                    System.out.println("  Min: " + stats.getMinInteger());
                    System.out.println("  Max: " + stats.getMaxInteger());
                    System.out.println("  Sum: " + stats.getSum());
                    System.out.println("  Average: " + String.format("%.2f", stats.getAverageInteger()));
                } else if (type == DataType.FLOAT) {
                    System.out.println("  Min: " + stats.getMinDouble());
                    System.out.println("  Max: " + stats.getMaxDouble());
                    System.out.println("  Sum: " + String.format("%.10f", stats.getDoubleSum()));
                    System.out.println("  Average: " + String.format("%.10f", stats.getAverageDouble()));
                } else if (type == DataType.STRING) {
                    System.out.println("  Min Length: " + stats.getMinStringLength());
                    System.out.println("  Max Length: " + stats.getMaxStringLength());
                }
            }
        }
    }

    private void closeAllWriters() {
        for (PrintWriter writer : writers.values()) {
            writer.flush();
            writer.close();
        }
    }

    private void printUsage() {
        System.out.println("Usage: java -jar uXl.jar [options] input_file1 [input_file2 ...]");
        System.out.println("Options:");
        System.out.println("  -o <path>      Output directory (default: current directory)");
        System.out.println("  -p <prefix>    Prefix for output filenames (default: none)");
        System.out.println("  -a             Append mode (default: overwrite)");
        System.out.println("  -s             Brief statistics (count only)");
        System.out.println("  -f             Full statistics (count, min, max, sum, average)");
    }
}
