package application.model;

import java.util.Objects;

import static webserver.http.message.HttpHeaderUtils.PATH_HEADER;

public class Cookie {
    private String name;
    private String value;
    private String path;

    public Cookie(String name, String value) {
        this.name = name;
        this.value = value;
        this.path = "/";
    }

    public String toHeaderString() {
        StringBuilder cookieHeader = new StringBuilder()
                .append(name)
                .append("=")
                .append(value)
                .append("; ")
                .append(PATH_HEADER)
                .append(path);

        return cookieHeader.toString();
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setUserId(String userId) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Cookie cookie = (Cookie) o;
        return Objects.equals(name, cookie.name) && Objects.equals(value, cookie.value) && Objects.equals(path, cookie.path);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, value, path);
    }

    @Override
    public String toString() {
        return "Cookie{" +
                "name='" + name + '\'' +
                ", value='" + value + '\'' +
                ", path='" + path + '\'' +
                '}';
    }
}
