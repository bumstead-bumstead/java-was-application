package webserver.http.message.parser.body;

import webserver.http.message.parser.HttpMessageParser;

import java.util.Map;

public class FormUrlencodedBodyParser implements RequestBodyParser {

    @Override
    public Map<String, String> parse(String stringBody) {
        return HttpMessageParser.parseParameters(stringBody);
    }
}
