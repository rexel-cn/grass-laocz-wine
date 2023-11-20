package com.rexel.common.core.domain;

import java.io.Serializable;

/**
 * ClassName PulseUser
 * Description
 * Author 孟开通
 * Date 2022/7/18 10:27
 **/
public class PulseUser implements Serializable {
    private String username = "admin";
    private String password = "admin123";

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
