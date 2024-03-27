package org.eventmanagement.dto;

import java.io.Serializable;

import org.eventmanagement.enums.Role;

public class TokenResponse implements Serializable {

    private String token;

    private Role role;

    public TokenResponse(String token, Role roles) {
        this.token = token;
        this.role = roles;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}
