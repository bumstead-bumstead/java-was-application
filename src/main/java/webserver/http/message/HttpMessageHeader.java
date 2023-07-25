package webserver.http.message;

import application.model.Cookie;

import java.util.*;

public class HttpMessageHeader {
    private Map<String, String> headerMap;
    private List<Cookie> cookies;

    public HttpMessageHeader() {
        this.headerMap = new HashMap<>();
        this.cookies = new ArrayList<>();
    }

    public static class Builder {

        private Map<String, String> headerMap;

        private List<Cookie> cookies;
        public Builder() {
            this.headerMap = new HashMap<>();
            this.cookies = new ArrayList<>();
        }
        public Builder addHeader(String key, String value) {
            headerMap.put(key, value);
            return this;
        }

        public Builder addCookie(Cookie cookie) {
            cookies.add(cookie);
            return this;
        }

        public Builder addLocation(String path) {
            headerMap.put(HttpHeaderUtils.LOCATION_HEADER, path);
            return this;
        }

        public Builder addContentType(String contentType) {
            headerMap.put(HttpHeaderUtils.CONTENT_TYPE_HEADER, contentType);
            return this;
        }

        public Builder addContentLength(int contentLength) {
            headerMap.put(HttpHeaderUtils.CONTENT_LENGTH_HEADER, String.valueOf(contentLength));
            return this;
        }

        public HttpMessageHeader build() {
            HttpMessageHeader responseHeader = new HttpMessageHeader();
            responseHeader.headerMap = this.headerMap;
            responseHeader.cookies = this.cookies;
            return responseHeader;
        }


    }
    public String getValue(String key) {
        return headerMap.get(key);
    }

    public boolean containsKey(String key) {
        return headerMap.containsKey(key);
    }

    public List<Cookie> getCookies() {
        return cookies;
    }

    public Map<String, String> getHeaderMap() {
        return headerMap;
    }

    @Override
    public String toString() {
        return "HttpMessageHeader{" +
                "headerMap=" + headerMap +
                ", cookies=" + cookies +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HttpMessageHeader that = (HttpMessageHeader) o;
        return Objects.equals(headerMap, that.headerMap) && Objects.equals(cookies, that.cookies);
    }

    @Override
    public int hashCode() {
        return Objects.hash(headerMap, cookies);
    }
}
