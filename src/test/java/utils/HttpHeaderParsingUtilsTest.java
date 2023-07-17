package utils;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.*;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class HttpHeaderParsingUtilsTest {

    @Test
    @DisplayName("줄바꿈을 기준으로 문자열을 나눠 List로 반환한다")
    void parseBufferedReader() throws IOException {
        //given
        String testString1 = "line 1";
        String testString2 = "line 2";
        String testString3 = "line 3";
        StringBuilder stringBuffer = new StringBuilder();
        stringBuffer.append(testString1)
                .append("\n")
                .append(testString2)
                .append("\n")
                .append(testString3);

        //when
        InputStream inputStream = new ByteArrayInputStream(stringBuffer.toString().getBytes());
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        List<String> result = ParsingUtils.parseBufferedReader(bufferedReader);

        //Then
        assertEquals(testString1, result.get(0));
        assertEquals(testString2, result.get(1));
        assertEquals(testString3, result.get(2));
    }

    @Test
    @DisplayName(": 기준으로 Key, value를 나눠 map에 저장한다")
    void parseMetaData() {
        //given
        List<String> list = List.of("key1: value1", "key2: value2", "key3: value3");
        Map<String, String> result = Map.of("key1", "value1", "key2", "value2", "key3", "value3");

        //when
        Map<String, String> actual = ParsingUtils.parseListToMap(list);

        //then
        assertEquals(result, actual);
    }

    @Test
    @DisplayName(": 기준으로 Key, value를 나눠 map에 저장한다")
    void parseMetaDataWithNoBlank() {
        //given
        List<String> list = List.of("key1:value1", "key2:value2", "key3:value3");
        Map<String, String> result = Map.of("key1", "value1", "key2", "value2", "key3", "value3");

        //when
        Map<String, String> actual = ParsingUtils.parseListToMap(list);

        //then
        assertEquals(result, actual);
    }
    @ParameterizedTest
    @MethodSource("provideStringListForRequestLineTest")
    void parseRequestLine(String input, List<String> result) {
        assertEquals(result, ParsingUtils.parseStringToList(input));
    }
    private static Stream<Arguments> provideStringListForRequestLineTest() {
        return Stream.of(
                Arguments.of("a b c", List.of("a", "b", "c")),
                Arguments.of("test test test", List.of("test", "test", "test"))
        );
    }

}
