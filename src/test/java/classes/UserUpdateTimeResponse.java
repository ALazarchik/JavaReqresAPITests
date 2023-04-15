package classes;

public class UserUpdateTimeResponse extends UserUpdateTime{
    private String updatedAt;

    public UserUpdateTimeResponse() {
    }

    public UserUpdateTimeResponse(String name, String job, String updatedAt) {
        super(name, job);
        this.updatedAt = updatedAt;
    }

    public UserUpdateTimeResponse(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }
}
