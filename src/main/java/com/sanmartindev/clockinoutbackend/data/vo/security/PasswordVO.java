package com.sanmartindev.clockinoutbackend.data.vo.security;

public class PasswordVO {
    String oldPassword;
    String newPassword;

    public PasswordVO(String oldPassword, String newPassword) {
        this.oldPassword = oldPassword;
        this.newPassword = newPassword;
    }

    public String getOldPassword() {
        return oldPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }


}
