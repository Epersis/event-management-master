package org.eventmanagement.dto;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.eventmanagement.enums.Role;
import org.eventmanagement.model.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class UserDetailsImpl implements UserDetails {

    private String email;

    private String password;

    private Role role;

    private Collection<? extends GrantedAuthority> authorities;

    public UserDetailsImpl(String email, String password, Collection<? extends GrantedAuthority> authorities,
                           Role role) {
        this.email = email;
        this.password = password;
        this.authorities = authorities;
        this.role = role;
    }

    public static UserDetailsImpl build(User user) {
        List<GrantedAuthority> authorities = Arrays.asList(new SimpleGrantedAuthority(user.getRole().toString()));
        return new UserDetailsImpl(
                user.getEmail(),
                user.getPassword(),
                authorities, user.getRole());
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authorities;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public Role getRole() {
        return role;
    }
}
