package com.example.asztar.wetklinikmobileapp.Models;

public class PasswordChangeModel {
    private String OldPassword;
    private String NewPassword;
    private String ConfirmNewPassword;

    public String getOldPassword() {
        return OldPassword;
    }

    public void setOldPassword(String oldPassword) {
        OldPassword = oldPassword;
    }

    public String getNewPassword() {
        return NewPassword;
    }

    public void setNewPassword(String newPassword) {
        NewPassword = newPassword;
    }

    public String getConfirmNewPassword() {
        return ConfirmNewPassword;
    }

    public void setConfirmNewPassword(String confirmNewPassword) {
        ConfirmNewPassword = confirmNewPassword;
    }
}
