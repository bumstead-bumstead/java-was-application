package webserver;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static utils.ParsingUtils.*;

public class URI {
    private final String path;
    private final Map<String, String> parameters;

    private URI(String path, Map<String, String> parameters) {
        this.path = path;
        this.parameters = parameters;
    }

    public static URI createURIWithString(String uriString) {
        Map<String, String> parameters = new HashMap<>();

        String[] uriArray = uriString.split("\\?");
        String path = uriArray[0];

        if (uriString.contains("?")) {
            String query = uriArray[1];
            List<String> queryList = parseStringToList(query, "&");

            parameters = parseListToMap(queryList);
        }

        return new URI(path, parameters);
    }

    public boolean hasParameter() {
        return !parameters.isEmpty();
    }
}
