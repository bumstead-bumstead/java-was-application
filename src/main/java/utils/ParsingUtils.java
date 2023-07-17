package utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class ParsingUtils {

    public static List<String> parseBufferedReader(BufferedReader bufferedReader) throws IOException {
        List<String> result = new ArrayList<>();
        String line = bufferedReader.readLine();

        while (line != null && !line.equals("")) {
            result.add(line);
            line = bufferedReader.readLine();
        }
        return result;
    }

    public static List<String> parseStringToList(String requestLine) {
        String[] requestLineArray = requestLine.split(" ");
        List<String> result = Arrays.stream(requestLineArray).collect(Collectors.toList());

        return result;
    }
    public static List<String> parseStringToList(String requestLine, String seperator) {
        String[] requestLineArray = requestLine.split(seperator);
        List<String> result = Arrays.stream(requestLineArray).collect(Collectors.toList());

        return result;
    }

    public static Map<String, String> parseListToMap(List<String> requestHeaderList) {
        Map<String, String> metadata = new HashMap<>();

        for (String line : requestHeaderList) {
            String[] field = line.split(":");
            metadata.put(removeSpaces(field[0]), removeSpaces(field[1]));
        }

        return metadata;
    }
    public static Map<String, String> parseListToMap(List<String> requestHeaderList, String seperator) {
        Map<String, String> metadata = new HashMap<>();

        for (String line : requestHeaderList) {
            String[] field = line.split(seperator);
            metadata.put(removeSpaces(field[0]), removeSpaces(field[1]));
        }

        return metadata;
    }

    private static String removeSpaces(String input) {
        return input.replaceAll("\\s", "");
    }
}
