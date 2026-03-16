package com.btljava.GiaSu.service;

import com.btljava.GiaSu.dto.AuthResponse;
import com.btljava.GiaSu.dto.LoginRequest;
import com.btljava.GiaSu.dto.RegisterRequest;
import com.btljava.GiaSu.entity.TaiKhoan;
import com.btljava.GiaSu.repository.TaiKhoanRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final TaiKhoanRepository taiKhoanRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthResponse register(RegisterRequest request) {
        if (taiKhoanRepository.existsByEmail(request.getEmail())) {
            return AuthResponse.builder()
                    .message("Email đã tồn tại!")
                    .success(false)
                    .build();
        }

        TaiKhoan taiKhoan = TaiKhoan.builder()
                .email(request.getEmail())
                .hoTen(request.getUsername())
                .matKhauMaHoa(passwordEncoder.encode(request.getPassword()))
                .soDienThoai(request.getPhone())
                .trangThai("HOAT_DONG") // Giả sử tài khoản được kích hoạt ngay
                .vaiTro(request.getRole()) // Mặc định là học viên, có thể thêm logic lựa chọn vai trò sau
                .build();

        taiKhoanRepository.save(taiKhoan);

        return AuthResponse.builder()
                .message("Đăng ký thành công!Vui lòng đăng nhập .")
                .success(true)
                .build();
    }

    public AuthResponse login(LoginRequest request) {
        Optional<TaiKhoan> optionalTaiKhoan = taiKhoanRepository.findByEmail(request.getEmail());

        if (optionalTaiKhoan.isEmpty()) {
            return AuthResponse.builder()
                    .message("Sai email hoặc mật khẩu!")
                    .success(false)
                    .build();
        }

        TaiKhoan taiKhoan = optionalTaiKhoan.get();

        if (!passwordEncoder.matches(request.getPassword(), taiKhoan.getMatKhauMaHoa())) {
            return AuthResponse.builder()
                    .message("Sai email hoặc mật khẩu!")
                    .success(false)
                    .build();
        }
        String token = jwtService.generateToken(taiKhoan);

        return AuthResponse.builder()
                .message("Login success")
                .success(true)
                .token(token)
                .username(taiKhoan.getHoTen())
                .role(taiKhoan.getVaiTro())
                .build();
    }
}
