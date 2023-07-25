package application.HTMLRenderer;

import java.util.Map;

public interface HTMLRenderer {
    public byte[] render(Map<String, Object> renderParameter);

}
