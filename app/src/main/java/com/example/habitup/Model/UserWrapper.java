package com.example.habitup.Model;

/**
 * Created by gojeffcho on 2017-12-02.
 */

public class UserWrapper {
    private String username;
    private int uid;

    public UserWrapper(UserAccount user) {
        username = user.getUsername();
        uid = user.getUID();
    }

    public String getUsername() { return this.username; }
    public int getUID() { return this.uid; }

    @Override
    public boolean equals(Object obj) {
        UserWrapper user = (UserWrapper) obj;
        return (this.username.equals(user.getUsername()) && this.uid == user.getUID());
    }
}