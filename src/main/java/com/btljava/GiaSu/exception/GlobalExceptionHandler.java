package com.btljava.GiaSu.exception;

import com.btljava.GiaSu.dto.AuthResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * TÁC DỤNG CỦA FILE:
 * 1. Tập trung hóa việc xử lý lỗi: Thay vì để các Exception (lỗi) ném ra log dài dằng dặc,
 * file này sẽ "tóm" tất cả các lỗi lại tại một nơi duy nhất.
 * * lỗi này được tìm trong quá trình test api register postman thì email khi được tạo lần 2 thì nó báo 400 bad request
 * dù json trả về đúng định dạng
 * 1. Sửa lỗi "400 Bad Request" vô danh: Thay vì trả về một mảng JSON lỗi phức tạp của Spring Boot
 * (như cái trace dài bạn thấy), nó sẽ rút gọn lại thành một thông báo dễ hiểu (ví dụ: "Email không đúng định dạng").
 * 2. Ngăn chặn sập API: Khi code gặp lỗi logic bất ngờ (NullPointerException, lỗi SQL...),
 * thay vì Frontend nhận về lỗi trắng trang, file này sẽ trả về JSON với success = false để Frontend xử lý.
 * * TÁC DỤNG CỤ THỂ:
 * - Giúp Frontend (React) luôn nhận được dữ liệu đúng cấu trúc {message, success, ...}
 * dù là lúc thành công hay lúc gặp lỗi.
 * - Tăng tính chuyên nghiệp và bảo mật (không để lộ cấu trúc thư mục code qua log trace).
 */

// đính chính lại tôi đã sai cái lỗi 400 đó là do tôi set bên AuthController
// ko sao cứ để file này nhé

@RestControllerAdvice
public class GlobalExceptionHandler {

    // Bắt các lỗi Validation (@NotBlank, @Email...) để không bị hiện log dài dòng
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleValidationExceptions(MethodArgumentNotValidException ex) {
        String errorMessage = ex.getBindingResult().getFieldErrors().get(0).getDefaultMessage();

        return ResponseEntity.badRequest().body(
                AuthResponse.builder()
                        .message(errorMessage)
                        .success(false)
                        .build()
        );
    }

    // Bắt các lỗi linh tinh khác để Server không bao giờ sập
    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleAllExceptions(Exception ex) {
        ex.printStackTrace(); // IN LỖI RA CONSOLE ĐỂ DEBUG
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                AuthResponse.builder()
                        .message("Lỗi (" + ex.getClass().getSimpleName() + "): " + ex.getMessage())
                        .success(false)
                        .build()
        );
    }
}