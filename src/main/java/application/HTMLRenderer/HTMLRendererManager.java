package application.HTMLRenderer;

import webserver.exceptions.BadRequestException;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class HTMLRendererManager {
    private static Map<String, HTMLRenderer> rendererMap;

    private static class SingletonHelper {
        private static final HTMLRendererManager HTML_RENDERER_MANAGER = new HTMLRendererManager();
    }

    private HTMLRendererManager() {
        rendererMap = new ConcurrentHashMap<>();
        rendererMap.put("/index.html", new IndexRenderer());
        rendererMap.put("/user/list.html", new UserListRenderer());
        rendererMap.put("/user/login.html", new LoginRenderer());
        rendererMap.put("/user/form.html", new SignUpRenderer());
    }

    public static HTMLRendererManager getInstance() {
        return SingletonHelper.HTML_RENDERER_MANAGER;
    }

    public byte[] render(String path, Map<String, Object> parameters) throws BadRequestException {
        if (!rendererMap.containsKey(path)) {
            throw new BadRequestException("렌더링할 수 없는 경로");
        }

        HTMLRenderer renderer = rendererMap.get(path);
        return renderer.render(parameters);
    }
}
