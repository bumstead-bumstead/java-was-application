package webserver.utils.parser;

import webserver.utils.HttpMessageParser;

import java.util.Map;

public class FormUrlencodedBodyParser implements RequestBodyParser {

    @Override
    public Map<String, String> parse(String stringBody) {
        return HttpMessageParser.parseParameters(stringBody);
    }
}
