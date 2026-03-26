package com.btljava.GiaSu.service;

import com.btljava.GiaSu.dto.AuthResponse;
import com.btljava.GiaSu.dto.LoginRequest;
import com.btljava.GiaSu.dto.RegisterRequest;
import com.btljava.GiaSu.entity.GiaSu;
import com.btljava.GiaSu.entity.HocVien;
import com.btljava.GiaSu.entity.TaiKhoan;
import com.btljava.GiaSu.repository.GiaSuRepository;
import com.btljava.GiaSu.repository.HocVienRepository;
import com.btljava.GiaSu.repository.TaiKhoanRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final TaiKhoanRepository taiKhoanRepository;
    private final GiaSuRepository giaSuRepository;
    private final HocVienRepository hocVienRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthResponse register(RegisterRequest request) {

        if (!request.getPassword().equals(request.getConfirmPassword())) {
            return AuthResponse.builder()
                    .message("Mật khẩu xác nhận không khớp!")
                    .success(false)
                    .build();
        }

        if (taiKhoanRepository.existsByEmail(request.getEmail())) {
            return AuthResponse.builder()
                    .message("Email đã tồn tại!")
                    .success(false)
                    .build();
        }

        String role = request.getRole();
        if (role == null || (!role.equals("HOC_VIEN") && !role.equals("GIA_SU"))) {
            role = "HOC_VIEN";
        }

        TaiKhoan taiKhoan = TaiKhoan.builder()
                .email(request.getEmail())
                .hoTen(request.getUsername())
                .matKhauMaHoa(passwordEncoder.encode(request.getPassword()))
                .soDienThoai(request.getPhone())
                .viTri(request.getAddress())
                .trangThai("HOAT_DONG")
                .vaiTro(role)
                .build();

        taiKhoanRepository.save(taiKhoan);

        if ("GIA_SU".equals(role)) {
            GiaSu giaSu = GiaSu.builder()
                    .taiKhoan(taiKhoan)
                    .build();
            giaSuRepository.save(giaSu);
        } else if ("HOC_VIEN".equals(role)) {
            HocVien hocVien = HocVien.builder()
                    .taiKhoan(taiKhoan)
                    .build();
            hocVienRepository.save(hocVien);
        }

        return AuthResponse.builder()
                .message("Đăng ký thành công!")
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
        Integer relativeId = null;
        if ("GIA_SU".equals(taiKhoan.getVaiTro())) {
            GiaSu giaSu = giaSuRepository.findByTaiKhoan_MaTaiKhoan(taiKhoan.getMaTaiKhoan())
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy thông tin Gia sư"));
            relativeId = giaSu.getMaGiaSu();
        } else if ("HOC_VIEN".equals(taiKhoan.getVaiTro())) {
            HocVien hocVien = hocVienRepository.findByTaiKhoan_MaTaiKhoan(taiKhoan.getMaTaiKhoan())
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy thông tin Học viên"));
            relativeId = hocVien.getMaHocVien();
        }

        String token = jwtService.generateToken(taiKhoan);

        return AuthResponse.builder()
                .message("Login success")
                .success(true)
                .token(token)
                .userId(relativeId)
                .username(taiKhoan.getHoTen())
                .role(taiKhoan.getVaiTro())
                .build();
    }
}
