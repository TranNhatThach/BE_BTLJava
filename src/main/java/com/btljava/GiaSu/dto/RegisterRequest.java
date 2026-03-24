package com.btljava.GiaSu.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {

    @NotBlank(message = "Username/HoTen không được để trống")
    private String username;

    @NotBlank(message = "Email không được để trống")
    @Email(message = "Email không đúng định dạng")
    private String email;

    @NotBlank(message = "Password không được để trống")
    @Size(min = 6, message = "Password phải có ít nhất 6 ký tự")
    private String password;

    @NotBlank(message = "Confirm Password không được để trống")
    private String confirmPassword;

    @NotBlank(message = "Vai trò không được để trống")
    private String role;

    @Pattern(regexp = "^[0-9]{10,11}$", message = "Số điện thoại không hợp lệ (10-11 số)")
    private String phone;
    
    private String address;
}
