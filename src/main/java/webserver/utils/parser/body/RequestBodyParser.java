package webserver.utils.parser.body;

import java.util.Map;

public interface RequestBodyParser {
    public Map<String, String> parse(String stringBody);

}
