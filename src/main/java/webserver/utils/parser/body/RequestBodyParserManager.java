package webserver.utils.parser.body;

import webserver.exceptions.BadRequestException;

import java.util.HashMap;
import java.util.Map;

public class RequestBodyParserManager {
    private static Map<String, RequestBodyParser> parserMap;

    static {
        parserMap = new HashMap<>();
        parserMap.put("application/x-www-form-urlencoded", new FormUrlencodedBodyParser());
        // 다른 Content-type에 맞는 RequestParser 구현 클래스들을 추가
    }

    public static Map<String, String> parse(String stringBody, String contentType) throws BadRequestException {
        if (!parserMap.containsKey(contentType)) {
            throw new BadRequestException("지원하지 않는 컨텐츠 타입");
        }

        RequestBodyParser requestBodyParser = parserMap.get(contentType);
        return requestBodyParser.parse(stringBody);
    }
}
