package classes;

public class SuccessfulRegistration {
    private Integer id;
    private String token;

    public SuccessfulRegistration(Integer id, String token) {
        this.id = id;
        this.token = token;
    }

    public SuccessfulRegistration() {
    }

    public Integer getId() {
        return id;
    }

    public String getToken() {
        return token;
    }
}
