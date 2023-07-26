package application.HTMLRenderer;

import java.util.Map;

public interface HTMLRenderer {
    byte[] render(Map<String, Object> renderParameter);
}
