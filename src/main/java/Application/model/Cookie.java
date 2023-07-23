package Application.model;

public class Cookie {
    private String value;
    private String userId;

    public Cookie(String value, String userId) {
        this.value = value;
        this.userId = userId;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
