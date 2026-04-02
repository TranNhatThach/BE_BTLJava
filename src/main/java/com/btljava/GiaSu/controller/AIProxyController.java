package com.btljava.GiaSu.controller;

import com.btljava.GiaSu.dto.GiaSuResponse;
import com.btljava.GiaSu.entity.YeuCauTimGiaSu;
import com.btljava.GiaSu.service.GiaSuService;
import com.btljava.GiaSu.service.YeuCauGiaSuService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.HttpMethod;
import org.springframework.core.ParameterizedTypeReference;
import java.util.*;
import java.util.stream.Collectors;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@RestController
@RequestMapping("/api/ai")
@CrossOrigin("*")
@RequiredArgsConstructor
public class AIProxyController {

    private final RestTemplate restTemplate = new RestTemplate();

    private final GiaSuService giaSuService;
    private final YeuCauGiaSuService yeuCauGiaSuService;

    @PostMapping("/ask")
    public ResponseEntity<Map<String, Object>> askAI(@RequestBody Map<String, Object> request) {
        String userQuestion = (String) request.get("question");
        Object historyContext = request.get("context");
        String userName = (String) request.get("userName");
        String userRole = (String) request.get("userRole");

        StringBuilder promptBuilder = new StringBuilder();

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String roleLabel = "Khách";
        if (userRole != null) {
            if (userRole.equals("GIA_SU"))
                roleLabel = "Gia sư";
            else if (userRole.equals("HOC_VIEN"))
                roleLabel = "Học viên";
            else if (userRole.equals("ADMIN"))
                roleLabel = "Admin";
        }

        if (auth != null && auth.isAuthenticated() && !auth.getPrincipal().equals("anonymousUser")) {
            promptBuilder.append("[ĐỊNH DANH: ").append(auth.getName()).append(" (")
                    .append(auth.getAuthorities()).append(")]\n");
        } else if (userName != null) {
            promptBuilder.append("[ĐỊNH DANH: ").append(userName).append(" (")
                    .append(roleLabel).append(")]\n");
        } else {
            promptBuilder.append("[ĐỊNH DANH: KHÁCH (CHƯA ĐĂNG NHẬP)]\n");
        }

        // 2. SIMPLE RAG LOGIC
        StringBuilder ragData = new StringBuilder("\n\n[DỮ LIỆU THỰC TẾ TỪ HỆ THỐNG]:\n");
        String questionLower = userQuestion.toLowerCase();
        if (questionLower.contains("gia sư") || questionLower.contains("tutor") || questionLower.contains("tìm")) {
            List<GiaSuResponse> topTutors = giaSuService.timKiemGiaSu(null, null, null, null, null, null, 0, 5)
                    .getContent();
            String tutorInfo = topTutors.stream()
                    .map(gs -> String.format("- %s: Dạy %s, Giá %s, Đánh giá %s sao",
                            gs.getHoTen(), gs.getMonHoc(), gs.getHocPhiMoiGio(), gs.getDiemDanhGia()))
                    .collect(Collectors.joining("\n"));
            ragData.append("Danh sách gia sư tiêu biểu hiện có:\n").append(tutorInfo).append("\n");
        }
        if (questionLower.contains("lớp") || questionLower.contains("tìm việc") || questionLower.contains("yêu cầu")) {
            List<YeuCauTimGiaSu> classes = yeuCauGiaSuService.layTatCa();
            String classInfo = classes.stream().limit(5)
                    .map(cl -> String.format("- Lớp %s: Ngân sách %s-%s, Địa điểm %s",
                            cl.getMonHoc().getTenMon(), cl.getNganSachMin(), cl.getNganSachMax(), cl.getDiaDiem()))
                    .collect(Collectors.joining("\n"));
            ragData.append("Danh sách lớp mới nhất đang tìm gia sư:\n").append(classInfo).append("\n");
        }

        Map<String, Object> payload = new HashMap<>();
        payload.put("model", "tutor-connect-ai");
        payload.put("prompt", promptBuilder.toString() + ragData + "\n\nNgười dùng hỏi: " + userQuestion);
        payload.put("stream", false);
        if (historyContext != null) {
            payload.put("context", historyContext);
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(payload, headers);

        try {
            String OLLAMA_URL = "http://localhost:11434/api/generate";
            ResponseEntity<Map<String, Object>> responseEntity = restTemplate.exchange(
                    OLLAMA_URL, HttpMethod.POST, entity, new ParameterizedTypeReference<>() {
                    });
            Map<String, Object> response = responseEntity.getBody();
            assert response != null;
            String aiAnswer = (String) response.get("response");
            Object newContext = response.get("context");

            Map<String, Object> result = new HashMap<>();
            result.put("answer", aiAnswer);
            result.put("context", newContext);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("answer", "Xin lỗi, trợ lý AI đang bận.");
            return ResponseEntity.ok(error);
        }
    }
}
