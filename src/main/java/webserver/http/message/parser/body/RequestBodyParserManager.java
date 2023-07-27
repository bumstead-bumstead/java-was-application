package webserver.http.message.parser.body;

import webserver.exceptions.BadRequestException;

import java.util.HashMap;
import java.util.Map;

public class RequestBodyParserManager {
    private static Map<String, RequestBodyParser> parserMap;

    private static class SingletonHelper {
        private static final RequestBodyParserManager REQUEST_BODY_PARSER_MANAGER = new RequestBodyParserManager();
    }

    public static RequestBodyParserManager getInstance() {
        return SingletonHelper.REQUEST_BODY_PARSER_MANAGER;
    }

    private RequestBodyParserManager() {
        parserMap = new HashMap<>();
        parserMap.put("application/x-www-form-urlencoded", new FormUrlencodedBodyParser());

    }

    public Map<String, String> parse(String stringBody, String contentType) throws BadRequestException {
        if (!parserMap.containsKey(contentType)) {
            throw new BadRequestException("지원하지 않는 컨텐츠 타입");
        }

        RequestBodyParser requestBodyParser = parserMap.get(contentType);
        return requestBodyParser.parse(stringBody);
    }
}
