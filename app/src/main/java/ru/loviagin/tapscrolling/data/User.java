package ru.loviagin.tapscrolling.data;

public class User {
    private int Uid;
    private String email, phone, avatar_url, username, password;

    public User() {
    }

    public User(String email, String avatar_url, String username, String password) {
        this.email = email;
        this.password = password;
        this.avatar_url = avatar_url;
        this.username = username;
    }

    public int getUid() {
        return Uid;
    }

    public void setUid(int uid) {
        Uid = uid;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAvatar_url() {
        return avatar_url;
    }

    public void setAvatar_url(String avatar_url) {
        this.avatar_url = avatar_url;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
