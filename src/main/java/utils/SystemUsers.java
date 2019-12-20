package utils;

public enum SystemUsers {
    ADMIN("admin");
    private String username;

    SystemUsers(String username) {
        this.username = username;
    }

    public String getUsername() {
        return this.username;
    }
}