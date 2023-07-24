package webserver.http.message;

import webserver.utils.HttpHeaderUtils;

import java.util.HashMap;
import java.util.Map;

public class HttpResponseHeader {
    private Map<String, String> headerMap;

    public HttpResponseHeader() {
        this.headerMap = new HashMap<>();
    }

    public void addHeader(String key, String value) {
        headerMap.put(key, value);
    }

    public void addCookie(String cookieValue) {
        headerMap.put(HttpHeaderUtils.SET_COOKIE_HEADER, cookieValue);
    }

    public void addLocation(String path) {
        headerMap.put(HttpHeaderUtils.LOCATION_HEADER, path);
    }

    public void addContentType(String contentType) {
        headerMap.put(HttpHeaderUtils.CONTENT_TYPE_HEADER, contentType);
    }

    public void addContentLength(int contentLength) {
        headerMap.put(HttpHeaderUtils.CONTENT_LENGTH_HEADER, String.valueOf(contentLength));
    }

    public String getValue(String key) {
        return headerMap.get(key);
    }

    public boolean containsKey(String key) {
        return headerMap.containsKey(key);
    }

    public Map<String, String> getHeaderMap() {
        return headerMap;
    }
}
