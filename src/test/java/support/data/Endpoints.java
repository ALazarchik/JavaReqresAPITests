package support.data;

public class Endpoints {
    private final String BASE_URL;
    private final String ALL_USERS;
    private final String ALL_USERS_PAGE;
    private final String REGISTER_USER;
    private final String ALL_RESOURCES;
    private final String NON_EXISTING_USER;
    private final String USER_TO_UPDATE;

    public Endpoints() {
        this.BASE_URL = "https://reqres.in";
        this.ALL_USERS = "/api/users";
        this.ALL_USERS_PAGE = "/api/users?page=";
        this.REGISTER_USER = "/api/register";
        this.ALL_RESOURCES = "/api/unknown";
        this.NON_EXISTING_USER = "/api/users/23";
        this.USER_TO_UPDATE = "/api/users/2";
    }

    public String getBASE_URL() {
        return BASE_URL;
    }

    public String getALL_USERS() {
        return ALL_USERS;
    }

    public String getALL_USERS_PAGE() {
        return ALL_USERS_PAGE;
    }

    public String getREGISTER_USER() {
        return REGISTER_USER;
    }

    public String getALL_RESOURCES() {
        return ALL_RESOURCES;
    }

    public String getNON_EXISTING_USER() {
        return NON_EXISTING_USER;
    }

    public String getUSER_TO_UPDATE() {
        return USER_TO_UPDATE;
    }
}
