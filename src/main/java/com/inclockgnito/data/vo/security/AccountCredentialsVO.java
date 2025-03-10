package com.inclockgnito.data.vo.security;

import java.io.Serializable;
import java.util.Objects;

public class AccountCredentialsVO implements Serializable {
    private static final long serialVersionUID = 1L;

    private String username;
    private String password;

    private String fullname;

    private String email;

    private Double hourlyWage;

    public AccountCredentialsVO(String username, String password, String fullname, String email, Double hourlyWage) {
        this.username = username;
        this.password = password;
        this.fullname = fullname;
        this.email = email;
        this.hourlyWage = hourlyWage;
    }



    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getFullname() {
        return fullname;
    }

    public String getEmail() {
        return email;
    }

    public Double getHourlyWage() {
        return hourlyWage;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AccountCredentialsVO that = (AccountCredentialsVO) o;
        return Objects.equals(username, that.username) && Objects.equals(password, that.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, password);
    }
}
