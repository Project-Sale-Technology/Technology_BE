package com.technology_be.dto;

import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

public class UserRegisterDTO {
    @NotEmpty
    @Length(min = 4, max = 50, message = "Username must be between 4 and 50")
    private String fullName;

    @NotEmpty
    @Length(min = 4, max = 50, message = "Password must be between 4 and 50")
    private String password;

    @NotEmpty
    @Length(min = 4, max = 50, message = "Password must be between 4 and 50")
    private String confirmPassword;

    @NotEmpty
    @Length(min = 17, max = 50, message = "Password must be between 17 and 50")
    @Pattern(regexp = "^\\w+([-+.']\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$", message = "Email is invalid")
    private String email;

    @NotEmpty
    @Pattern(regexp = "^(0?)(3[2-9]|5[6|8|9]|7[0|6-9]|8[0-6|8|9]|9[0-4|6-9])[0-9]{7}$")
    private String phoneNumber;

    private Long province;

    public UserRegisterDTO() {
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Long getProvince() {
        return province;
    }

    public void setProvince(Long province) {
        this.province = province;
    }
}
