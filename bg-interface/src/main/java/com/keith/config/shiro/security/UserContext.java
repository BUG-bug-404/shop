package com.keith.config.shiro.security;


import com.keith.modules.entity.user.UserMember;

public class UserContext implements AutoCloseable {

    static final ThreadLocal<UserMember> current = new ThreadLocal<>();

    public UserContext(UserMember user) {
        current.set(user);
    }

    public static UserMember getCurrentUser() {
        return current.get();
    }

    public static void setCurrentUser(UserMember user) {
        current.set(user);
    }

    public void close() {
        current.remove();
    }
}