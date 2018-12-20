package net.anumbrella.rest.server.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author anumbrella
 */
public class SysUser {


    @NotNull
    @JsonProperty("username")
    private String username;

    @NotNull
    @JsonProperty("email")
    private String email;

    @NotNull
    @JsonProperty("password")
    private String password;


    @NotNull
    @JsonProperty("role")
    private List<String> role;


    @NotNull
    public String getUsername() {
        return username;
    }

    public void setUsername(@NotNull String username) {
        this.username = username;
    }

    @NotNull
    public String getEmail() {
        return email;
    }

    public void setEmail(@NotNull String email) {
        this.email = email;
    }

    @NotNull
    public String getPassword() {
        return password;
    }

    public void setPassword(@NotNull String password) {
        this.password = password;
    }

    @NotNull
    public List<String> getRole() {
        return role;
    }

    public void setRole(@NotNull List<String> role) {
        this.role = role;
    }
}

