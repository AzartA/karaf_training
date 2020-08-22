package ru.training.karaf.rest.dto;

import javax.validation.constraints.Size;

public class AuthDTO {
    private String login;
    private String password;

    public AuthDTO() {
    }

    public AuthDTO(String login, String password) {
        this.login = login;
        this.password = password;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof AuthDTO))
            return false;

        AuthDTO authDTO = (AuthDTO) o;

        if (!login.equals(authDTO.login))
            return false;
        return password != null ? password.equals(authDTO.password) : authDTO.password == null;
    }

    @Override
    public int hashCode() {
        int result = login.hashCode();
        result = 31 * result + (password != null ? password.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "AuthDTO{" +
                "login='" + login + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
