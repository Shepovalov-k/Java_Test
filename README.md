# File Filtering Utility

A Java utility that separates mixed data types from input files into different output files.

## Requirements

- **Java**: 17 or later
- **Maven**: 3.6.0 or later

## Building

To build the project:

```bash
mvn clean package
```

This will create an executable JAR file: `target/uXl.jar`

## Usage

```bash
java -jar target/uXl.jar [options] input_file1 [input_file2 ...]
```

### Options

- `-o <path>`: Set output directory (default: current directory)
- `-p <prefix>`: Set prefix for output filenames (default: none)
- `-a`: Append mode - append to existing files instead of overwriting (default: overwrite)
- `-s`: Brief statistics - show count of elements only
- `-f`: Full statistics - show count, min, max, sum, and average values

### Output Files

By default, three types of output files are created (only if data of that type exists):
- `integers.txt` - Contains integer values
- `floats.txt` - Contains floating-point numbers
- `strings.txt` - Contains text strings

With prefix and output path options, files will be named:
- `<path>/<prefix>integers.txt`
- `<path>/<prefix>floats.txt`
- `<path>/<prefix>strings.txt`

### Examples

**Example 1: Basic filtering with brief statistics**
```bash
java -jar target/uXl.jar -s input1.txt input2.txt
```

Creates `integers.txt`, `floats.txt`, and `strings.txt` in the current directory, and displays count statistics.

**Example 2: Custom output path and prefix with append mode**
```bash
java -jar target/uXl.jar -a -o /tmp -p result_ input1.txt input2.txt
```

Appends data to files `/tmp/result_integers.txt`, `/tmp/result_floats.txt`, and `/tmp/result_strings.txt`.

**Example 3: Full statistics**
```bash
java -jar target/uXl.jar -f input1.txt
```

Shows full statistics including min, max, sum, and average for numbers, and string length ranges for text.

## Data Type Detection

The utility detects data types as follows:

1. **Integer**: Values that can be parsed as `long` (e.g., `-1`, `0`, `1234567890123456789`)
2. **Float**: Values that can be parsed as `double` (e.g., `3.14`, `1.528535047E-25`, `-0.001`)
3. **String**: Everything else (text that doesn't match integer or float patterns)

Lines are read sequentially from each input file in the order specified on the command line.

## Error Handling

The application gracefully handles errors:
- Missing input files are reported as warnings and processing continues
- File I/O errors are reported but don't stop processing of other files
- Parse errors for statistics don't stop file processing

## Implementation Details

- **Charset**: All files are read and written using UTF-8 encoding
- **Output Files**: Created only if data of that type exists in the input
- **Overwrite Behavior**: By default, output files are overwritten. Use `-a` for append mode
- **Statistics**: Integer and float statistics include min, max, sum, and average. String statistics include shortest and longest string lengths.

## Project Structure

```
src/
  main/java/com/example/
    FilteringUtility.java     - Main application class
    DataType.java             - Enum for data types
    DataTypeDetector.java      - Utility to detect data types
    Statistics.java            - Statistics collection class
  test/java/com/example/
    FilteringUtilityTest.java  - Unit tests
```
